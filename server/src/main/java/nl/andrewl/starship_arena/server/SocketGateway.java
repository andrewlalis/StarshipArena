package nl.andrewl.starship_arena.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.andrewl.record_net.Message;
import nl.andrewl.record_net.Serializer;
import nl.andrewl.starship_arena.core.net.*;
import nl.andrewl.starship_arena.server.control.ClientNetManager;
import nl.andrewl.starship_arena.server.data.ArenaStore;
import nl.andrewl.starship_arena.server.model.Arena;
import nl.andrewl.starship_arena.server.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.*;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The socket gateway is the central point at which all clients connect for any
 * arena. Client connections are then handed off to the associated arena for
 * management.
 */
@Service
@Slf4j
public class SocketGateway implements Runnable {
	public static final int INITIALIZATION_TIMEOUT = 1000;
	public static final Serializer SERIALIZER = new Serializer();
	static {
		SERIALIZER.registerType(1, ClientConnectRequest.class);
		SERIALIZER.registerType(2, ClientConnectResponse.class);
		SERIALIZER.registerType(3, ChatSend.class);
		SERIALIZER.registerType(4, ChatSent.class);
		SERIALIZER.registerType(5, ArenaStatus.class);
	}

	private final ServerSocket serverSocket;
	private final DatagramSocket serverUdpSocket;
	private final ArenaStore arenaStore;
	private final ExecutorService connectionProcessingExecutor = Executors.newSingleThreadExecutor();

	@Value("${starship-arena.gateway.host}") @Getter
	private String host;
	@Value("${starship-arena.gateway.tcp-port}") @Getter
	private short tcpPort;
	@Value("${starship-arena.gateway.udp-port}") @Getter
	private short udpPort;

	public SocketGateway(ArenaStore arenaStore) throws IOException {
		this.serverSocket = new ServerSocket();
		this.serverUdpSocket = new DatagramSocket(null);
		this.arenaStore = arenaStore;
		serverSocket.setReuseAddress(true);
		serverUdpSocket.setReuseAddress(true);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startGateway() {
		new Thread(this).start();
	}

	@PreDestroy
	public void shutdown() {
		try {
			log.info("Shutting down Socket Gateway.");
			serverSocket.close();
			serverUdpSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			serverSocket.bind(new InetSocketAddress(host, tcpPort));
			log.info("Socket Gateway bound TCP on {}:{}", host, tcpPort);
			serverUdpSocket.bind(new InetSocketAddress(host, udpPort));
			log.info("Socket Gateway bound UDP on {}:{}", host, udpPort);
		} catch (IOException e) {
			throw new IllegalStateException(e); // Quit; server cannot run if these sockets can't bind.
		}
		while (!serverSocket.isClosed()) {
			try {
				Socket clientSocket = serverSocket.accept();
				connectionProcessingExecutor.submit(() -> processIncomingConnection(clientSocket));
			} catch (IOException e) {
				if (!e.getMessage().equalsIgnoreCase("Socket closed")) e.printStackTrace();
			}
		}
	}

	/**
	 * Logic to do to initialize a client TCP connection, which involves getting
	 * some basic information about the connection, such as which arena the
	 * client is connecting to. A {@link ClientNetManager} is then started to
	 * handle further communication with the client.
	 * @param clientSocket The socket to the client.
	 */
	private void processIncomingConnection(Socket clientSocket) {
		try {
			clientSocket.setSoTimeout(INITIALIZATION_TIMEOUT); // Set limited timeout so new connections don't waste resources.
			Message msg = SERIALIZER.readMessage(clientSocket.getInputStream());
			if (msg instanceof ClientConnectRequest cm) {
				UUID arenaId = cm.arenaId();
				UUID clientId = cm.clientId();
				if (clientId == null) clientId = UUID.randomUUID();
				var oa = arenaStore.getById(arenaId);
				if (oa.isPresent()) {
					Arena arena = oa.get();
					Client client = new Client(clientId, arena, clientSocket);
					arena.registerClient(client);
					SERIALIZER.writeMessage(new ClientConnectResponse(true, clientId), clientSocket.getOutputStream());
					clientSocket.setSoTimeout(0); // Reset timeout to infinity after successful initialization.
					new Thread(client.getNetManager()).start();
					return;
				}
			}
			// If the connection wasn't valid, return a no-success response.
			SERIALIZER.writeMessage(new ClientConnectResponse(false, null), clientSocket.getOutputStream());
			clientSocket.close();
		} catch (SocketTimeoutException e) {
			try {
				clientSocket.close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

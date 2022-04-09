package nl.andrewl.starship_arena.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.andrewl.starship_arena.server.data.ArenaStore;
import nl.andrewl.starship_arena.server.model.Arena;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * The socket gateway is the central point at which all clients connect for any
 * arena. Client connections are then handed off to the associated arena for
 * management.
 */
@Service
@Slf4j
public class SocketGateway implements Runnable {
	private final ServerSocket serverSocket;
	private final DatagramSocket serverUdpSocket;
	private final ArenaStore arenaStore;

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
				new Thread(() -> processIncomingConnection(clientSocket)).start();
			} catch (IOException e) {
				if (!e.getMessage().equalsIgnoreCase("Socket closed")) e.printStackTrace();
			}
		}
	}

	/**
	 * Logic to do to initialize a client TCP connection, which involves getting
	 * some basic information about the connection, such as which arena the
	 * client is connecting to. A {@link ClientManager} is then started to
	 * handle further communication with the client.
	 * @param clientSocket The socket to the client.
	 */
	private void processIncomingConnection(Socket clientSocket) {
		try (
				var in = new DataInputStream(clientSocket.getInputStream());
				var out = new DataOutputStream(clientSocket.getOutputStream())
		) {
			UUID arenaId = new UUID(in.readLong(), in.readLong());
			UUID clientId;
			boolean reconnecting = in.readBoolean();
			if (reconnecting) {
				clientId = new UUID(in.readLong(), in.readLong());
			} else {
				clientId = UUID.randomUUID();
			}
			var oa = arenaStore.getById(arenaId);
			if (oa.isPresent()) {
				Arena arena = oa.get();
				ClientManager clientManager = new ClientManager(arena, clientSocket, clientId);
				arena.registerClient(clientManager);
				out.writeBoolean(true);
				clientManager.start();
			} else {
				out.writeBoolean(false);
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

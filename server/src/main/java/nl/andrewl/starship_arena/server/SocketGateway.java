package nl.andrewl.starship_arena.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.andrewl.starship_arena.server.data.ArenaStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
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

	@Override
	public void run() {
		try {
			serverSocket.bind(new InetSocketAddress(host, tcpPort));
			log.info("Socket Gateway bound TCP on {}:{}", host, tcpPort);
			serverUdpSocket.bind(new InetSocketAddress(host, udpPort));
			log.info("Socket Gateway bound UDP on {}:{}", host, udpPort);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (!serverSocket.isClosed()) {
			try {
				Socket clientSocket = serverSocket.accept();
				processIncomingConnection(clientSocket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processIncomingConnection(Socket clientSocket) throws IOException {
		var din = new DataInputStream(clientSocket.getInputStream());
		UUID arenaId = new UUID(din.readLong(), din.readLong());
		var oa = arenaStore.getById(arenaId);
		if (oa.isPresent()) {
			new ClientManager(oa.get(), clientSocket).start();
		} else {
			clientSocket.close();
		}
	}
}

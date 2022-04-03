package nl.andrewl.starship_arena.server;

import nl.andrewl.starship_arena.server.model.Arena;
import nl.andrewl.starship_arena.server.servlet.ArenasServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class StarshipArenaServer {
	private static final Logger logger = LoggerFactory.getLogger(StarshipArenaServer.class);

	private final Map<UUID, Arena> arenas = new ConcurrentHashMap<>();
	private final ServerSocket serverSocket;
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public static void main(String[] args) throws Exception {
		StarshipArenaServer server = new StarshipArenaServer();
		server.registerArena(new Arena());
		server.registerArena(new Arena("Andrew's Arena"));

		Server jettyServer = new Server(8080);
		Connector connector = new ServerConnector(jettyServer);
		jettyServer.addConnector(connector);

		ServletContextHandler servletContext = new ServletContextHandler();
		servletContext.setContextPath("/");
		servletContext.addServlet(new ServletHolder(new ArenasServlet(server)), "/arenas");

		jettyServer.setHandler(servletContext);
		jettyServer.start();
		server.acceptConnections();
	}

	public StarshipArenaServer() throws IOException {
		serverSocket = new ServerSocket();
		serverSocket.setReuseAddress(true);
	}

	public List<Arena> getArenas() {
		List<Arena> sortedArenas = new ArrayList<>(this.arenas.values());
		sortedArenas.sort(Comparator.comparing(Arena::getCreatedAt));
		return sortedArenas;
	}

	public void registerArena(Arena a) {
		arenas.put(a.getId(), a);
	}

	public void acceptConnections() throws Exception {
		serverSocket.bind(new InetSocketAddress("127.0.0.1", 8081));
		logger.info("Now accepting TCP connections.");
		while (!serverSocket.isClosed()) {
			Socket clientSocket = serverSocket.accept();
			logger.info("Client connected from {}", clientSocket.getRemoteSocketAddress());
			executor.submit(() -> {
				try {
					processIncomingConnection(clientSocket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private void processIncomingConnection(Socket clientSocket) throws IOException {
		var din = new DataInputStream(clientSocket.getInputStream());
		UUID arenaId = new UUID(din.readLong(), din.readLong());
		Arena arena = arenas.get(arenaId);
		if (arena != null) {
			var cm = new ClientManager(arenas.get(arenaId), clientSocket);
			cm.start();
		} else {
			clientSocket.close();
		}
	}
}

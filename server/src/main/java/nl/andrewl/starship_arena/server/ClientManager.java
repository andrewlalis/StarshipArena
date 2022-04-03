package nl.andrewl.starship_arena.server;

import nl.andrewl.starship_arena.server.model.Arena;

import java.io.IOException;
import java.net.Socket;

public class ClientManager extends Thread {
	private final Arena arena;
	private final Socket clientSocket;

	public ClientManager(Arena arena, Socket clientSocket) {
		this.arena = arena;
		this.clientSocket = clientSocket;
	}

	public void shutdown() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (clientSocket.isConnected() && !clientSocket.isClosed()) {

		}
	}
}

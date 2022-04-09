package nl.andrewl.starship_arena.server;

import lombok.Getter;
import nl.andrewl.starship_arena.server.model.Arena;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class ClientManager extends Thread {
	private final Arena arena;
	private final Socket clientSocket;
	@Getter
	private final UUID clientId;

	public ClientManager(Arena arena, Socket clientSocket, UUID id) {
		this.arena = arena;
		this.clientSocket = clientSocket;
		this.clientId = id;
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
		while (!clientSocket.isClosed()) {

		}
	}
}

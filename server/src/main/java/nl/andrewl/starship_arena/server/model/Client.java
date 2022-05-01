package nl.andrewl.starship_arena.server.model;

import lombok.Getter;
import nl.andrewl.starship_arena.server.control.ClientNetManager;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

@Getter
public class Client {
	private final UUID id;
	private final ClientNetManager netManager;
	private final Arena arena;

	public Client(UUID id, Arena arena, Socket socket) throws IOException {
		this.id = id;
		this.arena = arena;
		this.netManager = new ClientNetManager(this, socket);
	}
}

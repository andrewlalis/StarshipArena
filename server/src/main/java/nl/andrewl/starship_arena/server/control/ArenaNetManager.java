package nl.andrewl.starship_arena.server.control;

import lombok.extern.slf4j.Slf4j;
import nl.andrewl.record_net.Message;
import nl.andrewl.starship_arena.server.model.Arena;

import java.io.IOException;

import static nl.andrewl.starship_arena.server.SocketGateway.SERIALIZER;

/**
 * A class that handles all the network operations involved in running an
 * arena.
 */
@Slf4j
public class ArenaNetManager {
	private final Arena arena;

	public ArenaNetManager(Arena arena) {
		this.arena = arena;
	}

	public void broadcast(Message msg) {
		try {
			byte[] data = SERIALIZER.writeMessage(msg);
			broadcast(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void broadcast(byte[] data) {
		for (var client : arena.getClients()) {
			try {
				client.getNetManager().send(data);
			} catch (IOException e) {
				log.error("Could not broadcast data to client: " + client, e);
			}
		}
	}
}

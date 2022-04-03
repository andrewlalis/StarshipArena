package nl.andrewl.starship_arena.server.model;

import nl.andrewl.starship_arena.server.ClientManager;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Arena {
	private final UUID id;
	private final Instant createdAt;
	private final String name;

	private ArenaStage currentStage = ArenaStage.STAGING;

	private final Map<UUID, ClientManager> clients = new ConcurrentHashMap<>();

	public Arena(String name) {
		this.id = UUID.randomUUID();
		this.createdAt = Instant.now();
		this.name = name;
	}

	public Arena() {
		this("Unnamed Arena");
	}

	public UUID getId() {
		return id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public String getName() {
		return name;
	}

	public ArenaStage getCurrentStage() {
		return currentStage;
	}

	public void registerClient(UUID id, ClientManager clientManager) {
		if (clients.containsKey(id)) {
			clientManager.shutdown();
		} else {
			clients.put(id, clientManager);
		}
	}

	@Override
	public boolean equals(Object o) {
		return o == this || o instanceof Arena a && id.equals(a.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id.toString();
	}
}

package nl.andrewl.starship_arena.server.model;

import nl.andrewl.starship_arena.core.net.ChatSent;
import nl.andrewl.starship_arena.server.ClientManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static nl.andrewl.starship_arena.server.SocketGateway.SERIALIZER;

public class Arena {
	private final UUID id;
	private final Instant createdAt;
	private final String name;

	private ArenaStage currentStage = ArenaStage.STAGING;

	private final Map<UUID, ClientManager> clients = new ConcurrentHashMap<>();
	private final List<ChatMessage> chatMessages = new CopyOnWriteArrayList<>();

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

	public void registerClient(ClientManager clientManager) {
		if (clients.containsKey(clientManager.getClientId())) {
			clientManager.shutdown();
		} else {
			clients.put(clientManager.getClientId(), clientManager);
		}
	}

	public void chatSent(ChatMessage chat) throws IOException {
		chatMessages.add(chat);
		byte[] data = SERIALIZER.writeMessage(new ChatSent(chat.clientId(), chat.timestamp(), chat.message()));
		broadcast(data);
	}

	private void broadcast(byte[] data) {
		for (var cm : clients.values()) {
			try {
				cm.send(data);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
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

package nl.andrewl.starship_arena.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.andrewl.starship_arena.core.net.ArenaStatus;
import nl.andrewl.starship_arena.server.control.ArenaNetManager;
import nl.andrewl.starship_arena.server.control.ArenaUpdater;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Slf4j
public class Arena {
	private final UUID id;
	private final String name;

	private final Instant createdAt;

	private Instant startedAt;
	private Instant battleStartsAt;
	private Instant battleEndsAt;
	private Instant closesAt;

	private ArenaStage currentStage = ArenaStage.PRE_STAGING;

	private final Map<UUID, Client> clients = new ConcurrentHashMap<>();

	private final ArenaNetManager netManager;
	@Setter
	private ArenaUpdater updater;

	public Arena(String name) {
		this.id = UUID.randomUUID();
		this.createdAt = Instant.now();
		this.netManager = new ArenaNetManager(this);
		this.name = name;
	}

	public Arena() {
		this("Unnamed Arena");
	}

	public Set<Client> getClients() {
		return new HashSet<>(clients.values());
	}

	public void advanceStage() {
		if (currentStage == ArenaStage.PRE_STAGING) {
			start();
			currentStage = ArenaStage.STAGING;
		} else if (currentStage == ArenaStage.STAGING) {
			currentStage = ArenaStage.BATTLE;
		} else if (currentStage == ArenaStage.BATTLE) {
			currentStage = ArenaStage.ANALYSIS;
		} else if (currentStage == ArenaStage.ANALYSIS) {
			close();
			currentStage = ArenaStage.CLOSED;
		}
		netManager.broadcast(new ArenaStatus(currentStage.name()));
	}

	private void start() {
		this.startedAt = Instant.now();
		this.battleStartsAt = startedAt.plus(1, ChronoUnit.MINUTES);
		this.battleEndsAt = battleStartsAt.plus(2, ChronoUnit.MINUTES);
		this.closesAt = battleEndsAt.plus(1, ChronoUnit.MINUTES);
	}

	private void close() {
		for (var client : clients.values()) {
			client.getNetManager().shutdown();
		}
	}

	public void registerClient(Client client) {
		if (clients.containsKey(client.getId())) {
			client.getNetManager().shutdown();
		} else {
			clients.put(client.getId(), client);
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

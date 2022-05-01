package nl.andrewl.starship_arena.server.data;

import lombok.extern.slf4j.Slf4j;
import nl.andrewl.starship_arena.server.api.dto.ArenaCreationPayload;
import nl.andrewl.starship_arena.server.api.dto.ArenaResponse;
import nl.andrewl.starship_arena.server.control.ArenaUpdater;
import nl.andrewl.starship_arena.server.model.Arena;
import nl.andrewl.starship_arena.server.model.ArenaStage;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Service that manages the set of all active arenas.
 */
@Service
@EnableScheduling
@Slf4j
public class ArenaStore {
	private final Map<UUID, Arena> arenas = new ConcurrentHashMap<>();

	public List<ArenaResponse> getArenas() {
		return arenas.values().stream()
				.sorted(Comparator.comparing(Arena::getCreatedAt))
				.map(ArenaResponse::new)
				.toList();
	}

	public Optional<Arena> getById(UUID id) {
		return Optional.ofNullable(arenas.get(id));
	}

	public ArenaResponse registerArena(ArenaCreationPayload payload) {
		Arena arena = new Arena(payload.name());
		this.arenas.put(arena.getId(), arena);
		return new ArenaResponse(arena);
	}

	public ArenaResponse getArena(String arenaId) {
		try {
			Arena arena = arenas.get(UUID.fromString(arenaId));
			if (arena == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			return new ArenaResponse(arena);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid arena id.");
		}
	}

	public void startArena(String arenaId) {
		Arena arena = arenas.get(UUID.fromString(arenaId));
		if (arena == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		if (arena.getCurrentStage() != ArenaStage.PRE_STAGING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arena is already started.");
		}
		new Thread(new ArenaUpdater(arena)).start();
	}

	@Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
	public void cleanArenas() {
		Set<UUID> removalSet = new HashSet<>();
		final Instant cutoff = Instant.now().minus(5, ChronoUnit.MINUTES);
		for (var arena : arenas.values()) {
			if (
					(arena.getCurrentStage() == ArenaStage.CLOSED) ||
					(arena.getCurrentStage() == ArenaStage.PRE_STAGING && arena.getCreatedAt().isBefore(cutoff))
			) {
				removalSet.add(arena.getId());
			}
		}
		for (var id : removalSet) {
			arenas.remove(id);
		}
	}
}

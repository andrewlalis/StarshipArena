package nl.andrewl.starship_arena.server.data;

import lombok.extern.slf4j.Slf4j;
import nl.andrewl.starship_arena.server.api.dto.ArenaCreationPayload;
import nl.andrewl.starship_arena.server.api.dto.ArenaResponse;
import nl.andrewl.starship_arena.server.control.ArenaUpdater;
import nl.andrewl.starship_arena.server.model.Arena;
import nl.andrewl.starship_arena.server.model.ArenaStage;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("${starship-arena.max-arenas : 100}")
	private int maxArenas;

	private final Map<UUID, Arena> arenas = new ConcurrentHashMap<>();

	public List<ArenaResponse> getArenas() {
		return arenas.values().stream()
				.filter(Arena::isActive)
				.sorted(Comparator.comparing(Arena::getCreatedAt))
				.map(ArenaResponse::new)
				.toList();
	}

	public Optional<Arena> getById(UUID id) {
		return Optional.ofNullable(arenas.get(id));
	}

	private Arena getOrThrow(String id) {
		try {
			UUID uuid = UUID.fromString(id);
			Arena arena = arenas.get(uuid);
			if (arena == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arena with id \"" + id + "\" not found.");
			return arena;
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed id. Should be a UUID.");
		}
	}

	public ArenaResponse registerArena(ArenaCreationPayload payload) {
		if (arenas.size() >= maxArenas) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Too many arenas.");
		Arena arena = new Arena(payload.name());
		this.arenas.put(arena.getId(), arena);
		return new ArenaResponse(arena);
	}

	public ArenaResponse getArena(String arenaId) {
		return new ArenaResponse(getOrThrow(arenaId));
	}

	public void startArena(String arenaId) {
		Arena arena = getOrThrow(arenaId);
		if (arena.getCurrentStage() != ArenaStage.PRE_STAGING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arena is already started.");
		}
		new Thread(new ArenaUpdater(arena)).start();
	}

	@Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
	public void cleanArenas() {
		Set<UUID> removalSet = new HashSet<>();
		final Instant cutoff = Instant.now().minus(5, ChronoUnit.MINUTES);
		final Instant hardCutoff = Instant.now().minus(1, ChronoUnit.HOURS);
		for (var arena : arenas.values()) {
			if (
					(arena.getCurrentStage() == ArenaStage.CLOSED) ||
					(arena.getCurrentStage() == ArenaStage.PRE_STAGING && arena.getCreatedAt().isBefore(cutoff)) ||
					(arena.getCreatedAt().isBefore(hardCutoff))
			) {
				removalSet.add(arena.getId());
			}
		}
		for (var id : removalSet) {
			arenas.remove(id);
		}
	}
}

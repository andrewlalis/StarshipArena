package nl.andrewl.starship_arena.server.data;

import nl.andrewl.starship_arena.server.api.dto.ArenaCreationPayload;
import nl.andrewl.starship_arena.server.api.dto.ArenaResponse;
import nl.andrewl.starship_arena.server.model.Arena;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
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
}

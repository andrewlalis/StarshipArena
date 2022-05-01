package nl.andrewl.starship_arena.server.api;

import lombok.RequiredArgsConstructor;
import nl.andrewl.starship_arena.server.api.dto.ArenaCreationPayload;
import nl.andrewl.starship_arena.server.api.dto.ArenaResponse;
import nl.andrewl.starship_arena.server.data.ArenaStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/arenas")
@RequiredArgsConstructor
public class ArenasController {
	private final ArenaStore arenaStore;

	@GetMapping
	public List<ArenaResponse> getArenas() {
		return arenaStore.getArenas();
	}

	@GetMapping(path = "/{arenaId}")
	public ArenaResponse getArena(@PathVariable String arenaId) {
		return arenaStore.getArena(arenaId);
	}

	@PostMapping
	public ArenaResponse createArena(@RequestBody ArenaCreationPayload payload) {
		return arenaStore.registerArena(payload);
	}

	@PostMapping(path = "/{arenaId}/start")
	public void startArena(@PathVariable String arenaId) {
		arenaStore.startArena(arenaId);
	}
}

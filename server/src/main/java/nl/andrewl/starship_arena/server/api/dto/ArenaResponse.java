package nl.andrewl.starship_arena.server.api.dto;

import nl.andrewl.starship_arena.server.model.Arena;

public record ArenaResponse(
		String id,
		String createdAt,
		String name,
		String currentStage
) {
	public ArenaResponse(Arena arena) {
		this(
				arena.getId().toString(),
				arena.getCreatedAt().toString(),
				arena.getName(),
				arena.getCurrentStage().name()
		);
	}
}

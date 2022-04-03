package nl.andrewl.starship_arena.server.servlet;

import nl.andrewl.starship_arena.server.model.Arena;

public record ArenaResponse(
		String id,
		String createdAt,
		String name,
		String currentStage
) {
	public ArenaResponse(Arena a) {
		this(
				a.getId().toString(),
				a.getCreatedAt().toString(),
				a.getName(),
				a.getCurrentStage().name()
		);
	}
}

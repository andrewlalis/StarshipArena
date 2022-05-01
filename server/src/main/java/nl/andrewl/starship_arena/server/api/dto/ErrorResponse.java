package nl.andrewl.starship_arena.server.api.dto;

public record ErrorResponse(
		int status,
		String message
) {}

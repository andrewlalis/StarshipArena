package nl.andrewl.starship_arena.server.model;

import java.util.UUID;

public record ChatMessage(UUID clientId, long timestamp, String message) {
}

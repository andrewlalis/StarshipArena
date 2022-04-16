package nl.andrewl.starship_arena.core.net;

import nl.andrewl.record_net.Message;

import java.util.UUID;

public record ChatSent(UUID clientId, long timestamp, String msg) implements Message {}

package nl.andrewl.starship_arena.core.net;

import nl.andrewl.record_net.Message;

import java.util.UUID;

public record ClientConnectResponse(boolean success, UUID clientId) implements Message {}

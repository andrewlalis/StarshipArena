package nl.andrewl.starship_arena.core.net;

import nl.andrewl.record_net.Message;

import java.util.UUID;

public record ClientConnectRequest(
        UUID arenaId,
        UUID clientId
) implements Message {}

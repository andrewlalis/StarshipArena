package nl.andrewl.starship_arena.core.net;

import nl.andrewl.record_net.Message;

public record ArenaStatus (
		String currentStage
) implements Message {}

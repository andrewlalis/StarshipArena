package nl.andrewl.starship_arena.server.control;

import lombok.extern.slf4j.Slf4j;
import nl.andrewl.starship_arena.server.model.Arena;

import java.time.Duration;
import java.time.Instant;

/**
 * A runnable that, when started, manages the state of an {@link Arena}
 * throughout its lifecycle.
 */
@Slf4j
public class ArenaUpdater implements Runnable {
	private final Arena arena;

	public ArenaUpdater(Arena arena) {
		this.arena = arena;
		arena.setUpdater(this);
	}

	@Override
	public void run() {
		log.info("Starting arena.");
		arena.advanceStage();
		log.info("Waiting for battle to start at {}", arena.getBattleStartsAt());
		doStaging();
		log.info("Starting battle.");
		arena.advanceStage();
		doBattle();
		log.info("Battle done. Moving to analysis");
		arena.advanceStage();
		doAnalysis();
		log.info("Closing arena.");
		arena.advanceStage();
	}

	private void doStaging() {
		try {
			long millisUntilBattle = Duration.between(Instant.now(), arena.getBattleStartsAt()).toMillis();
			Thread.sleep(millisUntilBattle);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doBattle() {
		// TODO: actual physics updates!
		try {
			long millisUntilBattle = Duration.between(Instant.now(), arena.getBattleEndsAt()).toMillis();
			Thread.sleep(millisUntilBattle);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doAnalysis() {
		try {
			long millisUntilBattle = Duration.between(Instant.now(), arena.getClosesAt()).toMillis();
			Thread.sleep(millisUntilBattle);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

package nl.andrewl.starship_arena.control;

import nl.andrewl.starship_arena.model.Arena;
import nl.andrewl.starship_arena.view.ArenaPanel;

import javax.swing.*;

public class GameUpdater extends Thread {
	public static final double PHYSICS_FPS = 60.0;
	public static final double MILLISECONDS_PER_PHYSICS_TICK = 1000.0 / PHYSICS_FPS;
	public static final double PHYSICS_SPEED = 1.0;

	public static final double DISPLAY_FPS = 60.0;
	public static final double MILLISECONDS_PER_DISPLAY_FRAME = 1000.0 / DISPLAY_FPS;

	private final Arena arena;
	private final ArenaPanel arenaPanel;
	private volatile boolean running = true;

	public GameUpdater(Arena arena, ArenaPanel arenaPanel) {
		this.arena = arena;
		this.arenaPanel = arenaPanel;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		long lastPhysicsUpdate = System.currentTimeMillis();
		long lastDisplayUpdate = System.currentTimeMillis();
		while (running) {
			long currentTime = System.currentTimeMillis();
			long timeSinceLastPhysicsUpdate = currentTime - lastPhysicsUpdate;
			long timeSinceLastDisplayUpdate = currentTime - lastDisplayUpdate;
			if (timeSinceLastPhysicsUpdate >= MILLISECONDS_PER_PHYSICS_TICK) {
				double elapsedSeconds = timeSinceLastPhysicsUpdate / 1000.0;
				updateArena(elapsedSeconds * PHYSICS_SPEED);
				lastPhysicsUpdate = currentTime;
				timeSinceLastPhysicsUpdate = 0L;
			}
			if (timeSinceLastDisplayUpdate >= MILLISECONDS_PER_DISPLAY_FRAME) {
				SwingUtilities.invokeLater(arenaPanel::repaint);
				lastDisplayUpdate = currentTime;
				timeSinceLastDisplayUpdate = 0L;
			}
			long timeUntilNextPhysicsUpdate = (long) (MILLISECONDS_PER_PHYSICS_TICK - timeSinceLastPhysicsUpdate);
			long timeUntilNextDisplayUpdate = (long) (MILLISECONDS_PER_DISPLAY_FRAME - timeSinceLastDisplayUpdate);

			// Sleep to reduce CPU usage.
			try {
				Thread.sleep(Math.min(timeUntilNextPhysicsUpdate, timeUntilNextDisplayUpdate));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateArena(double t) {
		for (var s : arena.getShips()) {
			s.update(t);
		}
	}
}

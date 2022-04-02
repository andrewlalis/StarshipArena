package nl.andrewl.starship_arena.client.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the top-level model containing all objects in an arena that can
 * interact with each other.
 */
public class Arena {
	private final Collection<Ship> ships = new ArrayList<>();
	private final Camera camera = new Camera();

	public Collection<Ship> getShips() {
		return ships;
	}

	public Camera getCamera() {
		return camera;
	}
}

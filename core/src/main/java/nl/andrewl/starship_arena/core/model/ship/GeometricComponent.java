package nl.andrewl.starship_arena.core.model.ship;

import nl.andrewl.starship_arena.core.physics.Vec2F;

import java.util.List;

/**
 * Represents a component of a ship that can be drawn as a geometric primitive.
 */
public abstract class GeometricComponent extends ShipComponent {
	private List<Vec2F> points;

	public List<Vec2F> getPoints() {
		return points;
	}
}

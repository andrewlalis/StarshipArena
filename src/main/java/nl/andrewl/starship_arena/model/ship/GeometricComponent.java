package nl.andrewl.starship_arena.model.ship;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Represents a component of a ship that can be drawn as a geometric primitive.
 */
public abstract class GeometricComponent extends ShipComponent {
	private List<Point2D.Float> points;

	public List<Point2D.Float> getPoints() {
		return points;
	}
}

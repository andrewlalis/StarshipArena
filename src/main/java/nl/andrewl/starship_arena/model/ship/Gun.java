package nl.andrewl.starship_arena.model.ship;

import java.awt.geom.Point2D;

public class Gun extends ShipComponent {
	private String name;
	private Point2D.Float location;

	public String getName() {
		return name;
	}

	public Point2D.Float getLocation() {
		return location;
	}
}

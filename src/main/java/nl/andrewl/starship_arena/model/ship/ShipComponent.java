package nl.andrewl.starship_arena.model.ship;

import nl.andrewl.starship_arena.model.Ship;

/**
 * Represents the top-level component information for any part of a ship.
 */
public class ShipComponent {
	/**
	 * The ship that this component belongs to.
	 */
	private transient Ship ship;

	private float mass;

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public float getMass() {
		return mass;
	}
}

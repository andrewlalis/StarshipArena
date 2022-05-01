package nl.andrewl.starship_arena.core.model.ship;

import nl.andrewl.starship_arena.core.physics.Vec2F;

public class Gun extends ShipComponent {
	private String name;
	private Vec2F location;
	private float rotation;
	private float maxRotation;
	private float minRotation;
	private float barrelWidth;
	private float barrelLength;

	public Gun() {}

	public Gun(String name, Vec2F location, float rotation, float maxRotation, float minRotation, float barrelWidth, float barrelLength) {
		this.name = name;
		this.location = location;
		this.rotation = rotation;
		this.maxRotation = maxRotation;
		this.minRotation = minRotation;
		this.barrelWidth = barrelWidth;
		this.barrelLength = barrelLength;
	}

	public String getName() {
		return name;
	}

	public Vec2F getLocation() {
		return location;
	}

	public float getRotation() {
		return rotation;
	}

	public float getMaxRotation() {
		return maxRotation;
	}

	public float getMinRotation() {
		return minRotation;
	}

	public float getBarrelWidth() {
		return barrelWidth;
	}

	public float getBarrelLength() {
		return barrelLength;
	}
}

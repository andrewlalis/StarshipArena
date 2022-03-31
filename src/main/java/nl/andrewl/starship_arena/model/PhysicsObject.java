package nl.andrewl.starship_arena.model;

import java.awt.geom.Point2D;

public class PhysicsObject {
	/**
	 * The position of this object in the scene, in meters from the origin.
	 * Positive x-axis goes to the right, and positive y-axis goes down.
	 */
	private final Point2D.Float position = new Point2D.Float();

	/**
	 * The object's rotation in radians, from 0 to 2 PI.
	 */
	private float rotation;

	/**
	 * The object's velocity, in meters per second.
	 */
	private final Point2D.Float velocity = new Point2D.Float();

	/**
	 * The object's rotational speed, in radians per second.
	 */
	private float rotationSpeed;

	public Point2D.Float getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		while (rotation < 0) rotation += 2 * Math.PI;
		while (rotation > 2 * Math.PI) rotation -= 2 * Math.PI;
		this.rotation = rotation;
	}

	public Point2D.Float getVelocity() {
		return velocity;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public void update(double delta) {
		position.x += velocity.x * delta;
		position.y += velocity.y * delta;
		setRotation((float) (rotation + rotationSpeed * delta));
	}
}

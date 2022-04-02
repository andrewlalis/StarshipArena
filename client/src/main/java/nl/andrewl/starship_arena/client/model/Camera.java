package nl.andrewl.starship_arena.client.model;

import java.awt.geom.Point2D;

public class Camera {
	private static final float[] SCALE_FACTORS = {500, 200, 100, 50, 25, 10, 5, 1, 0.5f, 0.25f, 0.1f, 0.05f, 0.01f};
	public static final byte DEFAULT_SCALE_FACTOR_INDEX = 5;

	private PhysicsObject focus;
	private final Point2D.Float position = new Point2D.Float();
	private float rotation;
	private byte scaleIndex = DEFAULT_SCALE_FACTOR_INDEX;

	public PhysicsObject getFocus() {
		return focus;
	}

	public void setFocus(PhysicsObject focus) {
		this.focus = focus;
	}

	public Point2D.Float getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getScaleFactor() {
		return SCALE_FACTORS[scaleIndex];
	}

	public void zoomOut() {
		if (scaleIndex < SCALE_FACTORS.length - 1) scaleIndex++;
	}

	public void zoomIn() {
		if (scaleIndex > 0) scaleIndex--;
	}

	public void resetScale() {
		scaleIndex = DEFAULT_SCALE_FACTOR_INDEX;
	}
}

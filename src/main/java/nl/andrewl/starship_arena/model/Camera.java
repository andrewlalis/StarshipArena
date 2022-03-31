package nl.andrewl.starship_arena.model;

import java.awt.geom.Point2D;

public class Camera {
	public static final double SCALE_INTERVAL = 50.0;

	private Object focus;

	private Point2D.Float position = new Point2D.Float();

	private int scaleIncrement = 1;

	public Object getFocus() {
		return focus;
	}

	public void setFocus(Object focus) {
		this.focus = focus;
	}

	public Point2D.Float getPosition() {
		return position;
	}

	public void setPosition(Point2D.Float position) {
		this.position = position;
	}

	public int getScaleIncrement() {
		return scaleIncrement;
	}

	public void setScaleIncrement(int scaleIncrement) {
		this.scaleIncrement = scaleIncrement;
	}
}

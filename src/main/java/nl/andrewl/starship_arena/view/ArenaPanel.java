package nl.andrewl.starship_arena.view;

import nl.andrewl.starship_arena.model.Arena;
import nl.andrewl.starship_arena.model.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class ArenaPanel extends JPanel {
	private final Arena arena;

	private final ShipRenderer shipRenderer = new ShipRenderer();

	public ArenaPanel(Arena arena) {
		this.arena = arena;
		this.addMouseWheelListener(e -> {
			arena.getCamera().setScaleIncrement(arena.getCamera().getScaleIncrement() + e.getWheelRotation());
			repaint();
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());

		AffineTransform originalTx = g2.getTransform();
		AffineTransform tx = new AffineTransform();

		Camera cam = arena.getCamera();

		double translateX = (double) getWidth() / 2;
		double translateY = (double) getHeight() / 2;
		if (cam.getFocus() == null) {
			translateX += cam.getPosition().x;
			translateY += cam.getPosition().y;
		}

		double scale = 1 * Camera.SCALE_INTERVAL;
		if (cam.getScaleIncrement() > 0) {
			scale = cam.getScaleIncrement() * Camera.SCALE_INTERVAL;
		} else if (cam.getScaleIncrement() < 0) {
			scale = 1.0 / Math.abs(cam.getScaleIncrement() * Camera.SCALE_INTERVAL);
		}

		tx.translate(translateX, translateY);
		tx.scale(scale, scale);
		g2.setTransform(tx);

		for (var s : arena.getShips()) {
			shipRenderer.render(s, g2);
		}

		g2.setTransform(originalTx);

		g2.setColor(Color.GREEN);
		g2.fillRect(0, 0, 20, 20);
		g2.setColor(Color.BLUE);
		g2.fillRect(getWidth() - 20, getHeight() - 20, 20, 20);
	}
}

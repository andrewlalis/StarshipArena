package nl.andrewl.starship_arena.client.view;

import nl.andrewl.starship_arena.client.model.Arena;
import nl.andrewl.starship_arena.client.model.Camera;
import nl.andrewl.starship_arena.client.model.PhysicsObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class ArenaPanel extends JPanel {
	private final Arena arena;
	private final ShipRenderer shipRenderer = new ShipRenderer();

	public ArenaPanel(Arena arena) {
		this.arena = arena;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());

		AffineTransform originalTx = g2.getTransform();
		AffineTransform camTx = getCameraTransform();
		for (var s : arena.getShips()) {
			AffineTransform shipTx = new AffineTransform(camTx);
			shipTx.translate(s.getPosition().x, s.getPosition().y);
			shipTx.rotate(s.getRotation());
			g2.setTransform(shipTx);
			shipRenderer.render(s, g2);
		}
		g2.setTransform(originalTx);

		// Testing indicators.
		g2.setColor(Color.GREEN);
		g2.fillRect(0, 0, 20, 20);
		g2.setColor(Color.BLUE);
		g2.fillRect(getWidth() - 20, getHeight() - 20, 20, 20);
		g2.setColor(Color.MAGENTA);
		g2.fillOval(getWidth() / 2 - 5, getHeight() / 2 - 5, 10, 10);
	}

	private AffineTransform getCameraTransform() {
		AffineTransform tx = new AffineTransform();
		Camera cam = arena.getCamera();
		// Start by translating such that 0, 0 is in the center of the screen instead of top-left.
		tx.translate((double) getWidth() / 2, (double) getHeight() / 2);
		tx.scale(cam.getScaleFactor(), cam.getScaleFactor());

		double rotation = -cam.getRotation();
		double x = -cam.getPosition().x;
		double y = -cam.getPosition().y;
		if (cam.getFocus() != null) {
			PhysicsObject f = cam.getFocus();
			rotation -= f.getRotation();
			x -= f.getPosition().x;
			y -= f.getPosition().y;
		}

		tx.rotate(rotation);
		tx.translate(x, y);
		return tx;
	}
}

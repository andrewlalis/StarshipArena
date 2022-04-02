package nl.andrewl.starship_arena.client.control;

import nl.andrewl.starship_arena.client.model.Camera;
import nl.andrewl.starship_arena.client.model.PhysicsObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CameraController implements MouseWheelListener, KeyListener {
	private final Camera camera;

	public CameraController(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() > 0) {
			camera.zoomOut();
		} else {
			camera.zoomIn();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		// Reset view.
		if (e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown()) {
			System.out.println("Resetting view");
			camera.setRotation(0);
			camera.setPosition(0, 0);
			camera.resetScale();
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && camera.getFocus() != null) {
			System.out.println("Leaving focus!");
			PhysicsObject f = camera.getFocus();
			camera.setRotation(0);
			camera.setPosition(f.getPosition().x, f.getPosition().y);
			camera.setFocus(null);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}

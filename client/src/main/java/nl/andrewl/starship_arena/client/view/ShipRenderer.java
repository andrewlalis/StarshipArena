package nl.andrewl.starship_arena.client.view;

import nl.andrewl.starship_arena.client.model.Ship;
import nl.andrewl.starship_arena.client.model.ship.Cockpit;
import nl.andrewl.starship_arena.client.model.ship.GeometricComponent;
import nl.andrewl.starship_arena.client.model.ship.Gun;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class ShipRenderer implements Renderer<Ship> {
	@Override
	public void render(Ship ship, Graphics2D g) {
		for (var p : ship.getPanels()) renderGeometricComponent(p, g);
		for (var c : ship.getCockpits()) renderGeometricComponent(c, g);
		for (var gun : ship.getGuns()) renderGun(gun, g);
	}

	private void renderGeometricComponent(GeometricComponent geo, Graphics2D g) {
		Path2D.Float path = new Path2D.Float();
		var first = geo.getPoints().get(0);
		path.moveTo(first.x, first.y);
		for (int i = 0; i < geo.getPoints().size(); i++) {
			var point = geo.getPoints().get(i);
			path.lineTo(point.x, point.y);
		}
		if (geo instanceof Cockpit) {
			g.setColor(new Color(0f, 0f, 0.5f, 0.5f));
		} else {
			g.setColor(geo.getShip().getPrimaryColor());
		}
		g.fill(path);
	}

	private void renderGun(Gun gun, Graphics2D g) {
		AffineTransform originalTx = g.getTransform();
		AffineTransform tx = new AffineTransform(originalTx);
		tx.rotate(gun.getRotation() + Math.PI, gun.getLocation().x, gun.getLocation().y);
		tx.translate(gun.getLocation().x, gun.getLocation().y);
		g.setTransform(tx);
		Path2D.Float path = new Path2D.Float();
		path.moveTo(-gun.getBarrelWidth() / 2, 0);
		path.lineTo(gun.getBarrelWidth() / 2, 0);
		path.lineTo(gun.getBarrelWidth() / 2, gun.getBarrelLength());
		path.lineTo(-gun.getBarrelWidth() / 2, gun.getBarrelLength());
		g.setColor(Color.DARK_GRAY);
		g.fill(path);
		g.setTransform(originalTx);
	}
}

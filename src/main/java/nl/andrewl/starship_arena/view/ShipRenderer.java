package nl.andrewl.starship_arena.view;

import nl.andrewl.starship_arena.model.Ship;
import nl.andrewl.starship_arena.model.ship.Cockpit;
import nl.andrewl.starship_arena.model.ship.GeometricComponent;

import java.awt.*;
import java.awt.geom.Path2D;

public class ShipRenderer implements Renderer<Ship> {
	@Override
	public void render(Ship ship, Graphics2D g) {
		for (var c : ship.getComponents()) {
			if (c instanceof GeometricComponent geo) {
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
					g.setColor(ship.getPrimaryColor());
				}
				g.fill(path);
			}
		}
	}
}

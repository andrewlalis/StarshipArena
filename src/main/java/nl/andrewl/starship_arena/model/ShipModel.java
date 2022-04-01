package nl.andrewl.starship_arena.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.andrewl.starship_arena.model.ship.*;

import java.util.Collection;

public class ShipModel {
	private String name;
	private Collection<ShipComponent> components;

	public String getName() {
		return name;
	}

	public Collection<ShipComponent> getComponents() {
		return components;
	}

	/**
	 * Normalizes the geometric properties of the components such that the ship
	 * model's components are centered around (0, 0).
	 * TODO: Consider scaling?
	 */
	private void normalizeComponents() {
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;
		for (var c : components) {
			if (c instanceof GeometricComponent g) {
				for (var p : g.getPoints()) {
					minX = Math.min(minX, p.x);
					maxX = Math.max(maxX, p.x);
					minY = Math.min(minY, p.y);
					maxY = Math.max(maxY, p.y);
				}
			}
		}
		final float width = maxX - minX;
		final float height = maxY - minY;
		final float offsetX = -minX - width / 2;
		final float offsetY = -minY - height / 2;
		// Shift all components to the top-left.
		for (var c : components) {
			if (c instanceof GeometricComponent g) {
				for (var p : g.getPoints()) {
					p.x += offsetX;
					p.y += offsetY;
				}
			} else if (c instanceof Gun g) {
				g.getLocation().x += offsetX;
				g.getLocation().y += offsetY;
			}
		}
	}

	public static ShipModel load(String json) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(ShipComponent.class, new ComponentDeserializer())
				.registerTypeAdapter(Gun.class, new GunDeserializer())
				.create();
		ShipModel model = gson.fromJson(json, ShipModel.class);
		model.normalizeComponents();
		return model;
	}
}

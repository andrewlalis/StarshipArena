package nl.andrewl.starship_arena.model;

import nl.andrewl.starship_arena.model.ship.Cockpit;
import nl.andrewl.starship_arena.model.ship.Gun;
import nl.andrewl.starship_arena.model.ship.Panel;
import nl.andrewl.starship_arena.model.ship.ShipComponent;
import nl.andrewl.starship_arena.util.ResourceUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Ship extends PhysicsObject {
	private final String modelName;
	private final Collection<ShipComponent> components;

	private final Collection<Panel> panels;
	private final Collection<Gun> guns;
	private final Collection<Cockpit> cockpits;

	private Color primaryColor = Color.GRAY;

	private Ship(ShipModel model) {
		this.modelName = model.getName();
		this.components = model.getComponents();
		this.panels = new ArrayList<>();
		this.guns = new ArrayList<>();
		this.cockpits = new ArrayList<>();
		for (var c : components) {
			c.setShip(this);
			if (c instanceof Panel p) panels.add(p);
			if (c instanceof Gun g) guns.add(g);
			if (c instanceof Cockpit cp) cockpits.add(cp);
		}
	}

	public Ship(String modelResource) {
		this(ShipModel.load(ResourceUtils.getString(modelResource)));
	}

	public String getModelName() {
		return modelName;
	}

	public Collection<ShipComponent> getComponents() {
		return components;
	}

	public Collection<Panel> getPanels() {
		return panels;
	}

	public Collection<Gun> getGuns() {
		return guns;
	}

	public Collection<Cockpit> getCockpits() {
		return cockpits;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public float getMass() {
		float m = 0;
		for (var c : components) {
			m += c.getMass();
		}
		return m;
	}
}

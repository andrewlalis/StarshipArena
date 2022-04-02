package nl.andrewl.starship_arena.client.view;

import java.awt.*;

public interface Renderer<T> {
	void render(T obj, Graphics2D g);
}

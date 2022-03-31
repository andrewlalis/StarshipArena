package nl.andrewl.starship_arena.view;

import java.awt.*;

public interface Renderer<T> {
	void render(T obj, Graphics2D g);
}

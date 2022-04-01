package nl.andrewl.starship_arena;

import nl.andrewl.starship_arena.model.Arena;
import nl.andrewl.starship_arena.model.Ship;
import nl.andrewl.starship_arena.view.ArenaWindow;

/**
 * The main executable class which starts the program.
 */
public class StarshipArena {
	public static void main(String[] args) {
		Ship s1 = new Ship("/ships/corvette.json");
		s1.setVelocity(0, -0.5f);
		s1.setRotationSpeed(0.5f);
		Arena arena = new Arena();
		arena.getShips().add(s1);
		Ship s2 = new Ship("/ships/corvette.json");
		s2.setRotation((float) (Math.PI / 6));
		s2.getPosition().x = 3;
		s2.getPosition().y = -5;
		arena.getShips().add(s2);
		arena.getCamera().setFocus(s1);
		var window = new ArenaWindow(arena);
		window.setVisible(true);
	}
}

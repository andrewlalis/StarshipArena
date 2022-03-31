package nl.andrewl.starship_arena;

import nl.andrewl.starship_arena.model.Arena;
import nl.andrewl.starship_arena.model.Ship;
import nl.andrewl.starship_arena.model.ShipModel;
import nl.andrewl.starship_arena.util.ResourceUtils;
import nl.andrewl.starship_arena.view.ArenaWindow;

/**
 * The main executable class which starts the program.
 */
public class StarshipArena {
	public static void main(String[] args) {
		ShipModel corvette = ShipModel.load(ResourceUtils.getString("/ships/corvette.json"));
		Ship s = new Ship(corvette);
		Arena arena = new Arena();
		arena.getShips().add(s);
		var window = new ArenaWindow(arena);
		window.setVisible(true);
	}
}

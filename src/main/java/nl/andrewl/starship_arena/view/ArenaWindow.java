package nl.andrewl.starship_arena.view;

import nl.andrewl.starship_arena.model.Arena;
import nl.andrewl.starship_arena.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ArenaWindow extends JFrame {
	private final ArenaPanel arenaPanel;

	public ArenaWindow(Arena arena) {
		super("Starship Arena");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		try {
			GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			device.setFullScreenWindow(this);
			setPreferredSize(new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight()));
		} catch (HeadlessException e) {
			System.err.println("Cannot start the program on systems without a screen.");
			System.exit(1);
		}
		try {
			InputStream in = ResourceUtils.get("/img/icon.png");
			setIconImage(ImageIO.read(in));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		arenaPanel = new ArenaPanel(arena);
		add(arenaPanel);
		pack();
	}
}

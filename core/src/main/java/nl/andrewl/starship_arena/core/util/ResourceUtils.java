package nl.andrewl.starship_arena.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class ResourceUtils {
	public static InputStream get(String name) {
		InputStream in = ResourceUtils.class.getResourceAsStream(name);
		if (in == null) throw new UncheckedIOException(new IOException("Could not load resource: " + name));
		return in;
	}

	public static String getString(String name) {
		try (var in = get(name)) {
			return new String(in.readAllBytes());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}

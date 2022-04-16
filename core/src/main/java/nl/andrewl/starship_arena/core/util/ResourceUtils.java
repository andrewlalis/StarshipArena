package nl.andrewl.starship_arena.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class ResourceUtils {
	/**
	 * Gets an input stream for the given resource on the classpath.
	 * @param name The name of the resource.
	 * @return An input stream to read the resource.
	 * @throws UncheckedIOException If the resource could not be loaded.
	 */
	public static InputStream get(String name) {
		InputStream in = ResourceUtils.class.getResourceAsStream(name);
		if (in == null) throw new UncheckedIOException(new IOException("Could not load resource: " + name));
		return in;
	}

	/**
	 * Gets a classpath resource as a string.
	 * @param name The name of the resource.
	 * @return The string representation of that resource.
	 * @throws UncheckedIOException If the resource could not be loaded.
	 */
	public static String getString(String name) {
		try (var in = get(name)) {
			return new String(in.readAllBytes());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}

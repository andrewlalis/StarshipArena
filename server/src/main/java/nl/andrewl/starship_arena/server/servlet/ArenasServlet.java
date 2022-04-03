package nl.andrewl.starship_arena.server.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.andrewl.starship_arena.server.StarshipArenaServer;
import nl.andrewl.starship_arena.server.model.Arena;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ArenasServlet extends HttpServlet {
	private static final Gson gson = new Gson();
	private final StarshipArenaServer server;

	public ArenasServlet(StarshipArenaServer server) {
		this.server = server;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<ArenaResponse> data = server.getArenas().stream()
				.map(ArenaResponse::new).toList();
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		gson.toJson(data, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!req.getContentType().equals("application/json")) resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only JSON is allowed.");
		ArenaRequest arenaRequest;
		try {
			arenaRequest = gson.fromJson(new InputStreamReader(req.getInputStream()), ArenaRequest.class);
		} catch (JsonSyntaxException | JsonIOException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON.");
			return;
		}
		Arena arena = arenaRequest.name == null ? new Arena() : new Arena(arenaRequest.name);
		server.registerArena(arena);
		resp.setStatus(HttpServletResponse.SC_CREATED);
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		gson.toJson(new ArenaResponse(arena), resp.getWriter());
	}
}

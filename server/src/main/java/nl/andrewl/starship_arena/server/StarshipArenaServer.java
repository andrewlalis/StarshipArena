package nl.andrewl.starship_arena.server;

import nl.andrewl.starship_arena.server.servlet.ArenasServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class StarshipArenaServer {
	public static void main(String[] args) throws Exception {
		Server jettyServer = new Server(8080);
		Connector connector = new ServerConnector(jettyServer);
		jettyServer.addConnector(connector);

		ServletContextHandler servletContext = new ServletContextHandler();
		servletContext.setContextPath("/");
		servletContext.addServlet(new ServletHolder(new ArenasServlet()), "/arenas");

		jettyServer.setHandler(servletContext);
		jettyServer.start();
	}
}

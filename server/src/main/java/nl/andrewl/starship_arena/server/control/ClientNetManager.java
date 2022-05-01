package nl.andrewl.starship_arena.server.control;

import nl.andrewl.record_net.Message;
import nl.andrewl.starship_arena.core.net.ChatSend;
import nl.andrewl.starship_arena.core.net.ChatSent;
import nl.andrewl.starship_arena.server.model.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static nl.andrewl.starship_arena.server.SocketGateway.SERIALIZER;

/**
 * A runnable that manages sending and receiving from a client application.
 */
public class ClientNetManager implements Runnable {
	private final Client client;

	private final Socket clientSocket;
	private final InputStream in;
	private final OutputStream out;

	public ClientNetManager(Client client, Socket clientSocket) throws IOException {
		this.client = client;
		this.clientSocket = clientSocket;
		this.in = clientSocket.getInputStream();
		this.out = clientSocket.getOutputStream();
	}

	public void send(byte[] data) throws IOException {
		synchronized (out) {
			out.write(data);
			out.flush();
		}
	}

	public void shutdown() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!clientSocket.isClosed()) {
			try {
				Message msg = SERIALIZER.readMessage(in);
				if (msg instanceof ChatSend cs) {
					client.getArena().getNetManager().broadcast(new ChatSent(client.getId(), System.currentTimeMillis(), cs.msg()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

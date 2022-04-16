package nl.andrewl.starship_arena.server;

import lombok.Getter;
import nl.andrewl.record_net.Message;
import nl.andrewl.starship_arena.core.net.ChatSend;
import nl.andrewl.starship_arena.server.model.Arena;
import nl.andrewl.starship_arena.server.model.ChatMessage;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import static nl.andrewl.starship_arena.server.SocketGateway.SERIALIZER;

public class ClientManager extends Thread {
	@Getter
	private final UUID clientId;
	@Getter
	private String clientUsername;
	private final Arena arena;

	private final Socket clientSocket;
	private final InputStream in;
	private final OutputStream out;
	private final DataInputStream dIn;
	private final DataOutputStream dOut;

	public ClientManager(Arena arena, Socket clientSocket, UUID id) throws IOException {
		this.arena = arena;
		this.clientSocket = clientSocket;
		this.clientId = id;
		this.in = clientSocket.getInputStream();
		this.out = clientSocket.getOutputStream();
		this.dIn = new DataInputStream(clientSocket.getInputStream());
		this.dOut = new DataOutputStream(clientSocket.getOutputStream());
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
					arena.chatSent(new ChatMessage(clientId, System.currentTimeMillis(), cs.msg()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

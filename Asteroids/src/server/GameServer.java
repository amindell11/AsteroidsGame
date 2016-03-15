package server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.google.gson.Gson;

import Game.Starship;

public class GameServer {
	Gson jsonParser;
	HashMap<String, Vector2f> clients;
	int port;
	static final boolean REQUIRE_UNIQUE_CLIENTS = true;
	ServerSocket serverSocket = null;

	public GameServer(int port) {
		this.port = port;
		init();
	}

	public void init() {
		clients = new HashMap<>();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("Server is now running at address " + InetAddress.getLocalHost().getHostAddress()+ " port " + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}
		addClient(socket);
	}

	public void addClient(Socket socket) {
		InetAddress address = socket.getInetAddress();
		EchoThread clientThread = new EchoThread(socket, new EventHandler() {
			@Override
			public void run(String message, EchoThread thread) {
				onMessageRecieved(message, thread);
			}
		});
		clientThread.start();
		if (clients.containsKey(address.getHostAddress()) && REQUIRE_UNIQUE_CLIENTS) {
			closeClient(clientThread);
			System.err.println("Error: Client at address " + address
					+ " is already open. Please close any other clients and try again");
		} else {
			clients.put(clientThread.getIdentifier(REQUIRE_UNIQUE_CLIENTS), new Vector2f());
			System.out.println("Welcome, " + address.getHostName());
		}
	}

	public void onMessageRecieved(String message, EchoThread thread) {
		String key = thread.getIdentifier(REQUIRE_UNIQUE_CLIENTS);
		System.out.println(clients);
		if (message.equalsIgnoreCase("QUIT")) {
			closeClient(thread);
		} else {
			clients.put(key, new Gson().fromJson(message, Vector2f.class));
			thread.out.println(new Gson().toJson(clients.values()));
		}
	}

	public void closeClient(EchoThread clientThread) {
		clients.remove(clientThread.getIdentifier(REQUIRE_UNIQUE_CLIENTS));
		System.out.println("done2");
		clientThread.interrupt();
		try {
			new PrintWriter(clientThread.socket.getOutputStream(), true).println("QUIT");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] arguments) {
		GameServer server = new GameServer(8000);
		while (true) {
			server.update();
		}
	}
}
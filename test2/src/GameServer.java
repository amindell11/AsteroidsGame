import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.newdawn.slick.SlickException;

import com.google.gson.Gson;

public class GameServer {
	static Gson jsonParser;
	HashMap<String, GameObject> clients;
	int port;
	static final boolean REQUIRE_UNIQUE_CLIENTS = false;
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
		try {
			addClient(socket);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void addClient(Socket socket) throws SlickException {
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
			clients.put(clientThread.getIdentifier(REQUIRE_UNIQUE_CLIENTS), new Starship("res/blank.cfg"));
			System.out.println("Welcome, " + address.getHostName());
			System.out.println(clientThread);
			clientThread.out.println(clientThread.getIdentifier(REQUIRE_UNIQUE_CLIENTS));
		}
	}

	public void onMessageRecieved(String message, EchoThread thread) {
		String key = thread.getIdentifier(REQUIRE_UNIQUE_CLIENTS);
		if (message.equalsIgnoreCase("QUIT")) {
			closeClient(thread);
		} else {
			Starship fromJson = new Gson().fromJson(message, Starship.class);
			clients.put(key, fromJson);
			checkForCollision(fromJson);
			thread.out.println(new Gson().toJson(clients));
		}
	}
	public void checkForCollision(Starship ship){
		ArrayList<GameObject> otherShips=new ArrayList<GameObject>(clients.values());
		otherShips.remove(ship);
		for(GameObject otherShip:otherShips){
			Projectile collidingWith = ship.isCollidingWith(((Starship) otherShip).getProjectiles());
			if(collidingWith!=null){
				collidingWith.die();
				ship.die();
				clients.remove(ship);
			}
		}
	}

	public void closeClient(EchoThread clientThread) {
		clients.remove(clientThread.getIdentifier(REQUIRE_UNIQUE_CLIENTS));
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
package Game;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServerPlay extends Play {
	static final int PORT = 8000;
	static final boolean sameComputer = true;
	String hostName = "10.208.8.24";
	int portNumber = 8000;
	Socket socket;
	PrintWriter out;
	Input input;
	BufferedReader in;
	ArrayList<Vector2f> clients;
	long ping;

	@Override
	public void init(GameContainer container, StateBasedGame g) throws SlickException {
		super.init(container, g);
		clients = new ArrayList<>();
		try {
			input = new Input(400);
			if (sameComputer)
				hostName = InetAddress.getLocalHost().getHostAddress();
			System.out.println(hostName);
			socket = new Socket(hostName, portNumber);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);
		sendUpdate(ship.pos);
		recieveUpdate(gc);
	}

	public void sendUpdate(Object obj) {
		out.println(new Gson().toJson(obj));
	}
	public void recieveUpdate(GameContainer gc) {
		long time = System.nanoTime();
		String update = null;
		try {
			if ((update = in.readLine()) != null) {
				if (update.equals("QUIT")) {
					gc.exit();
				} else {
					clients = new Gson().fromJson(update, new TypeToken<List<Vector2f>>() {
					}.getType());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ping = System.nanoTime() - time;
		ping /= 1000000;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
		System.out.println(clients);
		for (Vector2f b : clients) {
			//g.fillOval(b.getX()-5, b.getY()-5, 10, 10);
			Starship s=new Starship();
			s.pos=b;
			s.render(gc, sbg, g);
		}
	}

	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) {

	}

	public ServerPlay(int play) {
		super(play);
	}
}

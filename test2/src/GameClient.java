import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GameClient extends BasicGame {
	static final int PORT = 8000;
	static final boolean sameComputer = true;
	String hostName = "10.208.8.250";
	Starship clientControlledObject;
	int portNumber = 8000;
	Socket socket;
	PrintWriter out;
	Input input;
	BufferedReader in;
	HashMap<String,Starship> clients;
	long ping;
	String id;

	public GameClient() {
		super("Client");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new GameClient());
			app.setDisplayMode(1280, 800, false);
			app.setAlwaysRender(true);
			app.setShowFPS(false);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		clients = new HashMap<>();
		try {
			input = new Input(400);
			if (sameComputer)
				hostName = InetAddress.getLocalHost().getHostAddress();
			socket = new Socket(hostName, portNumber);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			String update = null;
			try {
				if ((update = in.readLine()) != null) {
					id=update;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(

	IOException e)

	{
		e.printStackTrace();
	}

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Starship var = clients.get(id);
		clientControlledObject=var!=null?var:new Starship("res/shipTemplate.cfg");
		if (input.isKeyDown(Input.KEY_W)) {
			clientControlledObject.ForwardKeyPressed();
		}
		if (input.isKeyDown(Input.KEY_S)) {
			clientControlledObject.BackKeyPressed();
		}
		if (input.isKeyDown(Input.KEY_A)) {
			clientControlledObject.LeftKeyPressed();
		}
		if (input.isKeyDown(Input.KEY_D)) {
			clientControlledObject.RightKeyPressed();
		}
		if (input.isKeyDown(Input.KEY_SPACE)) {
			clientControlledObject.shoot(0);
		}
		clientControlledObject.update(gc, delta);
		sendUpdate(clientControlledObject);
		recieveUpdate(gc);
	}

	public void sendUpdate(Object obj) {
		String json = new Gson().toJson(clientControlledObject);
		out.println(json);
	}

	public void recieveUpdate(GameContainer gc) {
		long time = System.nanoTime();
		String update = null;
		try {
			if ((update = in.readLine()) != null) {
				if (update.equals("QUIT")) {
					gc.exit();
				} else {
					clients = new Gson().fromJson(update, new TypeToken<HashMap<String,Starship>>() {
					}.getType());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ping = System.nanoTime() - time;
		ping /= 1000000;
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		for (Starship b : clients.values()) {
			b.render(container, g);
		}
	}

	@Override
	public boolean closeRequested() {
		out.println("QUIT");
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0); // Use this if you want to quit the app.
		return false;
	}

}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GameClient extends BasicGame {
	static final int PORT = 8000;
	String hostName = "10.208.11.57";

	int portNumber = 8000;
	Socket echoSocket;
	PrintWriter out;
	Input input;

	public GameClient() {
		super("Wizard game");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new GameClient());
			app.setDisplayMode(500, 400, false);
			app.setShowFPS(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		try {
			input=new Input(400);
			echoSocket = new Socket(hostName, portNumber);

			out = new PrintWriter(echoSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(input.isKeyDown(Input.KEY_W)){
			out.println(0);
			System.out.println("hel");
		}
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
	}
}
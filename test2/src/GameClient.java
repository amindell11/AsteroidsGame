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
	String hostName = "10.208.8.24";
	Box box;
	int portNumber = 8000;
	Socket echoSocket;
	PrintWriter out;
	Input input;
    BufferedReader in;
	public GameClient() {
		super("Client");
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
			box=new Box(50,50);
			input=new Input(400);
			echoSocket = new Socket(hostName, portNumber);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			out = new PrintWriter(echoSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(input.isKeyDown(Input.KEY_W)){
			out.println(0);
			String update="0";
			try {
				if((update=in.readLine())!=null){
					box.x=Integer.parseInt(update);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		box.render(container, g);
	}
}
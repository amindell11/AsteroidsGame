import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class GameClient extends BasicGame {
	static final int PORT = 8000;
	static final boolean sameComputer = true;
	String hostName = "10.208.8.24";
	Box box;
	int portNumber = 8000;
	Socket socket;
	PrintWriter out;
	Input input;
	BufferedReader in;
	ArrayList<Box> clients;

	public GameClient() {
		super("Client");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new GameClient());
			app.setDisplayMode(500, 400, false);
			app.setShowFPS(true);
			app.setAlwaysRender(true);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		clients = new ArrayList<>();
		try {
			box = new Box(50, 50);
			input = new Input(400);
			if (sameComputer)
				hostName = InetAddress.getLocalHost().getHostAddress();
			System.out.println(hostName);
			socket = new Socket(hostName, portNumber);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (input.isKeyDown(Input.KEY_W)) {
			box.move(1,0);
		}
		if (input.isKeyDown(Input.KEY_S)) {
			box.move(-1,0);
		}
		out.println(new Gson().toJson(box));
		String update = null;
		try {
			if ((update = in.readLine()) != null) {
				if(update.equals("CLOSE")){
					gc.exit();
				}else{
				clients = new Gson().fromJson(update, new TypeToken<List<Box>>() {
				}.getType());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void recieveStatusUpdate() {

	}
    @Override
    public boolean closeRequested()
    {
    	out.println("CLOSE");
    	out.println("QUIT");
    	try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
      System.exit(0); // Use this if you want to quit the app.
      return false;
    }
	public void render(GameContainer container, Graphics g) throws SlickException {
		for (Box b : clients) {
			b.render(container, g);
		}
	}
}
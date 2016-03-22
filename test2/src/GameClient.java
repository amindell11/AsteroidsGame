import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GameClient extends BasicGame {
	static final int PORT = 8000;
	static final boolean sameComputer = false;
	String hostName;
	Starship clientControlledObject;
	int portNumber = 8000;
	Socket socket;
	PrintWriter out;
	Input input;
	BufferedReader in;
	HashMap<String,Starship> clients;
	long ping;
	String id;
	Image background;
	public GameClient() {
		super("Client");
	}

	public static void main(String[] arguments) {
		System.setProperty("org.lwjgl.librarypath", new File("windows").getAbsolutePath());
		try {
			GameClient client= new GameClient();
			client.openServer();
			AppGameContainer app = new AppGameContainer(client);
			app.setDisplayMode(1280, 800, false);
			app.setAlwaysRender(true);
			app.setShowFPS(true);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		background=new Image("res/stars.jpg");
		clients = new HashMap<>();
		input = new Input(400);
	}
	public static boolean testConnection(String hostName, int port, int timeOut) {
		try {
			Socket s=new Socket();
			s.connect(new InetSocketAddress(hostName, port), timeOut);
			while(true){
			System.out.println(s.isClosed());
			}
			//closeSocket(s);
			//return true;
		} catch (IOException e) {
			return false;
		}
	}

	public ArrayList<String> getOpenServers() {
		try {
			List<String> openServers = new ArrayList<>();
			String line;
			System.setProperty("java.net.preferIPv4Stack" , "true");
			InetAddress broadcast=null;
			Enumeration<NetworkInterface> interfaces =
				    NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
				  NetworkInterface networkInterface = interfaces.nextElement();
				  if (networkInterface.isLoopback())
				    continue;    // Don't want to broadcast to the loopback interface
				  for (InterfaceAddress interfaceAddress :
				           networkInterface.getInterfaceAddresses()) {
				    broadcast = interfaceAddress.getBroadcast();
				    if (broadcast == null)
				      continue;
				  }
				}
				System.out.println(broadcast.getHostAddress());
			Runtime.getRuntime().exec("ping "+broadcast.getHostName());
			Process p = Runtime.getRuntime().exec("arp -a");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			in.readLine();
			in.readLine();
			while ((line = in.readLine()) != null) {
				String host = parseIpAddress(line);
				System.out.println(host);
		/*		if (testConnection(host, portNumber, 1000)) {
					openServers.add(host);
				}*/
			}
		} catch (Exception e) {

		}
		return null;
	}

	public static String parseIpAddress(String read) {
		String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(read);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return "0.0.0.0";
		}

	}
	public void openServer(){
		try {
			if (sameComputer){
				hostName = InetAddress.getLocalHost().getHostAddress();
			}else{
				Scanner inp=new Scanner(System.in);
				System.out.println("please input host ip: \n");
				hostName=inp.nextLine().trim();
				inp.close();
			}
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
		clientControlledObject=var!=null?var:new Starship("res/shipTemplate2.cfg");
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
			clientControlledObject.shoot(1);
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
		background.draw(0,0,1280,800);
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
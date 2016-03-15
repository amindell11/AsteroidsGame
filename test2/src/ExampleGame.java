import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.google.gson.Gson;
public class ExampleGame extends BasicGame
{
	HashMap<String,Box> clients;
    static final int PORT = 8000;
	ServerSocket serverSocket = null;
    Socket socket = null;
    public ExampleGame()
    {
        super("Server");
    }
 
    public static void main(String[] arguments)
    {
        try
        {
            AppGameContainer app = new AppGameContainer(new ExampleGame());
            app.setDisplayMode(500, 400, false);
            app.setShowFPS(true);
            app.setTargetFrameRate(60);
            app.setAlwaysRender(true); 
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }
 
    @Override
    public void init(GameContainer container) throws SlickException
    {
    	clients=new HashMap<>();
    	try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    @Override
    public void update(GameContainer container, int delta) throws SlickException
    {
    	try {
            socket = serverSocket.accept();
            System.out.println("Welcome, "+socket.getInetAddress());
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        }
        Thread t=new EchoThread(socket,new EventHandler(){

			@Override
			public void run(String message,EchoThread thread) {
				clients.put(thread.toString(),new Gson().fromJson(message, Box.class));
				System.out.println(clients);
				thread.out.println(new Gson().toJson(clients.values()));
			}
        });
        t.start();
    	clients.put(t.toString(),new Box(50,50));

    }
    public void render(GameContainer container, Graphics g) throws SlickException
    {
    	for(Box b:clients.values()){
    		b.render(container, g);
    	}
    }
}
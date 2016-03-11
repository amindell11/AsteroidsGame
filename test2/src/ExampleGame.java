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
import org.newdawn.slick.SlickException;
public class ExampleGame extends BasicGame
{
    static final int PORT = 8000;
	ServerSocket serverSocket = null;
    Socket socket = null;
    Box box;
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
    	box=new Box(50,50);
    	try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
    	try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        }
        new EchoThread(socket,new EventHandler(){

			@Override
			public void run(String message,EchoThread thread) {
				if(message.equals("0")){
					box.move(1,0);
					thread.out.println(box.x);
				}
			}
        	
        }).start();
    }
    @Override
    public void update(GameContainer container, int delta) throws SlickException
    {

    }
    public void render(GameContainer container, Graphics g) throws SlickException
    {
    	box.render(container, g);
    }
}
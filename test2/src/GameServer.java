import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.google.gson.Gson;
public class GameServer
{
	HashMap<String,Box> clients;
    static final int PORT = 8000;
    static final boolean UNIQUE_CLIENTS=false;
	ServerSocket serverSocket = null;
    Socket socket = null;
    public static void main(String[] arguments)
    {
    	GameServer server=new GameServer();
    	server.init();
    	while(true){
    		server.update(0);
    	}
    }
    public void init()
    {
    	clients=new HashMap<>();
    	try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	try {
			System.out.println("Server is now running at address "+InetAddress.getLocalHost().getHostAddress()+" port "+PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }
    @SuppressWarnings("unused")
	public void update(int delta)
    {
    	try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        }
		InetAddress address = socket.getInetAddress();
    	if(clients.containsKey(address.toString())&&UNIQUE_CLIENTS) {
            try {
				new PrintWriter(socket.getOutputStream(), true).println("CLOSE");
			} catch (IOException e) {
				e.printStackTrace();
			}
    		System.err.println("Error: Client at address "+address+" is already open. Please close any other clients and try again");
		}else{
			EchoThread t=new EchoThread(socket,new EventHandler(){

				@Override
				public void run(String message,EchoThread thread) {
					if(message.equalsIgnoreCase("CLOSE")){
						System.out.println(thread.getIdentifier(UNIQUE_CLIENTS));
						clients.remove(thread.getIdentifier(UNIQUE_CLIENTS));
						System.out.println(clients);
					}else{
					clients.put(thread.getIdentifier(UNIQUE_CLIENTS),new Gson().fromJson(message, Box.class));
					thread.out.println(new Gson().toJson(clients.values()));
					}
				}
	        });
	        t.start();
	        System.out.println(t.getIdentifier(UNIQUE_CLIENTS));
	    	clients.put(t.getIdentifier(UNIQUE_CLIENTS),new Box(50,50));
			System.out.println("Welcome, "+address.getHostName());
		}
    }
}
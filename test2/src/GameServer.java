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
	static Gson jsonParser;
	HashMap<String,Box> clients;
    int port;
    static final boolean REQUIRE_UNIQUE_CLIENTS=false;
	ServerSocket serverSocket = null;
    public GameServer(int port){
    	this.port=port;
    	init();
    }
    public void init()
    {
    	clients=new HashMap<>();
    	try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	try {
			System.out.println("Server is now running at address "+InetAddress.getLocalHost().getHostAddress()+" port "+port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }
    
	public void update(int delta)
    {
		Socket socket=null;
    	try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        }
    	addClient(socket);
		

    }
    @SuppressWarnings("unused")

    public void addClient(Socket socket){
    	InetAddress address = socket.getInetAddress();
    	if(clients.containsKey(address.toString())&&REQUIRE_UNIQUE_CLIENTS) {
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
					onMessageRecieved(message,thread);
				}
	        });
	        t.start();
	        System.out.println(t.getIdentifier(REQUIRE_UNIQUE_CLIENTS));
	    	clients.put(t.getIdentifier(REQUIRE_UNIQUE_CLIENTS),new Box(50,50));
			System.out.println("Welcome, "+address.getHostName());
		}
    }
	public void onMessageRecieved(String message,EchoThread thread){
		String key=thread.getIdentifier(REQUIRE_UNIQUE_CLIENTS);
		if(message.equalsIgnoreCase("CLOSE")){
			//System.out.println("recieved update from "+key);
			clients.remove(key);
		}else{
		clients.put(key,new Gson().fromJson(message, Box.class));
		thread.out.println(new Gson().toJson(clients.values()));
		}
	}
	
    public static void main(String[] arguments)
    {
    	GameServer server=new GameServer(8000);
    	while(true){
    		server.update(0);
    	}
    }
}
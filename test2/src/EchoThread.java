import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoThread extends Thread {
    protected Socket socket;
    EventHandler onMessageRecieved;
    InputStream inp;
    BufferedReader brinp;
   	PrintWriter out;


    public EchoThread(Socket clientSocket, EventHandler onMessageRecieved) {
        this.socket = clientSocket;
        this.onMessageRecieved=onMessageRecieved;
    }

    public void run() {
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out= new PrintWriter(socket.getOutputStream(), true);                   

        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                	onMessageRecieved.run(line,this);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
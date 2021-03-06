import java.io.BufferedReader;
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
		this.onMessageRecieved = onMessageRecieved;
		try {
			inp = socket.getInputStream();
			brinp = new BufferedReader(new InputStreamReader(inp));
			out = new PrintWriter(socket.getOutputStream(), true);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		String line;
		while (!isInterrupted()&&!out.checkError()) {
			//System.out.println(out.checkError());
			try {
				if (brinp.ready()) {
					line = brinp.readLine();
					if (line == null) {
						return;
					}
					onMessageRecieved.run(line, this);

				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		System.out.println("Goodbye "+getClientAddress());
	}

	public String getClientAddress() {
		return socket.getInetAddress().getHostAddress();
	}

	public String getIdentifier(boolean uniqueAddressClients) {
		if (uniqueAddressClients)
			return getClientAddress();
		else
			return toString();
	}
}
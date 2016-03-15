package server;

public interface EventHandler {
	public void run(String message,EchoThread thread);
}

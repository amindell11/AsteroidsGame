import java.util.ArrayList;

public class SoloPlay extends Play{
	private static Starship ship;
	private static ArrayList<GameObject> listOfAsteroids;
	private static ArrayList<Asteroid> pendingAsteroids;
	private int score;
	private int level;
	private double levelTicks;
	private static boolean isGameOver;
	private boolean isPaused;
	private int updateCounter = 0;

}

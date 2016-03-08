import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/*CONVENTIONS:
 VARIABLES, LISTS, METHODS: first letter lowercase, rest camelCasing
 CLASSES: all CamelCasing
 *CLASS DESCRIPTION:
 main play state
 -updates and renders ship and asteroids
 -keeps track of score
 -pauses game when p key is pressed
 -ends game and offers to restart when ship is destroyed
 -displays level number at the start of each level
 -starts a new level when asteroids are depleted
 */
public class Play extends BasicGameState {

	private static Starship ship;
	private static ArrayList<GameObject> listOfAsteroids;
	private static ArrayList<Asteroid> pendingAsteroids;
	private int score;
	private int level;
	private double levelTicks;
	private static boolean isGameOver;
	private boolean isPaused;
	private int updateCounter = 0;
	private Image background;

	// UTILITY
	public Play(int play) {
	}

	@Override
	public int getID() {
		return 0;
	}

	// INIT
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		background = new Image("res/stars.jpg");
		ship = new Starship("res/shipTemplate.cfg");
		score = 0;
		level = 0;
		levelTicks = 0;
		listOfAsteroids = new ArrayList<GameObject>();
		pendingAsteroids = new ArrayList<Asteroid>();
		isGameOver = false;
	}

	// RENDER
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		renderBackground(gc, sbg, g);
		renderShip(gc, sbg, g);
		renderAsteroids(gc, sbg, g);
		renderHUD(gc, sbg, g);
		if (SetupClass.isDEBUGGING)
			renderDEBUG(gc, sbg, g);
	}

	private void renderBackground(GameContainer gc, StateBasedGame sbg,
			Graphics g) {
		background.draw();
	}

	// DEBUG
	private void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g) {
		System.out.println("===========================================");
		System.out.print("Update " + updateCounter + "\t");
		System.out.print("FPS:  " + gc.getFPS() + "\t");
		System.out.println("upTime:  "
				+ (gc.getFPS() == 0 ? 0 : updateCounter / gc.getFPS())
				+ " seconds\t");
		System.out.println("isPaused: " + isPaused + "\t");
		System.out.println("Level: " + level + "\t");
		System.out.println("Score: " + score + "\t");
	}

	// RENDER HUD
	private void renderHUD(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.drawString("Score: " + score, SetupClass.ScreenWidth,
				SetupClass.ScreenHeight / 100); // draw Score

		if (levelTicks < 5)
			g.drawString("Score: " + score, SetupClass.ScreenWidth,
					SetupClass.ScreenHeight / 100); // draw Level number if it
													// is a new level
	}

	// RENDER SHIP
	public void renderShip(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		ship.render(gc, sbg, g);
	}

	// RENDER ASTEROIDS
	public void renderAsteroids(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		for (GameObject asteroid : listOfAsteroids) {
			asteroid.render(gc, sbg, g);
		}
	}
	// UPDATE
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(isGameOver)sbg.enterState(SetupClass.GAME_OVER);
		getInput(gc);
		if (!isPaused) {
			purgeInactiveObjects();
			addPendingAsteroids();
			if (isLevelClear())
				makeNewLevel();
			updateShip(gc, sbg, delta);
			updateAsteroids(gc, sbg, delta);
		}
		if (SetupClass.isDEBUGGING)
			updateCounter++;
	}

	private boolean isLevelClear() {
		return (listOfAsteroids.isEmpty());
	}

	private void addPendingAsteroids() {
		listOfAsteroids.addAll(pendingAsteroids);
		pendingAsteroids = new ArrayList<Asteroid>();
	}

	// REMOVE INACTIVE GAME OBJECTS
	private void purgeInactiveObjects() {
		purgeAsteroids();
		purgeShipProjectiles();
		purgeEnemyProjectiles();
	}

	private void purgeEnemyProjectiles() {

	}

	private void purgeShipProjectiles() {
		Iterator<GameObject> j = ship.getProjectiles().iterator();
		while (j.hasNext()) {
			Projectile currentProjectile = (Projectile) j.next();
			if (!currentProjectile.isActive()) {
				j.remove();
			}
		}
	}

	private void purgeAsteroids() {
		Iterator<GameObject> j = listOfAsteroids.iterator();
		while (j.hasNext()) {
			GameObject currentAsteroid = j.next();
			if (!currentAsteroid.isActive()) {
				j.remove();
			}
		}
	}

	// UPDATE SHIP
	private void updateShip(GameContainer gc, StateBasedGame sbg, int delta) {
		getInput(gc);
		ship.update(gc, sbg, delta);
	}

	// UPDATE ASTEROIDS
	private void updateAsteroids(GameContainer gc, StateBasedGame sbg, int delta) {
		if (listOfAsteroids.isEmpty())
			makeNewLevel();
		else {
			for (GameObject asteroid : listOfAsteroids) {
				asteroid.update(gc, sbg, delta);
			}
		}
	}

	// GET INPUT
	private void getInput(GameContainer gc) {
		Input in = gc.getInput();
		if (in.isKeyPressed(Input.KEY_P)) {
			isPaused = !isPaused;
			return;
		} // pause the game when player presses 'P'
		if (in.isKeyPressed(Input.KEY_SPACE))
			ship.shoot(); // fire ship guns when player presses Spacebar
		if (in.isKeyDown(Input.KEY_W))
			ship.ForwardKeyPressed();
		if (in.isKeyDown(Input.KEY_A))
			ship.LeftKeyPressed();
		if (in.isKeyDown(Input.KEY_D))
			ship.RightKeyPressed();
		if (in.isKeyDown(Input.KEY_S))
			ship.BackKeyPressed();
	}

	// CREATE NEW LEVEL
	private void makeNewLevel() {
		levelTicks = 0;
		level++;
		for (int i = 0; i < level; i++)
			addAsteroid(new Asteroid());
	}

	public static Starship getShip() {
		return ship;
	}

	public static void addAsteroid(Asteroid a) {
		pendingAsteroids.add(a);
	}

	public static ArrayList<GameObject> getAsteroids() {
		return listOfAsteroids;
	}

	public static void GameOver() {
		isGameOver = true;
	}

}

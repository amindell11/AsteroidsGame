package Game;
import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SoloPlay extends Play{
	public SoloPlay(int play) {
		super(play);
	}
	private static ArrayList<GameObject> listOfAsteroids;
	private static ArrayList<Asteroid> pendingAsteroids;
	private int score;
	private int level;
	private double levelTicks;
	private static boolean isGameOver;
	private boolean isPaused;
	// INIT
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container,game);
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
		super.render(gc,sbg,g);
		renderShip(gc, sbg, g);
		renderAsteroids(gc, sbg, g);
		renderHUD(gc, sbg, g);
		if (SetupClass.isDEBUGGING)
			renderDEBUG(gc, sbg, g);
	}
	// UPDATE
		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta)
				throws SlickException {
			super.update(gc, sbg, delta);
			checkForCollision();
			if(isGameOver)sbg.enterState(SetupClass.GAME_OVER);
			if (!isPaused) {
				purgeInactiveObjects();
				addPendingAsteroids();
				if (isLevelClear()){
					makeNewLevel();
				}
				updateAsteroids(gc, sbg, delta);
			}
			if (SetupClass.isDEBUGGING)
				updateCounter++;
		}
		private void checkForCollision() {
			if(ship.isCollidingWith(getAsteroids())!=null)ship.die();
			for(GameObject a:listOfAsteroids){
				GameObject collidingWith = a.isCollidingWith(ship.getAllProjectiles());
				if(collidingWith!=null&&((ExplodingGameObject) a).isAlive()){
					collidingWith.die();
					a.die();
				}
			}
		}
		@Override
		protected void getInput(Input in){
			if (in.isKeyPressed(Input.KEY_P)) {
				isPaused = !isPaused;
				return;
			} // pause the game when player presses 'P'
			super.getInput(in);
		}
	protected void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g){
		super.renderDEBUG(gc, sbg, g);
		System.out.println("isPaused: " + isPaused + "\t");
		System.out.println("Level: " + level + "\t");
		System.out.println("Score: " + score + "\t");

	}
	// RENDER HUD
	protected void renderHUD(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.drawString("Score: " + score, SetupClass.ScreenWidth-100,
				SetupClass.ScreenHeight / 70); // draw Score

			g.drawString("Score: " + score, SetupClass.ScreenWidth,
					SetupClass.ScreenHeight / 100); // draw Level number if it
													// is a new level
	}

	// RENDER ASTEROIDS
	public void renderAsteroids(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		for (GameObject asteroid : listOfAsteroids) {
			asteroid.render(gc, sbg, g);
		}
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
	// CREATE NEW LEVEL
		private void makeNewLevel() {
			levelTicks = 0;
			level++;
			for (int i = 0; i < level; i++)
				addAsteroid(new Asteroid());
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

package Game;
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
	protected static Starship ship;
	private Image background;
	protected int updateCounter = 0;

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
		ship = new Starship("res/shipTemplate2.cfg");
		background = new Image("res/stars.jpg");
	}

	// RENDER
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		renderBackground(gc, sbg, g);
		//renderShip(gc, sbg, g);
		if (SetupClass.isDEBUGGING)
			renderDEBUG(gc, sbg, g);
	}

	private void renderBackground(GameContainer gc, StateBasedGame sbg,
			Graphics g) {
		background.draw();
	}

	// DEBUG
	protected void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g) {
		System.out.println("===========================================");
		System.out.print("Update " + updateCounter + "\t");
		System.out.print("FPS:  " + gc.getFPS() + "\t");
		System.out.println("upTime:  "
				+ (gc.getFPS() == 0 ? 0 : updateCounter / gc.getFPS())
				+ " seconds\t");
	}

	// RENDER HUD
	protected void renderHUD(GameContainer gc, StateBasedGame sbg, Graphics g) {
	}

	// UPDATE
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		getInput(gc.getInput());
		updateShip(gc, sbg, delta);
		if (SetupClass.isDEBUGGING)
			updateCounter++;
	}

	// GET INPUT
	protected void getInput(Input in) {
		if (in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			ship.shoot(1); // fire ship guns when player presses Spacebar
		if (in.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
			ship.shoot(0); // fire ship guns when player presses Spacebar
		if (in.isKeyDown(Input.KEY_W))
			ship.ForwardKeyPressed();
		if (in.isKeyDown(Input.KEY_A))
			ship.LeftKeyPressed();
		if (in.isKeyDown(Input.KEY_D))
			ship.RightKeyPressed();
		if (in.isKeyDown(Input.KEY_S))
			ship.BackKeyPressed();
	}
	// RENDER SHIP
	public void renderShip(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		ship.render(gc, sbg, g);
	}
	// UPDATE SHIP
	private void updateShip(GameContainer gc, StateBasedGame sbg, int delta) {
		ship.update(gc, sbg, delta);
	}
	public static Starship getShip() {
		return ship;
	}

	

}

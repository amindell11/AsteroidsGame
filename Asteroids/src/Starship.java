import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Starship extends ExplodingGameObject {

	private int maxSpeed;
	private float acceleration;
	private float velocityDecay;
	private float turnSpeed;
	private boolean accelerating, turningLeft, turningRight;
	private Image iconEnginesOff;
	private Image iconEnginesOn;
	Gun guns;

	// Default position at the center of the screen
	public Starship(String config) throws SlickException {
		super();
		Properties template = new Properties();
		try {
			template.load(new FileInputStream(config));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = Integer.parseInt(template.getProperty("width"));
		height = Integer.parseInt(template.getProperty("height"));
		iconEnginesOff = new Image(template.getProperty("iconEnginesOff"));
		iconEnginesOn = new Image(template.getProperty("iconEnginesOn"));
		maxSpeed = Integer.parseInt(template.getProperty("maxSpeed"));
		acceleration = Float.parseFloat(template.getProperty("acceleration"));
		velocityDecay =Float.parseFloat(template.getProperty("velocityDecay"));
		turnSpeed=Float.parseFloat(template.getProperty("turnSpeed"));
		int shotDelay = Integer.parseInt(template.getProperty("shotDelay"));
		String ammo=template.getProperty("ammo");
		guns = new Gun(new Image(ammo),shotDelay);
		ObjectImage = iconEnginesOff;
		alive = true;
		collisionModel = new Circle(pos.getX(), pos.getY(), height / 2);
	}

	// RENDER
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		super.render(gc, sbg, g);
		if (alive) {
			guns.render(gc, sbg, g);
		}
	}

	private void SetImage() {
		int v = (int) Math.ceil(Math.random() - .1);
		if (accelerating && v == 1)
			setObjectImage(iconEnginesOn);
		else
			setObjectImage(iconEnginesOff);
	}

	// UPDATE
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		guns.update(gc, sbg, delta);
		if (alive) {
			if (turningLeft)
				rotate((float) -turnSpeed * delta);
			if (turningRight)
				rotate((float) turnSpeed * delta);
			if (accelerating && speed.length() < maxSpeed)
				speed = speed.add(new Vector2f(getRotation())
						.scale(acceleration - 1));
			if (!accelerating)
				speed = speed.scale(velocityDecay);
			SetImage();
			turningLeft = false;
			accelerating = false;
			turningRight = false;
		}
		super.update(gc, sbg, delta);
	}

	public void shoot() {
		try {
			guns.shoot(pos.getX() + width / 2, pos.getY() + height / 2,
					getRotation());
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void ForwardKeyPressed() {
		accelerating = true;
	}

	public void LeftKeyPressed() {
		turningLeft = true;
	}

	public void RightKeyPressed() {
		turningRight = true;
	}

	public void BackKeyPressed() {

	}

	public ArrayList<GameObject> getProjectiles() {
		return guns.getMyLasers();
	}

	@Override
	protected boolean checkForCollision() {
		if (alive) {
			super.checkForCollision();
			return isCollidingWith(Play.getAsteroids())!=null;
		}
		return true;
	}
	protected void die() {
		super.die();
		if(!Active)Play.GameOver();
	}

	public String toString() {
		return "Player Starship";
	}
}

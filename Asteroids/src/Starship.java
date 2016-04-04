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
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.google.gson.Gson;

public class Starship extends ExplodingGameObject {

	private int maxSpeed;
	private float acceleration;
	private float velocityDecay;
	private float turnSpeed, turnAcceleration, turnDecay;
	private boolean accelerating, turningLeft, turningRight;
	private Image iconEnginesOff;
	private Image iconEnginesOn;
	float maxTurnVel;
	private ArrayList<Gun> guns;
	private float[] collisionPoints;

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
		velocityDecay = Float.parseFloat(template.getProperty("velocityDecay"));
		turnAcceleration = Float.parseFloat(template
				.getProperty("turnAcceleration"));
		turnDecay = Float.parseFloat(template.getProperty("turnDecay"));
		int shotDelay = Integer.parseInt(template.getProperty("shotDelay"));
		int ammoNumber=1;
		String ammo=null;
		guns = new ArrayList<>();
		while((ammo = template.getProperty("ammo"+ammoNumber))!= null){
			
		}
		guns.add(new Gun(new Missile(new Image("res/missile 1.png"), 1.2f, 1f,
				3, .5f), 1000));
		guns.add(new Gun(new Projectile(new Image("res/Beam1.png"), 30f, .5f),
				80));
		ObjectImage = iconEnginesOff;
		maxTurnVel= Float.parseFloat(template.getProperty("maxTurnVel"));
		collisionPoints = new Gson().fromJson(
				template.getProperty("collision"), float[].class);
		alive = true;
		turnSpeed = 0f;
	}

	// RENDER
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (alive) {
			for (Gun gun : guns) {
				gun.render(gc, sbg, g);
			}
		}
		super.render(gc, sbg, g);

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
		for (Gun gun : guns) {
			gun.update(gc, sbg, delta);
		}

		if (alive) {
			if (Math.abs(turnSpeed) < maxTurnVel) {
				if (turningLeft) {
					turnSpeed -= turnAcceleration * delta;
				} else if (turningRight) {
					turnSpeed += turnAcceleration * delta;
				}
			}
			if (!(turningRight||turningLeft)&&Math.abs(turnSpeed) > 0)
				turnSpeed *=turnDecay/delta;


			if (accelerating && speed.length() < maxSpeed)
				speed = speed.add(new Vector2f(getRotation())
						.scale(acceleration));
			if (!accelerating)
				speed = speed.scale(velocityDecay);
			SetImage();
			turningLeft = false;
			accelerating = false;
			turningRight = false;
			rotate((float) turnSpeed * delta);
		}
		super.update(gc, sbg, delta);

	}

	public Shape getCollisionInstance() {
		// float[]
		// points={30f,5f,30f,height-5f,width/2f,(float)height,(float)width,height/2f,width/2f,0f};
		// System.out.println(new Gson().toJson(points));
		return collisionPoints != null ? new Polygon(collisionPoints) : super
				.getCollisionInstance();
	}

	public void shoot(int gunIndex) {
		guns.get(gunIndex).shoot(pos.getX(), pos.getY(), getRotation());
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

	@Override
	protected boolean checkForCollision() {
		if (alive) {
			super.checkForCollision();
			GameObject collidingWith = isCollidingWith(Play.getAsteroids());
			if (collidingWith != null) {
				return true;
			}
			return false;
		}
		return true;
	}

	protected void die() {
		super.die();
		if (!active)
			Play.GameOver();
	}

	public String toString() {
		return "Player Starship";
	}
}

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

public class Starship extends GameObject {

	private int maxSpeed;
	private float acceleration;
	private float velocityDecay;
	private float turnSpeed, turnAcceleration, turnDecay;
	private boolean accelerating, turningLeft, turningRight;
	private String iconEnginesOff;
	private String iconEnginesOn;
	float maxTurnVel;
	private ArrayList<Gun> guns;

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
		iconEnginesOff = template.getProperty("iconEnginesOff");
		iconEnginesOn = template.getProperty("iconEnginesOn");
		maxSpeed = Integer.parseInt(template.getProperty("maxSpeed"));
		acceleration = Float.parseFloat(template.getProperty("acceleration"));
		velocityDecay = Float.parseFloat(template.getProperty("velocityDecay"));
		turnAcceleration = Float.parseFloat(template.getProperty("turnAcceleration"));
		turnDecay = Float.parseFloat(template.getProperty("turnDecay"));
		int shotDelay = Integer.parseInt(template.getProperty("shotDelay"));
		String ammo = template.getProperty("ammo");
		guns = new ArrayList<>();
		guns.add(new Gun(new Missile("res/missile 1.png", 1.2f, 1f, 3, .5f), 4000));
		guns.add(new Gun(new Projectile("res/Beam1.png", 30f, .5f), 500));
		maxTurnVel = Float.parseFloat(template.getProperty("maxTurnVel"));
		collisionPoints = new Gson().fromJson(template.getProperty("collision"), float[].class);
		turnSpeed = 0f;
	}

	// RENDER
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (active) {
			for (Gun gun : guns) {
				gun.render(gc, g);
			}
		}
		super.render(gc, g);

	}
	@Override
	public void setObjectImage(){
		System.out.println("setting Image");
		System.out.println(iconEnginesOn);
		int v = (int) Math.ceil(Math.random() - .1);
		if (accelerating && v == 1)
			setObjectImage(iconEnginesOn);
		else
			setObjectImage(iconEnginesOff);
	}

	// UPDATE
	public void update(GameContainer gc, int delta) throws SlickException {
		for (Gun gun : guns) {
			gun.update(gc, delta);
		}

		if (active) {
			if (Math.abs(turnSpeed) < maxTurnVel) {
				if (turningLeft) {
					turnSpeed -= turnAcceleration * delta;
				} else if (turningRight) {
					turnSpeed += turnAcceleration * delta;
				}
			}
			if (Math.abs(turnSpeed) > 0)
				turnSpeed -= (Math.abs(turnSpeed) / turnSpeed) * turnDecay * delta;

			if (accelerating && speed.length() < maxSpeed)
				speed = speed.add(new Vector2f(getRotation()).scale(acceleration));
			if (!accelerating)
				speed = speed.scale(velocityDecay);
			turningLeft = false;
			accelerating = false;
			turningRight = false;
			rotate((float) turnSpeed * delta);
		}
		super.update(gc, delta);

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
	public void die() {
		super.die();
	}

	public String toString() {
		return "Player Starship";
	}

	public ArrayList<Projectile> getProjectiles(){
		ArrayList<Projectile> list=new ArrayList<>();
		for(Gun gun:guns){
			list.addAll(gun.getMyLasers());
		}
		return list;
	}
}

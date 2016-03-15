package Game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Starship extends ExplodingGameObject {

	private int maxSpeed;
	private float acceleration;
	private float velocityDecay;
	private float turnSpeed;
	private boolean accelerating, turningLeft, turningRight;
	private Image iconEnginesOff;
	private Image iconEnginesOn;
	private ArrayList<Gun> guns;
	private float[] collisionPoints;
	// Default position at the center of the screen
	public Starship() throws SlickException{
		this("res/shipTemplate.cfg");
	}
	public Starship(String config) throws SlickException{
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
		guns=new ArrayList<>();
		guns.add(new Gun(new Missile(new Image("res/missile 1.png"),1.2f,1f,3,.5f),4000));
		guns.add(new Gun(new Projectile(new Image("res/Beam1.png"),30f,.5f),500));
		ObjectImage = iconEnginesOff;
		collisionPoints=new Gson().fromJson(template.getProperty("collision"),float[].class);
		alive = true;
	}

	// RENDER
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (alive) {
			for(Gun gun:guns){
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
		for(Gun gun:guns){
			gun.update(gc, sbg, delta);
		}

		if (alive) {
			if (turningLeft)
				rotate((float) -turnSpeed * delta);
			if (turningRight)
				rotate((float) turnSpeed * delta);
			if (accelerating && speed.length() < maxSpeed)
				speed = speed.add(new Vector2f(getRotation())
						.scale(acceleration));
			if (!accelerating)
				speed = speed.scale(velocityDecay);
			SetImage();
			turningLeft = false;
			accelerating = false;
			turningRight = false;
		}
		super.update(gc, sbg, delta);

	}
	public HashMap<TypeToken,String> getJsonRepresentation(Gson jsonParser){
		HashMap<TypeToken,String> map=new HashMap<>();
		map.put(posJson=jsonParser.toJson(pos));

	}
	public Shape getCollisionInstance(){
		//float[] points={30f,5f,30f,height-5f,width/2f,(float)height,(float)width,height/2f,width/2f,0f};
		//System.out.println(new Gson().toJson(points));
		return collisionPoints!=null?new Polygon(collisionPoints):super.getCollisionInstance();
	}
	public void shoot(int gunIndex) {
			guns.get(gunIndex).shoot(pos.getX(), pos.getY(),
					getRotation());
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
			GameObject collidingWith = isCollidingWith(SoloPlay.getAsteroids());
			if(collidingWith!=null){
				return true;
			}
			return false;
		}
		return true;
	}
	protected void die() {
		super.die();
		if(!active)SoloPlay.GameOver();
	}
	public ArrayList<GameObject> getAllProjectiles(){
		ArrayList<GameObject> projectiles=new ArrayList<>();
		for(Gun g:guns){
			projectiles.addAll(g.getBullets());
		}
		return projectiles;
	}
	public String toString() {
		return "Player Starship";
	}
}


import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import com.google.gson.Gson;

public abstract class GameObject {
	protected Vector2f pos;
	protected Vector2f speed;
	protected int width;
	protected int height;
	protected transient Image ObjectImage;
	protected String path;
	protected float rotation;
	protected boolean active;
	protected float[] collisionPoints;
	protected transient Polygon collisionModel;

	public GameObject() {
		this(new Vector2f(0, 0), new Vector2f(0, 0), 0, 0, "Ship.png");
	}

	public GameObject(Vector2f pos, Vector2f speed, int width, int height, String path) {
		active = true;
		this.pos = pos;
		this.speed = speed;
		this.height = height;
		this.width = width;
		this.path = path;
		collisionPoints=new Circle(height/2, height/2, height/2).getPoints();
	}

	/**
	 * render draws ObjectImage at pos coordinates
	 * 
	 * @param gc
	 * @param sbg
	 * @param g
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (active) {
			g.draw(getCollisionModel());
			try {
				setObjectImage();
			} catch (NullPointerException e) {
				System.out.println("aa");
				ObjectImage = new Image(10, 10);
			}
			ObjectImage.draw(pos.getX() - width / 2, pos.getY() - height / 2, width, height);
		}
	}
	/**
	 * update calls move(),wrapOnScreen(), and checkForCollision()
	 * 
	 * @param gc
	 * @param sbg
	 * @param delta
	 * @throws SlickException
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		if (active) {
			move();
			wrapOnScreen();
		}
	}

	// WRAP OBJECT TO VISIBLE SCREEN SPACE
	private void wrapOnScreen() {
		if (pos.getX() > 1280)
			pos.set(0, pos.getY());
		if (pos.getX() < 0)
			pos.set(1280, pos.getY());
		if (pos.getY() > 800)
			pos.set(pos.getX(), 0);
		if (pos.getY() < 0)
			pos.set(pos.getX(), 800);
	}

	public <T extends GameObject> T isCollidingWith(ArrayList<T> anyOfTheseGameObjects) {
		for (T object : anyOfTheseGameObjects) {
			T collisionObject = isCollidingWith(object);
			if (collisionObject != null)
				return collisionObject;
		}
		return null;

	}

	public <T extends GameObject> T isCollidingWith(T thisGameObject) {
		if (thisGameObject.active&&thisGameObject.getCollisionModel().intersects(this.getCollisionModel())) {
			return thisGameObject;
		}
		return null;

	}

	public Polygon getCollisionModel() {
		collisionModel = new Polygon(collisionPoints);
		Polygon collisionTemp=(Polygon) collisionModel.transform(Transform.createTranslateTransform(pos.getX()-width/2, pos.getY()-height/2));
		collisionModel=(Polygon) collisionTemp.transform(Transform.createRotateTransform((float) Math.toRadians(getRotation()), pos.x,pos.y));
		return collisionModel;
	}

	// CHANGE POS BY SPEED
	private void move() {
		pos.add(speed);
	}

	public void die() {
		active = false;
	}

	public float getX() {
		return pos.getX();
	}

	public float getY() {
		return pos.getY();
	}

	public float getRotation() {
		return rotation;
	}

	// SETTERS
	protected void setRotation(float theta) {
		rotation = theta;
	}

	protected void rotate(float theta) {
		rotation += theta;
	}

	protected void setObjectImage(String image) {
		try {
			ObjectImage = new Image(image).getScaledCopy(width, height);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		updateImageRotation();
	}
	protected void setObjectImage() {
		setObjectImage(path);
	}

	public void updateImageRotation() {
		ObjectImage.setCenterOfRotation(width / 2, height / 2);
		ObjectImage.setRotation(rotation);
	}

	public boolean isActive() {
		return active;
	}

}

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameObject {
	protected Vector2f pos;
	protected Vector2f speed;
	protected int width;
	protected int height;
	protected transient Image ObjectImage;
	protected String path;
	protected float rotation;
	public GameObject() {
		this(new Vector2f(0,0),new Vector2f(0, 0),0,0,"Ship.png");
	}

	public GameObject(Vector2f pos, Vector2f speed, int width, int height,String path) {
		this.pos=pos;
		this.speed = speed;
		this.height = height;
		this.width = width;
		this.path=path;
	}

	/**
	 * render draws ObjectImage at pos coordinates
	 * 
	 * @param gc
	 * @param sbg
	 * @param g
	 */
	public void render(GameContainer gc,Graphics g) throws SlickException {
		setObjectImage(new Image(path));
		g.setColor(Color.white);
		g.fillOval(ObjectImage.getCenterOfRotationX()-25, ObjectImage.getCenterOfRotationX()-25, 50, 50);
		ObjectImage.draw(pos.getX()-width/2, pos.getY()-height/2, width, height);
	}
	public Shape getCollisionInstance(){
		return new Circle(height/2, height/2, height/2);
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
				move();
				wrapOnScreen();
	}

	// WRAP OBJECT TO VISIBLE SCREEN SPACE
	private void wrapOnScreen() {
		if (pos.getX()> 1280)
			pos.set(0, pos.getY());
		if (pos.getX()< 0)
			pos.set(1280, pos.getY());
		if (pos.getY()> 800)
			pos.set(pos.getX(), 0);
		if (pos.getY() < 0)
			pos.set(pos.getX(),800);
	}

	// CHANGE POS BY SPEED
	private void move() {
		pos.add(speed);
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
	protected void setRotation(float theta) 
	{
		rotation=theta;
	}

	protected void rotate(float theta) {
		rotation+=theta;
	}

	protected void setObjectImage(Image image) {
		ObjectImage = image.getScaledCopy(width, height);
		updateImageRotation();
	}
	public void updateImageRotation(){
		ObjectImage.setCenterOfRotation(width/2, height/2);
		ObjectImage.setRotation(rotation);
	}

}

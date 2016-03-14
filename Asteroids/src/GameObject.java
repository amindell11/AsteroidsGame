import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
	protected Image ObjectImage;
	protected Shape collisionModel;
	protected boolean active;

	public GameObject() {
		this(new Vector2f(SetupClass.ScreenWidth / 2,
				SetupClass.ScreenHeight / 2),new Vector2f(0, 0),0,0);
	}

	public GameObject(Vector2f pos, Vector2f speed, int width, int height) {
		this.pos = pos;
		this.speed = speed;
		this.height = height;
		this.width = width;
		active = true;
		collisionModel=getCollisionInstance();
	}

	/**
	 * render draws ObjectImage at pos coordinates
	 * 
	 * @param gc
	 * @param sbg
	 * @param g
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		ObjectImage.draw(pos.getX()-width/2, pos.getY()-height/2, width, height);
		if (SetupClass.isDEBUGGING)
			renderDEBUG(gc, sbg, g);
	}

	public void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g) {
		//g.rotate(collisionModel.getCenterX(), collisionModel.getCenterY(), getRotation());
		//g.draw(collisionModel);
		//g.rotate(collisionModel.getCenterX(), collisionModel.getCenterY(), -getRotation());
		g.draw(collisionModel);
		//g.draw(new Circle(pos.x-5,pos.y-5,10));
		g.drawLine(pos.x, 0, pos.x,pos.y);
		g.drawLine(0, pos.y, pos.x,pos.y);
		g.drawLine(pos.x + speed.x * 50, pos.y + speed.y * 50, pos.x, pos.y);

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
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
			if (checkForCollision()) {
				die();
			} else {
				move();
				wrapOnScreen();
				Shape collisionTemp=getCollisionInstance().transform(Transform.createTranslateTransform(pos.getX()-width/2, pos.getY()-height/2));
				collisionModel=collisionTemp.transform(Transform.createRotateTransform((float) Math.toRadians(getRotation()), pos.x,pos.y));
			}
	}

	// WRAP OBJECT TO VISIBLE SCREEN SPACE
	private void wrapOnScreen() {
		if (pos.getX()/* - width / 2*/ > SetupClass.ScreenWidth)
			pos.set(0, pos.getY());
		if (pos.getX() /*+ width / 2 */< 0)
			pos.set(SetupClass.ScreenWidth, pos.getY());
		if (pos.getY()/* - height / 2*/> SetupClass.ScreenHeight)
			pos.set(pos.getX(), 0);
		if (pos.getY() /*+ height / 2 */< 0)
			pos.set(pos.getX(), SetupClass.ScreenHeight);
	}

	// CHANGE POS BY SPEED
	private void move() {
		pos.add(speed);
	}

	/**
	 * checkForCollision implemented in subclasses define which objects to
	 * collide with should call isCollidingWith for collidable objects/lists
	 */
	protected boolean checkForCollision() {
		return false;
	}

	/**
	 * isCollidingWith checks for collision with a single other GameObject
	 * 
	 * @param CollisionObject
	 * @return true if GameObject is colliding with CollisionObject
	 */
	protected boolean isCollidingWith(GameObject CollisionObject) {
		if (collisionModel.intersects(CollisionObject.getCollisionModel()))
			return true;
		else
			return false;
	}

	/**
	 * isCollidingWith checks for collision with multiple GameObjects
	 * 
	 * @param CollisionList
	 * @return the first CollisionObject which is touching this GameObject
	 * @return null if no collision is present
	 */
	protected GameObject isCollidingWith(ArrayList<GameObject> CollisionList) {
		for (GameObject CollisionObject : CollisionList) {
			if (isCollidingWith(CollisionObject)) {
				return CollisionObject;
			}
		}
		return null;
	}

	protected void die() {
		active = false;
	}

	// GETTERS
	public Shape getCollisionModel() {
		return collisionModel;
	}

	public boolean isActive() {
		return active;
	}

	public float getX() {
		return pos.getX();
	}

	public float getY() {
		return pos.getY();
	}

	public float getRotation() {
		return ObjectImage.getRotation();
	}

	// SETTERS
	protected void setRotation(float theta) {
		rotate(theta-getRotation());
	}

	protected void rotate(float theta) {
		ObjectImage.setCenterOfRotation(width / 2, height / 2);
		ObjectImage.rotate(theta);
	}

	protected void setObjectImage(Image image) {
		float theta = ObjectImage.getRotation();
		ObjectImage = image;
		setRotation(theta);
	}

}

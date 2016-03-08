import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameObject {
	protected Vector2f pos;
	protected Vector2f speed;
	protected int width;
	protected int height;
	protected Image ObjectImage;
	protected Shape collisionModel;
	protected boolean Active;

	public GameObject() {
		Active=true;
		height = 0;
		width = 0;
		pos = new Vector2f(SetupClass.ScreenWidth / 2 - width / 2,
				SetupClass.ScreenHeight / 2 - height / 2);
		speed = new Vector2f(0, 0);
	}

	public GameObject(Vector2f pos, Vector2f speed, int width, int height) {
		this.pos = pos;
		this.speed = speed;
		this.height = height;
		this.width = width;
		Active = true;
		collisionModel = new Circle(pos.getX(), pos.getY(), height / 2);
	}

	/**
	 * render draws ObjectImage at pos coordinates
	 * 
	 * @param gc
	 * @param sbg
	 * @param g
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		ObjectImage.draw(pos.getX(), pos.getY(), width, height);
		if (SetupClass.isDEBUGGING)
			renderDEBUG(gc, sbg, g);
	}

	public void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.draw(collisionModel);
		g.drawLine(pos.getX() + width / 2, 0, pos.getX() + width / 2,
				pos.getY() + height / 2);
		g.drawLine(0, pos.getY() + width / 2, pos.getX() + width / 2,
				pos.getY() + height / 2);
		g.drawLine(pos.getX() + width / 2 + speed.getX() * 50, pos.getY()
				+ height / 2 + speed.getY() * height / 2, pos.getX() + width
				/ 2, pos.getY() + height / 2);
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
		collisionModel.setCenterX(pos.getX()+width/2);
		collisionModel.setCenterY(pos.getY()+height/2);
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
		Active = false;
	}

	// GETTERS
	public Shape getCollisionModel() {
		return collisionModel;
	}

	public boolean isActive() {
		return Active;
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
		ObjectImage.setRotation(theta);
	}

	protected void rotate(float theta) {
		ObjectImage.rotate(theta);
	}

	protected void setObjectImage(Image image) {
		float theta = ObjectImage.getRotation();
		ObjectImage = image;
		ObjectImage.setCenterOfRotation(width / 2, height / 2);
		setRotation(theta);
	}

}

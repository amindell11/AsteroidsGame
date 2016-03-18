import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Starship extends GameObject {

	private int maxSpeed=70;
	private float acceleration=1.33f;
	private float velocityDecay=.99f;
	private float turnSpeed=.22f;
	private boolean accelerating, turningLeft, turningRight;
	private Gun mainGun;
	public Starship(int width, int height,String path) {
		super(new Vector2f(0,0), new Vector2f(0,0), width, height,path);
		mainGun=new Gun(new Projectile("Beam1.png",30f,.5f),500);
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		mainGun.update(gc, delta);
			setObjectImage(new Image(path));
			if (turningLeft)
				rotate((float) -turnSpeed * delta);
			if (turningRight)
				rotate((float) turnSpeed * delta);
			if (accelerating && speed.length() < maxSpeed)
				speed = speed.add(new Vector2f(getRotation())
						.scale(acceleration));
			if (!accelerating)
				speed = speed.scale(velocityDecay);
			turningLeft = false;
			accelerating = false;
			turningRight = false;
		super.update(gc, delta);
	}
	public void render(GameContainer gc, Graphics g) throws SlickException{
		super.render(gc, g);
		mainGun.render(gc, g);
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
	public void shoot(){
	mainGun.shoot(pos.getX(), pos.getY(),getRotation());
	}
}

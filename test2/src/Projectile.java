import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Projectile extends GameObject{
	private double lived = 0;	
	protected double maxLifeTime;
	float launchVelocity;
	protected Projectile(Vector2f pos, double rotation,String laserImage,float launchVelocity,double maxLifeTime){
		super(pos,new Vector2f(rotation).scale(launchVelocity),40,20, laserImage);
		this.maxLifeTime=maxLifeTime;
		this.launchVelocity=launchVelocity;
		setRotation((float)Math.toRadians(180+Math.toDegrees((float)this.speed.getTheta())));
	}
	public Projectile(String laserImage, float launchVelocity, double maxLifeTime){
		this(new Vector2f(0,0),0,laserImage,launchVelocity,maxLifeTime);
		active=false;
	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		setRotation((float)speed.getTheta());
		if(active){
			super.update(gc, delta);
			lived+=.001*delta;
			if(lived>maxLifeTime){
				die();
			}
		}
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		if(active){
			super.render(gc, g);
		}
	}
	public Projectile getInstance(Vector2f pos, double rotation){
		return new Projectile(pos,rotation,path,launchVelocity,maxLifeTime);
	}
	public Shape getCollisionInstance(){
		return new Rectangle(0,0, width,height);
	}
	public void die(){
		active=false;
	}
	public boolean isActive(){
		return active;
	}
}

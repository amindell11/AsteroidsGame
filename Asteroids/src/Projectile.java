import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Projectile extends GameObject implements UpdatingObject{
	private double lived = 0;	
	protected double maxLifeTime;
	float launchVelocity;
	protected Projectile(Vector2f pos, double rotation, Image laserImage,float launchVelocity,double maxLifeTime){
		super(pos,new Vector2f(rotation).scale(launchVelocity),40,20);
		this.maxLifeTime=maxLifeTime;
		this.launchVelocity=launchVelocity;
		ObjectImage=laserImage;
		setRotation((float)Math.toRadians(180+Math.toDegrees((float)this.speed.getTheta())));
	}
	public Projectile(Image laserImage, float launchVelocity, double maxLifeTime){
		this(new Vector2f(0,0),0,laserImage,launchVelocity,maxLifeTime);
		active=false;
	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		setRotation((float)speed.getTheta());
		if(isActive()){
			super.update(gc, sbg, delta);
			lived+=.001*delta;
			if(lived>maxLifeTime){
				die();
			}
		}
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g){
		if(isActive()){
			super.render(gc, sbg, g);
		}
	}
	public Projectile getInstance(Vector2f pos, double rotation){
		return new Projectile(pos,rotation,ObjectImage.copy(),launchVelocity,maxLifeTime);
	}
	public Shape getCollisionInstance(){
		return new Rectangle(0,0, width,height);
	}
	@Override
	protected boolean checkForCollision() {
		GameObject collidingWith = super.isCollidingWith(Play.getAsteroids());
		if(collidingWith!=null&&((ExplodingGameObject) collidingWith).isAlive()){
			collidingWith.die();
			die();
			return true;
		}
		return false;
	}
	public void die(){
		super.die();
	}
}

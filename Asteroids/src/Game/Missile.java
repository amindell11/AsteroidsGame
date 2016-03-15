package Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Missile extends Projectile{
	private float acceleration;
	private int hitsLeft;
	public Missile(Image laserImage, float launchVelocity, double maxLifeTime,int hits,float acceleration) {
		super(laserImage, launchVelocity, maxLifeTime);
		this.hitsLeft=hits;
		this.acceleration=acceleration;
	}
	public Missile(Vector2f pos,double rotation,Image laserImage, float launchVelocity, double maxLifeTime,int hits,float acceleration){
		super(pos,rotation,laserImage, launchVelocity, maxLifeTime);
		this.hitsLeft=hits;
		this.acceleration=acceleration;
	}
	@Override
	public void update(GameContainer gc, StateBasedGame sbg,int delta){
		super.update(gc, sbg, delta);
		speed=speed.add(new Vector2f(getRotation())
		.scale(acceleration));
	}
	public void die(){
		if(hitsLeft<=0){
			super.die();
		}else{
			hitsLeft--;
		}
	}
	@Override
	public Projectile getInstance(Vector2f pos,double rotation){
		return new Missile(pos,rotation,ObjectImage,launchVelocity,maxLifeTime,hitsLeft,acceleration);
	}

}

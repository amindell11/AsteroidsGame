import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Projectile extends GameObject implements UpdatingObject{
	private double lived = 0;	
	private double maxLifeTime;
	float launchVelocity;
	private Projectile(Vector2f pos, double rotation, Image laserImage,float launchVelocity,double maxLifeTime){
		super(pos,new Vector2f(rotation).scale(launchVelocity),40,20);
		this.maxLifeTime=maxLifeTime;
		this.launchVelocity=launchVelocity;
		ObjectImage=laserImage;
		ObjectImage.setCenterOfRotation(width/2, height/2);
		ObjectImage.setRotation((float)Math.toRadians(180+Math.toDegrees((float)this.speed.getTheta())));
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
			renderDEBUG(gc, sbg, g);
		}
	}
	
	public void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g){
		if(SetupClass.isDEBUGGING){
			g.drawRect(pos.getX(), pos.getY(), 40, 20);
			g.drawLine(pos.getX()+20,0,pos.getX()+20,pos.getY()+10);
			g.drawLine(0,pos.getY()+10,pos.getX()+20, pos.getY()+10);
			g.drawLine(pos.getX()+20+speed.getX()*100,pos.getY()+10+speed.getY()*100,pos.getX()+20, pos.getY()+20);

		}
	}		
	public Projectile getInstance(Vector2f pos, double rotation){
		return new Projectile(pos,rotation,ObjectImage.copy(),launchVelocity,maxLifeTime);
	}
	@Override
	protected boolean checkForCollision() {
		GameObject collidingWith = super.isCollidingWith(Play.getAsteroids());
		return(collidingWith!=null);
	}
	public void die(){
		super.die();
	}
}

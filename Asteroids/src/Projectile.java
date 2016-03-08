import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Projectile extends GameObject implements UpdatingObject{
	private double lived = 0;
	private double MAX_LIFETIME = .5;
	private Gun source;
	private int acceleration;
	
	
	public Projectile(Vector2f pos, Vector2f speed, Image laserImage, Gun source, int acceleration){
		super(pos,speed,40,20);
		ObjectImage=laserImage;
		ObjectImage.setCenterOfRotation(width/2, height/2);
		ObjectImage.setRotation((float)Math.toRadians(180+Math.toDegrees((float)this.speed.getTheta())));
		this.source=source;
		this.acceleration=acceleration;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		setRotation((float)speed.getTheta());
		if(isActive()){
			super.update(gc, sbg, delta);
			lived+=.001*delta;
			if(lived>MAX_LIFETIME)die();
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
	public Gun getSource(){
		return source;
	}
	@Override
	protected boolean checkForCollision() {
		GameObject collidingWith = super.isCollidingWith(Play.getAsteroids());
		return(collidingWith!=null);
	}
}

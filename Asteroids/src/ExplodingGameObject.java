import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class ExplodingGameObject extends GameObject{
	protected boolean alive;
	private SpriteSheet explosion;
	private Animation explode;
	public ExplodingGameObject() throws SlickException{
		alive=true;
		explosion = new SpriteSheet("res/Explosion.png", 27,27);
		explode = new Animation(explosion, 100);
		explode.stopAt(43);
		explode.setSpeed(3);
	}
	public ExplodingGameObject(Vector2f randomPos, Vector2f randomSpeed,
			int size, int size2) {
		super(randomPos,randomSpeed,size,size2);
		alive=true;
		try {
			explosion = new SpriteSheet("res/Explosion.png", 27,27);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		explode = new Animation(explosion, 100);
		explode.stopAt(43);
		explode.setSpeed(3);
	}
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if(alive)super.render(gc, sbg, g);
		else explode.draw(pos.getX(),pos.getY(), width,height);
	}
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		if(alive){
		super.update(gc, sbg, delta);
		}else{
			if(checkForCollision())die();
		}
	}
	protected void die(){
		if((!alive)&&explode.isStopped()){
			super.die();
		}
		alive=false;
	}
	public boolean isAlive(){
		return alive;
	}
	@Override
	public Shape getCollisionModel(){
		if(alive)return collisionModel;
		else return new Circle(0,0,0);
	}
}	

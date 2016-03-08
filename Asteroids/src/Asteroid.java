import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Asteroid extends ExplodingGameObject{

	static final float MaxVelocity = 2;
	static final float MinVelocity = .2f;
	static final float angularVel=1;//degrees per sec
	static final int maxDiam=225;
	static final int minDiam=150;
	private int hitsLeft;
	private static Random rnd=new Random();
	
	public Asteroid(Vector2f pos, int size,int numSplit){
		this(size, numSplit);
		this.pos=pos.copy();
	}
	
	public Asteroid(int size, int numSplit) {
		super(getRandomPos(),getRandomSpeed(),size,size);
		try {
			ObjectImage = getRandomImage();
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		hitsLeft=numSplit;
	}
	
	public Asteroid(){
		this(getRandomSize(), 3);
	}
	
	// Random Generators
		private static Image getRandomImage() throws SlickException {
		String[] files = {"res/DeadAsteroid2.png"};
		int randomFileIndex=rnd.nextInt(files.length);
		return(new Image(files[randomFileIndex]));
	}
	
		private static int getRandomSize() {
		return((int) (rnd.nextInt(maxDiam-minDiam) + minDiam));
	}
	
		private static Vector2f getRandomPos(){
		int posX=getRandomWithExclusion(rnd,0,SetupClass.ScreenWidth, (int)(Play.getShip()).getX());
		int posY=getRandomWithExclusion(rnd,0,SetupClass.ScreenHeight, (int)(Play.getShip()).getY());
		return new Vector2f(posX,posY);
	}
	
		private static Vector2f getRandomSpeed(){
		return new Vector2f(Math.random() * 360).scale((float) (Math.random()* (MaxVelocity - MinVelocity) + MinVelocity));
	}
	
		private static int getRandomWithExclusion(Random rnd, int start, int end, int... exclude) {
	    int random = start + rnd.nextInt(end - start + 1 - exclude.length);
	    for (int ex : exclude) {
	        if (random < ex) {
	            break;
	        }
	        random++;
	    }
	    return random;
	}


	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		rotate((float).001*angularVel/delta);
		super.update(gc, sbg, delta);
	}

	@Override
	protected boolean checkForCollision() {
		super.checkForCollision();
		if(alive){
		GameObject collidingWith = isCollidingWith(Play.getShip().getProjectiles());
		if(collidingWith!=null){
			collidingWith.die();
			return true;
		}
		return false;
		}
		return true;
	}
	
	@Override
	protected void die(){
		if(alive&&hitsLeft>1)splitAsteroid();
		super.die();
	}
	public void splitAsteroid(){
		for(int c = 0;c<hitsLeft;c++){
			Asteroid a = new Asteroid(pos,(int)(width/Math.sqrt((double)hitsLeft)),hitsLeft-1);
			Play.addAsteroid(a);
			}
	}
}

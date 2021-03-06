import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Asteroid extends ExplodingGameObject {

	static final float MaxVelocity = 2;
	static final float MinVelocity = .2f;
	static final float angularVel = .00f;
	static final int maxDiam = 225;
	static final int minDiam = 150;
	private int hitsLeft;
	private static Random rnd = new Random();

	public Asteroid(Vector2f pos, int size, int numSplit) {
		this(size, numSplit);
		this.pos = pos.copy();
	}

	public Asteroid(int size, int numSplit) {
		super(getRandomPos(), getRandomSpeed(), size, size);
		try {
			ObjectImage = getRandomImage();
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		hitsLeft = numSplit;
	}

	public Asteroid() {
		this(getRandomSize(), 3);
	}

	// Random Generators
	private static Image getRandomImage() throws SlickException {
		String[] files = { "res/DeadAsteroid2.png" };
		int randomFileIndex = rnd.nextInt(files.length);
		return (new Image(files[randomFileIndex]));
	}

	private static int getRandomSize() {
		return ((int) (rnd.nextInt(maxDiam - minDiam) + minDiam));
	}
	private static Vector2f getRandomPos() {
		int posX = getRandomCoord(rnd, 0,SetupClass.ScreenWidth);
		int posY = getRandomCoord(rnd, 0,SetupClass.ScreenHeight);
		Vector2f vector2f = new Vector2f(posX, posY);
		System.out.println(vector2f);
		return vector2f;
	}

	private static Vector2f getRandomSpeed() {
		return new Vector2f(Math.random() * 360)
				.scale((float) (Math.random() * (MaxVelocity - MinVelocity) + MinVelocity));
	}
	
	private static int getRandomCoord(Random rnd,int min, int max){
		return rnd.nextInt(max+min)+min;
	}


	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		rotate((float) angularVel * delta);
		super.update(gc, sbg, delta);
	}

	@Override
	protected boolean checkForCollision() {
		if (alive) {
			return false;
		}
		return true;
	}

	@Override
	protected void die() {
		if (alive && hitsLeft > 1) {
			splitAsteroid();
		}
		Play.addToScore(hitsLeft*100, this);
		super.die();
	}
	public int getHitsLeft() {
		return hitsLeft;
	}

	public void splitAsteroid() {
		for (int c = 0; c < hitsLeft; c++) {
			Asteroid a = new Asteroid(pos, (int) (width / Math.sqrt((double) hitsLeft)), hitsLeft - 1);
			Play.addAsteroid(a);
		}
	}
}

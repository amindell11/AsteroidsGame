import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface UpdatingObject {
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g);
	public void update(GameContainer gc, StateBasedGame sbg, int delta);
	public void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g);
}

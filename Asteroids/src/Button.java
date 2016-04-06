
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Button implements UpdatingObject{
	private Image buttonImage;
	Rectangle clickBox;
	private Runnable onClick;
	public Button(Image image,int x,int y,int width, int height, Runnable onClick){
		clickBox=new Rectangle(x,y,width,height);
		this.onClick=onClick;
		this.buttonImage=image;
	}
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		buttonImage.draw(clickBox.getX(),clickBox.getY(),clickBox.getWidth(),clickBox.getHeight());
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		// TODO Auto-generated method stub
		Input i=gc.getInput();
		if (clickBox.contains(i.getMouseX(),i.getMouseY())&&i.isMouseButtonDown(0)){
			onClick.run();
		}
	}
	@Override
	public void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// TODO Auto-generated method stub
		
	}

}

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Box {
	int width;
	int height;
	int x;
	int y;
	public Box(int width,int height){
		this.x=0;
		this.y=0;
		this.width=width;
		this.height=height;
	}
    public void update(GameContainer container, int delta) throws SlickException
    {
    }
    public void move(int x, int y){
    	this.x+=x;
    	this.y+=y;
    }
    public void render(GameContainer container, Graphics g) throws SlickException
    {
    	g.drawRect(x,y,width,height);
    }
}

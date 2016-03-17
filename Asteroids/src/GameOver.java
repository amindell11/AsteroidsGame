import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class GameOver extends BasicGameState {
	private Image background;
	StateBasedGame sbg;
	GameContainer g;
	public GameOver(int i){	
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		background = new Image("res/stars.jpg");
		sbg=arg1;
	}
	@Override
	public void keyPressed(int key, char c){
		try {
			sbg.getState(SetupClass.PLAY).init(g, sbg);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		sbg.enterState(SetupClass.PLAY);
	}
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		background.draw();
		g.setFont(new TrueTypeFont(new Font("Helvetica", Font.BOLD, 42),true));
		String s="Game Over. Press any key to play again";
		g.drawString(s, gc.getWidth()/2-new TrueTypeFont(new Font("Helvetica", Font.BOLD, 42),true).getWidth(s)/2, gc.getHeight()/2);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		return SetupClass.GAME_OVER;
	}

}

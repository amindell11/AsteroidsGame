import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Gun implements UpdatingObject {
	private ArrayList<GameObject> bullets;
	private int timeSinceFired;
	private int cooldown;
	private Projectile ammo;
	public Gun(Projectile ammo) {
		this.ammo=ammo;
		bullets = new ArrayList<GameObject>();
		this.cooldown=3000;
		timeSinceFired=cooldown;
	}
	public Gun(Projectile ammo, int cooldown) {
		this(ammo);
		this.cooldown=cooldown;
		timeSinceFired=cooldown;
	}
	private void addBullet(Projectile b){
		bullets.add(b);
	}
	public ArrayList<GameObject> getMyLasers(){
		return bullets;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g){
		for (GameObject b : bullets) {
			b.render(gc, sbg, g);
		}
		
	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		timeSinceFired+=delta;
		Iterator<GameObject> j = bullets.iterator();
		while (j.hasNext()) {
			GameObject p = j.next();
			p.update(gc, sbg, delta);
			if (!p.isActive()) {
				j.remove();
			}
		}
	}
	
	public void shoot(float beamx, float beamy, float posRotation){
		if(timeSinceFired>=cooldown){
			addBullet(ammo.getInstance(new Vector2f(beamx, beamy),posRotation));
			timeSinceFired=0;
		}
	}
	@Override
	public void renderDEBUG(GameContainer gc, StateBasedGame sbg, Graphics g) {
		
	}

}


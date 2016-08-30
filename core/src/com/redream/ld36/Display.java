package com.redream.ld36;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class Display {
	public boolean applyCam = true;
	private final SpriteBatch batch;
	
	private final ShapeRenderer shapeRenderer;

	public boolean debug = false;

	private final Comparator<Sprite> zSort = new Comparator<Sprite>() {
		public int compare(Sprite p1, Sprite p2) {
			return (p1.z > p2.z ? 1 : (p1.z == p2.z ? 0 : -1));
		}
	};
	public List<Sprite> renderQueue = new ArrayList<Sprite>();
	public List<Sprite> renderQueueHUD = new ArrayList<Sprite>();

	public Display(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		this.batch = batch;
		this.shapeRenderer = shapeRenderer;
	}

	public void render() {
		Collections.sort(this.renderQueue, zSort);
		
		this.renderList(this.renderQueue);
		
	}

	private void renderList(List<Sprite> renderQueue) {
		int rs = renderQueue.size();
		
		for (int i = 0; i < rs; i++) {
			Sprite s = renderQueue.get(i);
			s.render(shapeRenderer);
			s.render(batch);
		}
		
	}

	public void queueRender(Sprite r) {
		this.renderQueue.add(r);

	}

}
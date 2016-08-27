package com.redream.ld36;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Display {
	public boolean applyCam = true;
	private final SpriteBatch batch;

	public boolean debug = false;

	private final Comparator<Sprite> zSort = new Comparator<Sprite>() {
		public int compare(Sprite p1, Sprite p2) {
			return (p1.z > p2.z ? 1 : (p1.z == p2.z ? 0 : -1));
		}
	};
	public List<Sprite> renderQueue = new ArrayList<Sprite>();
	public List<Sprite> renderQueueHUD = new ArrayList<Sprite>();

	private double culldist;

	public Display(SpriteBatch batch) {
		this.batch = batch;
	}

	public void render() {
		culldist = (Game.DIAGDIST*Camera.cam.zoom)/2;

		Collections.sort(this.renderQueue, this.zSort);

		this.batch.setProjectionMatrix(Camera.cam.combined);
		this.renderList(this.renderQueue);
		
		this.batch.setProjectionMatrix(Camera.HUDcam.combined);
		this.renderList(this.renderQueueHUD);
	}

	private void renderList(List<Sprite> renderQueue) {
		int rs = renderQueue.size();
		for (int i = 0; i < rs; i++) {
			renderQueue.get(i).render(batch);
		}
	}

	public void queueRender(Sprite r) {
		if (r.applyCam && this.applyCam) {
//			float xd = (Camera.cam.position.x+Game.WIDTH/2) - r.x;
//			float yd = (Camera.cam.position.y+Game.HEIGHT/2) - r.y;
//			double distance = Math.abs(Math.sqrt(xd*xd+yd*yd)) - Math.max(r.width*r.xScale,r.height*r.yScale);
//			distance *= 0.8;
//			if (distance > culldist)
//				return;

			this.renderQueue.add(r);
		} else {
			this.renderQueueHUD.add(r);
		}
	}

}
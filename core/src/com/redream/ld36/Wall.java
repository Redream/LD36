package com.redream.ld36;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Wall extends Sprite {
	public ArrayList<Brick> bricks = new ArrayList<Brick>();
	public float baseY;
	public float baseX;
	private Floor floor;
	
	public Wall(Floor floor, float x, float y, float rot, int width, int height) {
		this.floor = floor;
		this.rot = rot;
		this.x = x;
		this.y = y;
		baseY = y + height*4;
		
		for(int i = 0;i<width;i++){
			for(int j = 0;j<height;j++){
				Brick brick = new Brick(floor, x+i*8, y+4.5f+j*9, 4, 9, 2, 1);
				if(!floor.building.player.isPlayer)brick.tex = 16;
				brick.setPosition(brick.getPosition().rotate(rot));
				brick.body.setTransform(brick.x, brick.y, (float) Math.toRadians(rot));
				bricks.add(brick);
				Game.addBody(brick.body);
			}
		}
	}
	
	public void queueRender(Display display){
		for(Brick e : bricks){
			e.color = this.color;
			e.queueRender(display);
		}
	}
	
	public void tick(){
		for(Brick e : bricks){
			e.tick();
		}
	}

	public boolean isCollapsed() {
		if(bricks.size() == 0)return false;
		Vector2 lastBrick = bricks.get(0).getPosition();
		for(int i=1;i<bricks.size();i++){
			Brick b = bricks.get(i);
			if(b.getPosition().dst(lastBrick) > 12)return true;
			lastBrick = b.getPosition();
		}
		return false;
	}

}

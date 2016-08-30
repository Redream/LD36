package com.redream.ld36;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Floor extends Sprite {
	public ArrayList<Wall> walls = new ArrayList<Wall>();
	public Brick roof;
	public Brick bed;
	public Boolean collapsed = false;
	public float collapseTimer = 4f;
	public boolean hover;
	public boolean doRemove;
	public Building building;	
	
	public Resident resident;
	
	public Floor(Building building, float x, float y, float rot, int height){
		this.building = building;
		this.rot = rot;
		this.resident = new Resident(this);
		this.genFloor(x,y,height);
		Input.registerListener(this);
	}
	
	public void genFloor(float x, float y, int height){
		walls.add(new Wall(this, x, y, rot-1, 1, height));
		walls.add(new Wall(this, x, y, rot+4, 1, height));
		roof = new Brick(this, x, y+1+height*9, 47, 3, 1.15f, 1);
		roof.tex = 3;
		roof.origX = 47/2;
		roof.origY = 1.5f;
		roof.damage = 0;
		
		bed = new Brick(this, x, y+1, 17, 5, 1, 1);
		bed.tex = 4;
		bed.origX = 17/2;
		bed.origY = 2.5f;
		bed.damage = 0;
		
		roof.setPosition(roof.getPosition().rotate(rot+1.5f));
		roof.body.setTransform(roof.x, roof.y, (float) Math.toRadians(rot+1.5));
		
		bed.setPosition(bed.getPosition().rotate(rot+2));
		bed.body.setTransform(bed.x, bed.y, (float) Math.toRadians(rot+2));
	}
	
	public void queueRender(Display display){
		for(Wall w : walls){
			w.color = this.color;
			w.queueRender(display);
		}
		roof.color = bed.color = resident.color = this.color;
		
		roof.queueRender(display);
		bed.queueRender(display);
		resident.queueRender(display);
	}
	
	public void tick(){
		for(Wall w : walls){
			w.tick();
			if(!collapsed && w.isCollapsed()){
				collapsed = true;
				int i = building.player.buildings.indexOf(this);
				if(i<6){
					building.player.leftCount--;
				}else{
					building.player.rightCount--;
				}
			}
		}
		roof.tick();
		bed.tick();
		if(!building.player.isPlayer && collapsed)doRemove = true;
		if(doRemove){
			collapseTimer *= 0.96f;
			this.color = new Color(1,1,1,collapseTimer);
		}
		resident.tick();
	}
	
	public boolean touchColl(int x, int y) {
		if(!HUD.removeMode || !building.player.isPlayer)return true;
		Vector2 pos = Camera.screenToCoords(new Vector2(x,y));
		Boolean found = false;
		for(Wall w : walls){
			for(Brick b : w.bricks){
				if(b.fixture.testPoint(pos)){
					found = true;
				}
			}
		}
		
		if(roof.fixture.testPoint(pos) || bed.fixture.testPoint(pos)){
			found = true;
		}
		
		if(found){
			this.hover = true;
			color = Color.RED;
		}else{
			this.hover = false;
			color = Color.WHITE;
		}
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer) {
		if(hover && pointer == 0){
			this.doRemove = true;
		}
		return true;
	}

	public void remove() {
		Game.addDestroy(roof.body);
		Game.addDestroy(bed.body);
		for(Wall w : walls){
			for(Brick b : w.bricks){
				Game.addDestroy(b.body);
			}
		}
		resident.doRemove();
		Input.removeListener(this);
	}


}

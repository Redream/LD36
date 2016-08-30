package com.redream.ld36;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Brick extends Entity {
	private Floor floor;

	public Brick(Floor floor, float x, float y, int width, int height, float xScale, float yScale) {
		super(false, BodyType.DynamicBody, Game.GROUP_BUILDINGS, 10f, x, y, width, height, xScale, yScale);
		this.floor = floor;
		this.tex = 2;
		this.applyCam = true;
		this.z = 1;
		this.origX = 2;
		this.origY = 4.5f;
		Input.registerListener(this);
	}
	
	public void tick(){
		super.tick();
	}
}

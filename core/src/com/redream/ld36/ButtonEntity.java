package com.redream.ld36;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ButtonEntity extends Entity {
	public ButtonEntity(float x, float y, int width, int height, float xScale, float yScale) {
		super(true, BodyType.StaticBody, Game.GROUP_BUILDINGS, 0f, x, y, width, height, xScale, yScale*2);
		this.xScale = xScale;
		this.yScale = yScale;
		this.z = 11;
	}
}

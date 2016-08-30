package com.redream.ld36;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Planet extends Sprite {
	
	public Planet(World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		Body body = world.createBody(bodyDef);
		
		body.setUserData(this);
		CircleShape circle = new CircleShape();
		circle.setRadius(Game.PLANET_SIZE);
		
		FixtureDef def = new FixtureDef();
		def.shape = circle;
		def.density = 1f; 
		def.friction = 4f;
		def.restitution = 0f;
		body.createFixture(def);
		circle.dispose();
		Game.addBody(body);
		this.applyCam = true;
		this.z = 1;
	}
	
	public void render(ShapeRenderer shapeRenderer){
		shapeRenderer.setProjectionMatrix(Camera.cam.combined);
		shapeRenderer.setColor(Color.valueOf("F1BF97"));
		shapeRenderer.circle(x, y, Game.PLANET_SIZE, 120);
		shapeRenderer.setColor(Color.valueOf("FDCBA3k"));
		shapeRenderer.circle(x, y, Game.PLANET_SIZE*0.98f, 120);
		shapeRenderer.setColor(Color.valueOf("A3A3A3"));
		shapeRenderer.circle(x, y, Game.PLANET_SIZE*0.8f, 120);
		shapeRenderer.setColor(Color.valueOf("8E8E8E"));
		shapeRenderer.circle(x, y, Game.PLANET_SIZE*0.75f, 120);
		shapeRenderer.setColor(Color.valueOf("3F2C18"));
		shapeRenderer.circle(x, y, Game.PLANET_SIZE*0.5f, 120);
		
	}
}

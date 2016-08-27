package com.redream.ld36;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Entity extends Sprite {

	private Vector2 pull;
	public Body body;
	
	public Entity(World world, int width, int height, int xScale, int yScale){
		
		this.width = width;
		this.height = height;
		this.xScale = xScale;
		this.yScale = yScale;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(100, 200);
		this.body = world.createBody(bodyDef);
		
		body.setUserData(this);
		PolygonShape box = new PolygonShape();  

		box.setAsBox(this.width*this.xScale/2, this.height*this.yScale/2);
		
		FixtureDef def = new FixtureDef();
		def.shape = box;
		def.density = 0.4f; 
		def.friction = 4f;
		def.restitution = 0f;
		body.createFixture(def);
		box.dispose();
	}
	
	public void tick(){
		if(pull != null){
			Vector2 pull2 = this.pull.cpy();
			body.applyForce(pull2.sub(this.getCenter()).scl(100), this.getCenter(), true);
		}
	}
	
	public Vector2 getCenter(){
		return new Vector2(this.x, this.y);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setRotation(float f) {
		this.rot = f;
	}
	
	public boolean touchDown(int x, int y, int pointer) {
		this.pull = Camera.screenToCoords(new Vector2(x,y));
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		this.pull = Camera.screenToCoords(new Vector2(x,y));
		return true;
	}

	public boolean touchUp(int x, int y, int pointer) {
		this.pull = null;
		return true;
	}

}

package com.redream.ld36;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Entity extends Sprite {

	public Vector2 pull;
	public Body body;
	public Fixture fixture;
	public int damage;
	
	public Entity(boolean hud, BodyType bodytype, short group, float density, float x, float y, int width, int height, float xScale, float yScale){
		this.width = width;
		this.damage = 2;
		this.height = height;
		this.xScale = xScale;
		this.yScale = yScale;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodytype;
		bodyDef.position.set(x, y);
		
		this.x = x;
		this.y = y;
		this.body = Game.world.createBody(bodyDef);
		
		body.setUserData(this);
		PolygonShape box = new PolygonShape();  

		box.setAsBox(this.width*this.xScale/2, this.height*this.yScale/2);
		
		FixtureDef def = new FixtureDef();
		def.shape = box;
		def.density = density;
		def.filter.groupIndex = group;
		def.friction = 2f;
		def.restitution = 0f;
		def.isSensor = hud;
		this.fixture = body.createFixture(def);
		box.dispose();
		
		
	}

	public float getDensity(){
		return 10f;
	}

	public void tick(){
		Vector2 pull = Game.planet.getPosition().cpy();
		body.applyForce(pull.sub(this.getPosition()).scl(50), this.getPosition(), true);
		body.setLinearDamping(0.02f);
	}

	public void contacted(Entity ent) {
	}


}

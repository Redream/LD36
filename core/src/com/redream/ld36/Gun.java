package com.redream.ld36;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Gun extends Entity {

	private Sprite base;
	private Sprite gun;
	private GunFloor floor;
	public float gunRot;
	private ArrayList<Entity> balls;

	public Gun(GunFloor floor, float x, float y) {
		super(false, BodyType.DynamicBody, Game.GROUP_GUN, 2f, x, y, 23, 15, 1, 1);
		setPosition(getPosition().rotate(floor.rot+2));
		balls = new ArrayList<Entity>();
		body.setTransform(this.x, this.y, (float) Math.toRadians(floor.rot+2));
		this.floor = floor;
		base = new Sprite();
		base.applyCam = true;
		base.tex = 13;
		base.xScale = 1;
		base.yScale = 1;
		base.width = 17;
		base.height = 7;
		base.origX = 17/2;
		base.origY = 7/2;
		base.x = x;
		base.y = y;
		base.rot = floor.rot;
		
		gun = new Sprite();
		gun.applyCam = true;
		gun.tex = 12;
		gun.width = 23;
		gun.height = 9;
		gun.origX = 10;
		gun.origY = 4;
		gun.x = x;
		gun.y = y+10;
		gun.xScale = 1;
		gun.yScale = 1;
		gun.rot = floor.rot;
		
		gunRot = 10;
		if(floor.rot > 360){
			gunRot = 135;
		}
		body.setUserData(this);
		Game.addBody(body);
	}
	
	public void queueRender(Display display){
		base.color = this.color;
		gun.color = this.color;
		
		if(floor.gunCooldown > 20){
			Sprite bar = new Sprite();
			bar.tex = 9;
			bar.applyCam = true;
			bar.xScale = floor.gunCooldown/15;
			bar.width = bar.height = 1;
			bar.yScale = 2;
			bar.rot = this.rot;
			bar.color = Color.GREEN;
			
			Vector2 pos = new Vector2(x,y);
			double rot2 = Math.toRadians(rot) + Math.PI/2;
			Vector2 dir = new Vector2((float) Math.cos(rot2), (float) Math.sin(rot2));
			
			bar.setPosition(pos.add(dir.scl(10)).add(dir.rotate90(1).scl(0.7f)));
			bar.queueRender(display);
		}
		base.queueRender(display);
		gun.queueRender(display);
		for(Entity e : balls){
			e.queueRender(display);
		}
	}
	
	public void tick(){
		Player p = floor.building.player;
		if(!p.isPlayer){
			Random r = new Random();
			
			if(r.nextInt(700) == 0 && floor.gunCooldown == 0){
				this.gunRot = (this.rot > p.beacon.rot ? 180 : 0);
				this.gunRot -= r.nextInt(45) * (this.rot > p.beacon.rot ? 1 : -1);
				this.shoot();
				floor.gunCooldown = 200;
			}
		}
		base.rot = this.rot;
		gun.rot = base.rot + gunRot;
		
		Vector2 pos = new Vector2(x,y);
		double rot2 = Math.toRadians(rot) + Math.PI/2;
		Vector2 dir = new Vector2((float) Math.cos(rot2), (float) Math.sin(rot2));
		
		base.setPosition(pos.sub(dir.scl(5)));
		gun.setPosition(pos.add(dir.scl(1)));
		
		for(int i=0;i<balls.size();i++){
			Entity e = balls.get(i);
			if(e.body.getLinearVelocity().len() < 1){
				balls.remove(i--);
				Game.world.destroyBody(e.body);
			}else{
				e.tick();
			}
		}
		super.tick();
	}

	public void shoot() {
		Entity ball = new Entity(false, BodyType.DynamicBody, Game.GROUP_GUN, 20f, gun.x, gun.y, 10, 10, 0.5f, 0.5f);
		ball.damage = 5;
		ball.tex = 14;
		ball.origX = 5;
		ball.origY = 5;
		ball.applyCam = true;
		ball.body.setUserData(ball);
		double rot2 = Math.toRadians(rot + gunRot) + Math.PI/2;
		Vector2 dir = new Vector2((float) Math.cos(rot2), (float) Math.sin(rot2)).scl(10000).rotate90(-1);
		ball.body.setLinearVelocity(dir);
		Game.addBody(ball.body);
		balls.add(ball);
		Resources.cannon.play();
	}
}

package com.redream.ld36;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Beacon extends Entity {

	public float health = 100;
	public Player player;
	public int population;
	private float colorTimer;

	public Beacon(Player player, float x, float y, float rot) {
		super(false, BodyType.StaticBody, Game.GROUP_BUILDINGS, 10f, x, y, 40, 29, 0.5f, 1);
		this.xScale = 1;
		this.player = player;
		this.tex = (player.isPlayer ? 10 : 22);
		this.applyCam = true;
		this.origX = 20;
		this.origY = 29/2;
		this.rot = rot;
		body.setUserData(this);
		
		setPosition(getPosition().rotate(rot));
		body.setTransform(this.x, this.y, (float) Math.toRadians(rot));
	}
	
	public void contacted(Entity ent) {
		this.health -= ent.damage;
		if(ent.damage == 5){
			this.color = Color.RED;
			this.colorTimer = 50;
		}
	}
	
	public void tick(){
		
		if(colorTimer > 0){
			colorTimer--;
			float mul = colorTimer/50f;
			color = new Color(1, 1-mul, 1-mul, 1);
		}
		Random r = new Random();
		if(r.nextInt(200) == 0 && population > 0)this.player.giveMoney(10+r.nextInt(population)*5+population);
	}
	

}

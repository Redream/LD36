package com.redream.ld36;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class Resident extends Sprite {
	private Beacon beacon;
	private Floor floor;
	public int state;
	public static final int SLEEPING = 1;
	public static final int PRAYING = 2;
	public static final int WALKING_HOME = 3;
	public static final int WALKING_BEACON = 4;
	
	public float prayOffset = 0;
	
	public int timer;
	public int animTimer;

	public Resident(Floor floor){
		this.floor = floor;
		this.beacon = floor.building.player.beacon;
		this.xScale = 1;
		this.yScale = 1;
		this.applyCam = true;
		this.state = Resident.SLEEPING;
		
		Random r = new Random();
		prayOffset = r.nextFloat()*8+1;
		timer = r.nextInt(100)+100;
	}
	
	public void tick(){
		double rot2 = Math.toRadians(rot) + Math.PI/2;
		Vector2 dir = new Vector2((float) Math.cos(rot2), (float) Math.sin(rot2));
		Random r = new Random();
		if(state == Resident.SLEEPING){
			this.x = floor.bed.x;
			this.y = floor.bed.y;
			this.setPosition(getPosition().add(dir.scl(3)));
			this.rot = floor.bed.rot;
			this.origX = 7.5f;
			this.origY = 1.5f;
			this.tex = 18;
			this.width = 15;
			this.height = 3;
			this.z = 9;
			
			timer--;
			if(timer <= 0){
				state = Resident.WALKING_BEACON;
			}
		}
		if(state == Resident.WALKING_BEACON || state == Resident.WALKING_HOME){
			this.origX = 5;
			this.origY = 7.5f;
			this.tex = 17;
			this.width = 10;
			this.height = 15;
			this.z = 10;
			float targetRot = 0;
			if(state == Resident.WALKING_BEACON)targetRot = beacon.rot;
			if(state == Resident.WALKING_HOME)targetRot = floor.rot;
			animTimer--;
			if(animTimer <= 0){
				this.mirrorX = !mirrorX;
				this.animTimer = 10;
			}
			float offset = (state == Resident.WALKING_BEACON ? prayOffset : 1);
			if(targetRot - rot > offset){
				this.rot += 0.1f;
			}else if(targetRot - rot < -offset){
				this.rot -= 0.1f;
			}else{
				if(state == Resident.WALKING_BEACON){
					state = Resident.PRAYING;
					beacon.population++;
					timer = r.nextInt(1000)+1000;
				}else if(state == Resident.WALKING_HOME && !floor.collapsed){
					state = Resident.SLEEPING;
					timer = r.nextInt(1000)+500;
				}else{
					animTimer++;
				}
			}
			this.setPosition(new Vector2(0, Game.PLANET_SIZE+8).rotate(rot));
		}
		if(state == Resident.PRAYING){
			animTimer--;
			if(animTimer <= 0){
				if(tex == 19){
					this.origX = 6;
					this.origY = 2;
					this.tex = 20;
					this.width = 12;
					this.height = 4;
				}else{
					this.origX = 6;
					this.origY = 2;
					this.tex = 19;
					this.width = 9;
					this.height = 9;
				}
				animTimer = r.nextInt(5)+20;
				this.mirrorX = beacon.rot - rot > 0;
				this.setPosition(new Vector2(0, Game.PLANET_SIZE+2).rotate(rot));
			}
			timer--;
			if(timer <= 0){
				state = Resident.WALKING_HOME;
				beacon.population--;
			}
		}
	}

	public void doRemove() {
		if(state == Resident.PRAYING)beacon.population--;
	}
}

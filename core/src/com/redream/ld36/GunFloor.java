package com.redream.ld36;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class GunFloor extends Floor {

	public Gun gun;
	private boolean aiming;
	public int gunCooldown;
	private float baseY;

	public GunFloor(Building building, float x, float y, float rot, int height) {
		super(building, x, y, rot, height);
		Input.registerListener(this);
		baseY = y + height*4;
	}
	
	public void genFloor(float x, float y, int height){
		this.gun = new Gun(this, x, y+5);
	}
	
	public void queueRender(Display display){
		gun.color = this.color;
		gun.queueRender(display);
	}
	
	public void tick(){
		if(gun.getPosition().dst(Game.planet.getPosition()) < Game.PLANET_SIZE+ 30){
			this.doRemove = true;
		}
		if(doRemove){
			collapseTimer *= 0.96f;
			this.color = new Color(1,1,1,collapseTimer);
		}
		if(this.gunCooldown > 0)this.gunCooldown--;
		gun.tick();
	}
	
	public boolean touchColl(int x, int y) {
		if(!building.player.isPlayer)return false;
		if(!(HUD.removeMode || HUD.cannonMode) || this.doRemove)return true;
		
		Vector2 pos = Camera.screenToCoords(new Vector2(x,y));
		Boolean found = false;
		
		if(aiming){
			Vector2 pos2 = pos.sub(gun.getPosition());
			gun.gunRot = (float) Math.toDegrees(Math.atan2(pos2.y, pos2.x)) - gun.rot;
		}else{
			if(gun.fixture.testPoint(pos)){
				found = true;
			}
			
			if(found){
				this.hover = true;
				if(HUD.cannonMode){
					color = Color.VIOLET;
				}else{
					color = Color.RED;
				}
			}else{
				this.hover = false;
				color = Color.WHITE;
			}
		}
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer) {
		if(!HUD.cannonMode)aiming = false;
		if(aiming && pointer == 0 && gunCooldown == 0){
			gun.shoot();
			aiming = false;
			hover = false;
			this.gunCooldown = 300;
		}
		
		if(hover && pointer == 0){
			if(HUD.removeMode)this.doRemove = true;
			if(HUD.cannonMode){
				if(!aiming && this.gunCooldown == 0)this.gunCooldown = 20;
				this.aiming = true;
				this.color = Color.WHITE;
			}
		}
		
		return true;
	}
	
	public void remove(){
		this.building.hasGun = false;
		Game.addDestroy(gun.body);
		Input.removeListener(this);
	}
}

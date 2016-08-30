package com.redream.ld36;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Building extends Sprite {
	public ArrayList<Floor> floors = new ArrayList<Floor>();
	public Entity addButton;
	public float fHeight;
	private boolean addHover;
	private boolean gunHover;
	private boolean addFloor;

	private Entity gunButton;
	public boolean addGun;
	public boolean hasGun;
	public Player player;
	
	public Building(Player player, float x, float y, float rot, float height){
		this.player = player;
		
		this.rot = rot;
		for(int i=0;i<height;i++){
			Floor f = new Floor(this, x, y+39*i, rot, 4);
			floors.add(f);
			fHeight = f.roof.getPosition().dst(Game.planet.getPosition());
		}
		
		addButton = new ButtonEntity(x, y, 9, 9, 1, 1);
		addButton.tex = 6;
		addButton.applyCam = true;
		addButton.origX = 4.5f;
		addButton.origY = 4.5f;
		
		gunButton = new ButtonEntity(x, y, 9, 9, 1, 1);
		gunButton.tex = 11;
		gunButton.applyCam = true;
		gunButton.origX = 4.5f;
		gunButton.origY = 4.5f;
		
		Game.addBody(addButton.body);
		Input.registerListener(this);
	}
	
	public void addFloor(){
		floors.add(new Floor(this, x, fHeight, rot, 4));
	}
	
	public void addGun() {
		floors.add(new GunFloor(this, x, fHeight, rot, 4));
		this.hasGun = true;
		player.hasGun = true;
	}
	
	public boolean touchColl(int x, int y) {
		if(!this.player.isPlayer)return true;
		Vector2 pos = Camera.screenToCoords(new Vector2(x,y));
		if(fHeight < Game.PLANET_SIZE+140 && HUD.addMode){
			this.addHover = addButton.fixture.testPoint(pos);
			this.gunHover = gunButton.fixture.testPoint(pos);
		}
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer) {
		if(fHeight < Game.PLANET_SIZE+140 && HUD.addMode && pointer == 0 && !this.hasGun){
			if(this.addHover)this.addFloor = true;
			if(this.gunHover && floors.size() > 0)this.addGun = true;
		}
		return true;
	}
	
	public void queueRender(Display display){
		for(int i=0;i<floors.size();i++){
			Floor f = floors.get(i);
			f.queueRender(display);
		}
		if(fHeight < Game.PLANET_SIZE+140 && HUD.addMode && !this.hasGun && player.isPlayer){
			addButton.queueRender(display);
			if(floors.size() > 0)gunButton.queueRender(display);
		}
	}
	
	public boolean canAddFloor(){
		return fHeight < Game.PLANET_SIZE+140 && player.money > Player.HOUSE_COST && !this.hasGun;
	}
	
	public void tick(){
		if(this.addFloor){
			if(player.money >= Player.HOUSE_COST){
				player.giveMoney(-Player.HOUSE_COST);
				this.addFloor();
				Resources.build.play();
			}else if(player.isPlayer){
				HUD.showMessage("You need "+Player.HOUSE_COST+" gold to purchase a house.");
			}
		}
		addFloor = false;
		
		if(this.addGun){
			if(player.money >= Player.GUN_COST){
				player.giveMoney(-Player.GUN_COST);
				this.addGun();
				Resources.build.play();
			}else if(player.isPlayer){
				HUD.showMessage("You need "+Player.GUN_COST+" gold to purchase a cannon.");
			}
		}
		addGun = false;
		
		if(floors.size() == 0){
			fHeight = Game.PLANET_SIZE;
		}else{
			fHeight = 0;
		}
		for(int i=0;i<floors.size();i++){
			Floor f = floors.get(i);
			if(f.collapseTimer <= 0.02f){
				floors.remove(i--);
				Game.players.get((player.isPlayer ? 1 : 0)).giveMoney(Player.HOUSE_COST/2);

				f.remove();
			}else{
				f.tick();
				if(!f.collapsed && f.roof != null)fHeight = f.roof.getPosition().dst(Game.planet.getPosition());
			}		
		}
		int gHover = 0;
		int aHover = 0;
		if(addHover)aHover = 1;
		if(gunHover)gHover = 1;
		
		Vector2 bpos = new Vector2(0, fHeight+10+aHover);
		bpos.rotate(rot+0.5f);
		addButton.rot = rot;
		addButton.setPosition(bpos);
		addButton.body.setTransform(bpos, (float) Math.toRadians(rot+1.5f));
		
		bpos = new Vector2(0, fHeight+10+gHover);
		bpos.rotate(rot+2.5f);
		gunButton.rot = rot;
		gunButton.setPosition(bpos);
		gunButton.body.setTransform(bpos, (float) Math.toRadians(rot+2.5f));
	}

	
}

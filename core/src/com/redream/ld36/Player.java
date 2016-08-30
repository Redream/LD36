package com.redream.ld36;

import java.util.ArrayList;
import java.util.Random;

public class Player extends Sprite {
	public ArrayList<Building> buildings = new ArrayList<Building>();
	public Beacon beacon;
	public boolean isPlayer = false;
	
	public int money = 300;
	private boolean savingForGun;
	public Game game;
	public boolean hasGun;
	
	public int rightCount = 1;
	public int leftCount;
	
	public static int GUN_COST = 400;
	public static int HOUSE_COST = 100;
	
	public Player(Game game, boolean isPlayer, float rot){
		this.isPlayer = isPlayer;
		this.game = game;
		
		beacon = new Beacon(this, 0, Game.PLANET_SIZE+13, rot-2);
		Game.addBody(beacon.body);
		
		for(float i = rot-80;i<rot-20;i+=10){
			buildings.add(new Building(this, 0, Game.PLANET_SIZE, i, 0));
		}
		
		buildings.add(new Building(this, 0, Game.PLANET_SIZE, rot-20, 1));
		
		for(float i = rot+10;i<rot+80;i+=10){
			buildings.add(new Building(this, 0, Game.PLANET_SIZE, i, 0));
		}
	}
	
	public void queueRender(Display display){
		beacon.queueRender(display);
		for(Building b : buildings){
			b.queueRender(display);
		}
	}
	
	public void tick(){
		if(Game.state == Game.STATE_GAME){
			if(!isPlayer){
				if(beacon.health <= 0){
					Game.state = Game.STATE_WON;
				}
				Random r = new Random();
				if(money > GUN_COST && savingForGun){
					boolean right = r.nextBoolean();
					if(right){
						for(int i=buildings.size()-1;i>buildings.size()/2;i--){
							Building b = buildings.get(i);
							if(!b.hasGun && b.floors.size() > 0){
								b.addGun();
								this.giveMoney(-GUN_COST);
								savingForGun = false;
								break;
							}
						}
					}else{
						for(int i=0;i<buildings.size()/2;i++){
							Building b = buildings.get(i);
							if(!b.hasGun && b.floors.size() > 0){
								b.addGun();
								this.giveMoney(-GUN_COST);
								savingForGun = false;
								break;
							}
						}
					}
					if(!savingForGun)HUD.showMessage("The enemy has built a cannon!");
				}
				if(money > HOUSE_COST && r.nextInt(savingForGun ? (beacon.population < 3 ? 600 : 1400) : 300) == 0){
					int i = r.nextInt(6) + (rightCount < leftCount ? 0 : (rightCount == leftCount ? 3 : 6) );
					System.out.println(leftCount+" "+rightCount+" "+i);
					Building b = buildings.get(i);
					if(b.canAddFloor()){
						b.addFloor();
						this.giveMoney(-HOUSE_COST);
						if(i<6){
							rightCount++;
						}else{
							leftCount++;
						}
					}
					
				}
				if(beacon.population > 2 && r.nextInt(1000) == 0 && money > 125 && !savingForGun){
					savingForGun = true;
					if(!hasGun)HUD.showMessage("The enemy is saving for a cannon!");
				}
			}else{
				if(beacon.health <= 0){
					Game.state = Game.STATE_LOST;
				}
			}
		}
		for(Building b : buildings){
			b.tick();
		}
		beacon.tick();
	}

	public void giveMoney(int amount) {
		if(money + amount >= 0)money += amount;
		if(isPlayer)HUD.moneyPopup(amount);
	}
}

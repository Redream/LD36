package com.redream.ld36;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class HUD extends Sprite {
	public Sprite addButton;
	public Sprite trashButton;
	public Sprite cannonButton;
	public Sprite bg;
	private boolean addHover;
	private boolean removeHover;
	private boolean cannonHover;
	
	public static boolean addMode = false;
	public static boolean removeMode = false;
	public static boolean cannonMode = false;
	
	public static ArrayList<Font> moneyPopups = new ArrayList<Font>();
	
	public static Array<Font> messageQueue = new Array<Font>();
	public static Font message;
	public float fontTimer = 0;
	
	public HUD(){
		bg = new Sprite();
		bg.tex = 9;
		bg.width = 1;
		bg.height = 1;
		bg.xScale = Game.WIDTH;
		bg.yScale = 120;
		bg.x = 0;
		bg.y = 200;
		bg.z = 12;
		bg.color = new Color(1,1,1,0.4f);
		
		addButton = new Sprite();
		addButton.applyCam = false;
		addButton.width = addButton.height = 12;
		addButton.xScale = addButton.yScale = 3;
		addButton.tex = 7;
		addButton.x = 35;
		addButton.y = 230;
		addButton.z = 13;
		
		trashButton = new Sprite();
		trashButton.applyCam = false;
		trashButton.width = trashButton.height = 12;
		trashButton.xScale = trashButton.yScale = 3;
		trashButton.tex = 8;
		trashButton.x = 120;
		trashButton.y = 230;
		trashButton.z = 13;
		
		cannonButton = new Sprite();
		cannonButton.applyCam = false;
		cannonButton.width = cannonButton.height = 12;
		cannonButton.xScale = cannonButton.yScale = 3;
		cannonButton.tex = 15;
		cannonButton.x = 250;
		cannonButton.y = 230;
		cannonButton.z = 13;
		
		Input.registerListener(this);
	}
	
	public boolean touchColl(int x, int y) {
		x *= Game.screenRatioX;
		y *= Game.screenRatioY;
		y = -y+Game.HEIGHT/2;
		
		addHover = addButton.getBounds().contains(x, y);
		removeHover = trashButton.getBounds().contains(x, y);
		cannonHover = cannonButton.getBounds().contains(x, y);
		
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer){
		if(addHover){
			addMode = !addMode;
			if(addMode){
				removeMode = false;
				cannonMode = false;
			}
		}
		if(removeHover){
			removeMode = !removeMode;
			if(removeMode){
				addMode = false;
				cannonMode = false;
			}
		}
		if(cannonHover){
			cannonMode = !cannonMode;
			if(cannonMode){
				addMode = false;
				removeMode = false;
			}
		}
		return true;
		
	}
	
	public void queueRender(Display display){
		if(Game.state == Game.STATE_GAME){
			bg.xScale = Game.WIDTH;
			
			Font f = new Font("Build tools", 45, 275, Font.POS_LEFT);
			f.z = 13;
			f.color = Color.BLACK;
			f.queueRender(display);
			
			if(Game.players.get(0).hasGun){
				Font f1 = new Font("Fire cannon", 210, 275, Font.POS_LEFT);
				f1.z = 13;
				f1.color = Color.BLACK;
				f1.queueRender(display);
			}
			
			Font f3 = new Font("Gold: "+Game.players.get(0).money, Game.WIDTH-10, 200, Font.POS_RIGHT);
			f3.z = 13;
			f3.color = Color.BLACK;
			f3.queueRender(display);
			
			Font f4 = new Font("Beacon health: "+Math.round(Game.players.get(0).beacon.health)+"%", Game.WIDTH-10, 275, Font.POS_RIGHT);
			f4.z = 13;
			f4.color = Color.BLACK;
			f4.queueRender(display);
			
			Font f5 = new Font("Enemy health: "+Math.round(Game.players.get(1).beacon.health)+"%", Game.WIDTH-10, 250, Font.POS_RIGHT);
			f5.z = 13;
			f5.color = Color.BLACK;
			f5.queueRender(display);
	
			
			addButton.color = (addMode ? new Color(1,1,1,0.5f) : Color.WHITE);
			trashButton.color = (removeMode ? new Color(1,1,1,0.5f) : Color.WHITE);
			cannonButton.color = (cannonMode ? new Color(1,1,1,0.5f) : Color.WHITE);
			
			addButton.y = (addHover ? 232 : 230);
			trashButton.y = (removeHover ? 232 : 230);
			cannonButton.y = (cannonHover ? 232 : 230);
			
	
			for(Font font : moneyPopups){
				font.queueRender(display);
			}
			
			addButton.queueRender(display);
			trashButton.queueRender(display);
			if(Game.players.get(0).hasGun)cannonButton.queueRender(display);
			
			bg.queueRender(display);
		}
		if((Game.state == Game.STATE_INTRO || Game.state == Game.STATE_GAME) && message != null)message.queueRender(display);
		
	}
	
	public static void showMessage(String txt){
		Font f = new Font(txt, Game.WIDTH/2, 200, Font.POS_CENTER);
		f.color = Color.BLACK;
		f.z = 15;
		messageQueue.add(f);
	}

	public static void moneyPopup(int amount) {
		moneyPopups.add(new Font((amount > 0 ? "+" : "")+Integer.toString(amount), Game.WIDTH-10, 190, Font.POS_RIGHT));
	}
	
	public void tick(){
		if(Game.state == Game.STATE_INTRO){
			if(fontTimer < 0.02f && messageQueue.size == 0){
				Game.startGame = true;
			}
			if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
				fontTimer = 0;
			}
		}
		if((fontTimer > (Game.state == Game.STATE_GAME ? 0.2f : 0.02f)) || messageQueue.size == 0){
			fontTimer *= 0.98f;
			if(message != null)message.color = new Color(0,0,0,fontTimer);
		}else if(messageQueue.size > 0){
			message = messageQueue.get(0);
			fontTimer = (Game.state == Game.STATE_INTRO ? 30 : 60);

			messageQueue.removeIndex(0);
		}
		for(int i = 0;i<moneyPopups.size();i++){
			Font f = moneyPopups.get(i);
			if(f.color.a < 0.01f){
				moneyPopups.remove(i--);
			}else{
				f.y -= 0.2f;
				f.color = new Color(0,0,0,f.color.a - 0.01f);
			}
		}
	}
}

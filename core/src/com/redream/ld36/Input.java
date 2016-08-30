package com.redream.ld36;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor{
	private static ArrayList<InputListener> listeners = new ArrayList<InputListener>();
	public static boolean disabled;

	public Input(){
		Gdx.input.setInputProcessor(this);
	}
	
	public static void removeListeners(){
		listeners.clear();
	}

	public static void registerListener(InputListener il){
		Input.listeners.add(il);
	}

	public static void removeListener(InputListener il){
		Input.listeners.remove(il);
	}

	public static void disable(){
		Input.disabled = true;
	}

	public static void enable(){
		Input.disabled = false;
	}

	public boolean keyDown(int keycode) {
		if(keycode == Keys.F8)Game.debug = !Game.debug;
		if(Input.disabled)return false;
		boolean success = false;
		for(InputListener il : listeners){
			Boolean temp = il.keyDown(keycode);
			if(!success)success = temp;
		}
		return success;
	}

	public boolean keyUp(int keycode) {
		if(Input.disabled)return false;
		boolean success = false;
		for(InputListener il : listeners){
			Boolean temp = il.keyUp(keycode);
			if(!success)success = temp;
		}
		return success;
	}

	public boolean keyTyped(char character) {
		if(Input.disabled)return false;
		boolean success = false;
		for(InputListener il : listeners){
			Boolean temp = il.keyTyped(character);
			if(!success)success = temp;
		}
		return success;
	}

	public boolean touchDown(int x, int y, int pointer, int button) {
		if(Input.disabled)return false;
		
//		x *= Game.screenRatioX;
//		y *= Game.screenRatioY;
		boolean success = false;
		for(InputListener il : listeners){
			if(il.touchColl(x, y)){
				Boolean temp = il.touchDown(x, y, button);
				if(!success)success = temp;
			}
		}
		return success;
	}

	public boolean touchUp(int x, int y, int pointer, int button) {
		if(Input.disabled)return false;
		
//		x *= Game.screenRatioX;
//		y *= Game.screenRatioY;
		boolean success = false;
		for(InputListener il : listeners){
			if(il.touchColl(x, y)){
				Boolean temp = il.touchUp(x, y, pointer);
				if(!success)success = temp;
			}
		}
		return success;
	}

	public boolean touchDragged(int x, int y, int pointer) {
		if(Input.disabled)return false;
		
//		x *= Game.screenRatioX;
//		y *= Game.screenRatioY;
		boolean success = false;
		for(InputListener il : listeners){
			if(il.touchColl(x, y)){
				Boolean temp = il.touchDragged(x, y, pointer);
				if(!success)success = temp;
			}
		}
		return success;
	}

	public boolean touchMoved(int x, int y) {
		if(Input.disabled)return false;
		
//		x *= Game.screenRatioX;
//		y *= Game.screenRatioY;
		
		boolean success = false;
		for(InputListener il : listeners){
			if(il.touchColl(x, y)){
				Boolean temp = il.touchMoved(x, y);
				if(!success)success = temp;
			}
		}
		return success;
	}

	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return this.touchMoved(x, y);
	}
}

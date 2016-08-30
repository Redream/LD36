package com.redream.ld36;

public interface InputListener {      
		public boolean touchColl(int x, int y);
		
        public boolean keyDown(int keycode);

        public boolean keyUp(int keycode);
        
        public boolean keyTyped(char character);
        
        public boolean touchDown(int x,int y,int pointer);
        
        public boolean touchUp(int x,int y,int pointer);    
        
        public boolean touchDragged(int x, int y,int pointer);
        
        public boolean touchMoved(int x, int y);
        
        public boolean scrolled(int amount);
}
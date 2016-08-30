package com.redream.ld36;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Camera {
	public static int BoundaryX = 10000;
	public static int BoundaryY = 0;
	public static int BoundaryStartX = 0 ;
	public static int BoundaryStartY = 0;

	public static float angle = 0;
	public static float scrollspeed = 0;

	public static boolean disable = false;

	public static OrthographicCamera cam;
	public static OrthographicCamera HUDcam;
	
	public static float planetRot = 0;

	public static void tick() {
		if(Game.state == Game.STATE_GAME){
			if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)){
				scrollspeed += 0.02f;
			}else if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)){
				scrollspeed -= 0.02f;
			}else{
				scrollspeed *= 0.96f;
			}
			planetRot += scrollspeed;
			
			if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
				if(cam.zoom > 0.2f)cam.zoom -= 0.01;
			}

			if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
				if(cam.zoom < 3f)cam.zoom += 0.01;
			}

			Vector2 pos = new Vector2(0, Game.PLANET_SIZE+40).rotate(-planetRot);
			cam.position.x = pos.x;
			cam.position.y = pos.y;
			cam.rotate(scrollspeed);
		}

		cam.update();
	}

	public static void reset() {
		Camera.cam = new OrthographicCamera(Game.WIDTH, Game.HEIGHT);
		Camera.HUDcam = new OrthographicCamera(Game.WIDTH, Game.HEIGHT);
		Camera.cam.position.x = 0;
		Camera.cam.position.y = 880;
		Camera.cam.zoom = 0.5f;
		Camera.planetRot = 0;
		Camera.scrollspeed = 0;
		
		if(Game.state != Game.STATE_GAME){
			Camera.cam.zoom = 0.2f;
			Vector2 pos = new Vector2(0, Game.PLANET_SIZE+40).rotate(-8);
			cam.position.x = pos.x;
			cam.position.y = pos.y;
		}
		Camera.cam.update();
	}

	public static Vector2 screenToCoords(Vector2 screen) {
		Vector3 coords = Camera.cam.unproject(new Vector3(screen.x, screen.y, 0));
		return new Vector2(coords.x, coords.y);
	}
}


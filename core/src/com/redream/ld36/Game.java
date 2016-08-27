package com.redream.ld36;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Game extends ApplicationAdapter implements InputListener {
	public static int WIDTH;
	public static int HEIGHT;
	
	public static float DIAGDIST;
	
	public static float screenRatioY;
	public static float screenRatioX;
	public static boolean debug;
	
	private SpriteBatch batch;
	private Display display;
	private Input input;
	
	private double unprocessed;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	public static final double TICK_TIME = 0.0166667;
	private Array<Body> bodies;
	private Array<Entity> boxes;
	private Array<Entity> addQueue;
	
	@Override
	public void create () {
		Input.registerListener(this);
		this.resize(480, 320);
		
		this.batch = new SpriteBatch();
		this.display = new Display(this.batch);
		this.world = new World(new Vector2(0, -20f), true);
		this.debugRenderer = new Box2DDebugRenderer();
		
		this.input = new Input();
		Gdx.input.setInputProcessor(input);
		
		bodies = new Array<Body>();
		boxes = new Array<Entity>();
		addQueue = new Array<Entity>();
		
		Camera.cam.position.x = 100;
		Camera.cam.position.y = 250;
		Camera.cam.zoom = 5f;
		Camera.cam.update();
		
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 0));  

		Body groundBody = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();  
		
		groundBox.setAsBox(Game.WIDTH, 10.0f); 
		
		groundBody.createFixture(groundBox, 0.0f); 
		groundBox.dispose();
	}

	@Override
	public void render () {
		if (this.unprocessed < 3) {
			this.unprocessed += Game.timeDelta();
		}

		if (this.unprocessed > 1) {
			this.tick();
			this.unprocessed -= 1;
		}
		
		this.batch.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0,0,0,1);
		
		for(Entity e : boxes){
			e.queueRender(display);
		}
		
		this.display.render();
		debugRenderer.render(this.world, Camera.cam.combined);
		
		this.display.renderQueue.clear();
		this.display.renderQueueHUD.clear();

		this.batch.end();
	}
	
	private void tick() {
		for(Entity e : addQueue){
			if(e != null){
				boxes.add(e);
				bodies.add(e.body);
				Input.registerListener(e);
			}
		}
		addQueue.clear();
		for(Entity e : boxes){
			if(e != null)e.tick();
		}
		world.step((float) TICK_TIME, 6, 2);
		world.getBodies(bodies);
		for (Body b : bodies) {
		    Entity e = (Entity) b.getUserData();

		    if (e != null) {
		        e.setPosition(b.getPosition().x, b.getPosition().y);
		        e.setRotation(MathUtils.radiansToDegrees * b.getAngle());
		    }
		}
		
		Camera.tick();
	}
	
	public void resize(int width, int height) {
		float dratio = (float)width/(float)height;

		Game.WIDTH = (int) (480 * dratio);
		Game.HEIGHT = 480;
		
		Game.screenRatioX = (float)Game.WIDTH / (float)width;
		Game.screenRatioY = (float)Game.HEIGHT / (float)height;
		
		DIAGDIST = (float) Math.abs(Math.sqrt(Math.pow(-Game.WIDTH, 2.0D) + Math.pow(Game.HEIGHT, 2.0D)));
		
		Camera.cam = new OrthographicCamera(Game.WIDTH, Game.HEIGHT);
		Camera.HUDcam = new OrthographicCamera(Game.WIDTH, Game.HEIGHT);
		Camera.cam.zoom = 0.4f;
		Camera.cam.update();
	}

	public static double timeDelta() {
		return Gdx.graphics.getDeltaTime() / TICK_TIME;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public boolean touchCollTest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer) {
		if(world == null)return true;
		Entity box = new Entity(this.world, 20, 20, 1, 1);	
		box.tex = 1;
		box.applyCam = true;
		box.origX = 10;
		box.origY = 10;
		addQueue.add(box);
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

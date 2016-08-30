package com.redream.ld36;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Game extends ApplicationAdapter implements InputListener {
	public static int WIDTH;
	public static int HEIGHT;
	
	public static float DIAGDIST;
	
	public static float screenRatioY;
	public static float screenRatioX;
	public static boolean debug;
	
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private Display display;
	private Input input;
	
	private double unprocessed;
	public static World world;
	private Box2DDebugRenderer debugRenderer;
	public static final double TICK_TIME = 0.0166667;
	private static Array<Body> bodies;
	private Array<Entity> boxes;
	private Array<Entity> addQueue;
	public static Planet planet;
	private static Array<Body> destroyQueue;
	
	public HUD hud;
	
	private Listener listener;
	
	public static ArrayList<Player> players;
	
	public static final int PLANET_SIZE = 300;
	public static final short GROUP_GUN = -1;
	public static final short GROUP_BUILDINGS = 1;
	
	public static final int STATE_MENU = 1;
	public static final int STATE_INTRO = 2;
	public static final int STATE_GAME = 3;
	public static final int STATE_LOST = 4;
	public static final int STATE_WON = 5;
	
	public static int state;
	public static boolean startGame;
	
	@Override
	public void create () {
		Input.registerListener(this);
		this.resize(960, 640);
		
		this.batch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.display = new Display(this.batch, this.shapeRenderer);
		
		this.debugRenderer = new Box2DDebugRenderer();
		
		hud = new HUD();
		this.input = new Input();
		Gdx.input.setInputProcessor(input);
		
		state = Game.STATE_MENU;
		Resources.song.setLooping(true);
		Resources.intro.setLooping(true);
		Resources.song.play();
		resetWorld();
	}
	
	public void resetWorld(){
		Game.world = new World(new Vector2(0, 0), true);
		bodies = new Array<Body>();

		addQueue = new Array<Entity>();
		destroyQueue = new Array<Body>();
		
		listener = new Listener();
		world.setContactListener(listener);
		planet = new Planet(world);
		players = new ArrayList<Player>();
		players.add(new Player(this, true, 0));
		players.add(new Player(this, false, 180));
		HUD.messageQueue.clear();
		HUD.message = null;
		
		
		Camera.reset();
		
		if(Game.state != Game.STATE_GAME)players.get(0).buildings.get(6).addGun();
	}
	
	public static void addBody(Body body){
		Game.bodies.add(body);
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
		shapeRenderer.begin(ShapeType.Filled);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.376f,0.725f,1,1);

		hud.queueRender(display);
		
		
		if(state != Game.STATE_GAME && state != Game.STATE_INTRO){
			Sprite logo = new Sprite();
			logo.tex = 21;
			logo.width = 69;
			logo.height = 29;
			logo.origX = 69/2;
			logo.origY = 29/2;
			logo.x = Game.WIDTH/2;
			logo.y = 200;
			logo.applyCam = false;
			logo.xScale = 5;
			logo.yScale = 5;
			logo.z = 14;
			logo.queueRender(display);
		}
		if(state == Game.STATE_MENU){
			Font f = new Font("Press SPACE to begin", Game.WIDTH/2, 70, Font.POS_CENTER);
			f.color = Color.BLACK;
			f.queueRender(display);
			Font f1 = new Font("Made for Ludum Dare 36", Game.WIDTH/2, 0, Font.POS_CENTER);
			f1.color = Color.BLACK;
			f1.z = 14;
			f1.queueRender(display);
		}
		
		if(state == Game.STATE_WON || state == Game.STATE_LOST){
			if(state == Game.STATE_LOST){
				Font f = new Font("You have been defeated! :(", Game.WIDTH/2, 70, Font.POS_CENTER);
				f.color = Color.BLACK;
				f.z = 14;
				f.queueRender(display);
			}
			if(state == Game.STATE_WON){
				Font f = new Font("You are victorious!", Game.WIDTH/2, 70, Font.POS_CENTER);
				f.color = Color.BLACK;
				f.z = 14;
				f.queueRender(display);
			}
			Font f = new Font("Press SPACE to play again", Game.WIDTH/2, 30, Font.POS_CENTER);
			f.color = Color.BLACK;
			f.z = 14;
			f.queueRender(display);
		}
		for(Player p : players){
			p.queueRender(display);
		}
		planet.queueRender(display);
		
		// don't ask
		Sprite bugfix = new Sprite();
		bugfix.z = 999;
		bugfix.tex = 9;
		bugfix.queueRender(display);
		
		this.display.render();
		
//		debugRenderer.render(Game.world, Camera.cam.combined);
		
		this.display.renderQueue.clear();
		this.display.renderQueueHUD.clear();
		shapeRenderer.end();
		this.batch.end();
	}
	
	private void tick() {
		if((Game.state == Game.STATE_INTRO && Game.startGame) 
				|| ((Game.state == Game.STATE_WON || Game.state == Game.STATE_LOST) && Gdx.input.isKeyJustPressed(Keys.SPACE))){
			Resources.intro.stop();
			Resources.song.play();
			state = Game.STATE_GAME;
			this.resetWorld();
			HUD.showMessage("Use WASD or arrow keys to look around.");
			Game.startGame = false;
		}
		
		if(state == Game.STATE_MENU && Gdx.input.isKeyPressed(Keys.SPACE)){
			Resources.song.stop();
			Resources.intro.play();
			state = Game.STATE_INTRO;
			this.resetWorld();
			HUD.messageQueue.clear();
			HUD.showMessage("The year is 100 BC.");
			HUD.showMessage("For eternity, ancient civilisations have fought over natural resources.");
			HUD.showMessage("That is, until our village was gifted a beacon by the Gods!");
			HUD.showMessage("We pray to the beacon and are rewarded with gold and technology..");
			HUD.showMessage("But our enemies have also received a beacon from their Gods.");
			HUD.showMessage("We must destroy the enemy beacon!");
		}
		
		
		for(Body b : destroyQueue){
			world.destroyBody(b);
		}
		destroyQueue.clear();
		for(Entity e : addQueue){
			if(e != null){
				boxes.add(e);
				bodies.add(e.body);
				Input.registerListener(e);
			}
		}
		addQueue.clear();
		for(Player p : players){
			p.tick();
		}
		hud.tick();
		
		world.step((float) TICK_TIME, 6, 2);
		world.getBodies(bodies);
		for (Body b : bodies) {
			Sprite e = (Sprite) b.getUserData();

		    if (e != null) {
		        e.setPosition(b.getPosition().x, b.getPosition().y);
		        e.setRotation(MathUtils.radiansToDegrees * b.getAngle());
		    }
		}
		
		Camera.tick();
	}
	
	public void resize(int width, int height) {
		float dratio = (float)width/(float)height;

		Game.WIDTH = (int) (640 * dratio);
		Game.HEIGHT = 640;
		
		Game.screenRatioX = (float)Game.WIDTH / (float)width;
		Game.screenRatioY = (float)Game.HEIGHT / (float)height;
		
		Camera.reset();
	}

	public static double timeDelta() {
		return Gdx.graphics.getDeltaTime() / TICK_TIME;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchColl(int x, int y) {
		return true;
	}

	public static void addDestroy(Body body) {
		destroyQueue.add(body);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}
}

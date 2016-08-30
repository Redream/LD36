package com.redream.ld36;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Resources {
	public static TextureAtlas atlas = new TextureAtlas(file("pack.atlas"));

	public static boolean[] flipX = new boolean[500];
	public static boolean[] flipY = new boolean[500];

	public static AtlasRegion[] regions = new AtlasRegion[500];

	public static BitmapFont font = new BitmapFont(file("volter.fnt"),file("volter.png"),false);
	
	public static Sound build = Gdx.audio.newSound(file("build.wav"));
	public static Sound cannon = Gdx.audio.newSound(file("cannon.wav"));
	
	public static Music song = Gdx.audio.newMusic(file("song.wav"));
	public static Music intro = Gdx.audio.newMusic(file("intro.wav"));
	
	public static FileHandle file(String src) {
		return Gdx.files.internal(src);
	}

}

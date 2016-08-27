package com.redream.ld36;
import com.badlogic.gdx.Gdx;
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
	
	public static FileHandle file(String src) {
		return Gdx.files.internal(src);
	}

}

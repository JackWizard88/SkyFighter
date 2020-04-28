package ru.geebrains.starfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StarFighter extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backgroundMain;
	int screenWidth;
	int screenHeight;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		backgroundMain = new Texture("background.jpg");
		Gdx.graphics.setVSync(false);
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(backgroundMain, 0, 0, screenWidth, screenHeight);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		backgroundMain.dispose();
	}
}

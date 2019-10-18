package com.philosfight.game.ScreenClasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.philosfight.game.gameStuff.WorldController;
import com.philosfight.game.gameStuff.WorldRenderer;

public class GameScreen extends AbstractGameScreen {
	private static final String TAG = GameScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;

	public GameScreen (Game game) {
		super(game);
	}

	@Override  public void render (float deltaTime) {
		// Do not update gameStuff world when paused.
		if (!paused) {
			// Update gameStuff world by the time that has passed
			// since last rendered frame.
			worldController.update(deltaTime);
		}

		// Sets the clear screen color to: Rosso scuro
		Gdx.gl.glClearColor(28 / 255.0f, 17 / 255.0f,23/ 255.0f, 1);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render gameStuff world to screen
		worldRenderer.render();
	}

	@Override  public void resize (int width, int height) {
		worldRenderer.resize(width, height);
	}
	@Override  public void show () {
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}
	@Override  public void hide () {
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}
	@Override  public void pause () {
		paused = true;
	}
	@Override  public void resume () {
		super.resume();
		// Only called on Android!
		paused = false;
	}
}

package com.philosfight.game.ScreenClasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.philosfight.game.Menu.MenuRenderer;


public class MenuScreen extends AbstractGameScreen {
	private static final String TAG = MenuScreen.class.getName();

	private MenuRenderer menuRenderer;
	private boolean paused;

	/**
	 * Costruttore
	 * @param game
	 */
	public MenuScreen(Game game){
		super(game);
	}

	@Override
	public void show() {
		this.menuRenderer = new MenuRenderer();
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render(float delta) {
		// Sets the clear screen color to: Rosso scuro
		Gdx.gl.glClearColor(28 / 255.0f, 17 / 255.0f,23/ 255.0f, 1);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Renderizza il menu di gioco
		menuRenderer.render();
		if(Gdx.input.isTouched())
			game.setScreen(new GameScreen(game));
	}

	@Override
	public void resize(int width, int height) {
 		menuRenderer.resize(width, height);
	}

	@Override
	public void pause() {
		super.resume();
		// Only called on Android!
		paused = false;
	}

	@Override
	public void hide() {

	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}

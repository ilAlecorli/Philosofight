package com.philosfight.game.ScreenClasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.philosfight.game.game.Assets;

public abstract class AbstractGameScreen implements Screen {

	protected Game game;

	/**
	 * Costruttore
	 * @param game
	 */
	public AbstractGameScreen (Game game) {    this.game = game;  }

	@Override
	public abstract void show();
	@Override
	public abstract void render(float delta);
	@Override
	public abstract void resize(int width, int height);
	@Override
	public abstract void pause();
	@Override
	public abstract void hide();

	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	@Override
	public void dispose() {
		Assets.instance.dispose();
	}
}

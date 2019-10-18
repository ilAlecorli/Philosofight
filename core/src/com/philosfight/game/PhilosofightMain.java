package com.philosfight.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.philosfight.game.Menu.MenuAsset;
import com.philosfight.game.ScreenClasses.MenuScreen;
import com.philosfight.game.gameStuff.Assets;

public class PhilosofightMain extends Game {
	@Override  public void create () {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load gameStuff assets
		Assets.instance.init(new AssetManager());
		// Load menù assets
		MenuAsset.instance.init(new AssetManager());
		// Start gameStuff at menu screen
		setScreen(new MenuScreen(this));
	}
}
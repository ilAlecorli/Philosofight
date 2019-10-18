package com.philosfight.game.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.philosfight.game.utils.Constants;

import java.awt.Rectangle;

public class MenuAsset implements Disposable, AssetErrorListener {
	public static final String TAG = com.philosfight.game.gameStuff.Assets.class.getName();
	public static final MenuAsset instance = new MenuAsset();
	private AssetManager assetMenuManager;

	//Assets dello schermo di Menù
	public AssetCircleButton circleButton;
	public AssetRectangleButton rectangleButton;
	public AssetCheckMark checkMark;
	public AssetCrossMark crossMark;
	public AssetBackground menuBackground;


	/**
	 * Singleton: Può esistere solo una sola istanza di Assets,
	 * definendo un costruttore privato si previene che le altre
	 * classi istanzino la stessa classe.
	 */
	private MenuAsset() {

	}

	/**
	 * Si occupa di gestire gli assets di gioco: dal caricamento al checking degli errori.
	 *
	 * @param assetMenuManager contiene tutti i metodi necessari per utilizzare efficacemente gli assets.
	 */
	public void init(AssetManager assetMenuManager) {
		this.assetMenuManager = assetMenuManager;
		// Imposta l'error handler dell'asset manager
		assetMenuManager.setErrorListener(this);

		// Carica le texture atlas dell'arena
		assetMenuManager.load(Constants.TEXTURE_ATLAS_MENU, TextureAtlas.class);

		//Inizia il caricamento degli assets ed aspetta che abbiano finito
		assetMenuManager.finishLoading();

		//Messaggi di debug
		Gdx.app.debug(TAG, "# of assets loaded: " + assetMenuManager.getAssetNames().size);
		for (String a : assetMenuManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		//Carica i dati degli asset dalla fonte definita in Constants.TEXTURE_ATLAS_ARENA
		TextureAtlas atlas = assetMenuManager.get(Constants.TEXTURE_ATLAS_MENU);

		//Attiva il texture filtering
		for (Texture t : atlas.getTextures()) {
			t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}

		//Crea le risorse di gioco
		circleButton = new AssetCircleButton(atlas);
		rectangleButton = new AssetRectangleButton(atlas);
		checkMark = new AssetCheckMark(atlas);
		crossMark = new AssetCrossMark(atlas);
		menuBackground = new AssetBackground(atlas);

	}

	/**
	 * Asset bottone circolare
	 */
	public class AssetCircleButton {

		public final TextureAtlas.AtlasRegion circleButton;

		public AssetCircleButton(TextureAtlas atlas) {
			circleButton = atlas.findRegion("CircleButtonIcon");
		}

	}

	/**
	 * Asset bottone rettangolare
	 */
	public class AssetRectangleButton {

		public final TextureAtlas.AtlasRegion rectangleButton;

		public AssetRectangleButton(TextureAtlas atlas) {
			rectangleButton = atlas.findRegion("RectangeButtonIcon");
		}

	}
	/**
	 * Asset marchio check
	 */
	public class AssetCheckMark {

		public final TextureAtlas.AtlasRegion checkMark;

		public AssetCheckMark(TextureAtlas atlas) {
			checkMark = atlas.findRegion("CheckMark");
		}

	}
	/**
	 * Asset marchio x
	 */
	public class AssetCrossMark {

		public final TextureAtlas.AtlasRegion crossMark;

		public AssetCrossMark (TextureAtlas atlas) {
			crossMark = atlas.findRegion("CrossMark");
		}

	}

	/**
	 * Asset sfondo Menù
	 */
	public class AssetBackground {

		public final TextureAtlas.AtlasRegion menuBackground;

		public AssetBackground(TextureAtlas atlas) {
			menuBackground = atlas.findRegion("NightSky Background");
		}

	}

	/**
	 * Dispose di tutti gli assets.
	 */
	@Override
	public void dispose() {
		assetMenuManager.dispose();
	}

	public void error(String filename, Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception) throwable);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
	}
}


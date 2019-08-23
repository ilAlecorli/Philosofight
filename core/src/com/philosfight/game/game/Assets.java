package com.philosfight.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.philosfight.game.utils.Constants;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    public AssetWall wall;

    // Singleton: Può esistere solo una sola istanza di Assets,
    // definendo un costruttore privato si previene che le altre
    // classi istanzino la classe.
    private Assets () {}

    public void init (AssetManager assetManager) {
        this.assetManager = assetManager;
        // Imposta l'error handler dell'asset manager
        assetManager.setErrorListener(this);
        // Carica le texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        //Carica gli assets ed aspetta che abbiano finito
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear,  Texture.TextureFilter.Linear);
        }

        //Crea le risorse di gioco
        wall = new AssetWall(atlas);
    }
    public class AssetWall {
        public final TextureAtlas.AtlasRegion barrier;
        public AssetWall (TextureAtlas atlas) {
            barrier = atlas.findRegion("wall");
        }
    }

    @Override
    public void dispose () {
        assetManager.dispose();
    }

    public void error(String filename, Class type, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '"+ filename + "'", (Exception)throwable);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
    }
}


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

    //Definisco gli assets di gioco
    public AssetWall wall;
    public AssetTile tile;
    public AssetPlayer player;

    /**
     * Singleton: Può esistere solo una sola istanza di Assets,
     * definendo un costruttore privato si previene che le altre
     * classi istanzino la stessa classe.
     */
    private Assets() {
    }

    /**
     * Si occupa di gestire gli assets di gioco: dal caricamento al checking degli errori.
     *
     * @param assetManager contiene tutti i metodi necessari per utilizzare efficacemente gli assets.
     */
    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // Imposta l'error handler dell'asset manager
        assetManager.setErrorListener(this);
        // Carica le texture atlas dell'arena
        assetManager.load(Constants.TEXTURE_ATLAS_ARENA, TextureAtlas.class);
        //Inizia il caricamento degli assets ed aspetta che abbiano finito
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        //Carica i dati degli asset dalla fonte definita in Constants.TEXTURE_ATLAS_ARENA
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_ARENA);

        //Attiva il texture filtering
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        //Crea le risorse di gioco
        wall = new AssetWall(atlas);
        tile = new AssetTile(atlas);
        player = new AssetPlayer(atlas);
    }


    public class AssetPlayer{
        public final TextureAtlas.AtlasRegion pg;
        public AssetPlayer(TextureAtlas atlas){
            pg = atlas.findRegion("player");
        }
    }

    /**
     * Asset dei muri dell'arena.
     */
    public class AssetWall {
        public final TextureAtlas.AtlasRegion east;
        public final TextureAtlas.AtlasRegion nord;
        public final TextureAtlas.AtlasRegion corner;
        public AssetWall(TextureAtlas atlas) {
            east = atlas.findRegion("wall0");
            nord = atlas.findRegion("wall2");
            corner = atlas.findRegion("wall1");
        }
    }

    /**
     * Asset del pavimento
     */
    public class AssetTile {
        public final TextureAtlas.AtlasRegion tile00;

        public AssetTile(TextureAtlas atlas) {
            tile00 = atlas.findRegion("floor");
        }
    }

    /**
     * Dispose degli assets.
     */
    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public void error(String filename, Class type, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception) throwable);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
    }
}


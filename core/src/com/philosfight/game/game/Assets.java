package com.philosfight.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.philosfight.game.utils.Constants;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    //Assets di gioco
    public AssetWall wall;
    public AssetTile tile;
    public AssetPlayer player;
    public AssetBullet bullet;
    public AssetMelee circle;
    public AssetFonts fonts;
    public AssetBlankPixel blank;



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

        //Messaggi di debug
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
        bullet = new AssetBullet(atlas);
        circle = new AssetMelee(atlas);
        fonts = new AssetFonts();
        blank = new AssetBlankPixel(atlas);
	}

	/**
	 * Asset per il Font Arial
	 */
    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts () {
            // Create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("Images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("Images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("Images/arial-15.fnt"), true);

            // Set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);

            // Enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    /**
     * Asset dei giocatori
     */
    public class AssetPlayer{
        public final TextureAtlas.AtlasRegion pg1;
        public final TextureAtlas.AtlasRegion pg2;
        public AssetPlayer(TextureAtlas atlas){
            pg1 = atlas.findRegion("PG1_front1");
            pg2 = atlas.findRegion("PG2_front1");
        }
    }

    /**
     * Asset dei muri dell'arena.
     */
    public class AssetWall {
        public final TextureAtlas.AtlasRegion front;
        public final TextureAtlas.AtlasRegion back;
        public final TextureAtlas.AtlasRegion side;
        public final TextureAtlas.AtlasRegion lateral;
        public final TextureAtlas.AtlasRegion corner;
        public AssetWall(TextureAtlas atlas) {
            lateral = atlas.findRegion("wall_lateral");
            corner = atlas.findRegion("wall_corner");
            front = atlas.findRegion("wall_front");
            back = atlas.findRegion("wall_back");
            side= atlas.findRegion("wall_side");
        }
    }

    /**
     * Asset del pavimento
     */
    public class AssetTile {
        public final TextureAtlas.AtlasRegion tile00;

        public AssetTile(TextureAtlas atlas) {
            tile00 = atlas.findRegion("tile");
        }
    }

    /**
     * Asset dei proiettili
     */
    public  class AssetBullet {
        public final TextureAtlas.AtlasRegion bullet;
        public AssetBullet(TextureAtlas atlas) { bullet = atlas.findRegion("bullet");}
    }

    /**
     * Asset dell'area Melee
     */
    public  class AssetMelee {
        public final TextureAtlas.AtlasRegion melee;
        public AssetMelee(TextureAtlas atlas) { melee = atlas.findRegion("melee");}
    }

	/**
	 * Asset pixel bianco da colorare
	 */
	public class AssetBlankPixel {
		public final TextureAtlas.AtlasRegion blank;
		public AssetBlankPixel(TextureAtlas atlas) { blank= atlas.findRegion("blank");}

	}

    /**
     * Dispose di tutti gli assets.
     */
    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    public void error(String filename, Class type, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception) throwable);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
    }
}


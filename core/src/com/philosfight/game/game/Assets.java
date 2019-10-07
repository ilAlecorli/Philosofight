package com.philosfight.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public class AssetPlayer {
        public final TextureAtlas.AtlasRegion PG1_standby;
        public final TextureAtlas.AtlasRegion PG2_standby;
        public final Animation PG1_walk_up;
        public final Animation PG1_walk_down;
        public final Animation PG1_walk_left;
        public final Animation PG1_walk_right;

        public AssetPlayer(TextureAtlas atlas) {
            //Variabili generali:

            //Assets Player 1: Standby
            PG1_standby = atlas.findRegion("PG1_front1");
            //Assets Player 2: Standby
            PG2_standby = atlas.findRegion("PG2_front1");

            //Animazione Player 1: Camminata verso l'alto
            PG1_walk_up = createAnimation("Character1/PG1_up.png", 1, 5);

            //Animazione Player 1: Camminata verso il basso
            PG1_walk_down = createAnimation("Character1/PG1_down.png", 1, 5);

            //Animazione Player 1: Camminata verso sinistra
            PG1_walk_left = createAnimation("Character1/PG1_left.png", 1, 3);

            //Animazione Player 1: Camminata verso destra
            PG1_walk_right = createAnimation("Character1/PG1_right.png", 1, 3);
        }
        private Animation createAnimation(String image_path, int n_rows, int n_cols) {
            Texture image = new Texture(Gdx.files.internal(image_path));
            TextureRegion[][] tmp = TextureRegion.split(image,
                    image.getWidth() / n_cols,
                    image.getHeight() / n_rows);
            TextureRegion[] frames  = new TextureRegion[n_rows * n_cols];
            int index = 0;
            index = 0;
            for(int i = 0; i < n_rows; i++) {
                for (int j = 0; j < n_cols; j++) {
                    frames[index++] = tmp[i][j];
                }
            }
            image.dispose();

            return new Animation<TextureRegion>(1.0f / 10.0f, frames);
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


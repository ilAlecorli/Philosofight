package com.philosfight.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
    private static String TAG = WorldRenderer.class.getName();

    private OrthographicCamera camera;

//    SpriteBatch permette di disegnare gli oggetti rispettando le impostazioni della telecamera
    private SpriteBatch batch;
    private WorldController worldController;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    public void init(){


    }

//    Metodo per decretare l'ordine di renderizzazione degli oggeti
    public void render(){


    }

    public void resize(){

    }

    @Override
    public void dispose() {

    }
}

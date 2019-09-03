package com.philosfight.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.philosfight.game.utils.Constants;

public class WorldRenderer implements Disposable {
    private static String TAG = WorldRenderer.class.getName();
    private OrthographicCamera camera;

//  batch permette di disegnare gli oggetti rispettando le impostazioni della telecamera
    private SpriteBatch batch;
    private WorldController worldController;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    /**
     * Metodo per l'inizializzazione degli sprite di gioco
     */
    private void init(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
    }

    /**
     * Metodo per decretare l'ordine di renderizzazione degli oggetti
     */
    public void render(){
        renderWorld(batch);
    }


    private void renderWorld (SpriteBatch batch) {
        //Applica i setting di cameraHelper(WorldController) a camera
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.arena.render(batch);
        worldController.renderBullets(batch);
        batch.end();
    }


    /**
     *  Metodo di resize di renderer nel caso ci siano delle modifiche alla finestra di gioco
     */
    public void resize(){
    }

    /**
     * Metodo per deallocare correttamente la memoria alla fine dell'esecuzione
     */
    @Override
    public void dispose() {
        batch.dispose();
    }
}

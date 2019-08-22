package com.philosfight.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

//    Metodo per l'inizializzazione degli sprite di gioco
    private void init(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
    }

//    Metodo per decretare l'ordine di renderizzazione degli oggetti
    public void render(){
        renderTestObjects();
    }
//    Metodo per la renderizzazione degli oggetti di testing
    private void renderTestObjects(){
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Sprite sprite : worldController.testSprites){
            sprite.draw(batch);
        }
        batch.end();
    }
    public void resize(){

    }

//    Metodo per deallocare correttamente la memoria alla fine del'esecuzione
    @Override
    public void dispose() {
        batch.dispose();
    }
}

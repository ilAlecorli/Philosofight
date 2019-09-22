package com.philosfight.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.philosfight.game.utils.Constants;

public class WorldRenderer implements Disposable {
    private static String TAG = WorldRenderer.class.getName();
    //Telecamera di gioco
    private OrthographicCamera camera;
    //Interfaccia statistiche
    private OrthographicCamera cameraGUI;

    //batch permette di disegnare gli oggetti rispettando le impostazioni della telecamera
    private SpriteBatch batch;
    private WorldController worldController;

    /**
     * Inizializzazione del Renderizzatore con il Controllore di gioco
     * @param worldController
     */
    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    /**
     * Renderizzazione FPS del gioco
     * -    Lunghezza lettera singola in font Arial: 9 pxl
     * -    Altezza Parola in font Arial: 15 pxl
     * @param batch
     */
    private void renderGuiFpsCounter (SpriteBatch batch) {
        //Coordinate schermo: nord-est;
        float x = cameraGUI.viewportWidth - 9 * 6;           //larghezza in pixel font * numero lettere parola
        float y = 0;
        int fps = com.badlogic.gdx.Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        //Colorazioni differenti per velocità
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        //Scritta con coordinate settate
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }

    private void  renderHealth(SpriteBatch batch){
        //Disegna la vita del player 1
        batch.draw(Assets.instance.blank.blank, 0,0,worldController.arena.player1.getHealthPlayer(),5 );
    }

    /**
     * Renderizzazione Interfaccia Utente:
     * presenta statistiche di ogni tipo in game
     * @param batch
     */
    private void renderGui (SpriteBatch batch) {

        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        //Disegna la vita dei players
        renderHealth(batch);
        // draw FPS text
        renderGuiFpsCounter(batch);
        batch.end();
    }

    /**
     * Metodo per l'inizializzazione degli sprite di gioco
     */
    private void init(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    /**
     * Metodo per decretare l'ordine di renderizzazione degli oggetti
     */
    public void render(){
        renderWorld(batch); renderGui(batch);
    }


    private void renderWorld (SpriteBatch batch) {
        //Applica i setting di cameraHelper(WorldController) a camera
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.arena.render(batch);
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

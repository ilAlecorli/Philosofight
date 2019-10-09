package com.philosfight.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.philosfight.game.utils.Constants;
import com.philosfight.game.ScreenClasses.GameGUI;

public class WorldRenderer implements Disposable {
    private static String TAG = WorldRenderer.class.getName();
    //Telecamera di gioco
    private OrthographicCamera camera;
    //Interfaccia statistiche
    private OrthographicCamera cameraGUI;

    //batch permette di disegnare gli oggetti rispettando le impostazioni della telecamera
    private SpriteBatch batch;
    //Il controllore delle dinamiche interne del Game
    private WorldController worldController;
    //Interfaccia Grafica Utente
    private GameGUI gameGUI;

    /**
     * Inizializzazione del Renderizzatore con il Controllore di gioco
     * @param worldController
     */
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
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        gameGUI = new GameGUI(batch,cameraGUI,worldController.arena);
        cameraGUI.update();
    }

    /**
     * Metodo per decretare l'ordine di renderizzazione degli oggetti
     */
    public void render(){
        renderWorld(batch);
        gameGUI.renderGui();
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
    public void resize(int width, int height){
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    /**
     * Metodo per deallocare correttamente la memoria alla fine dell'esecuzione
     */
    @Override
    public void dispose() {
        batch.dispose();
    }
}

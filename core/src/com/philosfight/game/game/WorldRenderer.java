package com.philosfight.game.game;

import com.badlogic.gdx.graphics.Color;
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

    //Vita iniziale 2 player
    private float intialHealthP1;
    private float intialHealthP2;

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
        //Inizializzazione della vita iniziale dei player, per renderizzarne
        //l'aggiornamento in percentuale nella GUI
        intialHealthP1 = worldController.arena.player1.getHealthPlayer();
        intialHealthP2 = worldController.arena.player2.getHealthPlayer();

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    /**
     * Metodo per decretare l'ordine di renderizzazione degli oggetti
     */
    public void render(){
        renderWorld(batch);
       renderGui(batch);
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
     * Renderizzazione Interfaccia Utente:
     * presenta statistiche di ogni tipo in game
     * @param batch
     */
    private void renderGui (SpriteBatch batch) {

        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        //Disegna la vita dei players
        renderHealth(batch);
        //Disegna il mana dei players
        renderMana(batch);
        //Draw FPS text
        renderGuiFpsCounter(batch);
        batch.end();
    }

    /**
     * Metodo per renderizzare la barra del Mana dei giocatori
     * @param batch
     */
    private void  renderMana(SpriteBatch batch){
        //Inserimento delle vite dei Player giocanti
        float manaP1 = worldController.arena.player1.getMana();
        float manaP2 = worldController.arena.player2.getMana();
        //Dimensioni barra da visualizzare
        float heightBar = 10;
        float widthBar = 3;
        //Disegna la vita del player 1
        batch.setColor(Color.ROYAL);
        //In alto a sinistra
        batch.draw(Assets.instance.blank.blank, 0,4 + 10,widthBar * manaP1,1 * heightBar );
        batch.setColor(Color.WHITE);
        //Disegna la vita del player 2
        batch.setColor(Color.ROYAL);
        //In basso a destra
        batch.draw(Assets.instance.blank.blank, 1 * cameraGUI.viewportWidth - widthBar * manaP2 ,1 * cameraGUI.viewportHeight - heightBar - 4,
                widthBar * manaP2,1*heightBar );
        batch.setColor(Color.WHITE);
    }

    /**
     * Metodo per renderizzare l'healthbar dei giocatori
     * @param batch
     */
    private void  renderHealth(SpriteBatch batch){
        //Inserimento delle vite dei Player giocanti
        float healthP1 = worldController.arena.player1.getHealthPlayer();
        float healthP2 = worldController.arena.player2.getHealthPlayer();
        //Dimensioni barra da visualizzare
        float heightBar = 10;
        float widthBar = 3;
        //Disegna la vita del player 1
        if (healthP1 >= intialHealthP1 *2/3) {
            //Vita iniziale a più 2/3
            batch.setColor(0, 1, 0, 1);
        } else if (healthP1 >= intialHealthP2/3) {
            //Vita iniziale a più 1/3
            batch.setColor(1, 1, 0, 1);
        } else {
            //Vita meno di un 1/3
            batch.setColor(1, 0, 0, 1);
        }
        //In alto a sinistra
        batch.draw(Assets.instance.blank.blank, 0,4,widthBar * healthP1,1 * heightBar );
        batch.setColor(Color.WHITE);
        //Disegna la vita del player 2
        if (healthP2 >= intialHealthP2 * 2/3) {
            //Vita iniziale a più 2/3
            batch.setColor(0, 1, 0, 1);
        } else if (healthP2 >= intialHealthP2 /3) {
            //Vita iniziale a più 1/3
            batch.setColor(1, 1, 0, 1);
        } else {
            //Vita meno di un 1/3
            batch.setColor(1, 0, 0, 1);
        }
        //In basso a destra
        batch.draw(Assets.instance.blank.blank, 1 * cameraGUI.viewportWidth - widthBar * healthP2 ,1 * cameraGUI.viewportHeight - heightBar - 4 - 10,
                widthBar * healthP2,1*heightBar );
        batch.setColor(Color.WHITE);
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

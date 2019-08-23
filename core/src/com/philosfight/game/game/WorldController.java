package com.philosfight.game.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.philosfight.game.utils.CameraHelper;
import com.badlogic.gdx.utils.Array;

public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();
    public Sprite[] testSprites;
    public int selectedSprite;
    public CameraHelper cameraHelper;

    public WorldController() {
        init();
    }

//    Metodo per i comandi di debug
    @Override
    public boolean keyUp(int keycode) {
//    Resetta il mondo di gioco
        if(keycode == Input.Keys.R){
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
//        Seleziona il prossimo sprite
        else if(keycode == Input.Keys.SPACE){
            selectedSprite = (selectedSprite + 1) % testSprites.length;
            //Aggiorna l’obiettivo della telecamera per seguire lo sprite selezionato
            if(cameraHelper.hasTarget()){
                cameraHelper.setTarget(testSprites[selectedSprite]);
            }

            Gdx.app.debug(TAG, "Sprite #" + selectedSprite + "selected");
        }
        //Aziona il camera follow
        else if(keycode == Input.Keys.ENTER){
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : testSprites[selectedSprite]);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        return false;
    }

//        Metodo in cui vengono inizializzati gli oggetti del game world
    private void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        initTestObject();
    }

//       Metodo per l'inzializzazione degli oggetti di testing
    public void initTestObject() {
/**
 * Crea un array per 5 sprites
 */
        testSprites = new Sprite[5];
        TextureRegion region = new TextureRegion();
            region = Assets.instance.wall.barrier;
//        Crea una pixmap vuota POT-sized a 8 bit RGBA
//        int width = 32;
//        int height = 32;
//        Pixmap pixmap = createProceduralPixmap(width, height);

//        Crea una nuova texture dai dati pixmap,
//        cioè crea il prototipo dell'oggetto
//        Texture texture = new Texture(pixmap);

//        Genera nuovi sprite utilizzando la texture appena creata
        for (int i = 0; i < testSprites.length; i++) {
            Sprite spr = new Sprite(region);
//            Definisce la grandezza degli sprite ad 1m x 1m
            spr.setSize(1, 1);

//            Imposta l'origine degli sprites nel centro
            spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);

//            Imposta la posizione dello sprite randomicamente
            float randomX = MathUtils.random(-2.0f, 2.0f);
            float randomY = MathUtils.random(-2.0f, 2.0f);
            spr.setPosition(randomX, randomY);

//            Salva il nuovo sprite nell'array
            testSprites[i] = spr;
        }
//        Imposta il primo sprite come quello selezionato
        selectedSprite = 0;
    }

//        Metodo per la creazione delle Pixmap
    private Pixmap createProceduralPixmap(int width, int height) {
//        Crea un quadrato
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGB888);

//        Riempie il quadrato col colore rosso al 50% opaco
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();

//        Disegna una X gialla sul quadrato
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);

//        Disegna un bordo color ciano  sul quadrato
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

//        Metodo per l'aggiornamento degli oggetti durante l'esecuzione
    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        updateTestObject(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void handleDebugInput(float deltaTime){
        if(Gdx.app.getType() != Application.ApplicationType.Desktop) return;
//        Controlli per lo sprite selezionato
        float sprMoveSpeed = 5 * deltaTime;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            moveSelectedSprite(-sprMoveSpeed, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            moveSelectedSprite(sprMoveSpeed, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            moveSelectedSprite(0, sprMoveSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            moveSelectedSprite(0, -sprMoveSpeed);

        // Camera Controls (move)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            camMoveSpeed *= camMoveSpeedAccelerationFactor;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            moveCamera(-camMoveSpeed, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            moveCamera(camMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            moveCamera(0, camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            moveCamera(0, -camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
            cameraHelper.setPosition(0, 0);

// Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD))
            cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH))
            cameraHelper.setZoom(1);
    }

    private void moveCamera(float x, float y){
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    private void moveSelectedSprite(float x, float y){
        testSprites[selectedSprite].translate(x,y);
    }

//     Metodo per l'aggiornamento degli oggetti di testing
    private void updateTestObject(float deltaTime) {
//        Riceve la rotazione corrente dallo sprite selezionato
        float rotation = testSprites[selectedSprite].getRotation();

//        Ruota l'oggetto di 90 gradi al secondo
        rotation += 90 * deltaTime;

//        Avvolge l'oggetto di 360 gradi
//        rotation %= 360;

//        Seleziona un nuovo valore di rotazione per lo sprite selezionato
        testSprites[selectedSprite].setRotation(rotation);
    }
}


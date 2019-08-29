package com.philosfight.game.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.philosfight.game.game.objects.AbstractGameObject;
import com.philosfight.game.game.objects.Player;
import com.philosfight.game.game.objects.Wall;
import com.philosfight.game.utils.CameraHelper;
import com.philosfight.game.utils.Constants;
import com.badlogic.gdx.math.Rectangle;


public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();
    public CameraHelper cameraHelper;
    public Arena arena;

//    Rettangoli per il riconoscimento delle collisioni
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();


    public WorldController() {
        init();
    }

    /**
     * Metodo di inizializzazione degli oggetti del game world
     */
    private void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        initArena();
    }

    /**
     * Metodo per l'aggiornamento degli oggetti durante l'esecuzione
     */
    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void initArena(){
        arena = new Arena(Constants.ARENA_00);
    }


    /**
     * Metodo che contiene i comandi della telecamera e di alcuni oggetti
     */
    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        // Camera Controls (move)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            camMoveSpeed *= camMoveSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            moveCamera(-camMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
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
        if (Gdx.input.isKeyPressed(Input.Keys.F1))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.F2))
            cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.setZoom(1);
    }

    private void moveCamera(float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    /**
     * Metodo per i comandi di debug
     */

    @Override
    public boolean keyUp(int keycode) {
//    Resetta il mondo di gioco
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        return false;
    }


    /**
     * Funzione per controllare le collisioni fra oggetti astratti
     * @param obj1
     * @param obj2
     */
    public void CheckCollisions(AbstractGameObject obj1, AbstractGameObject obj2) {
        r1.set(obj1.position.x, obj1.position.y, obj1.bounds.width, obj1.bounds.height);
        r2.set(obj2.position.x, obj2.position.y, obj2.bounds.width, obj2.bounds.height);
        if(r1.overlaps(r2)) {
            if (obj1 instanceof Wall || obj2 instanceof  Wall) {
                if (obj1 instanceof Player)
                    onCollisionPlayerWithWall(obj1, obj2);
                else if (obj2 instanceof Player)
                    onCollisionPlayerWithWall(obj2, obj1);
                else{}
            }
        }
    }

    private void onCollisionPlayerWithWall(AbstractGameObject player, AbstractGameObject obj) {
        float heightDifference = Math.abs(player.position.y - (obj.position.y + obj.bounds.height));
        float widthDifference = Math.abs(player.position.x - (obj.position.x + obj.bounds.width));
        if(heightDifference < (obj.bounds.height / 2)) {
            if(player.position.y > obj.position.y)
                player.position.y = player.position.y + (heightDifference - (obj.bounds.height / 2));
            else
                player.position.y = player.position.y - (heightDifference - (obj.bounds.height / 2));
        }
        if(widthDifference < (obj.bounds.width / 2)){
            if(player.position.x > obj.position.x)
                player.position.x = player.position.x + (widthDifference - (obj.bounds.width / 2));
            else
                player.position.x = player.position.x - (widthDifference - (obj.bounds.width / 2));

        }
        return;
    }
//
//
//    }
}


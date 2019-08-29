package com.philosfight.game.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
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

    private void onCollisionPlayerWithWall(Wall wall, Player player){
        float heightDifference = Math.abs(player.position.y - (  wall.position.y + wall.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = player.position.x > (wall.position.x + wall.bounds.width / 2.0f);
            if (hitRightEdge) {
                player.position.x = wall.position.x + wall.bounds.width;
            } else {
                player.position.x = wall.position.x - player.bounds.width;
            }
        }
        return;
    }

    private void testCollisions(){
       // r1.set(arena.player.position.x, arena.player.position.y, arena.player.bounds.width, arena.player.bounds.height);
        //    Test collision: Player <-> Wall
        for(Wall wall : arena.walls){
            r2.set(wall.position.x, wall.position.y, wall.bounds.width, wall.bounds.height);
            if(!r1.overlaps(r2))
                continue;
         //   onCollisionPlayerWithWall(wall, arena.player);
        }
    }




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
}
package com.philosfight.game.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philosfight.game.game.Effects.Bullet;
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

    //Rettangoli per il riconoscimento delle collisioni
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
        initArena();
        cameraHelper = new CameraHelper();
        cameraHelper.setPosition(arena.pixmap.getWidth() / 2, arena.pixmap.getHeight() / 2);
        cameraHelper.setZoom(2.5f);
    }

    /**
     * Metodo per l'aggiornamento del movimento degli oggetti durante l'esecuzione:
     * inserire qui i nuovi oggetti mobili da animare
     */
    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        arena.player1.movementCheck(deltaTime);
        arena.player1.update(deltaTime);
        //Anima ogni singolo proiettile:
        for (Bullet e :
                arena.player1.loader) {
            e.update(deltaTime);
            //se il proiettile selezionato deve sparire verrà rimosso
            if (e.shouldRemove()) arena.player1.loader.remove(e);
        }
        CheckCollisions(arena.player1);

        arena.player2.movementCheck(deltaTime);
        arena.player2.update(deltaTime);
        //Anima ogni singolo proiettile:
        for (Bullet e :
                arena.player2.loader) {
            e.update(deltaTime);
            //se il proiettile selezionato deve sparire verrà rimosso
            if (e.shouldRemove()) arena.player1.loader.remove(e);
        }
        CheckCollisions(arena.player2);
        cameraHelper.update(deltaTime);
    }

    private void initArena() {
        arena = new Arena(Constants.ARENA_00);
    }


    /**
     * Metodo che contiene i comandi della telecamera e di alcuni oggetti
     */
    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) checkShooting();

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

    //Fa un controllo degli spari dei players
    private void checkShooting() {
        arena.player1.shootAt(arena.player2);
        arena.player2.shootAt(arena.player1);
    }

    /**
     * Funzione per controllare le collisioni fra oggetti generici
     */
    public void CheckCollisions(Player player) {
        r1.set(player.position.x, player.position.y, player.bounds.width, player.bounds.height);
        for (Wall wall : arena.walls) {
            r2.set(wall.position.x, wall.position.y, wall.bounds.width, wall.bounds.height);
            if (r1.overlaps(r2)) {
                onCollisionPlayerWithWall(player, wall);
            }
        }
    }

    private void onCollisionPlayerWithWall(Player player, Wall wall) {
        float search_collision_x = Math.abs((player.position.x + player.bounds.width) - (wall.position.x + wall.bounds.width));
        float search_collision_y = Math.abs((player.position.y + player.bounds.height) - (wall.position.y + wall.bounds.height));

        if (search_collision_x < wall.bounds.width) {
            player.velocity.x = 0;
            if (player.position.x > wall.position.x) {
                player.position.x = wall.position.x + wall.bounds.width;
            } else {
                player.position.x = wall.position.x - player.bounds.width + 0.01f;
            }
        } else if (search_collision_y < wall.bounds.height) {
            player.velocity.y = 0;
            if (player.position.y > wall.position.y) {
                player.position.y = wall.position.y + wall.bounds.height;
            } else {
                player.position.y = wall.position.y - player.bounds.y + 0.01f;
            }
        }
        return;
    }

    //Metodo per renderizzare i proiettili in separata sede
    public void renderBullets(SpriteBatch batch)
    {
        //Disegna tutti i proiettili
        for(Bullet b : arena.player1.loader)
        {
            b.render(batch);
        }
        for(Bullet b: arena.player2.loader)

        {
            b.render(batch);
        }
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
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
            //Se la camera è libera
            if (cameraHelper.hasTarget() == false) {
                //Attiva il player1
                arena.player1.setMovementEnable(true);
                arena.player1.setShootEnable(true);
                //Puntala sul primo player
                cameraHelper.setTarget(arena.player1);
                Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
            } else if (cameraHelper.hasTarget() == true && cameraHelper.getTarget() == arena.player1) {
                //Disattiva il player1
                arena.player1.setMovementEnable(false);
                arena.player1.setShootEnable(false);
                //Attiva il player2
                arena.player2.setMovementEnable(true);
                arena.player2.setShootEnable(true);
                //Se è già occupata dal primo player va al successivo
                cameraHelper.setTarget(arena.player2);
            } else  if (cameraHelper.hasTarget() == true && cameraHelper.getTarget() == arena.player2){
                //Disattiva player2
                arena.player2.setMovementEnable(false);
                arena.player2.setShootEnable(false);
                //Se è già occupata puntala dal secondo player viene liberata
                cameraHelper.setTarget(null);
            }

        }
        return false;
    }
}



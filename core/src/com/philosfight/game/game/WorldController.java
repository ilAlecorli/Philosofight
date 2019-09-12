package com.philosfight.game.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Effects.Bullet;
import com.philosfight.game.game.objects.AbstractGameObject;
import com.philosfight.game.game.objects.Player;
import com.philosfight.game.game.objects.Wall;
import com.philosfight.game.utils.CameraHelper;
import com.philosfight.game.utils.Constants;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Classe che si occupa di gestire l'aggiornamento del mondo di gioco.
 */
public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Arena arena;

	//Cestino bullet:
	public ArrayList<Bullet> bulletsDump;

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
		bulletsDump = new ArrayList<Bullet>();
	}

	/**
	 * Metodo per l'aggiornamento degli oggetti durante l'esecuzione
	 */
	public void update(float deltaTime) {
		//Ricezione dei comandi
		handleDebugInput(deltaTime);
		arena.player1.movementCheck(deltaTime);
		arena.player1.update(deltaTime);
		//Anima ogni singolo proiettile:
		for (Bullet e:
				arena.bulletsLoader1) {
			e.update(deltaTime);
			//Se il proiettile selezionato deve sparire verrà buttato in un array da rimuovere:
			if (e.shouldRemove()) bulletsDump.add(e);

		}
		//Rimuovi tutti i proiettili da rimuovere:
		arena.bulletsLoader1.removeAll(bulletsDump);

		arena.player2.movementCheck(deltaTime);
		arena.player2.update(deltaTime);
		//Anima ogni singolo proiettile:
		for (Bullet e:
				arena.bulletsLoader2) {
			e.update(deltaTime);
			//Se il proiettile selezionato deve sparire verrà buttato in un array da rimuovere:
			if (e.shouldRemove()) bulletsDump.add(e);

		}
		//Rimuovi tutti i proiettili da rimuovere:
		arena.bulletsLoader2.removeAll(bulletsDump);

		//Controllo delle collisioni dell'arena
		arena.checkCollisions();

		//Aggiornamento della telecamera
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

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			if (arena.player1.isShootEnable()){
				Gdx.app.debug(TAG, arena.player1.getNamePlayer() + " is shooting");
				arena.player1.shootAt(arena.player2);}
			else if (arena.player2.isShootEnable()) {
				Gdx.app.debug(TAG, arena.player2.getNamePlayer() + " is shooting");
				arena.player2.shootAt(arena.player1);
			}
		}

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
		//Resetta il mondo di gioco
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



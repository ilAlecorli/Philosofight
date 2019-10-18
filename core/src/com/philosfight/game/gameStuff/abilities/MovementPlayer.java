package com.philosfight.game.gameStuff.abilities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.philosfight.game.gameStuff.objects.Player;

public class MovementPlayer {

	//Flag di movimento
	private boolean movementEnableWest;
	private boolean movementEnableEast;
	private boolean movementEnableNord;
	private boolean movementEnableSud;

	//Comandi di Movimento
	private int key_Up;
	private int key_Down;
	private int key_Left;
	private int key_Right;

	/**
	 * Costruttore
	 */
	public MovementPlayer() {
		movementEnableWest = false;
		movementEnableEast = false;
		movementEnableNord = false;
		movementEnableSud = false;
	}

	/**
	 *Setters Flag Movimento
	 */
	public void setMovementEnable(boolean state) {
		//Gdx.app.debug(TAG, "Movement player set: " + movementEnableEast + " on Player: " + namePlayer);
		this.movementEnableEast = state;
		this.movementEnableWest = state;
		this.movementEnableNord = state;
		this.movementEnableSud = state;
	}
	public void setMovementEnableOvest(boolean movementEnableWest) {
		this.movementEnableWest = movementEnableWest;
	}
	public void setMovementEnableEast(boolean movementEnableEast) {
		this.movementEnableEast = movementEnableEast;
	}
	public void setMovementEnableNord(boolean movementEnableNord) {
		this.movementEnableNord = movementEnableNord;
	}
	public void setMovementEnableSud(boolean movementEnableSud) {
		this.movementEnableSud = movementEnableSud;
	}

	/**
	 *Getters Flag Movimento
	 */
	public boolean getMovementEnableEast() {
		return movementEnableEast;
	}
	public boolean getMovementEnableWest() {
		return movementEnableWest;
	}
	public boolean getMovementEnableNord() {
		return movementEnableNord;
	}
	public boolean getMovementEnableSud() {
		return movementEnableSud;
	}

	/**
	 * Setter comandi di movimento su assi x e y
	 * @param key_Up
	 * @param key_Down
	 * @param key_Left
	 * @param key_Right
	 */
	public void setMoveControls(int key_Up, int key_Down, int key_Left, int key_Right){
		this.key_Up = key_Up;
		this.key_Down = key_Down;
		this.key_Left = key_Left;
		this.key_Right = key_Right;
	}

	/**
	 * Getters comandi di movimento
	 * @return
	 */
	public int getKey_Up() {
		return key_Up;
	}

	public int getKey_Down() {
		return key_Down;
	}

	public int getKey_Left() {
		return key_Left;
	}

	public int getKey_Right() {
		return key_Right;
	}

	public void check(Player player) {
		if (Gdx.input.isKeyPressed(getKey_Left()) && getMovementEnableWest()== true) {
			//movimento verso Ovest
			//Gdx.app.debug(TAG, namePlayer + " moving sx");
			player.velocity.x = -player.terminalVelocity.x;
		}
		if (Gdx.input.isKeyPressed(getKey_Right()) && getMovementEnableEast() == true) {
			//movimento verso Est
			//Gdx.app.debug(TAG, namePlayer + " moving dx");
			player.velocity.x = player.terminalVelocity.x;
		}
		if (Gdx.input.isKeyPressed(getKey_Down()) && getMovementEnableSud() == true) {
			// movimento verso sud
			//Gdx.app.debug(TAG, namePlayer + " moving south");
			player.velocity.y = -player.terminalVelocity.y;
		}
		if (Gdx.input.isKeyPressed(getKey_Up()) && getMovementEnableNord() == true) {
			// movimento verso nord
			//Gdx.app.debug(TAG, namePlayer + " moving north");
			player.velocity.y = player.terminalVelocity.y;
		}
		// Execute auto-forward movement on non-desktop platform
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
			player.velocity.x = player.terminalVelocity.x;
		}
		// Execute auto-forward movement on non-desktop platform
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
			player.velocity.y = player.terminalVelocity.y;
		}
	}
}

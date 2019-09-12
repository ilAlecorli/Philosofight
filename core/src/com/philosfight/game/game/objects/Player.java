package com.philosfight.game.game.objects;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Assets;
import com.philosfight.game.game.Effects.Bullet;

import java.util.ArrayList;


public class Player extends AbstractGameObject {

	//Utile nelle exeptions per mostrare il nome dell'oggetto
	public static final String TAG = Player.class.getName();


	/**
	 * Flags
	 */
	//Flag di movimento
	private boolean movementEnable = false;
	//Flag di sparo
	private boolean shootEnable = false;

	/**
	 * Caratteristiche del giocatore
	 */
	//Nome
	private String namePlayer;
	//Vita del Player
	private float lifePlayer;
	//Mana del Player
	private float mana;
	//Estensione dell'area Melee con la quale il Player attacca fisicamente;
	private float meleeExtension;
	//Caricatore dei proiettili
	//Rimane "public" per l'update nel WorldController
	public ArrayList<Bullet> loader;
	//Massimi bullets al secondo
	public  static final int MAX_BULLETS = 5;


	public void setNamePlayer(String namePlayer) {
		this.namePlayer = namePlayer;
	}

	public String getNamePlayer() {
		return namePlayer;
	}

	public float getLifePlayer() {
		return lifePlayer;
	}

	public void setLifePlayer(float lifePlayer) {
		this.lifePlayer = lifePlayer;
	}

	public float getMana() {
		return mana;
	}

	public void setMana(float mana) {
		this.mana = mana;
	}

	public float getMeleeExtension() {
		return meleeExtension;
	}

	public void setMeleeExtension(float meleeExtension) {
		//La meleeExtension non sarà mai negativa
		if (meleeExtension < 0) meleeExtension = 0;
		this.meleeExtension = meleeExtension;
	}

	public void setLoader(ArrayList<Bullet> loader) {
		this.loader = loader;
	}

	public boolean isShootEnable() {
		return shootEnable;
	}

	public void setShootEnable(boolean shootEnable) {
		this.shootEnable = shootEnable;
	}

	//Costruttore
	public Player() {
		init();
	}

	private void init(){
		dimension.set(0.75f, 0.75f);
		bounds.set(position.x, position.y, dimension.x, dimension.y);

		// Set physics values
		terminalVelocity.set(3.0f, 3.0f);   //3 è un valore medio
		friction.set(12.0f, 12.0f);         //12 è un valore medio
		ObjectAssets = Assets.instance.player.pg;
	}

	public boolean getMovementEnable(){
		return  movementEnable;
	}

	/**
	 * Attiva il movimento del Player
	 * se @param movementEnable True = movimento abilitato
	 * se @param movementEnable False = movimento disabilitato
	 */
	public void setMovementEnable(boolean movementEnable){
		Gdx.app.debug(TAG, "Movement player set: " + movementEnable + " on Player: " + namePlayer);
		this.movementEnable = movementEnable;
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected void updateMotionX (float deltaTime) {
		if (velocity.x != 0) {
			// Apply friction
			if (velocity.x > 0) {
				velocity.x =
						Math.max(velocity.x - friction.x * deltaTime, 0);
			} else {
				velocity.x =
						Math.min(velocity.x + friction.x * deltaTime, 0);
			}
		}
		// Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.x = MathUtils.clamp(velocity.x,
				-terminalVelocity.x, terminalVelocity.x);
	}


	@Override
	protected void updateMotionY (float deltaTime) {
		if (velocity.y != 0) {
			// Apply friction
			if (velocity.y > 0) {
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			} else {
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
			}
		}
		// Apply acceleration
		velocity.y += acceleration.y * deltaTime;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
	}

	public void movementCheck(float deltatime){

		//se il movimento è possibilitato
		if (!movementEnable)
			return;
		// Movimento player attivo sull'asse x
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			//movimento verso Ovest
			//Gdx.app.debug(TAG, namePlayer + " moving sx");
			velocity.x = -terminalVelocity.x;
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			//movimento verso Est
			//Gdx.app.debug(TAG, namePlayer + " moving dx");
			velocity.x = terminalVelocity.x;
		} else {
			// Execute auto-forward movement on non-desktop platform
			if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
				velocity.x = terminalVelocity.x;
			}
		}
		// Movimento player attivo sull'asse Y
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			// movimento verso nord
			//Gdx.app.debug(TAG, namePlayer + " moving south");
			velocity.y = -terminalVelocity.y;
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			// movimento verso sud
			//Gdx.app.debug(TAG, namePlayer + " moving north");
			velocity.y = terminalVelocity.y;
		} else {
			// Execute auto-forward movement on non-desktop platform
			if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
				velocity.y = terminalVelocity.y;
			}
		}
	}

	/**
	 * Sistema di shooting da parte del player verso un obiettivo
	 * @param target Obiettivo a cui mira
	 */
	public void shootAt(AbstractGameObject target){
		//Se ha raggiunto la massima capacità di proiettili o è inabilitato a sparare
		if(loader.size() == MAX_BULLETS || !isShootEnable()) return;
		Bullet b = new Bullet(this.position,target.position);
		//Crea un nuovo proiettile
		loader.add(b);
	}

	@Override
	/**
	 * Renderizzazione del giocatore
	 */
	public void render(SpriteBatch batch) {
		/**
		 * texture rappresenta la texture dell'oggetto e quindi l'immagine da prendere in considerazione
		 * x e y servono a disegnare il rettangolo a determinate coordinate
		 * originX ed originY servono a dichiarare l'origine dell'oggetto, (0,0) implica l'origine nell'angolo in basso a sinistra.
		 * width e height definiscono la dimensione dell'imagine da visualizzare
		 * scaleX e scaleY definiscono la scala del rettangolo intorno all'origine
		 * rotation definisce di quanti gradi ruotare l'immagine
		 * srcX e scrY servono a "tagliare" un rettangolo, dalla texture o dal texture atlas
		 * srcHeight e srcWidth
		 * flipX e flipY specchiano l'immagine sui relativi assi.
		 * */
		if (namePlayer == "Player1")Gdx.app.debug(TAG, namePlayer + " position: " + "(" + position.x + "," + position.y + ")");
		batch.draw(ObjectAssets.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, ObjectAssets.getRegionX(), ObjectAssets.getRegionY(), ObjectAssets.getRegionWidth(), ObjectAssets.getRegionHeight(), flipX, flipY);
	}

}
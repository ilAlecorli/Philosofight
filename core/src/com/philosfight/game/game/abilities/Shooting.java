package com.philosfight.game.game.abilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Effects.Bullet;
import com.philosfight.game.game.objects.AbstractGameObject;
import com.philosfight.game.game.objects.Player;

import java.util.ArrayList;

import static com.philosfight.game.game.objects.Player.COOLDOWNTIME;
import static com.philosfight.game.game.objects.Player.MAX_BULLETS;
import static com.philosfight.game.game.objects.Player.manaMax;


public class Shooting {

	//Flag di sparo
	private boolean shootEnable;
	//Quantità mana per lo shooting
	private float mana;
	//Tempo di ricarica mana
	private float timerMana;
	//Caricatore proiettili
	private ArrayList<Bullet> loader;

	
	/**
	 * Costruttore
	 * @param shootEnable Flag di sparo
	 * @param mana Quantità mana per lo shooting
	 * @param loader Caricatore proiettili
	 */
	public Shooting(boolean shootEnable, float mana,  ArrayList<Bullet> loader) {
		setShootEnable(shootEnable);
		setMana(mana);
		setLoader(loader);
	}
	
	/**
	 * Costruttore
	 * @param shootEnable Flag di sparo
	 * @param mana Quantità mana per lo shooting
	 */
	public Shooting(boolean shootEnable, float mana) {
		setShootEnable(shootEnable);
		setMana(mana);
	}
	
	/**
	 * Sistema di shooting da parte del player verso un obiettivo
	 * @param shooter Colui che spara
	 * @param target Obiettivo a cui mira
	 */
	public void shoot(AbstractGameObject shooter, AbstractGameObject target) {
		//Se ha raggiunto la massima capacità di proiettili in gioco o è inabilitato a sparare termina la funzione
		if (loader.size() == MAX_BULLETS || !isShootEnable()) return;

		//Se il mana è finito termina la funzione
		if(getMana() <= 0) return;

		//Setto il timerMana a zero per iniziare il conteggio dello shooting cooldown
		resetTimerMana();

		//Angolo fra le posizioni dei due player
		float angle;

		//Posizione di partenza del proiettile
		Vector2 startPoint;

		angle = MathUtils.atan2(
				(target.position.y + target.dimension.y / 2) - (shooter.position.y + shooter.dimension.y / 2),
				(target.position.x + target.dimension.x / 2) - (shooter.position.x + shooter.dimension.x / 2)
		);

		startPoint = new Vector2((shooter.position.x + shooter.dimension.x / 2) + ((shooter.dimension.x / 2) * MathUtils.cos(angle)),
				(shooter.position.y + shooter.dimension.y / 2) + ((shooter.dimension.x / 2) * MathUtils.sin(angle)));

		//Diminuzione del mana
		setMana(getMana() - 1);

		//Crea un nuovo proiettile
		Bullet bullet = new Bullet(startPoint, angle);

		//Aggiunzione del proiettile al caricatore
		loader.add(bullet);
	}

	/**
	 * Setter e Getter  mana
	 */
	public void setMana(float mana) {
		//Mana mai negativo
		if (mana <= 0) mana = 0;
		this.mana = mana;
	}
	public float getMana() {
		return mana;
	}


	/**
	 * Setter e Getter timerMana
	 */
	private void resetTimerMana(){
		timerMana = 0;
	}
	private float getTimerMana(){
		return timerMana;
	}

	/**
	 * Setter e Getter shoot Enable
	 */
	public void setShootEnable(boolean shootEnable) {
		this.shootEnable = shootEnable;
	}
	public boolean isShootEnable() {
		return shootEnable;
	}

	/**
	 * Setter e Getter Loader
	 */
	public void setLoader(ArrayList<Bullet> loader) {
		this.loader = loader;
	}
	public ArrayList<Bullet> getLoader() {
		return loader;
	}

	/**
	 * Metodo per incrementare il mana utilizzato
	 * @param deltaTime parametro per il trascorrere del tempo
	 */
	public void updateMana(float deltaTime){
		//Incremento il Timer di ricaricamento del mana
		timerMana += deltaTime;

		//Dopo aver passato un tempo specifico
		// e aver controllato che il mana non sia già al massimo
		if (getTimerMana() > COOLDOWNTIME && getMana() < manaMax) {
			//Incremento del mana alla volta
			setMana(getMana() + 1);
			//Intervallo di incremento senza shooting
			//più è grande la differenza, più ricarica
			//velocemente fra un proiettile e l'altro
			timerMana = COOLDOWNTIME - 0.2f;
		}
	}
}

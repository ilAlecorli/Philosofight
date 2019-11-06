package com.philosfight.game.gameStuff.Effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.gameStuff.Assets;
import com.philosfight.game.gameStuff.objects.AbstractGameObject;


public class Bullet extends AbstractGameObject {

	//Velocità proiettili
	public static final float SPEED_BULLET = 10f;
	//Vita proiettile
	private float lifeTimer = 0;
	//fine vita proiettile
	private float lifeTime = 5f;
	//Target a cui è inviato
	private Vector2 targetPosition;
	//angolo del bullet
	private float angle;
	//Danno del bullet;
	private float damage = 2.50f;

	//flag di rimozione
	private boolean remove = false;

	public Bullet(Vector2 position, float angle) {
		this.position = position;
		this.angle = angle;
		dimension.set(0.1f, 0.1f);
		Asset = Assets.instance.bullet.bullet;
	}

	public Vector2 getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(Vector2 targetPosition) {
		this.targetPosition = targetPosition;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		if (damage < 0) damage = 0;
		this.damage = damage;
	}

	@Override
	protected void updateMotionX(float deltaTime) {
		//Mantiene la traiettoria su cui è posto (no attrito)
		//Traiettoria calcolata in base al target e alla posizione di spawn
		this.position.x += SPEED_BULLET * MathUtils.cos(angle) * deltaTime;
	}


	@Override
	protected void updateMotionY(float deltaTime) {
		//Mantiene la traiettoria su cui è posto (no attrito)
		//Traiettoria calcolata in base al target e alla posizione di spawn
		this.position.y += SPEED_BULLET * MathUtils.sin(angle) * deltaTime;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		//Diminuisce la vita rimasta del proiettile
		lifeTimer += deltaTime;

		//Se è a fine vita deve essere rimosso
		if (lifeTimer > lifeTime) setRemove(true);
	}

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
	@Override
	public void render(SpriteBatch batch) {

		batch.draw(Asset.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, Asset.getRegionX(), Asset.getRegionY(), Asset.getRegionWidth(), Asset.getRegionHeight(), flipX, flipY);
	}

}

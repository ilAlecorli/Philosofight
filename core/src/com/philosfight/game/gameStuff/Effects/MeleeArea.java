package com.philosfight.game.gameStuff.Effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.gameStuff.Assets;
import com.philosfight.game.gameStuff.objects.AbstractGameObject;

public class MeleeArea extends AbstractGameObject {

	Vector2 playerPosition;

	public MeleeArea(Vector2 position, float meleeSize) {
		this.position = position;
		setPlayerPosition(position);
		dimension.set(meleeSize, meleeSize);
		Asset = Assets.instance.circle.melee;
	}

	public void setPlayerPosition(Vector2 playerPosition) {
		this.playerPosition = playerPosition;
	}

	public Vector2 getPlayerPosition() {
		return playerPosition;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected void updateMotionX(float deltaTime) {
		this.position.x += playerPosition.x;
	}

	@Override
	protected void updateMotionY(float deltaTime) {
		this.position.y += playerPosition.y;
	}

	@Override
	public void render(SpriteBatch batch) {

		batch.draw(Asset.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, Asset.getRegionX(), Asset.getRegionY(), Asset.getRegionWidth(), Asset.getRegionHeight(), flipX, flipY);
	}
}

package com.philosfight.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philosfight.game.utils.Constants;
import com.philosfight.game.game.Assets;

public class Wall extends AbstractGameObject {
    private TextureRegion wall;
    private int lenght;

    public Wall() {
        init();
    }

    private void init() {
        dimension.set(4f, 0.5f);
        setLenght(1);
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public void increaseLenght(int amount) {
        setLenght(lenght + amount);
    }

    @Override
    public void render(SpriteBatch batch) {

        // Draw wall
        batch.draw(wall.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, wall.getRegionX(), wall.getRegionY(), wall.getRegionWidth(), wall.getRegionHeight(), false, false);
    }
}

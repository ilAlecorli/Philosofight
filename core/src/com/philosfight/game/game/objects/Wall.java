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
        dimension.set(13f, 1f);
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
        /*
         * srcX e scrY servono a "tagliare" un rettangolo, dalla texture o dal texture atlas
         * x e y servono a disegnare il rettangolo a determinate coordinate
         * originX ed originY servono a dichiarare l'origine di disegno dell'oggetto, (0,0) implica l'origine nell'angolo in basso a sinistra.
         * width e height definiscono la dimensione dell'imagine da visualizzare
         * scaleX e scaleY definiscono la scala del rettangolo intorno all'origine
         * rotation definisce di quanti gradi ruotare l'immagine
         * flipX e flipY specchiano l'immagine sui relativi assi.
         * */
        batch.draw(wall.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, wall.getRegionX(), wall.getRegionY(), wall.getRegionWidth(), wall.getRegionHeight(), false, false);
    }
}

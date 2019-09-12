package com.philosfight.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philosfight.game.utils.Constants;
import com.philosfight.game.game.Assets;

public class Wall extends AbstractGameObject {

    public Wall() {
        init();
    }

    private void init() {
        dimension.set(1f,1f);
        bounds.set(position.x, position.y, dimension.x, dimension.y);
    }

    @Override
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
        batch.draw(ObjectAssets.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, ObjectAssets.getRegionX(), ObjectAssets.getRegionY(), ObjectAssets.getRegionWidth(), ObjectAssets.getRegionHeight(), flipX, flipY);
    }
}

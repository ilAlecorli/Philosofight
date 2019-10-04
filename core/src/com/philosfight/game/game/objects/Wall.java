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
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(
                Asset.getTexture(),          //Asset attuale dell'oggetto
                position.x,                         //Posizione X
                position.y,                         //Posizione Y
                origin.x,                           //Origine dell'oggetto X, default 0
                origin.y,                           //Origine dell'oggetto Y, default 0
                dimension.x,                        //Dimensione dell'oggetto X
                dimension.y,                        //Dimensione dell'oggetto Y
                scale.x,                            //Scala dell'oggetto rispetto all'origine X
                scale.y,                            //Scala dell'oggetto rispetto all'origine y
                rotation,                           //Gradi di rotazione
                Asset.getRegionX(),          //Posizione X da cui ritagliare dalla Texture X
                Asset.getRegionY(),          //Posizione Y da cui ritagliare dalla Texture Y
                Asset.getRegionWidth(),      //Larghezza da ritagliare
                Asset.getRegionHeight(),     //Altezza da ritagliare
                flipX,                              //Specchiare o meno su X
                flipY);                             //Specchiare o meno su Y
    }
}

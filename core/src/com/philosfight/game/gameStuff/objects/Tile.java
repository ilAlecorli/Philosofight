package com.philosfight.game.gameStuff.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philosfight.game.gameStuff.Assets;
public class Tile extends AbstractGameObject{
    private TextureRegion tile;
    public Tile() {
        init();
    }

    private void init() {
        dimension.set(1f,1f);
        Asset = Assets.instance.tile.tile00;
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
                flipY);                             //Specchiare o meno su Y}
    }
}


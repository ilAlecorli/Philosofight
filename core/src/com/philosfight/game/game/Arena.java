package com.philosfight.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.philosfight.game.game.objects.AbstractGameObject;
import com.philosfight.game.game.objects.Tile;
import com.philosfight.game.game.objects.Wall;
import com.philosfight.game.game.Assets;

public class Arena {
    public static final String TAG = Arena.class.getName();

    //Oggetti
    public Array<Wall> walls;
    public Array<Tile> floor;

    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0),     // black
        WALL(255, 216, 0);  //yellow

        private int color;

        private BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            if (this.color == color)
                return true;
            return false;
        }

        public int getColor() {
            return color;
        }
    }



    public Arena(String filename) {
        init(filename);
    }

    private void init(String filename) {
//        Carica l'immagine che rappresenta il livello
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
//      Tag che permette di cambiare la dimensione dei muri a seconda della loro posizione
        this.walls = new Array();
        this.floor = new Array();
        int lastPixel = -1;
//        Scannerizza l'immagine dal pixel in basso a sinistra fino al pixel in alto a destra
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
//                height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;

//                Trova il colore del pxel corrente in RGBA
                int currentPixel = pixmap.getPixel(pixelX, pixelY);

//                find matching color value to identify block type at (x,y)
//                point and create the corresponding game object if there is
//                a match

//                Empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    obj = new Tile();
                    obj.position.set((float)pixelX, (float)pixelY);
                    this.floor.add((Tile)obj);

                }
//                Wall
                else if (BLOCK_TYPE.WALL.sameColor(currentPixel)) {
                    obj = new Wall();
                    obj.position.set((float)pixelX, (float)pixelY);
                    if(pixelX > 0 && pixelX < (pixmap.getWidth() - 1)){
                        obj.ObjectAssets = Assets.instance.wall.nord;
                        obj.dimension.set(1f,0.5f);
                    }
                    else if(pixelY > 0 && pixelY < (pixmap.getHeight() - 1)) {
                        obj.ObjectAssets = Assets.instance.wall.east;
                        obj.dimension.set(0.5f,1f);
                    }
                    else {
                        obj.ObjectAssets = Assets.instance.wall.corner;
                        obj.dimension.set(0.5f,0.5f);
                        if(pixelY == 0 && pixelX == 0){
                            obj.origin.set(0.5f, 0.5f);
                            obj.rotation = 180;
                        }
                        if(pixelY == 0 && pixelX == pixmap.getWidth()){
                           obj.origin.set(0.5f, 0.5f);
                           obj.rotation = -90;
                        }
                    }
                    this.walls.add((Wall)obj);

                }
            }
        }
        //free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "Arena'" + filename + "' loaded");
    }
    public void render(SpriteBatch batch){
        //Disegna il pavimento
        for(Tile tile : floor){
            tile.render(batch);
        }
        //Disegna i muri
        for(Wall wall : walls){
            //Impostazioni di disegno dei muri
            wall.render(batch);
        }
    }
}

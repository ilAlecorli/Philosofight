package com.philosfight.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.philosfight.game.game.objects.AbstractGameObject;
import com.philosfight.game.game.objects.Wall;

public class Arena {
    public static final String TAG = Arena.class.getName();
    //Oggetti
    public Array<Wall> walls;

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
        this.walls = new Array();
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
//                    non fa nulla
                }
//                Wall
                else if (BLOCK_TYPE.WALL.sameColor(currentPixel)) {
                    obj = new Wall();
                    obj.position.set((float)pixelX, (float)pixelY);
                    this.walls.add((Wall)obj);
                }

            }
        }
        //free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "Arena'" + filename + "' loaded");
    }
    public void render(SpriteBatch batch){
        //Disegna i muri
        for(Wall wall : walls){
            //Impostazioni di disegno dei muri
            wall.render(batch);
        }
    }
}

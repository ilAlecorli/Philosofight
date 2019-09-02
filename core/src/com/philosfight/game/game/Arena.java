package com.philosfight.game.game;

import com.philosfight.game.game.WorldController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.philosfight.game.game.objects.AbstractGameObject;
import com.philosfight.game.game.objects.Player;
import com.philosfight.game.game.objects.Tile;
import com.philosfight.game.game.objects.Wall;
import com.philosfight.game.game.Assets;

public class Arena {
    public static final String TAG = Arena.class.getName();
    private int FLAG_P = 0;
    public Pixmap pixmap;

    //Oggetti
    public Array<Wall> walls;
    public Array<Tile> floor;
    public Player player1;
    public Player player2;

    public enum BLOCK_TYPE {
        TILE(160, 160, 160),     //grey
        WALL(255, 216, 0),      //yellow
        SPAWN(255,255,255),     //white
        EMPTY(0,0,0);           //black

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
        pixmap = new Pixmap(Gdx.files.internal(filename));

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

                if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)){

                }

//                Tile
                if (BLOCK_TYPE.TILE.sameColor(currentPixel)) {
                    obj = new Tile();
                    obj.position.set((float)pixelX, (float)pixelY);
                    this.floor.add((Tile)obj);

                }

//               Wall
                else if (BLOCK_TYPE.WALL.sameColor(currentPixel)) {
                    obj = new Wall();
                    obj.position.set((float)pixelX, (float)pixelY);
                    if(pixelX > 1 && pixelX < (pixmap.getWidth() - 2))
                        obj.ObjectAssets = Assets.instance.wall.nord;
                    else if(pixelY > 1 && pixelY < (pixmap.getHeight() - 2))
                        obj.ObjectAssets = Assets.instance.wall.east;
                    else{
                        obj.ObjectAssets = Assets.instance.wall.corner;
//                           obj.rotation = -90;
                        }
                    this.walls.add((Wall)obj);
                }

                else if(BLOCK_TYPE.SPAWN.sameColor(currentPixel)){
                    obj = new Player();
                    obj.position.set(pixelX, pixelY);
                    if(FLAG_P == 0) {
                        player1 = (Player) obj;
                        player1.setNamePlayer("Player1");
                        player1.position.set(2f, 2f);
                        FLAG_P = 1;
                    }
                    else if(FLAG_P == 1){
                        player2 = (Player)obj;
                        player2.setNamePlayer("Player2");
                        player2.position.set(7f, 11f);
                    }
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
        for(Wall wall : walls) {
            //Impostazioni di disegno dei muri
            wall.render(batch);
        }
//            Disegna i giocatori
        player1.render(batch);
        player2.render(batch);
    }
}

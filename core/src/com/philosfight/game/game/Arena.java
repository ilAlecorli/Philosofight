package com.philosfight.game.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Effects.Bullet;
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

import java.util.ArrayList;
import java.util.List;

public class Arena {

    public static final String TAG = Arena.class.getName();
    private int FLAG_SPAWN_PLAYERS = 0;
    public Pixmap pixmap;

    //Oggetti di gioco
    public Array<Wall> walls;
    public Array<Tile> floor;

    public Player player1;
    public Player player2;

    public ArrayList<Bullet> bulletsLoader1;
    public ArrayList<Bullet> bulletsLoader2;
    public List<Bullet> bulletsDump = new ArrayList<Bullet>();

    /**
     *  Classe che crea le costanti legate agli oggetti di gioco tramite i colori
     */
    public enum BLOCK_TYPE {
        TILE(160, 160, 160),    //grey
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


    /**
     *Costruttore della classe
     * @param filename posizione e nome del file che rappresenta l'arena
     */
    public Arena(String filename) {
        init(filename);
    }

    /**
     * Metodo di inizializzazione dell'arena
     * @param filename
     */
    private void init(String filename) {
        //Carica l'immagine che rappresenta il livello
        pixmap = new Pixmap(Gdx.files.internal(filename));

        //Tag che permette di cambiare la dimensione dei muri a seconda della loro posizione
        this.walls = new Array();
        this.floor = new Array();

        //Inizializza anche i caricatori per i proiettili
        this.bulletsLoader1 = new ArrayList<Bullet>();
        this.bulletsLoader2 = new ArrayList<Bullet>();

        int lastPixel = -1;

        //Scannerizza l'immagine dal pixel in basso a sinistra fino al pixel in alto a destra
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;

                //height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;

                //Trova il colore del pxel corrente in RGBA
                int currentPixel = pixmap.getPixel(pixelX, pixelY);


                //Check per determinare l'oggetto nella posizione che si sta trattando tramite il
                // colore sulla pixmap.
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
                    //Impostazioni dei muri
                    obj.position.set((float) pixelX, (float) pixelY);
                    obj.bounds.setPosition(obj.position.x, obj.position.y);
                    //Cambiamento degli Asset a seconda della posizione
                    if (pixelX > 1 && pixelX < (pixmap.getWidth() - 2))
                        obj.ObjectAssets = Assets.instance.wall.nord;
                    else if(pixelY > 1 && pixelY < (pixmap.getHeight() - 2))
                        obj.ObjectAssets = Assets.instance.wall.east;
                    else{
                        obj.ObjectAssets = Assets.instance.wall.corner;
                    }
                    this.walls.add((Wall)obj);
                }
                else if(BLOCK_TYPE.SPAWN.sameColor(currentPixel)){
                    obj = new Player();
                    obj.position.set(pixelX, pixelY);
                    if(FLAG_SPAWN_PLAYERS == 0) {
                        player1 = (Player) obj;
                        player1.setNamePlayer("Player1");
                        player1.position.set(2f, 2f);
                        //Dai al player il suo caricatore inizializzato
                        player1.setLoader(bulletsLoader1);
                        //Setta il flag per dire che il player1 è già stato inserito
                        FLAG_SPAWN_PLAYERS = 1;
                    }
                    //Se il player1 è già stato inserito
                    else if(FLAG_SPAWN_PLAYERS == 1){
                        player2 = (Player)obj;
                        player2.setNamePlayer("Player2");
                        player2.position.set(7f, 11f);
                        //Dai al player il suo caricatore inizializzato
                        player2.setLoader(bulletsLoader2);
                    }
                }
            }
        }
        //free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "Arena'" + filename + "' loaded");
    }

    /**
     * Metodo sperimentale per la creazione procedurale dell'arena
     */
    public void generateProceduralWall(){
        Wall wall;
        double ran;
        for(int i = 0; i < 5; i++){
            wall =new Wall();
            /* range 0.5 -> 2.0*/
            ran = getRandomFloat(0.5f, 2.0f);
            wall.dimension.set((float)ran, (float)ran);
            wall.position.set(getRandomFloat(3f, 7f), getRandomFloat(3f ,12f));
        }
    }
    float getRandomFloat(float min, float max) {
        return (float)(Math.random() * (max - min) + min);
    }

    /**
     * Metodo per la renderizzazione degli oggetti di gioco
     * @param batch
     */
    public void render(SpriteBatch batch){
        //Disegna il pavimento
        for(Tile tile : floor){
            tile.render(batch);
        }
        //Disegna i muri
        for(Wall wall : walls) {
            wall.render(batch);
        }
        //Disegna i giocatori
        player1.render(batch);
        player2.render(batch);
        //Disegna tutti i proiettili
        for(Bullet bullet : player1.loader){
            bullet.render(batch);
        }
        for(Bullet bullet:  player2.loader){
            bullet.render(batch);
        }
    }

    /**
     * Metodo per il controllo delle collisioni (Richiamato in: WorldController.update())
     */
    public void checkCollisions(float deltaTime) {
        player1.bounds.setPosition(player1.position.x, player1.position.y);
        player2.bounds.setPosition(player2.position.x, player2.position.y);

        for(Wall wall : walls) {
            if (player1.bounds.overlaps(wall.bounds)) {
                onCollisionPlayerWithWall(player1, wall);
            }
            if(player2.bounds.overlaps(wall.bounds)){
                onCollisionPlayerWithWall(player2, wall);
            }
        }

        for(Bullet bullet : player1.loader){
            bullet.bounds.setPosition(bullet.position.x, bullet.position.y);
            if (bullet.bounds.overlaps(player2.bounds)){
                Gdx.app.debug(TAG, player2.getNamePlayer() + " hit");
                onCollisionBulletWithObject(bullet);
            }for(Wall wall : walls){
                if(bullet.bounds.overlaps(wall.bounds)){
                    onCollisionBulletWithObject(bullet);
                }
            }
        }

        for(Bullet bullet : player2.loader){
            bullet.bounds.setPosition(bullet.position.x, bullet.position.y);
            if (bullet.bounds.overlaps(player1.bounds)) {
                Gdx.app.debug(TAG, player1.getNamePlayer() + " hit");
                onCollisionBulletWithObject(bullet);
            }
            for(Wall wall : walls){
                if(bullet.bounds.overlaps(wall.bounds)){
                    onCollisionBulletWithObject(bullet);
                }
            }
        }

        if(player1.bounds.overlaps(player2.bounds)){
           onCollsionPlayerWithPlayer(deltaTime);
        }
    }


    /**
     * Metodo per le collisioni dei giocatori con i muri
     * @param player
     * @param wall
     */
    private void onCollisionPlayerWithWall(Player player, Wall wall) {
        if(wall.position.y == 1) {
            player.position.y = wall.position.y + wall.bounds.height;
        }
        else if(wall.position.y == (pixmap.getHeight() - 2)) {
            player.position.y = wall.position.y - player.bounds.height;
        }
        else if(wall.position.x == 1){
            player.position.x = wall.position.x + wall.bounds.width;
        }
        else if(wall.position.x == (pixmap.getWidth() - 2)){
            player.position.x = wall.position.x - player.bounds.width;
        }
    }

    /**
     * Metodo per le collisioni dei proiettili con i giocatori
     * @param bullet
     */
    private void onCollisionBulletWithObject(Bullet bullet){
        bulletsDump.add(bullet);
    }
    private void onCollsionPlayerWithPlayer(float deltaTime){
        /*Distanza fra i due giocatori*/
        Vector2 distance = new Vector2( (player2.position.y + player2.dimension.y / 2) - (player1.position.y + player1.dimension.y / 2),
                                        (player2.position.x + player2.dimension.x / 2) - (player1.position.x + player1.dimension.x / 2));

        /* Angolo fra le posizioni dei due giocatori*/
        float angle = MathUtils.atan2(distance.y, distance.x);
        float radius = (float)Math.sqrt(Math.pow(player1.dimension.x / 2, 2) + Math.pow(player2.dimension.x / 2, 2));


        if(player1.velocity.x != 0) {
            player1.velocity.x = 0;
            player1.position.x = (player1.position.x) + radius * MathUtils.cos(angle);
        }
        if(player1.velocity.y != 0) {
            player1.velocity.y = 0;
            player1.position.y = (player1.position.y) + radius * MathUtils.sin(angle);
        }

        if(player2.velocity.x != 0) {
            player2.velocity.x = 0;
            player2.position.x = (player2.position.x) + radius * MathUtils.cos(angle);
        }
        if(player2.velocity.y != 0) {
            player2.velocity.y = 0;
            player2.position.y = (player2.position.y) + radius * MathUtils.sin(angle);
        }

    }

}

package com.philosfight.game.gameStuff;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.gameStuff.Effects.Bullet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.philosfight.game.gameStuff.objects.AbstractGameObject;
import com.philosfight.game.gameStuff.objects.Player;
import com.philosfight.game.gameStuff.objects.Tile;
import com.philosfight.game.gameStuff.objects.Wall;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    public static final String TAG = Arena.class.getName();

    //Immagine scheletro dell'arena
    public Pixmap pixmap;

    //Oggetti di gioco
    public Array<Wall> walls;
    public Array<Tile> floor;

    //Players
    public Player player1;
    public Player player2;

    //caricatori bullets
    public ArrayList<Bullet> bulletsLoader1;
    public ArrayList<Bullet> bulletsLoader2;
    public List<Bullet> bulletsDump = new ArrayList<Bullet>();


    /**
     * Classe che crea le costanti legate agli oggetti di gioco tramite i colori
     */
    public enum BLOCK_TYPE {
        TILE(160, 160, 160),            //Grey
        WALL(255, 216, 0),              //Yellow
        CENTER_WALL_1(76, 255, 0),      //Green
        CENTER_WALL_2(255, 0, 21),      //Red
        EMPTY(0, 0, 0);                 //Black
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
     * Costruttore della classe
     *
     * @param filename posizione e nome del file che rappresenta l'arena
     */
    public Arena(String filename) {
        init(filename);
    }

    /**
     * Metodo di inizializzazione dell'arena
     *
     * @param filename
     */
    private void init(String filename) {
        //Carica l'immagine che rappresenta il livello
        pixmap = new Pixmap(Gdx.files.internal(filename));

        //Tag che permette di cambiare la dimensione dei muri a seconda della loro posizione
        walls = new Array();
        floor = new Array();

        //Inizializza anche i caricatori per i proiettili
        bulletsLoader1 = new ArrayList<Bullet>();
        bulletsLoader2 = new ArrayList<Bullet>();

        int lastPixel = -1;

        //Scannerizza l'immagine dal pixel in basso a sinistra fino al pixel in alto a destra
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;

                //height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;

                //Trova il colore del pixel corrente in RGBA
                int currentPixel = pixmap.getPixel(pixelX, pixelY);


                //Riconoscimento del colore e assegnazione del oggetto
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    //Non succede nulla
                }

                //Tile
                if (BLOCK_TYPE.TILE.sameColor(currentPixel)) {
                    obj = new Tile();
                    obj.position.set((float) pixelX, (float) pixelY);
                    this.floor.add((Tile) obj);

                }

                //Wall
                else if (BLOCK_TYPE.WALL.sameColor(currentPixel)) {
                    obj = new Wall();

                    //Impostazioni dei muri
                    obj.position.set((float) pixelX, (float) pixelY);
                    obj.bounds.set(obj.position.x, obj.position.y, obj.dimension.x, obj.dimension.y);

                    //Cambiamento degli Asset a seconda della posizione
                    //Muri laterali nord
                    if (pixelY == pixmap.getHeight() - 1 && pixelX != 0 && pixelX != pixmap.getWidth() - 1) {
                        obj.Asset = Assets.instance.wall.front;
                        obj.bounds.set(obj.position.x, obj.position.y + (obj.dimension.y / 2), obj.dimension.x, obj.dimension.y / 2);
                    }
                    //Muri laterali est
                    else if (pixelY > 0 && pixelX == 0) {
                        obj.Asset = Assets.instance.wall.side;
                        obj.origin.set(obj.dimension.x / 2, obj.dimension.y / 2);
                        obj.rotation = -90;
                    }
                    //Muri laterali ovest
                    else if (pixelY > 0 && pixelX == pixmap.getWidth() - 1) {
                        obj.Asset = Assets.instance.wall.side;
                        obj.origin.set(obj.dimension.x / 2, obj.dimension.y / 2);
                        obj.rotation = 90;
                    }
                    //Muri laterali sud
                    else if (pixelY == 0) {
                        obj.Asset = Assets.instance.wall.back;
                    }
                    this.walls.add((Wall) obj);
                }

                else if(BLOCK_TYPE.CENTER_WALL_1.sameColor(currentPixel)){
                    obj = new Wall();

                    //Impostazoioni muri
                    obj.position.set((float) pixelX, (float) pixelY);
                    obj.bounds.set(obj.position.x, obj.position.y, obj.dimension.x, obj.dimension.y);

                    obj.Asset = Assets.instance.wall.center_front;

                    this.walls.add((Wall) obj);

                }
                else if(BLOCK_TYPE.CENTER_WALL_2.sameColor(currentPixel)){
                    obj = new Wall();

                    //Impostazoioni muri
                    obj.position.set((float) pixelX, (float) pixelY);
                    obj.bounds.set(obj.position.x, obj.position.y, obj.dimension.x, obj.dimension.y);

                    obj.Asset = Assets.instance.wall.center_back;

                    this.walls.add((Wall) obj);
                }
            }
        }


        //Impostazioni del Player 1
        player1 = new Player();
        player1.setNamePlayer("Friederich N.");
        player1.setAnimations(
                Assets.instance.player.PG1_walk_up,         //Camminata su
                Assets.instance.player.PG1_walk_down,       //Camminata giù
                Assets.instance.player.PG1_walk_left,       //Camminata a sinistra
                Assets.instance.player.PG1_walk_right,      //Camminata a destra

                Assets.instance.player.PG1_standby_up,      //Attesa mentre si cammina in alto
                Assets.instance.player.PG1_standby_down,    //Attesa mentre si cammina in basso
                Assets.instance.player.PG1_standby_left,    //Attesa mentre si cammina a sinistra
                Assets.instance.player.PG1_standby_right,    //Attesa mentre si cammina a destra
                Assets.instance.player.PG1_fainted
        );
        player1.setDirection(Player.dir.up);
        player1.position.set(1.5f, 1.5f);
        //Fissa il punto di Spawn del Player
        player1.setSpawnPointPlayer(player1.position);
        //Dai al player il suo caricatore inizializzato
        player1.shooting.setLoader(bulletsLoader1);

        //Impostazioni del Player 2
        player2 = new Player();
        player2.setNamePlayer("Fëdor D.");
        player2.setAnimations(
                Assets.instance.player.PG2_walk_up,         //Camminata su
                Assets.instance.player.PG2_walk_down,       //Camminata giù
                Assets.instance.player.PG2_walk_left,       //Camminata a sinistra
                Assets.instance.player.PG2_walk_right,      //Camminata a destra

                Assets.instance.player.PG2_standby_up,      //Attesa mentre si cammina in alto
                Assets.instance.player.PG2_standby_down,    //Attesa mentre si cammina in basso
                Assets.instance.player.PG2_standby_left,    //Attesa mentre si cammina a sinistra
                Assets.instance.player.PG2_standby_right,    //Attesa mentre si cammina a destra
                Assets.instance.player.PG2_fainted
        );
        player2.setDirection(Player.dir.down);
        player2.position.set(10f, 17f);
        //Fissa il punto di Spawn del Player
        player2.setSpawnPointPlayer(player2.position);
        //Dai al player il suo caricatore inizializzato
        player2.shooting.setLoader(bulletsLoader2);


        //free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "Arena'" + filename + "' loaded");
    }

    /**
     * Metodi sperimentale per la creazione procedurale dell'arena
     */
    public void generateProceduralWall() {
        Wall wall;
        double ran;
        for (int i = 0; i < 5; i++) {
            wall = new Wall();
            /* range 0.5 -> 2.0*/
            ran = getRandomFloat(0.5f, 2.0f);
            wall.dimension.set((float) ran, (float) ran);
            wall.position.set(getRandomFloat(3f, 7f), getRandomFloat(3f, 12f));
        }
    }

    float getRandomFloat(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    /**
     * Metodo per la renderizzazione degli oggetti di gioco
     *
     * @param batch
     */
    public void render(SpriteBatch batch) {
        //Disegna il pavimento
        for (Tile tile : floor) {
            tile.render(batch);
        }
        //Disegna i muri
        for (Wall wall : walls) {
            wall.render(batch);
        }
        //Disegna i giocatori
        player1.render(batch);
        player2.render(batch);

        //Disegna tutti i proiettili
        for (Bullet bullet : player1.shooting.getLoader()) {
            bullet.render(batch);
        }
        for (Bullet bullet : player2.shooting.getLoader()) {
            bullet.render(batch);
        }

    }

    /**
     * Metodo per il controllo delle collisioni (Richiamato in: WorldController.update())
     */
    public void checkCollisions(float deltaTime) {
        player1.movement.setMovementEnable(true);
        player2.movement.setMovementEnable(true);
        player1.bounds.setPosition(player1.position.x, player1.position.y);
        player2.bounds.setPosition(player2.position.x, player2.position.y);

        for (Wall wall : walls) {
            if (player1.bounds.overlaps(wall.bounds)) {
                onCollisionPlayerWithWall(player1, wall);
            }
            if (player2.bounds.overlaps(wall.bounds)) {
                onCollisionPlayerWithWall(player2, wall);
            }
        }

        //if(Intersector.intersectPolygons(){}
        if (player1.bounds.overlaps(player2.bounds)) {
            onCollsionPlayerWithPlayer();
        }

        //Interazioni Bullet del 1° player
        for (Bullet bullet : player1.shooting.getLoader()) {
            bullet.bounds.setPosition(bullet.position.x, bullet.position.y);
            if (bullet.bounds.overlaps(player2.bounds)) {
                Gdx.app.debug(TAG, player2.getNamePlayer() + " hit");

                //Controlla i danni fatti al player
                if (player2.isAlive())
                    player2.takeDamage(bullet);
                onCollisionBulletWithObject(bullet);
            }
            for (Wall wall : walls) {
                if (bullet.bounds.overlaps(wall.bounds)) {
                    onCollisionBulletWithObject(bullet);
                }
            }
        }

        //Interazioni Bullet del 2° player
        for (Bullet bullet : player2.shooting.getLoader()) {
            bullet.bounds.setPosition(bullet.position.x, bullet.position.y);
            if (bullet.bounds.overlaps(player1.bounds)) {
                Gdx.app.debug(TAG, player1.getNamePlayer() + " hit");
                //Controlla i danni fatti al player
                if (player1.isAlive())
                    player1.takeDamage(bullet);
                onCollisionBulletWithObject(bullet);
            }
            for (Wall wall : walls) {
                if (bullet.bounds.overlaps(wall.bounds)) {
                    onCollisionBulletWithObject(bullet);
                }
            }
        }
    }


    /**
     * Metodo per le collisioni dei giocatori con i muri
     *
     * @param player
     * @param wall
     */
    private void onCollisionPlayerWithWall(Player player, Wall wall) {

        //Muri Sud
        if (wall.position.y == 0) {
            player.movement.setMovementEnableSud(false);
            player.position.y = wall.position.y + wall.bounds.height;
        }

        // Muri Nord
        else if (wall.position.y == (pixmap.getHeight() - 1)) {
            player.movement.setMovementEnableNord(false);
            player.position.y = wall.position.y + (wall.dimension.y / 2) - player.bounds.height;
        }
        //Muri est
        else if (wall.position.x == 0) {
            player.movement.setMovementEnableEast(false);
            player.position.x = wall.position.x + wall.bounds.width;
        }
        //Muri ovest
        else if (wall.position.x == (pixmap.getWidth() - 1)) {
            player.movement.setMovementEnableOvest(false);
            player.position.x = wall.position.x - player.bounds.width;

            //Muri centrali
        } else {
            float pushing = 0.05f;
            //Player a Sud del muro
            if (    player.bounds.y + player.bounds.height > wall.bounds.y &&
                    player.bounds.y + player.bounds.height < wall.bounds.y + wall.bounds.height / 2) {
                player.velocity.y = 0;
                player.movement.setMovementEnableNord(false);
//                player.position.y = wall.bounds.y - player.bounds.height;
                player.position.y = player.position.y - pushing;
            }

            //Player a Nord del muro
            if (    player.bounds.y < wall.bounds.y + wall.bounds.height &&
                    player.bounds.y > wall.bounds.y + wall.bounds.height / 2) {
                player.velocity.y = 0;
                player.movement.setMovementEnableSud(false);
//                player.position.y = wall.bounds.y + wall.bounds.height;
                player.position.y = player.position.y + pushing;

            }

            //Player a Ovest del muro
            if (player.bounds.x + player.bounds.width > wall.bounds.x &&
                    player.bounds.x + player.bounds.width < wall.bounds.x + wall.bounds.width / 2) {
                player.velocity.x = 0;
                player.movement.setMovementEnableEast(false);
//                player.position.x = wall.bounds.x - player.bounds.width;
                player.position.x = player.position.x - pushing;

            }

            //Player a Est del muro
            if (player.bounds.x < wall.bounds.x + wall.bounds.width &&
                    player.bounds.x > wall.bounds.x + wall.bounds.width / 2) {
                player.velocity.x = 0;
                player.movement.setMovementEnableOvest(false);
//                player.position.x = wall.bounds.x + wall.bounds.width;
                player.position.x = player.position.x + pushing;

            }
        }
    }

    /**
     * Metodo per le collisioni dei proiettili con gli oggetti
     *
     * @param bullet
     */
    private void onCollisionBulletWithObject(Bullet bullet) {
        bulletsDump.add(bullet);
    }


    private void onCollsionPlayerWithPlayer() {
        /*Distanza fra i due giocatori*/
        Vector2 distance = new Vector2((player2.position.x + player2.dimension.x / 2) - (player1.position.x + player1.dimension.x / 2),
                (player2.position.y + player2.dimension.y / 2) - (player1.position.y + player1.dimension.y / 2)
        );

        /* Angolo fra le posizioni dei due giocatori*/
        float angle = MathUtils.atan2(distance.y, distance.x);

        if (angle > (-Math.PI * (0.25)) && angle < (Math.PI * (0.25))) {
            player1.velocity.x = 0;
            player2.velocity.x = 0;
            player1.movement.setMovementEnableEast(false);
            player2.movement.setMovementEnableOvest(false);
        } else if (angle > (Math.PI * (0.25)) && angle < (Math.PI * (0.75))) {
            player1.velocity.y = 0;
            player2.velocity.y = 0;
            player1.movement.setMovementEnableNord(false);
            player2.movement.setMovementEnableSud(false);
        } else if (angle < (-Math.PI * (0.25)) && angle > (-Math.PI * (0.75))) {
            player1.velocity.y = 0;
            player2.velocity.y = 0;
            player1.movement.setMovementEnableSud(false);
            player2.movement.setMovementEnableNord(false);
        } else if (Math.abs(angle) > (Math.PI * (0.75)) && Math.abs(angle) < (Math.PI)) {
            player1.velocity.x = 0;
            player2.velocity.x = 0;
            player1.movement.setMovementEnableOvest(false);
            player2.movement.setMovementEnableEast(false);
        }
    }
}

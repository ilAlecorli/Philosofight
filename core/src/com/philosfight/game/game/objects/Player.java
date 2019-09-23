package com.philosfight.game.game.objects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Assets;
import com.philosfight.game.game.Effects.Bullet;
import com.philosfight.game.game.Effects.MeleeArea;
//import com.philosfight.game.game.Effects.MeleeArea;

import java.util.ArrayList;
import java.util.Timer;


public class Player extends AbstractGameObject {
    //Utile nelle exeptions per mostrare il nome dell'oggetto
    public static final String TAG = Player.class.getName();
    //Timer per Mana
    private Timer timer;

    /**
     * Flags
     */
    //Flag di movimento
    private boolean movementEnableOvest = false;
    private boolean movementEnableEast = false;
    private boolean movementEnableNord = false;
    private boolean movementEnableSud = false;
    //Flag di sparo
    private boolean shootEnable = true;
    //Flag della vita
    private boolean alive = true;

    /**
     * Comandi del giocatore
     */
    private int key_Up;
    private int key_Down;
    private int key_Left;
    private int key_Right;
    public int key_Shoot;


    /**
     * Caratteristiche del giocatore
     */
    //Nome
    private String namePlayer;
    //Vita del Player
    private float healthPlayer;
    //Mana del Player
    private float mana;
    //Area Melee con la quale il Player attacca fisicamente
    private MeleeArea meleeArea;

    //Estensione dell'area Melee
    private float meleeValue;
    //Caricatore dei proiettili, rimane "public" per l'update nel WorldController
    public ArrayList<Bullet> loader;
    //Massimi bullets al secondo
    public static final int MAX_BULLETS = 15;
    //Raggio del melee
    public Circle rangeMelee;

    /**
     * Costruttore
     */
    public Player() {
        init();
    }

    private void init() {
        dimension.set(0.55f, 1f);
        rangeMelee = new Circle(new Vector2(position.x + dimension.x / 2, position.y + dimension.y / 2), dimension.x * (3 / 2));
        bounds.set(position.x, position.y, dimension.x, dimension.y * (float)0.75);
        origin.set(dimension.x / 2, dimension.y / 2);
        timer = new Timer();

        // Set physics values
        terminalVelocity.set(3.0f, 3.0f);   //3 è un valore medio
        friction.set(12.0f, 12.0f);         //12 è un valore medio

        //Player charatteristics
        setHealthPlayer(70);
        setMana(10);
        setMeleeValue(2);
        meleeArea = new MeleeArea(this.position,getMeleeValue());
    }

    /**
     * Set nome player
     * @param namePlayer
     */
    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    /**
     * Get nome player
     *
     * @return nome Player
     */
    public String getNamePlayer() {
        return namePlayer;
    }

    public void setHealthPlayer(float healthPlayer) {
        //La vita non può andare sotto zero
        if (healthPlayer <= 0) {
            if (isAlive())
                setAlive(false);
            healthPlayer = 0;
        }
        this.healthPlayer = healthPlayer;
    }

    public float getHealthPlayer() {
        return healthPlayer;
    }

    public void setMana(float mana) {
        if (mana <= 0) {mana = 0;
            Gdx.app.debug(TAG, getNamePlayer() + " ha finito il mana");
        }
        this.mana = mana;
    }

    public float getMana() {
        return mana;
    }

    public void updateMana(float deltaTime){

        setMana((getMana() + 1));
    }

    public float getMeleeValue() {
        return meleeValue;
    }

    public void setMeleeValue(float meleeValue) {
        this.meleeValue = meleeValue;
    }

    public void setLoader(ArrayList<Bullet> loader) {
        this.loader = loader;
    }


    public void setShootEnable(boolean shootEnable) {
        this.shootEnable = shootEnable;
    }

    public boolean isShootEnable() {
        return shootEnable;
    }


    /**
     * Assegnamento dei comandi del giocatore
     */
    public void setControls(int key_Up, int key_Down, int key_Left, int key_Right, int key_Shoot){
        this.key_Up = key_Up;
        this.key_Down = key_Down;
        this.key_Left = key_Left;
        this.key_Right = key_Right;
        this.key_Shoot = key_Shoot;
    }

    /**
     * Metodi di set/get per la gestione dei flag di movimento1
     * */
    public void setMovementEnable(boolean state) {
        //Gdx.app.debug(TAG, "Movement player set: " + movementEnableEast + " on Player: " + namePlayer);

        this.movementEnableEast = state;
        this.movementEnableOvest = state;
        this.movementEnableNord = state;
        this.movementEnableSud = state;
    }

    public void setMovementEnableOvest(boolean movementEnableOvest) {
        this.movementEnableOvest = movementEnableOvest;
    }
    public void setMovementEnableEast(boolean movementEnableEast) {
        this.movementEnableEast = movementEnableEast;
    }
    public void setMovementEnableNord(boolean movementEnableNord) {
        this.movementEnableNord = movementEnableNord;
    }
    public void setMovementEnableSud(boolean movementEnableSud) {
        this.movementEnableSud = movementEnableSud;
    }

    public boolean getMovementEnableEast() {
        return movementEnableEast;
    }
    public boolean getMovementEnableOvest() {
        return movementEnableOvest;
    }
    public boolean getMovementEnableNord() {
        return movementEnableNord;
    }
    public boolean getMovementEnableSud() {
        return movementEnableSud;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        if (!alive)
            Gdx.app.debug(TAG, getNamePlayer() + " is dead.");
        this.alive = alive;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //updateMana(deltaTime);
        //meleeArea.setPlayerPosition(position);
        //meleeArea.update(deltaTime);
    }

    @Override
    protected void updateMotionX(float deltaTime) {
        if (velocity.x != 0) {
            // Apply friction
            if (velocity.x > 0) {
                velocity.x =
                        Math.max(velocity.x - friction.x * deltaTime, 0);
            } else {
                velocity.x =
                        Math.min(velocity.x + friction.x * deltaTime, 0);
            }
        }
        // Apply acceleration
        velocity.x += acceleration.x * deltaTime;
        // Make sure the object's velocity does not exceed the
        // positive or negative terminal velocity
        velocity.x = MathUtils.clamp(velocity.x,
                -terminalVelocity.x, terminalVelocity.x);
    }

    @Override
    protected void updateMotionY(float deltaTime) {
        if (velocity.y != 0) {
            // Apply friction
            if (velocity.y > 0) {
                velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
            } else {
                velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
            }
        }
        // Apply acceleration
        velocity.y += acceleration.y * deltaTime;
        // Make sure the object's velocity does not exceed the
        // positive or negative terminal velocity
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
    }

    public void movementCheck(float deltaTime) {
        if (Gdx.input.isKeyPressed(key_Left) && movementEnableOvest == true) {
            //movimento verso Ovest
            //Gdx.app.debug(TAG, namePlayer + " moving sx");
            velocity.x = -terminalVelocity.x;
        }
        if (Gdx.input.isKeyPressed(key_Right) && movementEnableEast == true) {
            //movimento verso Est
            //Gdx.app.debug(TAG, namePlayer + " moving dx");
            velocity.x = terminalVelocity.x;
        }
        if (Gdx.input.isKeyPressed(key_Down) && movementEnableSud == true) {
            // movimento verso nord
            //Gdx.app.debug(TAG, namePlayer + " moving south");
            velocity.y = -terminalVelocity.y;
        }
        if (Gdx.input.isKeyPressed(key_Up) && movementEnableNord == true) {
            // movimento verso sud
            //Gdx.app.debug(TAG, namePlayer + " moving north");
            velocity.y = terminalVelocity.y;
        }
        // Execute auto-forward movement on non-desktop platform
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            velocity.x = terminalVelocity.x;
        }
        // Execute auto-forward movement on non-desktop platform
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            velocity.y = terminalVelocity.y;
        }
    }


    /**
     * Sistema di shooting da parte del player verso un obiettivo
     *
     * @param target Obiettivo a cui mira
     */
    public void shootAt(AbstractGameObject target) {
        //Se ha raggiunto la massima capacità di proiettili in gioco o è inabilitato a sparare termina la funzione
        if (loader.size() == MAX_BULLETS || !isShootEnable()) return;

        //Se il mana è finito termina la funzione
        if(getMana() <= 0) return;

        //Angolo fra le posizioni dei due player
        float angle;

        //Posizione di partenza del proiettile
        Vector2 startPoint;

        angle = MathUtils.atan2(
                (target.position.y + target.dimension.y / 2) - (position.y + dimension.y / 2),
                (target.position.x + target.dimension.x / 2) - (position.x + dimension.x / 2)
        );

        startPoint = new Vector2((position.x + dimension.x / 2) + ((dimension.x / 2) * MathUtils.cos(angle)),
                (position.y + dimension.y / 2) + ((dimension.x / 2) * MathUtils.sin(angle)));

        //Diminuzione del mana
        setMana(getMana() - 1);

        //Crea un nuovo proiettile
        Bullet bullet = new Bullet(startPoint, angle);

        //Aggiunzione del proiettile al caricatore
        loader.add(bullet);
    }

    /**
     * Funzione che calcola il danno che il player subisce
     * @param bullet Proiettile da cui estrarre il danno
     */
    public void takeDamage(Bullet bullet){
        setHealthPlayer(getHealthPlayer() - bullet.getDamage());
        Gdx.app.debug(TAG, getNamePlayer() + " has taken " + bullet.getDamage() + " damage point.");
        Gdx.app.debug(TAG, getNamePlayer() + " health is " + getHealthPlayer() + ".");
    }


    @Override
    /**
     * Renderizzazione del giocatore
     */
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

        //if (movementEnable)Gdx.app.debug(TAG, namePlayer + " position: " + "(" + position.x + "," + position.y + ")");
        batch.draw(ObjectAssets.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, ObjectAssets.getRegionX(), ObjectAssets.getRegionY(), ObjectAssets.getRegionWidth(), ObjectAssets.getRegionHeight(), flipX, flipY);
    }

}
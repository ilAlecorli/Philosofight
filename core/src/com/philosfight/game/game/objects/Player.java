package com.philosfight.game.game.objects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Assets;
import com.philosfight.game.game.Effects.Bullet;
import com.philosfight.game.game.Effects.MeleeArea;
import com.philosfight.game.game.abilities.MovementPlayer;
//import com.philosfight.game.game.Effects.MeleeArea;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Player extends AbstractGameObject {
    //Utile nelle exeptions per mostrare il nome dell'oggetto
    public static final String TAG = Player.class.getName();

    /**
     * Animations
     */
    private Animation walk_up;
    private Animation walk_down;
    private Animation walk_left;
    private Animation walk_right;

    /**
     * Flags
     */
    //Flag di sparo
    private boolean shootEnable = true;
    //Flag della vita
    private boolean alive = true;

    /**
     * Comandi del giocatore
     */
    //Movimento Giocatore
    public MovementPlayer movement;
    //Sparo giocatore
    private int key_Shoot;


    /**
     * Caratteristiche del giocatore
     */
    //Nome Giocatore
    private String namePlayer;
    //Quantità vita
    private float healthPlayer;
    //Quantità mana per lo shooting
    private float mana;
    //Tempo di ricarica mana
    private float timerMana;
    //Area di interazione Melee
    private MeleeArea meleeArea;
    //Roba da decidere se utile
    public Circle rangeMelee;
    private float meleeValue;
    //Caricatore proiettili
    public ArrayList<Bullet> loader;
    //Punto di spawn
    private Vector2 spawnPointPlayer;

    /**
     * Costanti Player
     */
    //Massimo di bullet correnti in animazione
    public static final int MAX_BULLETS = 15;
    //Tempo di cooldown per la ricarica del mana
    private static final float cooldownTime = 0.6f;
    //Setta la salute iniziale
    public static final float healthMax = 50;
    //Setta mana iniziale
    private static final float manaMax = 12;

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
        spawnPointPlayer = new Vector2();

        // Set physics values
        terminalVelocity.set(3.0f, 3.0f);   //3 è un valore medio
        friction.set(12.0f, 12.0f);         //12 è un valore medio
        movement = new MovementPlayer();

        //Player charatteristics
        setHealthPlayer(healthMax);
        setMana(manaMax);
        setMeleeValue(2);
        //meleeArea = new MeleeArea(this.position,getMeleeValue());
    }

    /**
     * Assegnamento dei comandi del giocatore
     * @param key_Up    Movimento positivo asse y
     * @param key_Down  Movimento negativo asse y
     * @param key_Left  Movimento negativo asse x
     * @param key_Right Movimento positivo asse x
     * @param key_Shoot Comando di sparo
     */
    public void setControls(int key_Up, int key_Down, int key_Left, int key_Right, int key_Shoot){
        this.movement.setMoveControls(key_Up, key_Down, key_Left, key_Right);
        this.key_Shoot = key_Shoot;
    }

    public int getKey_Shoot() {
        return key_Shoot;
    }

    /**
     * Setters delle animazioni
     */
    public void setAnimations(Animation walk_up, Animation walk_down, Animation walk_left, Animation walk_right){
        this.walk_up = walk_up;
        this.walk_down = walk_down;
        this.walk_left = walk_left;
        this.walk_right = walk_right;
    }

    /**
     *Getters delle animazioni
     */
    public Animation getWalk_up() {
        return walk_up;
    }
    public Animation getWalk_down() {
        return walk_down;
    }
    public Animation getWalk_left() {
        return walk_left;
    }
    public Animation getWalk_right() {
        return walk_right;
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
            return;
        }
        this.healthPlayer = healthPlayer;
    }

    public float getHealthPlayer() {
        return healthPlayer;
    }

    public void setMana(float mana) {
        //Mana mai negativo
        if (mana <= 0) mana = 0;
        this.mana = mana;
    }

    public float getMana() {
        return mana;
    }

    /**
     * Metodo per incrementare il mana utilizzato
     * @param deltaTime parametro per il trascorrere del tempo
     */
    public void updateMana(float deltaTime){
        //Incremento il Timer di ricaricamento del mana
        timerMana += deltaTime;

        //Dopo aver passato un tempo specifico
        // e aver controllato che il mana non sia già al massimo
        if (getTimerMana() > cooldownTime && getMana() < manaMax) {
            //Incremento del mana alla volta
            setMana(getMana() + 1);
            //Intervallo di incremento senza shooting
            //più è piccola la differenza più ricarica
            //velocemente fra un proiettile e l'altro
            timerMana = cooldownTime - 0.3f;
        }
    }

    private void resetTimerMana(){
        timerMana = 0;
    }

    private float getTimerMana(){
        return timerMana;
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        if (!alive) {
            Gdx.app.debug(TAG, getNamePlayer() + " is dead.");
            death();
            return;
        }
        if (alive) Gdx.app.debug(TAG, getNamePlayer() + " is alive.");
        this.alive = alive;
    }

    public Vector2 getSpawnPointPlayer() {
        return spawnPointPlayer;
    }

    public void setSpawnPointPlayer(Vector2 spawnPointPlayer) {
        this.spawnPointPlayer.x = spawnPointPlayer.x;
        this.spawnPointPlayer.y = spawnPointPlayer.y;
        Gdx.app.debug(TAG,"New spawn point of player " +
                this.getNamePlayer() + ": " + getSpawnPointPlayer());
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //Aggiorna il Mana
        updateMana(deltaTime);
        // Gdx.app.debug(TAG, this.position.toString());
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

        //Setto il timerMana a zero per iniziare il conteggio dello shooting cooldown
        resetTimerMana();

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
        //Scala il danno del proiettile dalla vita
        setHealthPlayer(getHealthPlayer() - bullet.getDamage());
        //Gdx.app.debug(TAG, getNamePlayer() + " has taken " + bullet.getDamage() + " damage point.");
        Gdx.app.debug(TAG, getNamePlayer() + " health is " + getHealthPlayer() + ".");
    }

    public void death(){
        //ritorna vivo
        setAlive(true);

        //Ritorna al punto iniziale di Spawn
        this.position.set(spawnPointPlayer);

        //Riporta le statistiche a livelli iniziali
        setHealthPlayer(healthMax);
        setMana(manaMax);
    }

    @Override
    /**
     * Renderizzazione del giocatore
     */
    public void render(SpriteBatch batch) {
        if(velocity.y < 0)
            setAnimation(walk_down);
        else if(velocity.y > 0)
            setAnimation(walk_up);
        else if(velocity.x < 0)
            setAnimation(walk_left);
        else if(velocity.x > 0)
            setAnimation(walk_right);

        //if(animation != null) Asset = animation.getKeyFrame(stateTime, true);

        //if (movementEnable)Gdx.app.debug(TAG, namePlayer + " position: " + "(" + position.x + "," + position.y + ")");
        batch.draw(
                Asset.getTexture(),                 //Asset attuale dell'oggetto
                position.x,                         //Posizione X
                position.y,                         //Posizione Y
                origin.x,                           //Origine dell'oggetto X, default 0
                origin.y,                           //Origine dell'oggetto Y, default 0
                dimension.x,                        //Dimensione dell'oggetto X
                dimension.y,                        //Dimensione dell'oggetto Y
                scale.x,                            //Scala dell'oggetto rispetto all'origine X
                scale.y,                            //Scala dell'oggetto rispetto all'origine y
                rotation,                           //Gradi di rotazione
                Asset.getRegionX(),                 //Posizione X da cui ritagliare dalla Texture X
                Asset.getRegionY(),                 //Posizione Y da cui ritagliare dalla Texture Y
                Asset.getRegionHeight(),            //Altezza da ritagliare
                Asset.getRegionWidth(),             //Larghezza da ritagliare
                flipX,                              //Specchiare o meno su X
                flipY);                             //Specchiare o meno su Y
    }

}
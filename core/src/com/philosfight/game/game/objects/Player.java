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
import com.philosfight.game.game.abilities.Shooting;
//import com.philosfight.game.game.Effects.MeleeArea;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Player extends AbstractGameObject {
    //Utile nelle exeptions per mostrare il nome dell'oggetto
    public static final String TAG = Player.class.getName();

    public enum state
    {
        standby,
        walking,
        dying
    }

    public enum dir
    {
        left,
        right,
        up,
        down
    }

    dir direction = dir.up;
    state st = state.standby;

    /**
     * Animations
     */
    private Animation walk_up;
    private Animation walk_down;
    private Animation walk_left;
    private Animation walk_right;

    private Animation standby_up;
    private Animation standby_down;
    private Animation standby_left;
    private Animation standby_right;

    /**
     * Flags
     */
    //Flag della vita
    private boolean alive = true;

    /**
     * Comandi del giocatore
     */
    //Movimento Giocatore
    public MovementPlayer movement;
    //Shooting giocatore
    public Shooting shooting;
    //Sparo giocatore
    private int key_Shoot;


    /**
     * Caratteristiche del giocatore
     */
    //Nome Giocatore
    private String namePlayer;
    //Quantità vita
    private float healthPlayer;
    //Area di interazione Melee
    private MeleeArea meleeArea;
    //Roba da decidere se utile
    public Circle rangeMelee;
    private float meleeValue;
    //Punto di spawn
    private Vector2 spawnPointPlayer;

    /**
     * Costanti Player
     */
    //Massimo di bullet correnti in animazione
    public static final int MAX_BULLETS = 15;
    //Tempo di cooldown per la ricarica del mana
    public static final float COOLDOWNTIME = 0.6f;
    //Setta la salute iniziale
    public static final float healthMax = 70;
    //Setta mana iniziale
    public static final float manaMax = 20;

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
        terminalVelocity.set(4.0f, 4.0f);   //4 è un valore medio
        friction.set(12.0f, 12.0f);         //12 è un valore medio
        movement = new MovementPlayer();

        //Player charatteristics
        setHealthPlayer(healthMax);
        setMeleeValue(2);
        shooting = new Shooting(true, manaMax);
        //meleeArea = new MeleeArea(this.position,getMeleeValue());

        stateTime = 0;
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
    public void setAnimations(Animation walk_up, Animation walk_down, Animation walk_left, Animation walk_right,
                              Animation standby_up, Animation standby_down, Animation standby_left, Animation standby_right){
        this.walk_up = walk_up;
        this.walk_down = walk_down;
        this.walk_left = walk_left;
        this.walk_right = walk_right;
        this.standby_up = standby_up;
        this.standby_down = standby_down;
        this.standby_left = standby_left;
        this.standby_right = standby_right;
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

    public Animation getStandby_up() {
        return standby_up;
    }
    public Animation getStandby_down() {
        return standby_down;
    }
    public Animation getStandby_left() {
        return standby_left;
    }
    public Animation getStandby_right() {
        return standby_right;
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

    public float getMeleeValue() {
        return meleeValue;
    }

    public void setMeleeValue(float meleeValue) {
        this.meleeValue = meleeValue;
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
        //Aumenta la variabile delle animazioni
        stateTime += deltaTime;
        //Aggiorna il Mana
        shooting.updateMana(deltaTime);
        // Gdx.app.debug(TAG, this.position.toString());

        movement.check(this);
    }

        @Override
    protected void updateMotionX(float deltaTime) {
        if (velocity.x != 0) {
            // Apply friction
            if (velocity.x > 0) {
                velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
                direction = dir.right;
            } else {
                velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
                direction = dir.left;
            }
            st = state.walking;
        }
        else
            st = state.standby;
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
                direction = dir.up;
            } else {
                velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
                direction = dir.down;
            }
            st = state.walking;
        }
        else
            st = state.standby;
        // Apply acceleration
        velocity.y += acceleration.y * deltaTime;
        // Make sure the object's velocity does not exceed the
        // positive or negative terminal velocity
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
    }

    /**
     * Metodo per sparare ad un preciso target
     * @param target Obiettivo
     */
    public void shootAt(AbstractGameObject target){
        shooting.shoot(this.position, this.dimension, target);
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
        shooting.setMana(manaMax);
    }

    @Override
    /**
     * Renderizzazione del giocatore
     */
    public void render(SpriteBatch batch) {
        switch (st) {
                case standby: if(direction == dir.up){
                    setAnimation(standby_up);
                }
                else if(direction == dir.down){
                    setAnimation(standby_down);
                }else if(direction == dir.left){
                    setAnimation(standby_left);
                }else if(direction == dir.right){
                    setAnimation(standby_right);
                }
                    break;
                case walking: if(direction == dir.up){
                    setAnimation(walk_up);
                }
                else if(direction == dir.down){
                    setAnimation(walk_down);
                }else if(direction == dir.left){
                    setAnimation(walk_left);
                }else if(direction == dir.right){
                    setAnimation(walk_right);
                }
                break;
            }

        Asset = (TextureRegion)animation.getKeyFrame(stateTime, true);
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
                Asset.getRegionWidth(),            //Altezza da ritagliare
                Asset.getRegionHeight(),             //Larghezza da ritagliare
                flipX,                              //Specchiare o meno su X
                flipY);                             //Specchiare o meno su Y
    }

}
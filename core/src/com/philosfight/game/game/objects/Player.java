package com.philosfight.game.game.objects;

        import com.badlogic.gdx.Application;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.math.MathUtils;
        import com.philosfight.game.game.Assets;
    


public class Player extends AbstractGameObject {

    //Utile nelle exeptions per mostrare il nome dell'oggetto
    public static final String TAG = Player.class.getName();

    //Nome
    private String namePlayer;
    //Vita del Player
    private float lifePlayer;
    //Mana del Player
    private float mana;
    //Estensione dell'area Melee con la quale il Player attacca fisicamente;
    private float meleeExtension;
    //Flag di movimento
    private boolean movementEnable = false;


    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public float getLifePlayer() {
        return lifePlayer;
    }

    public void setLifePlayer(float lifePlayer) {
        this.lifePlayer = lifePlayer;
    }

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        this.mana = mana;
    }

    public float getMeleeExtension() {
        return meleeExtension;
    }

    public void setMeleeExtension(float meleeExtension) {
        //La meleeExtension non sarà mai negativa
        if (meleeExtension < 0) meleeExtension = 0;
        this.meleeExtension = meleeExtension;
    }

    //Costruttore
    public Player() {
        init();
    }

    private void init(){
        dimension.set(0.75f, 0.75f);
        bounds.set(position.x, position.y, dimension.x, dimension.y);
        // Set physics values
        terminalVelocity.set(3.0f, 3.0f);   //3 è un valore medio
        friction.set(12.0f, 12.0f);         //12 è un valore medio

        ObjectAssets = Assets.instance.player.pg;

    }
    @Override
    public void update (float deltaTime) {
        super.update(deltaTime);
    }


    @Override
    protected void updateMotionX (float deltaTime) {
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
    protected void updateMotionY (float deltaTime) {
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


    @Override
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
    public void render(SpriteBatch batch) {
        batch.draw(ObjectAssets.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, ObjectAssets.getRegionX(), ObjectAssets.getRegionY(), ObjectAssets.getRegionWidth(), ObjectAssets.getRegionHeight(), flipX, flipY);
    }


    public void movementCheck(float deltatime){
        //se il movimento è possibilitato
        if (!movementEnable)
            return;
        // Movimento player attivo sull'asse x
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //movimento verso Ovest
            velocity.x = -terminalVelocity.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //movimento verso Est
            velocity.x = terminalVelocity.x;
        } else {
            // Execute auto-forward movement on non-desktop platform
            if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                velocity.x = terminalVelocity.x;
            }
        }
        // Movimento player attivo sull'asse Y
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            // movimento verso nord
            velocity.y = -terminalVelocity.y;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            // movimento verso sud
            velocity.y = terminalVelocity.y;
        } else {
            // Execute auto-forward movement on non-desktop platform
            if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                velocity.y = terminalVelocity.y;
            }
        }
    }

    public boolean getMovementEnable(){
        return  movementEnable;
    }

    /**
     * Attiva il movimento del Player
     * se @param movementEnable True = in movimento
     * se @param movementEnable False = disabilitato
    */
    public void setMovementEnable(boolean movementEnable){
        Gdx.app.debug(TAG, "Movement player set: " + movementEnable + " on Player: " + namePlayer);
        this.movementEnable = movementEnable;
    }

}

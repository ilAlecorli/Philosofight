package com.philosfight.game.game.objects;

        import com.badlogic.gdx.Application;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.philosfight.game.game.Assets;
    


public class Player extends AbstractGameObject {

    //Utile nelle exeptions per mostrare il nome dell'oggetto
    public static final String TAG = Player.class.getName();


    private String namePlayer;
    //Vita del Player
    private float lifePlayer;
    //Mana del Player
    private float mana;
    //Estensione dell'area Melee con la quale il Player attacca fisicamente;
    private float meleeExtension;


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
        ObjectAssets = Assets.instance.player.pg;

    }
    @Override
    public void update (float deltaTime) {
        super.update(deltaTime);

    }

    @Override
    protected void updateMotionY (float deltaTime) {
        super.updateMotionY(deltaTime);
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

    
    private void movementCheck(){
        // Movimento player attivo sull'asse x
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //movimento verso Ovest
            velocity.x = -terminalVelocity.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //movimento verso Est
            velocity.x = terminalVelocity.x;
        } else {
            // Execute auto-forward movement on non-desktop platform
            if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                velocity.x = terminalVelocity.x;
            }
        }

        // Movimento player attivo sull'asse Y
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // movimento verso nord
            velocity.y = -terminalVelocity.y;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // movimento verso sud
            velocity.y = terminalVelocity.y;
        } else {
            // Execute auto-forward movement on non-desktop platform
            if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                velocity.y = terminalVelocity.y;
            }
        }
    }
}

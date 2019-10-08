package com.philosfight.game.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;


public abstract class AbstractGameObject{

    //This is the object's current speed in m/s.
    public Vector2 velocity;

    //This is the object's positive and negative maximum speed in m/s.
    public Vector2 terminalVelocity;

    //This is an opposing force that slows down the object until its velocity equals zero.
    //This value is given as a coefficient that is dimensionless.
    //A value of zero means no friction, and thus the object's velocity will not decrease.
    public Vector2 friction;

    //This is the object's constant acceleration in m/s².
    public Vector2 acceleration;

    //The object's bounding box describes the physical body that will be used for collision detection with other objects.
    //The bounding box can be set to any size and is completely independent of the actual dimension of the object in the game world.
    public Rectangle bounds;
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    public boolean flipX;
    public boolean flipY;
    public TextureRegion Asset;

    /**
     * Animation
     */
    public float stateTime;
    public Animation animation;

    public void setAnimation (Animation animation) {
        this.animation = new Animation(animation.getFrameDuration(),animation.getKeyFrames());
//        stateTime = 0;
    }
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Costruttore
     */
    public AbstractGameObject () {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
        flipX = false;
        flipY = false;


    }

    public void update (float deltaTime) {
        updateMotionX(deltaTime);
        updateMotionY(deltaTime);
        // Move to new position

        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

    }

    protected void updateMotionX (float deltaTime){}
    protected void updateMotionY (float deltaTime){}
    public abstract void render (SpriteBatch batch);
}

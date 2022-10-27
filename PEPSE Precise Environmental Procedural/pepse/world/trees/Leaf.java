package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;

import danogl.components.*;
import danogl.gui.rendering.*;
import danogl.util.*;
import pepse.util.ColorSupplier;
import pepse.world.*;

import java.awt.*;
import java.util.Random;
/**
 * tree leaf game object
 */
public class Leaf extends Block {
    private static final float FULL_360 = 360f;
    private static final float LEAF_WOBBLE = 10;
    private static final float WIND_SPEED = 0.1f;
    private static final float BASE_SIZE = 30;
    private static final float FADEOUT_TIME = 7;
    private static final float FALL_SPEED = 33;
    private static final float JAEGER_FACTOR = 4;
    private static final float MAX_FALL_TIME = 50;
    private static final float MAX_FADE_TIME = 2;
    private static final float MAX_RESPAWN_TIME = 2;
    private static final float DEFAULT_CRAZINESS = 4;
    private static final double RNG_FACTOR = 8;
    private static final float BASE_SIZE_FACTOR = 1.5f;
    private static final float FALL_SPEED_BUFFER = 2;
    private static final float ANGLE_FACTOR = 10;
    private static final float COUNTER_INCREMENT = 0.1f;
    private final Vector2 originalLocation;
    private float craziness;
    private float horizontalVelocity;
    private Random rand;
    private boolean isFalling;
    private boolean isSpawned;
    private float fallTime;
    private float fadeTime;
    private float respawnTime;
    private float counter;
    private Renderable curRenderable;

    /**
     * constructor for the class
     * @param topLeftCorner top-left corner at which the leaf renders
     * @param renderable image to render the leaf
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        originalLocation = topLeftCorner;
        craziness = DEFAULT_CRAZINESS;
        horizontalVelocity = 0;
        isFalling = false;
        isSpawned = true;
        rand = new Random((int)(RNG_FACTOR * Math.PI * topLeftCorner.x() + RNG_FACTOR * Math.E * topLeftCorner.y()));
        fallTime = ((rand.nextFloat()) * MAX_FALL_TIME);
        fadeTime = ((rand.nextFloat()) * MAX_FADE_TIME);
        respawnTime = ((rand.nextFloat()) * MAX_RESPAWN_TIME);
        new Transition<Float>(
                this,
                (angle) -> this.renderer().setRenderableAngle(getLeafAngle(angle)),//the game object being changed
                FULL_360,    //initial transition value
                0f,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                LEAF_WOBBLE,   //transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        new Transition<Float>(
                this,
                (angle) -> this.setDimensions(getLeafDimensions(angle)),//the game object being changed
                FULL_360,    //initial transition value
                0f,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                LEAF_WOBBLE,   //transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }

    /**
     * gets the leaf dimensions
     * @param angle the angle for deciding the dimensions
     * @return vector that decides the leafs dimensions
     */
    private Vector2 getLeafDimensions(Float angle) {
        float sinput = (float)(Math.sin(getCenter().x()) + Math.sin(getCenter().y()));
        float xDim = Math.abs(craziness * (float) (Math.sin(sinput * angle * WIND_SPEED)) + BASE_SIZE_FACTOR * BASE_SIZE);
        float yDim = Math.abs(craziness * (float) (Math.sin(sinput * Math.E * angle * WIND_SPEED)) + BASE_SIZE_FACTOR * BASE_SIZE);
        return new Vector2(xDim, yDim);
    }

    /**
     * calculates the laefs angle
     * @param angle the angle for deciding the dimensions
     * @return the desired angle
     */
    private float getLeafAngle(Float angle) {
        return ANGLE_FACTOR * (float) Math.sin((Math.sin(getCenter().x()) + Math.sin(getCenter().y())) * angle * WIND_SPEED);
    }

    /**
     * respawns the leaves back on the trees
     */
    private void respawn()
    {
        setVelocity(Vector2.ZERO);
        setCenter(originalLocation.add(getDimensions().mult(0.5f)));
        renderer().setRenderable(curRenderable);
        renderer().fadeIn(0);
        isSpawned = true;
        isFalling = false;
        fallTime = ((rand.nextFloat()) * MAX_FALL_TIME);
        fadeTime = ((rand.nextFloat()) * MAX_FADE_TIME);
        respawnTime = ((rand.nextFloat()) * MAX_RESPAWN_TIME);
    }

    /**
     * initiates the fading out process of the leaves
     */
    private void startFade()
    {
        renderer().fadeOut(FADEOUT_TIME);
        curRenderable = renderer().getRenderable();
        renderer().setRenderable(new RectangleRenderable(ColorSupplier.approximateColor(Color.orange.darker())));
        new ScheduledTask(
                this,
                FADEOUT_TIME + respawnTime,
                false,
                this::respawn
        );
    }

    /**
     * initiates the falling process of the leaves
     */
    private void startFall()
    {
        setVelocity(Vector2.DOWN.mult((rand.nextFloat() + FALL_SPEED_BUFFER) * FALL_SPEED));
        isFalling = true;
        new ScheduledTask(
                this,
                fadeTime,
                false,
                this::startFade
        );
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isSpawned) {
            isSpawned = false;
            new ScheduledTask(
                    this,
                    4 * fallTime,
                    false,
                    this::startFall
            );
        }
        if (isFalling) {
                setVelocity(new Vector2(counter * LEAF_WOBBLE * (float) Math.sin(counter), getVelocity().y()));
                counter += COUNTER_INCREMENT;
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Dirt) {
            isFalling = false;
            this.setVelocity(Vector2.ZERO);
        }
    }

    /**
     * increases the craziness of the foliage
     */
    public void increaseCraziness(float offset) {
        craziness += JAEGER_FACTOR * offset;
    }
}

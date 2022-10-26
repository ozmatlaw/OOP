package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * game ball class
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private final Counter collisionCounter;
    private static final int BALL_INIT_SPEED = 150;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param collisionSound The sound to make when collision occurs
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = new Counter();
    }

    /**
     * resets the ball to the initial state
     */
    public void resetBall(Vector2 where) {
        Random random = new Random();
        if (random.nextBoolean()) {
            this.setVelocity(new Vector2(1, 1).mult(BALL_INIT_SPEED));
        } else {
            this.setVelocity(new Vector2(-1, 1).mult(BALL_INIT_SPEED));
        }
        this.setCenter(where);
    }

    /**
     * logic to perform when collision occurs
     * @param other the object that collided with
     * @param collision collision logic interface
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter.increment();
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }

    /**
     * gets the current count
     * @return int of count
     */
    public int getCount(){ return collisionCounter.value();}


}

package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.*;

public class Brick extends GameObject {

    public CollisionStrategy collisionStrategy;
    private final Counter BrickCounter;

    /**
     * Construct a new GameObject instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param brickCounter  The current brick count
     */
    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy collisionStrategy    ,
                 Counter brickCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        BrickCounter = brickCounter;
    }


    /**
     * overrides the collision logic for bricks
     * @param other the object that collided with the brick
     * @param collision the collision variable in order to alter the game
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this,other,BrickCounter);
    }
}

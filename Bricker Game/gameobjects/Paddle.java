package src.gameobjects;

import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * main game paddle
 */
public class Paddle extends danogl.GameObject{

    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private static int MIN_DISTANCE_FROM_SCREEN_EDGE;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param inputListener the input stream that gets the user input
     * @param windowDimensions the current window dimensions
     * @param minDistanceFromEdge  the minimun dist from the edge that the paddle can travel
     */
    public Paddle(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Renderable renderable,
                  UserInputListener inputListener,
                  Vector2 windowDimensions,
                  int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        MIN_DISTANCE_FROM_SCREEN_EDGE = minDistanceFromEdge;
    }

    /**
     * overrides the update method to customize for the paddle
     * @param deltaTime the difference in time between each update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        float rightSide = (windowDimensions.x() - MIN_DISTANCE_FROM_SCREEN_EDGE- getDimensions().x());
        // check boundaries
        if(this.getTopLeftCorner().x() < MIN_DISTANCE_FROM_SCREEN_EDGE){
            this.setTopLeftCorner(new Vector2(MIN_DISTANCE_FROM_SCREEN_EDGE,getTopLeftCorner().y()));
        }
        if(this.getTopLeftCorner().x() > rightSide){
            this.setTopLeftCorner(new Vector2(rightSide, getTopLeftCorner().y()));
        }
    }
}

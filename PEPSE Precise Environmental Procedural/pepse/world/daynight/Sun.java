package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * sun game object
 */
public class Sun extends GameObject {

    private static final float FULL_360 = 360;
    private static final int SUN_SIZE = 100;
    private static final String SUN_TAG = "sun";
    private static float DIST_FOR_ROTATION = 700;
    private final Vector2 dimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param windowDimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Sun(Vector2 topLeftCorner, Vector2 windowDimensions, Renderable renderable) {
        super(topLeftCorner, windowDimensions, renderable);
        this.dimensions = windowDimensions;
    }

    /**
     * creates the sun GameObject
     * @param windowDimensions Width and height in window coordinates.
     * @param cycleLength the length of a day cycle
     * @param gameObjects gameObject collections
     * @param layer desired layer for the sun
     * @return the sun GameObject
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            final Vector2 windowDimensions,
            float cycleLength) {
        GameObject sun = new GameObject(
                Vector2.ZERO, new Vector2(SUN_SIZE, SUN_SIZE),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN_TAG);
        new Transition<Float>(
                sun,
                (angle) -> sun.setCenter(windowDimensions.mult(0.5f).multY(2).add(Vector2.UP.mult(DIST_FOR_ROTATION).rotated(angle))),//the game object being changed
                FULL_360,    //initial transition value
                0f,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                cycleLength,   //transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null    //nothing further to execute upon reaching final value
        );
        return sun;
    }
}

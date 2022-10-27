package pepse.world.daynight;

import danogl.*;
import danogl.collisions.*;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.*;
import danogl.util.*;
import java.awt.*;
/**
 * night game object
 */
public class Night extends GameObject {
    private static final Color BASIC_NIGHT_COLOR = Color.BLACK;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String SKY_TAG = "sky";

    /**
     * constructor for the class
     * @param topLeftCorner top left corner of the night
     * @param dimensions dimensions of the night
     * @param renderable renderable of the night
     */
    public Night(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * creates the night object
     * @param gameObjects game object collection
     * @param windowDimensions window dimensions
     * @param cycleLength the length of the night cycle
     * @param layer the desired layer for the night object
     * @return game object of the night
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength)
    {
        GameObject night = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_NIGHT_COLOR));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag(SKY_TAG);
        new Transition<Float>(
                night, //the game object being changed
                night.renderer()::setOpaqueness,  //the method to call
                0f,    //initial transition value
                MIDNIGHT_OPACITY,   //final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                cycleLength / 2,   //transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  //nothing further to execute upon reaching final value
        return night;
    }
}

package pepse.world;

import danogl.*;
import danogl.collisions.*;
import danogl.gui.rendering.*;
import danogl.util.*;
import danogl.components.*;
import java.awt.*;

/**
 * sky game object
 */
public class Sky extends GameObject {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * constructor for the class
     * @param topLeftCorner top-left corner at which the sky renders
     * @param dimensions dimensions of the sky
     * @param renderable image to render the sky
     */
    public Sky(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * creates the sky
     * @param gameObjects object collection to which the sky is added
     * @param windowDimensions dimensions of the window
     * @param skyLayer layer of the sky
     * @return the sky
     */
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions, int skyLayer)
    {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag("sky");
        return sky;
    }
}

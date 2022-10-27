package pepse.world.daynight;
//
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * sun halo game object
 */
public class SunHalo extends GameObject {

    private static final float SUN_HALO_SIZE = 200;
    private static final String SUN_HALO_TAG = "sunHalo";


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public SunHalo(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * creates the sun halo
     * @param gameObjects the game object collection
     * @param sun the sun game object for the halo to attach on to
     * @param color the desired color to the halo
     * @param layer the layer to be displayed
     * @return the halo game object
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        GameObject sunHalo = new GameObject(
                Vector2.ZERO,new Vector2(SUN_HALO_SIZE,SUN_HALO_SIZE),
                new OvalRenderable(color));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }

    /**
     * it's a functional interface for the class
    */
    @FunctionalInterface
    public interface Component {
        void update(float deltaTime);
    }
}

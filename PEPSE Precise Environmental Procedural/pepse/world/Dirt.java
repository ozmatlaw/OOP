package pepse.world;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * dirt ground game object
 */
public class Dirt extends Block {
    /**
     * constructor for the class
     * @param topLeftCorner top-left corner at which the dirt renders
     * @param dimensions dimensions of the dirt
     * @param renderable image to render the dirt
     */
    public Dirt(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, renderable);
        setDimensions(dimensions);
        setTopLeftCorner(topLeftCorner);
    }
}

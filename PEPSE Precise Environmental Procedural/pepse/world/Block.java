package pepse.world;

import danogl.*;
import danogl.components.*;
import danogl.gui.rendering.*;
import danogl.util.*;

/**
 *Block game object
 */
public class Block extends GameObject {
    public static final float SIZE = 30;

    /**
     * constructor for the class
     * @param topLeftCorner top-left corner at which the block renders
     * @param renderable image to render the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, new Vector2(SIZE, SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}

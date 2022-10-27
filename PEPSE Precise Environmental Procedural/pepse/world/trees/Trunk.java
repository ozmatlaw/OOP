package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.*;

import java.awt.*;
/**
 * tree trunk game object
 */
public class Trunk {
    private static final int BLOCK_SIZE = 30;
    private static final Color BASE_TRUNK_COLOR = new Color(100, 50, 20);
    private static final int TRUNK_LAYER = -15;
    private Block[] trunkPieces;

    /**
     * constructor for the class
     * @param gameObjects object collection to which blocks must be added
     * @param sourceLocation location of the trunk's base
     * @param height height of the trunk
     */
    public Trunk(GameObjectCollection gameObjects, Vector2 sourceLocation, int height)
    {
        trunkPieces = new Block[height];
        for (int i = 0; i < height; i++)
        {
            Block block = new Block(
                    sourceLocation.add(Vector2.UP.mult(BLOCK_SIZE * i)),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR)));
            trunkPieces[i] = block;
            gameObjects.addGameObject(block, TRUNK_LAYER);
        }
    }
}

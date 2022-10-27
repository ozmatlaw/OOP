package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
/**
 * tree foliage game object
 */
public class Foliage {
    private static final int BLOCK_SIZE = 30;
    private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    private final Leaf[] leaves;

    /**
     * constructor for the class
     * @param gameObjects game object collection
     * @param sourceLocation where the tree starts
     * @param height the height of the tree
     * @param layer layer for the foliage to be
     * @param colorDiff the difference in color
     */
    public Foliage(GameObjectCollection gameObjects, Vector2 sourceLocation, int height, int layer, int colorDiff)
    {
        leaves = new Leaf[height * height / 4];
        for (int i = 0; i < height / 2; i++)
        {
            for (int j = 0; j < height / 2; j++)
            {
                Color actualColor = (colorDiff == 0) ? BASE_LEAF_COLOR : ((colorDiff > 0) ? BASE_LEAF_COLOR.brighter() : BASE_LEAF_COLOR.darker());
                leaves[i * height / 2 + j] = new Leaf(
                        sourceLocation.add(Vector2.UP.mult(BLOCK_SIZE * height).add(
                                Vector2.RIGHT.mult(BLOCK_SIZE * (i - height / 4))).add(
                                        Vector2.DOWN.mult(BLOCK_SIZE * (j - height / 4)))),
                        new RectangleRenderable(ColorSupplier.approximateColor(actualColor)));
                gameObjects.addGameObject(leaves[i * height / 2 + j], layer + colorDiff);
            }
        }
    }

    /**
     * gets the leaves on the tree
     * @return array of leaves
     */
    public Leaf[] getLeaves()
    {
        return leaves;
    }
}

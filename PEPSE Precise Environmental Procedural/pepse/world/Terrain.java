package pepse.world;

import danogl.collisions.*;
import danogl.util.*;
import danogl.gui.rendering.*;
import pepse.util.*;

import java.awt.*;
import java.util.*;

/**
 * terrain class
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int BLOCK_SIZE = 30;
    private static final int TERRAIN_PRECISION = 15;
    private static final float TERRAIN_RUGGEDNESS = 5;
    private static final float TERRAIN_HOMOGENEITY = 200;
    private static final float TERRAIN_VARIATION = 1000;
    private static final float TERRAIN_HORIZONTALITY = 9001;
    private static final float TERRAIN_DEPTH = 0.1f;
    private static final float BOTTOM_SIZE = 20;

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final int seed;
    private final float groundHeightAtX0;

    private Set<Dirt> blocks;

    /**
     * constructor for the class
     * @param gameObjects object collection to update
     * @param groundLayer layer of the ground
     * @param windowDimensions dimensions of the window
     * @param seed seed for terrain generation
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        groundHeightAtX0 = windowDimensions.y();
        blocks = new HashSet<Dirt>();
        this.seed = seed;
    }


    /**
     * creates terrain in a given range of x values
     * @param minX minimal x value at which to generate terrain
     * @param maxX maximal x value at which to generate terrain
     */
    public void createInRange(int minX, int maxX)
    {
        for (int x = minX; x <= maxX; x+=BLOCK_SIZE)
        {
            int minY = BLOCK_SIZE * (int)(groundHeightAt(x) / BLOCK_SIZE);
            for (int y = minY; y < minY + TERRAIN_DEPTH * windowDimensions.y(); y+=BLOCK_SIZE)
            {
                Dirt block = new Dirt(
                        new Vector2(x, y),
                        new Vector2(BLOCK_SIZE, BLOCK_SIZE),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                blocks.add(block);
                gameObjects.addGameObject(block, groundLayer);
            }
            Dirt bottomBlock = new Dirt(
                    new Vector2(x, minY + TERRAIN_DEPTH * windowDimensions.y()),
                    new Vector2(BLOCK_SIZE, BOTTOM_SIZE * BLOCK_SIZE),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
            blocks.add(bottomBlock);
            gameObjects.addGameObject(bottomBlock, groundLayer);
        }
    }

    /**
     * gets a specialized x value
     * @param x the x coordinate
     * @return the specialized x value
     */
    private float specialX(float x)
    {
        float sum = 0;
        for (int i = 0; i < TERRAIN_PRECISION; i++)
        {
            sum += Math.sin(Math.sqrt(i) * x + i) / TERRAIN_HOMOGENEITY;
        }
        return sum;
    }

    /**
     * returns the height of the terrain at a given x point
     * @param x point in the terrain
     * @return the height in question
     */
    public float groundHeightAt(float x)
    {
        x /= TERRAIN_HORIZONTALITY;
        float sum = 0;
        for (int i = 0; i < TERRAIN_PRECISION; i++)
        {
            sum += Math.sin(Math.pow(specialX(x) + TERRAIN_RUGGEDNESS, i)) / Math.pow(2, i);
        }
        sum += Math.sin(x);
        return (int)(TERRAIN_VARIATION * sum);
    }
}

package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.trees.drinks.Drink;
import pepse.world.trees.drinks.DrinkFactory;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

/**
 * tree game object
 */
public class Tree {
    private static final int BLOCK_SIZE = 30;
    private static final int DRINK_LAYER = -1;
    private static final int FOLIAGE_OFFSET = 80;
    private static final float RANDOMIZER_FACTOR = 10;
    private static final float DRINK_SIZE = 100;
    private static float dropChance = 0.2f;
    private final Function<Float, Float> function;
    private final int seed;
    private static final int RNG_FACTOR = 10;
    private final WindowController windowController;
    private final int layer;
    private DrinkFactory factory;
    private static final float TREE_CHANCE = 0.2f;
    private final Vector2 sourceLocation;
    private final GameObjectCollection gameObjects;
    private final Random rand;
    private Trunk trunk;
    private Foliage foliage0;
    private Foliage foliage1;
    private int height;
    private Set<Tree> trees;


    /**
     * constructor for the class
     * @param gameObjects object collection to which the tree is added
     * @param sourceLocation location of the base of the trunk
     * @param height height of the tree
     * @param layer layer of the tree
     * @param seed the seed for the randomization
     * @param function the get groud hieght function
     * @param trees set of trees in the game
     * @param windowController window controller
     */
    public Tree(GameObjectCollection gameObjects,
                Vector2 sourceLocation,
                int height,
                int layer,
                int seed,
                Function<Float, Float> function,
                Set<Tree> trees,
                WindowController windowController) {
        this.rand = new Random();
        this.height = height;
        this.layer = layer;
        this.trunk = new Trunk(gameObjects, sourceLocation, height);
        this.sourceLocation = sourceLocation;
        this.gameObjects = gameObjects;
        generateFoliage(gameObjects, layer);
        new ScheduledTask(
                foliage0.getLeaves()[0],
                1,
                true,
                this::dropDrink
        );
        this.seed = seed;
        this.function = function;
        this.windowController = windowController;
        this.trees = trees;
    }



    /**
     * drops a drink from the tree based on the drop chance
     */
    private void dropDrink() {
        if (rand.nextFloat() < dropChance) {
            Drink drink = factory.getDrink(
                    sourceLocation.add(Vector2.UP.mult(height * BLOCK_SIZE)),
                    new Vector2(DRINK_SIZE, DRINK_SIZE));
            gameObjects.addGameObject(drink, DRINK_LAYER);
        }
    }

    /**
     * sets the dropChance for the FizzyBubblech
     * @param newBonus factor to increase the drink drop chance
     */
    public static void setDropChanceBonus(float newBonus) {
        dropChance *= newBonus;
    }

    /**
     * sets the drinkFactory to the trees
     *
     * @param factory drink factory
     */
    public void setFactory(DrinkFactory factory) {
        this.factory = factory;
    }


    public void createInRange(int minX, int maxX) {
        for (int x = minX; x <= maxX; x+=BLOCK_SIZE)
        {
            int minY = BLOCK_SIZE * (int)(function.apply((float)x) / BLOCK_SIZE);
            float pseudoRandom0 = (float)Math.pow((Math.abs((x + seed) * Math.PI) / RNG_FACTOR) % RNG_FACTOR, Math.PI) - (int)Math.pow((Math.abs((x + seed) * Math.PI)/ RNG_FACTOR) % RNG_FACTOR, Math.PI);
            float pseudoRandom1 = (float)Math.pow((Math.abs((x + seed) * Math.PI) / RNG_FACTOR) % RNG_FACTOR, Math.E) - (int)Math.pow((Math.abs((x + seed) * Math.PI)/ RNG_FACTOR) % RNG_FACTOR, Math.E);
            if (pseudoRandom0 < TREE_CHANCE && (x / RNG_FACTOR) % RNG_FACTOR / 2 == 0 || 2 * Math.abs(x - windowController.getWindowDimensions().x() / 2) < BLOCK_SIZE)
            {
                trees.add(new Tree(gameObjects, new Vector2(x, minY), (int)((pseudoRandom1 + 2) * RNG_FACTOR / 2), layer, seed, function, trees, windowController));
            }
        }
    }

    /**
     * getter for the trees object
     * @return set containing the trees
     */
    public Set<Tree> getTrees()
    {
        return trees;
    }



    /**
     * generates the foliage based on random principle
     *
     * @param gameObjects gameObjects collection
     * @param layer       the desired layer for the foliage
     */
    private void generateFoliage(GameObjectCollection gameObjects, int layer) {
        float pseudoRandom0 = getPseudoRandom(Math.PI);
        float pseudoRandom1 = getPseudoRandom(Math.E);
        foliage0 = new Foliage(gameObjects, sourceLocation, height, layer, 0);
        foliage1 = new Foliage(gameObjects, sourceLocation.add(new Vector2(
                pseudoRandom0 * FOLIAGE_OFFSET - FOLIAGE_OFFSET/2,
                pseudoRandom1 * FOLIAGE_OFFSET - FOLIAGE_OFFSET/2)), height, layer, 1);
    }

    /**
     * get the PseudoRandom to decide the tree foliage
     * @param secondarySeed the seed factor to multiply by
     * @return the PseudoRandom
     */
    private float getPseudoRandom(double secondarySeed) {
        return (float) Math.pow((Math.PI * Math.abs(sourceLocation.x() + seed) / RANDOMIZER_FACTOR) % RANDOMIZER_FACTOR, secondarySeed) -
                (int) Math.pow((Math.PI * Math.abs(sourceLocation.x() + seed) / RANDOMIZER_FACTOR) % RANDOMIZER_FACTOR, secondarySeed);
    }


    /**
     * increases the craziness of the foliage
     */
    public void activateJaegerBomb(float offset)
    {
        for (Leaf leaf : foliage0.getLeaves()) {
            if (leaf != null) {
                leaf.increaseCraziness(offset);
            }
        }
        for (Leaf leaf : foliage1.getLeaves()) {
            if (leaf != null) {
                leaf.increaseCraziness(offset);
            }
        }
    }
}

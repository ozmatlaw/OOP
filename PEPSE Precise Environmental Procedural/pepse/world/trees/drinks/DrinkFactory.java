package pepse.world.trees.drinks;

import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.trees.Tree;
import java.util.HashSet;
import java.util.Random;

/**
 * drink factory to provide the desired drinks
 */
public class DrinkFactory {
    private final Random random = new Random();
    private static final int NUMBER_OF_DRINKS = 4;
    private final ImageReader imageReader;
    private final HashSet<Tree> trees;
    private static final String IMAGE_PATH = "assets/";
    private final Avatar avatar;

    public DrinkFactory(ImageReader imageReader, HashSet<Tree> trees, Avatar avatar) {
        this.imageReader = imageReader;
        this.trees = trees;
        this.avatar = avatar;
    }

    /**
     * gets the drink image
     * @param name the image to render
     * @return a renderable of the drink
     */
    public Renderable getDrinkRenderable(String name)
    {
        return imageReader.readImage(IMAGE_PATH + name, true);
    }

    /**
     * gets the drinks based on random values
     * @param topLeftCorner the top left corner to spawn the drink on
     * @param dimensions the dimensions of the drink
     * @return the drink to spawn
     */
    public Drink getDrink(Vector2 topLeftCorner, Vector2 dimensions) {
        switch (random.nextInt(NUMBER_OF_DRINKS)) {
            case 0:
                return new Redbull(topLeftCorner, dimensions, getDrinkRenderable("redbull.png"), trees, avatar);
            case 1:
                return new SevenUp(topLeftCorner, dimensions, getDrinkRenderable("sevenUp.png"), trees, avatar);
            case 2:
                return new JaegerBomb(topLeftCorner, dimensions, getDrinkRenderable("jaegerBomb.png"), trees, avatar);
            case 3:
                return new FizzyBubblech(topLeftCorner, dimensions, getDrinkRenderable("fizzyBubblech.png"), trees, avatar);
            default:
                return null;
        }
    }
}

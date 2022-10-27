package pepse.world.trees.drinks;


import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.trees.Tree;

import java.util.HashSet;
/**
 * sevenup drink
 */
public class SevenUp extends Drink {
    private static final float SEVEN_UP_POWER = 1.25f;

    public SevenUp (Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, HashSet<Tree> trees, Avatar avatar) {
        super(topLeftCorner, dimensions, renderable, trees, avatar);
    }

    @Override
    protected void doPowerDown()
    {
        avatar.sevenUpSpeed(1/SEVEN_UP_POWER);
    }

    @Override
    protected void doPowerUp()
    {
        avatar.sevenUpSpeed(SEVEN_UP_POWER);
    }
}

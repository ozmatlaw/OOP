package pepse.world.trees.drinks;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.trees.Tree;

import java.util.HashSet;
/**
 * jaegerbomb drink
 */
public class JaegerBomb extends Drink {
    private static final float JAEGER_POWER = 3;

    public JaegerBomb(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, HashSet<Tree> trees, Avatar avatar) {
        super(topLeftCorner, dimensions, renderable, trees, avatar);
    }

    @Override
    protected void doPowerDown()
    {
        for (Tree tree : trees)
        {
            tree.activateJaegerBomb(-JAEGER_POWER);
        }
    }

    @Override
    protected void doPowerUp()
    {
        for (Tree tree : trees)
        {
            tree.activateJaegerBomb(JAEGER_POWER);
        }
    }
}

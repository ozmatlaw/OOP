package pepse.world.trees.drinks;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.trees.Tree;

import java.util.HashSet;
/**
 * fizzybubblech drink
 */
public class FizzyBubblech extends Drink {
    private static final float BUBBLECH_POWER = 1.25f;

    public FizzyBubblech(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, HashSet<Tree> trees, Avatar avatar) {
        super(topLeftCorner, dimensions, renderable, trees, avatar);
    }

    @Override
    protected void doPowerUp() {
        Tree.setDropChanceBonus(BUBBLECH_POWER);
    }

    @Override
    protected void doPowerDown() {
        Tree.setDropChanceBonus(1/BUBBLECH_POWER);
    }
}

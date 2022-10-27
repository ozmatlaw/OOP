package pepse.world.trees.drinks;


import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.trees.Tree;

import java.util.HashSet;

/**
 * red bull drink
 */
public class Redbull extends Drink {

    public Redbull(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, HashSet<Tree> trees, Avatar avatar) {
        super(topLeftCorner, dimensions, renderable, trees, avatar);
    }

    @Override
    protected void doPowerUp()
    {
        avatar.setRedbullActive(true);
    }

    @Override
    protected void doPowerDown(){
        avatar.setRedbullActive(false);
    }
}

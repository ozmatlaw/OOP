package pepse.world.trees.drinks;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.trees.Tree;

import java.util.HashSet;

public class Drink extends GameObject {
    private static final float DESPAWN_OFFSET = 1000;
    private static final float POWERUP_TIME = 15;
    private static final String AVATAR_TAG = "avatar";
    protected final Avatar avatar;
    protected final HashSet<Tree> trees;

    /**
     * constructor for the class
     * @param topLeftCorner top-left corner at which the drink renders
     * @param dimensions dimensions of the drink
     * @param renderable image to render the drink
     * @param trees hashset of trees through which to modify trees
     * @param avatar avatar object through which to modify the avatar
     */
    public Drink(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, HashSet<Tree> trees, Avatar avatar) {
        super(topLeftCorner, dimensions, renderable);
        this.trees = trees;
        this.avatar = avatar;
    }

    /**
     * initiates the powerUp for the drink
     */
    protected void doPowerUp() {}

    /**
     * initiates the powerDown after the time is up
     */
    protected void doPowerDown() {}

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setVelocity(getVelocity().add(Vector2.DOWN.mult(10)));
    }

    /**
     * upon collision, removes the drink from the game and activates its power-up
     * @param other object with which it is colliding
     * @param collision collision object
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(AVATAR_TAG))
        {
            setCenter(getCenter().add(Vector2.DOWN.mult(DESPAWN_OFFSET)));
            doPowerUp();
            new ScheduledTask(
                    avatar,
                    POWERUP_TIME,
                    false,
                    this::doPowerDown
            );
        }
    }
}

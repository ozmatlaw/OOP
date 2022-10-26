package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;

/**
 * double strategy implementation
 */
public class DoubleStrategyStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjectCollection;
    private final CollisionStrategy firstStrategy;
    private final CollisionStrategy secondStrategy;

    public DoubleStrategyStrategy(GameObjectCollection gameObjectCollection,
                                  BrickerGameManager gameManager,
                                  ImageReader imageReader,
                                  SoundReader soundReader,
                                  UserInputListener inputListener,
                                  WindowController windowController,
                                  Vector2 windowDimensions) {

        BrickStrategyFactory factory = new BrickStrategyFactory(gameObjectCollection,
                gameManager,
                imageReader,
                soundReader,
                inputListener,
                windowController,
                windowDimensions);
        this.gameObjectCollection = gameObjectCollection;
        firstStrategy = factory.getStrategy();
        factory.setDoubleCall();
        secondStrategy = factory.getStrategy();
    }

    /**
     * overrides the collision logic for bricks
     * @param thisObject   the object that was collided with
     * @param otherObject     the object that collided with  thisObject
     * @param counter the counter that counts how many bricks left in the game
     */
    @Override
    public void onCollision(GameObject thisObject, GameObject otherObject, Counter counter) {
        firstStrategy.onCollision(thisObject,otherObject,counter);
        secondStrategy.onCollision(thisObject,otherObject,counter);
    }

    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }

}

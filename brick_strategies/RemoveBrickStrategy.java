package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;
import src.gameobjects.Ball;

/**
 * removes the brick strategy
 */
public class RemoveBrickStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectsCollection;

    public RemoveBrickStrategy(GameObjectCollection gameObjects) {
        this.gameObjectsCollection = gameObjects;
    }

    /**
     * overrides the collision logic for bricks
     * @param thisObject   the object that was collided with
     * @param otherObject     the object that collided with  thisObject
     * @param counter the counter that counts how many bricks left in the game
     */
    @Override
    public void onCollision(GameObject thisObject, GameObject otherObject, Counter counter) {
        if(gameObjectsCollection.removeGameObject(thisObject, Layer.STATIC_OBJECTS) && otherObject instanceof Ball){
            counter.decrement();
        }
    }

    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectsCollection;
    }


}

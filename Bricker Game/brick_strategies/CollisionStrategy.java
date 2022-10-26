package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public interface CollisionStrategy {
    /**
     * abstract method for the collision logic for bricks
     * @param thisObject   the object that was collided with
     * @param otherObject     the object that collided with  thisObject
     * @param counter the counter that counts how many bricks left in the game
     */
    void onCollision(GameObject thisObject, GameObject otherObject, Counter counter);

    /**
     * gets the gameObjectCollection
     * @return the gameObjectCollection
     */
    GameObjectCollection getGameObjectCollection();
}

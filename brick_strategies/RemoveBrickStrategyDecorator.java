package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * collision strategy to decorate
 */
public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy{
    private final CollisionStrategy collisionStrategy;

    public RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated){
        this.collisionStrategy = toBeDecorated;
    }

    /**
     * overrides the collision logic for bricks
     * @param thisObject   the object that was collided with
     * @param otherObject     the object that collided with  thisObject
     * @param counter the counter that counts how many bricks left in the game
     */
    @Override
    public void onCollision(GameObject thisObject, GameObject otherObject, Counter counter) {
        collisionStrategy.onCollision(thisObject,otherObject,counter);
    }

    public GameObjectCollection getGameObjectCollection(){return collisionStrategy.getGameObjectCollection();}

}

package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * makes a mock paddle on collision
 */
public class MockPaddle extends Paddle {
    private static final int BUFFER = 20;
    private final GameObjectCollection gameObjectCollection;
    private int NUM_OF_COLLISIONS_LEFT;
    public static boolean isInstatiated = false;

    public MockPaddle(Vector2 topLeftCorner,
                      Vector2 Dimensions,
                      Renderable Image,
                      UserInputListener inputListener,
                      Vector2 windowDimensions,
                      GameObjectCollection gameObjectCollection,
                      int minDistEdgePaddle,
                      int numCollisionsToDisappear) {
        super(topLeftCorner,Dimensions,Image,inputListener,windowDimensions,minDistEdgePaddle+BUFFER);
        this.NUM_OF_COLLISIONS_LEFT = numCollisionsToDisappear;
        this.gameObjectCollection = gameObjectCollection;
        isInstatiated = true;
    }

    /**
     * overrides the collision logic for the mockpadle
     * @param other    the object that collided with thisObject
     * @param collision the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        NUM_OF_COLLISIONS_LEFT --;
        if(NUM_OF_COLLISIONS_LEFT <= 0){
            gameObjectCollection.removeGameObject(this);
            isInstatiated = false;
        }
    }
}

package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;
import src.gameobjects.Puck;

/**
 * sets the camera on the ball strategy
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator{


    private static final int INITIAL_COUNTDOWN = 4;
    private final BrickerGameManager gameManager;
    private final WindowController windowController;
    private BallCollisionCountdownAgent agent;

    public ChangeCameraStrategy(CollisionStrategy toBeDecorated,
                                WindowController windowController,
                                BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.gameManager = gameManager;
        this.windowController = windowController;
    }

    /**
     * overrides the collision logic for bricks
     * @param thisObject   the object that was collided with
     * @param otherObject     the object that collided with  thisObject
     * @param counter the counter that counts how many bricks left in the game
     */
    @Override
    public void onCollision(GameObject thisObject, GameObject otherObject, Counter counter) {
        super.onCollision(thisObject, otherObject, counter);
        if (gameManager.getCamera() == null && !(otherObject instanceof Puck)) {
            this.agent = new BallCollisionCountdownAgent((Ball) otherObject, this, INITIAL_COUNTDOWN);
            getGameObjectCollection().addGameObject(agent, Layer.BACKGROUND);
            gameManager.setCamera(new Camera(otherObject,
                    Vector2.ZERO,
                    windowController.getWindowDimensions().mult(1.2f),
                    windowController.getWindowDimensions()));
        }
    }

    /**
     * turns off the camera when the ball gets to 4 collisions
     */
    public void turnOffCameraChange(){
        gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(agent, Layer.BACKGROUND);
    }
}

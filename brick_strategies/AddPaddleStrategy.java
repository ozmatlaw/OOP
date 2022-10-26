package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.MockPaddle;

/**
 * adds a mockpaddle to the game on collision
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator{

    private static final int NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE  = 3;
    private final Vector2 windowDimensions;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final CollisionStrategy collisionStrategy;

    public AddPaddleStrategy(CollisionStrategy toBeDecorated,
                             ImageReader imageReader,
                             UserInputListener inputListener,
                             Vector2 windowDimensions) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
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
        super.onCollision(thisObject, otherObject, counter); // todo what to do inorder for the new paddles to disappear
        if(!MockPaddle.isInstatiated){
            makeMockPaddle();
        }
    }

    /**
     * makes the mockPaddle and adds it to the game
     */
    private void makeMockPaddle() {
        Renderable paddleImage = imageReader.readImage(BrickerGameManager.PADDLE_IMG, true);
        MockPaddle mockPaddle = new MockPaddle(Vector2.ZERO,
                BrickerGameManager.PADDLE_DIMENSIONS,
                paddleImage,
                inputListener,
                windowDimensions,
                getGameObjectCollection(),
                BrickerGameManager.MIN_DIST_EDGE_PADDLE,
                NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE );
        mockPaddle.setCenter(new Vector2(this.windowDimensions.x() / 2, this.windowDimensions.y()/2));
        getGameObjectCollection().addGameObject(mockPaddle);
    }
}

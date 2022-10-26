package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

/**
 * factory for brick strategies
 */
public class BrickStrategyFactory {
    private static final int NOT_ALL_STRATEGIES = 4;
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final GameObjectCollection gameObjectCollection;
    private final WindowController windowController;
    private final Vector2 windowDimensions;
    private final SoundReader soundReader;
    private static final int ALL_STRATEGIES = 5;
    private boolean calledFromDouble;

    public BrickStrategyFactory(danogl.collisions.GameObjectCollection gameObjectCollection,
                                 BrickerGameManager gameManager,
                                 danogl.gui.ImageReader imageReader,
                                 danogl.gui.SoundReader soundReader,
                                 danogl.gui.UserInputListener inputListener,
                                 danogl.gui.WindowController windowController,
                                 danogl.util.Vector2 windowDimensions){

        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.gameObjectCollection = gameObjectCollection;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.calledFromDouble = false;
    }

    /**
     * gets a random strategy for the bricks
     * @return brick strategy
     */
    public CollisionStrategy getRightStrategy(int limit){
        Random random = new Random();
        switch (random.nextInt(limit)) {
            case 0:
                return new ChangeSpeedStrategy(new RemoveBrickStrategy(gameObjectCollection), imageReader,
                    windowController);
            case 1:
                return new PuckStrategy(new RemoveBrickStrategy(gameObjectCollection), imageReader,
                        soundReader);
            case 2:
                return new AddPaddleStrategy(new RemoveBrickStrategy(gameObjectCollection), imageReader,
                        inputListener, windowDimensions);
            case 3:
                return new ChangeCameraStrategy(new RemoveBrickStrategy(gameObjectCollection),
                    windowController, gameManager);
            case 4:
                return new DoubleStrategyStrategy(gameObjectCollection, gameManager, imageReader, soundReader,
                        inputListener, windowController, windowDimensions);
            case 5:
                return new RemoveBrickStrategy(gameObjectCollection);
            default:
                return null;
        }
    }

    public void setDoubleCall(){
        this.calledFromDouble = true;
    }

    public CollisionStrategy getStrategy(){
        if(calledFromDouble){
            calledFromDouble = false;
            return getRightStrategy(NOT_ALL_STRATEGIES);
        }
        return getRightStrategy(ALL_STRATEGIES);
    }


}

package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.util.Counter;
import src.gameobjects.Status;
import src.gameobjects.StatusFactory;

import java.util.Random;

/**
 * adds change speed objects to the game on collision
 */
public class ChangeSpeedStrategy extends RemoveBrickStrategyDecorator {
    public static final float SLOW = (float) 0.9;
    public static final float FAST = (float) 1.1;
    private final ImageReader imageReader;
    private final WindowController windowController;
    private final String[] speeds = {StatusFactory.QUICKEN,StatusFactory.SLOW};

    public ChangeSpeedStrategy(CollisionStrategy collisionStrategy, ImageReader imageReader, WindowController windowController) {
        super(collisionStrategy);
        this.imageReader = imageReader;
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
        super.onCollision(thisObject,otherObject,counter);
         StatusFactory statusFactory = new StatusFactory(imageReader,getGameObjectCollection(),windowController);
         Status speed;
         if(windowController.getTimeScale() == FAST){
             speed = statusFactory.getStatus(thisObject, StatusFactory.SLOW);
         }else if(windowController.getTimeScale() == SLOW){
             speed = statusFactory.getStatus(thisObject, StatusFactory.QUICKEN);
         }else{
             Random random = new Random();
             speed = statusFactory.getStatus(thisObject,speeds[random.nextInt(speeds.length)]);
         }
         getGameObjectCollection().addGameObject(speed);
    }
}

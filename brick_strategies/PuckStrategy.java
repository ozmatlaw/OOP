package src.brick_strategies;


import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Puck;
import java.util.Random;

/**
 * adds pucks to the game strategy
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator{

    private static final int NUM_OF_BALLS = 3;
    private final SoundReader soundReader;
    private final ImageReader imageReader;
    private static final float[] INITIAL_DIRECTIONS = {1, -1, 0};
    private static final String PUCK_IMG = "assets/mockBall.png";
    private static final int INITIAL_SPEED = 150;

    public PuckStrategy(CollisionStrategy toBeDecorated,
                 danogl.gui.ImageReader imageReader,
                 danogl.gui.SoundReader soundReader){
        super(toBeDecorated); // todo check that this is what they want
        this.soundReader = soundReader;
        this.imageReader = imageReader;
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
            makePucks(thisObject,otherObject);
    }

    /**
     * makes the pucks for the puck strategy on collision
     * @param thisObject the current brick with the puck strategy
     * @param otherObject the ball or puck on collision
     */
    private void makePucks(GameObject thisObject, GameObject otherObject) {
        Random random = new Random();
        for(int i = 0; i < NUM_OF_BALLS; i++){
            Vector2 puckDim = new Vector2(thisObject.getDimensions().x()/3,thisObject.getDimensions().x()/3);
            Vector2 where = thisObject.getCenter().add(new Vector2(-1*puckDim.x()/2,0));
            Puck puck = new Puck(
                    where,
                    puckDim,
                    imageReader.readImage(PUCK_IMG,true),
                    soundReader.readSound(BrickerGameManager.SOUND_FILE));
            puck.setVelocity(new Vector2(INITIAL_SPEED*INITIAL_DIRECTIONS[random.nextInt(3)],INITIAL_SPEED));
            this.getGameObjectCollection().addGameObject(puck); // todo fix
        }
    }
}

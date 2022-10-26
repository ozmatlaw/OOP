package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.ChangeSpeedStrategy;

/**
 * makes the different status types for the game
 */
public class StatusFactory {
    private final Renderable slowImage;
    private final Renderable fastImage;
    private final GameObjectCollection gameObjectCollection;
    private final WindowController windowController;
    public static final String QUICKEN = "quicken";
    public static final String SLOW = "slow";
    private static final String SLOW_IMG = "assets/slow.png";
    private static final String FAST_IMG = "assets/quicken.png";
    private static final Vector2 IMG_DIMENSIONS = new Vector2(70,20);

    public StatusFactory(ImageReader imageReader,
                                GameObjectCollection gameObjectCollection,
                                WindowController windowController) {
        this.gameObjectCollection = gameObjectCollection;
        this.windowController = windowController;
        this.fastImage = imageReader.readImage(FAST_IMG,true);
        this.slowImage = imageReader.readImage(SLOW_IMG,true);

    }

    /**
     * gets the relevant status based on the current game speed
     * @param statusObject current status object
     * @param typeCase string indiacting the case needed
     * @return the relevant speed set
     */
    public Status getStatus(GameObject statusObject, String typeCase){
        switch (typeCase){
            case QUICKEN:
                return new SpeedSet(statusObject.getCenter().add(new Vector2(-1*IMG_DIMENSIONS.x()/2, 0)),
                        IMG_DIMENSIONS,
                        fastImage,
                        gameObjectCollection,
                        windowController,
                        ChangeSpeedStrategy.FAST);
            case SLOW:
                return new SpeedSet(statusObject.getCenter().add(new Vector2(-1*IMG_DIMENSIONS.x()/2, 0)),
                        IMG_DIMENSIONS,
                        slowImage,
                        gameObjectCollection,
                        windowController,
                        ChangeSpeedStrategy.SLOW);

            default:
                return null;
        }
    }

}

package src.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * sets the speed for the speed change strategy
 */
public class SpeedSet extends Status{
    private final WindowController windowController;
    private final float speed;

    public SpeedSet(Vector2 topLeftCorner,
                    Vector2 dimensions,
                    Renderable renderable,
                    GameObjectCollection gameObjectCollection,
                    WindowController windowController,
                    float speed) {
        super(topLeftCorner, dimensions, renderable, gameObjectCollection);
        this.windowController = windowController;
        this.speed = speed;
    }

    /**
     * overrides the updateStatus for the game
     */
    @Override
    public void updateStatus() {
        super.updateStatus();
        windowController.setTimeScale(speed);
    }
}

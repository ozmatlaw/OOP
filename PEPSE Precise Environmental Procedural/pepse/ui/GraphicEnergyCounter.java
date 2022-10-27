package pepse.ui;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * GraphicEnergyCounter used to display the amount of energy left
 */
public class GraphicEnergyCounter extends GameObject {

    private final GameObjectCollection gameObject;
    private final Counter energyCounter;
    private static final String TEXT = "Energy Level: %f";

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public GraphicEnergyCounter(Counter energyCounter,
                                Vector2 topLeftCorner,
                                Vector2 dimensions,
                                GameObjectCollection gameObject) {
        super(topLeftCorner, dimensions, null);
        this.gameObject = gameObject;
        this.energyCounter = energyCounter;
        setText(energyCounter);
        this.setCoordinateSpace(CoordinateSpace.WORLD_COORDINATES);
    }

    /**
     * sets the text display
     * @param energyCounter The current count of lives
     */
    private void setText(Counter energyCounter) {
        String counterString = String.format(TEXT, (float)(energyCounter.value()) / 2);
        int indexOfDecimalPoint = counterString.indexOf(".");
        counterString = counterString.substring(0, indexOfDecimalPoint + 2);
        TextRenderable textRenderable = new TextRenderable(
                counterString,
                Font.DIALOG, false, true);
        textRenderable.setColor(Color.BLACK);
        renderer().setRenderable(textRenderable);
    }

    /**
     *creates an energy counter
     * @param trackingEnergyCounter the current counter for the energy state
     * @param winControl the windowController
     * @param dimensions the window dimensions
     * @param gameObjects the game object collections
     * @return the graphic counter of type counter
     */
    public static GraphicEnergyCounter create(Counter trackingEnergyCounter, WindowController winControl, Vector2 dimensions, GameObjectCollection gameObjects) {
        GraphicEnergyCounter graphicEnergyCounter = new GraphicEnergyCounter(
                trackingEnergyCounter,
                new Vector2(0,winControl.getWindowDimensions().y() - dimensions.y()),
                dimensions,
                gameObjects);
        gameObjects.addGameObject(graphicEnergyCounter, Layer.UI);
        return graphicEnergyCounter;
    }


    /**
     * updates the current status of the display
     * @param deltaTime time difference between each update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setText(energyCounter);
    }
}

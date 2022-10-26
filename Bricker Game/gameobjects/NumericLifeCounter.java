package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * shows the number of lives left on the screen
 */
public class NumericLifeCounter extends GameObject {

    private final GameObjectCollection gameObject;
    private final Counter livesCounter;
    private static final String TEXT = "LIVES: %d";
    private GameObject textDisplay;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public NumericLifeCounter(Counter livesCounter,
                              Vector2 topLeftCorner,
                              Vector2 dimensions,
                              GameObjectCollection gameObject) {
        super(topLeftCorner, dimensions, null);
        this.gameObject = gameObject;
        this.livesCounter = livesCounter;

        setText(livesCounter, topLeftCorner, dimensions, gameObject);

    }

    /**
     * sets the text display
     * @param livesCounter The current count of lives
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameObject    GameObject reference to alter the display
     */
    private void setText(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObject) {
        TextRenderable textRenderable = new TextRenderable(String.format(TEXT, livesCounter.value()),
                Font.DIALOG, false, true);
        textRenderable.setColor(Color.RED);
        textDisplay = new GameObject(topLeftCorner, dimensions, textRenderable);
        textDisplay.setCenter(topLeftCorner);
        gameObject.addGameObject(textDisplay, Layer.BACKGROUND);
    }


    /**
     * updates the current status of the display
     * @param deltaTime time difference between each update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        gameObject.removeGameObject(textDisplay, Layer.BACKGROUND);
        setText(livesCounter, this.getTopLeftCorner(), this.getDimensions(), gameObject);
    }
}

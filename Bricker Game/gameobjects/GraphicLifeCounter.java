package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {

    private static int initialLives;
    private final GameObjectCollection gameObject;
    private final Counter counter;
    private final GameObject[] hearts;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param counter       The current count of lives
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObject    GameObject reference to alter the display
     * @param numOfLives    Given initial number of lives
     */
    public GraphicLifeCounter(Vector2 topLeftCorner,
                              Vector2 dimensions,
                              Counter counter,
                              Renderable renderable,
                              GameObjectCollection gameObject,
                              int numOfLives) {
        super(topLeftCorner, dimensions, renderable);
        initialLives = numOfLives;
        this.gameObject = gameObject;
        this.counter = counter;
        this.hearts = new GameObject[initialLives];
        displayHeartsInitial(topLeftCorner, dimensions, renderable, gameObject);
    }

    /**
     * displays the initial hearts
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *      *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObject    GameObject reference to alter the display
     */
    private void displayHeartsInitial(Vector2 topLeftCorner,
                                      Vector2 dimensions,
                                      Renderable renderable,
                                      GameObjectCollection gameObject) {
        for(int i = 0; i < initialLives; i++){
            Vector2 placement = new Vector2(topLeftCorner.x()+(dimensions.x()*i), topLeftCorner.y());
            hearts[i] = new GameObject(placement, dimensions, renderable);
            gameObject.addGameObject(hearts[i],Layer.BACKGROUND);
        }
    }

    /**
     * updates the current status of the display
     * @param deltaTime time difference between each update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(counter.value() < initialLives){
            gameObject.removeGameObject(hearts[counter.value()], Layer.BACKGROUND);
        }

    }
}

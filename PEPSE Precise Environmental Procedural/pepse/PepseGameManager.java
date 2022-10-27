package pepse;

import danogl.*;
import danogl.collisions.*;
import danogl.gui.rendering.*;
import danogl.gui.*;
import danogl.util.Counter;
import danogl.util.FPSCounter;
import danogl.util.Vector2;
import pepse.ui.GraphicEnergyCounter;
import pepse.world.*;
import pepse.world.daynight.*;
import pepse.world.trees.Tree;
import pepse.world.trees.drinks.DrinkFactory;
import java.time.LocalDateTime;
import java.awt.Color;
import java.util.HashSet;

public class PepseGameManager extends GameManager {
    private static final int NIGHT_CYCLE = 30;
    private static final int SUN_CYCLE_LEN = 30;
    private static final int SUN_HALO_LAYER = Layer.DEFAULT-1;
    private static final int SUN_LAYER = Layer.BACKGROUND+1;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int TERRAIN_LAYER = Layer.DEFAULT;

    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float BLOCK_SIZE = 30;
    private static final Vector2 COUNTER_DIM = new Vector2(20,20);
    private static final float DELETION_FACTOR = 3;
    private static final int TARGET_FRAMERATE = 60;
    private static final int AVATAR_LAYER = 1;
    private static final int DRINK_LAYER = -1;
    private static final int LEAF_LAYER_0 = -9;
    private static final int LEAF_LAYER_1 = -10;
    private static final int TRUNK_LAYER = -15;
    private static final int FPS_COUNTER_SIZE = 40;
    private static final float ENERGY_COUNTER_DISTANCE_FROM_CENTER = 0.45f;
    private static final float BUFFER_SIZE = 0.55f;
    private static final float BUFFER = 100;
    private static final float TREE_SPAWNER_PLACE = 10000;
    private static final String SUN_HALO_TAG = "sunHalo";

    private HashSet<Tree> trees;
    private GameObject sky;
    private GameObject night;
    private Terrain terrain;
    private GameObject sun;
    private GameObject sunHalo;
    private Avatar avatar;
    private WindowController winControl;
    private ImageReader imageReader;
    private float minRange;
    private float maxRange;
    private GraphicEnergyCounter graphicEnergyCounter;
    private DrinkFactory factory;
    private int seed;
    private Tree treeSpawner;


    public static void main(String[] args) { new PepseGameManager().run(); }

    /**
     * initializes the game
     * @param imageReader reads the images for the GUI
     * @param soundReader reads the sound for the GUI
     * @param inputListener reads the input from the user
     * @param windowController controls all the window elements
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.seed =LocalDateTime.now().hashCode();
        winControl = windowController;
        trees = new HashSet<>();
        windowController.setTargetFramerate(TARGET_FRAMERATE);
//        gameObjects().addGameObject(FPSCounter.create(Vector2.ZERO, 40, Color.BLACK), Layer.UI);
        this.sky = Sky.create(gameObjects(), windowController.getWindowDimensions(),SKY_LAYER);
        this.sun = Sun.create(gameObjects(), SUN_LAYER, windowController.getWindowDimensions(), SUN_CYCLE_LEN);
        this.sunHalo = SunHalo.create(gameObjects(), SUN_HALO_LAYER, this.sun, SUN_HALO_COLOR);
        this.night = Night.create(gameObjects(), NIGHT_LAYER, windowController.getWindowDimensions(), NIGHT_CYCLE);
        this.terrain = new Terrain(
                gameObjects(),
                TERRAIN_LAYER,
                windowController.getWindowDimensions(),
                seed);
        trees = new HashSet<Tree>();
        treeSpawner = new Tree(gameObjects(), Vector2.LEFT.mult(TREE_SPAWNER_PLACE), 3, LEAF_LAYER_1, seed, terrain::groundHeightAt, trees, windowController);
        treeSpawner.createInRange(0, (int)windowController.getWindowDimensions().x());

        this.imageReader = imageReader;
        Counter trackingEnergyCounter = new Counter();
        this.avatar = Avatar.create(gameObjects(),
                AVATAR_LAYER,
                new Vector2(
                        windowController.getWindowDimensions().x() / 2,
                        terrain.groundHeightAt((int)(windowController.getWindowDimensions().x() * 0.5))),
                inputListener,
                imageReader);
        createEnergyCounter(trackingEnergyCounter);
        this.terrain.createInRange(0, (int)windowController.getWindowDimensions().x());
        this.factory = new DrinkFactory(imageReader, trees, avatar);
        treeSpawner.setFactory(factory);
        setRange(windowController);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        updateCollisionLogic();
    }

    private void createEnergyCounter(Counter trackingEnergyCounter) {
        this.avatar.setEnergyCounter(trackingEnergyCounter);
        this.graphicEnergyCounter = GraphicEnergyCounter.create(trackingEnergyCounter, winControl, COUNTER_DIM, gameObjects());
    }

    /**
     * sets the range for the min max range for object deletion logic
     * @param windowController
     */
    private void setRange(WindowController windowController) {
        minRange = 0;
        maxRange = windowController.getWindowDimensions().x();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTerrainGeneration();
        updateDrinkFactories();
        graphicEnergyCounter.setCenter(avatar.getCenter().add(winControl.getWindowDimensions().mult(ENERGY_COUNTER_DISTANCE_FROM_CENTER).flipped(Vector2.LEFT)));
        delOutOfBounds();
    }

    /**
     * updates the drink factories
     */
    private void updateDrinkFactories() {
        for (Tree tree : trees)
        {
            tree.setFactory(factory);
        }
    }

    /**
     * updates the terrain generation
     */
    private void updateTerrainGeneration() {
        if (getMinRange() < minRange)
        {
            terrain.createInRange((int)getMinRange(), (int)minRange);
            treeSpawner.createInRange((int)getMinRange(), (int)minRange);
        }
        if (getMaxRange() > maxRange)
        {
            terrain.createInRange((int)maxRange, (int)getMaxRange());
            treeSpawner.createInRange((int)maxRange, (int)getMaxRange());
        }
        minRange = getMinRange();
        maxRange = getMaxRange();
    }

    /**
     * gets the max range for terrain generation
     * @return float the max val for generation
     */
    private float getMaxRange() {
        return BLOCK_SIZE * ((int) ((avatar.getCenter().x() + winControl.getWindowDimensions().x() * BUFFER_SIZE) / BLOCK_SIZE));
    }

    /**
     * gets the min range for terrain generation
     * @return float the minimum val for generation
     */
    private float getMinRange() {
        return BLOCK_SIZE * ((int) ((avatar.getCenter().x() - winControl.getWindowDimensions().x() * BUFFER_SIZE) / BLOCK_SIZE));
    }

    /**
     * updates the collision logic for gameObjects
     */
    private void updateCollisionLogic() {
        gameObjects().layers().shouldLayersCollide(DRINK_LAYER, AVATAR_LAYER, true);
        gameObjects().layers().shouldLayersCollide(TERRAIN_LAYER, AVATAR_LAYER, true);
        gameObjects().layers().shouldLayersCollide(TRUNK_LAYER, AVATAR_LAYER, true);
        gameObjects().layers().shouldLayersCollide(TERRAIN_LAYER, LEAF_LAYER_0, true);
        gameObjects().layers().shouldLayersCollide(TERRAIN_LAYER, LEAF_LAYER_1, true);
    }

    /**
     * removes all the gameObjects that are out of bounds
     */
    private void delOutOfBounds(){
        for (GameObject gameObject : gameObjects()) {
            if (
                    (Float.isNaN(gameObject.getTopLeftCorner().x()) ||
                            Float.isNaN(gameObject.getTopLeftCorner().y()) ||
                            gameObject.getTopLeftCorner().x() < minRange - BUFFER ||
                            gameObject.getTopLeftCorner().x() > maxRange + BUFFER ||
                            gameObject.getTopLeftCorner().y() > winControl.getWindowDimensions().y() * DELETION_FACTOR) &&
                            !gameObject.getTag().equals(SUN_HALO_TAG)
            ) {
                gameObjects().removeGameObject(gameObject, DRINK_LAYER);
                gameObjects().removeGameObject(gameObject, TERRAIN_LAYER);
                gameObjects().removeGameObject(gameObject, AVATAR_LAYER);
                gameObjects().removeGameObject(gameObject, LEAF_LAYER_0);
                gameObjects().removeGameObject(gameObject, LEAF_LAYER_1);
                gameObjects().removeGameObject(gameObject, TRUNK_LAYER);
            }
        }
    }
}

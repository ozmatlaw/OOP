package src;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;
import src.brick_strategies.*;


/**
 * game manger
 */
public class BrickerGameManager extends GameManager {
    public static final int BORDER_WIDTH = 20;
    public static final int ROWS_OF_BRICKS = 5;
    public static final int COLS_OF_BRICKS = 8;
    public static final int BRICK_HEIGHT = 15;
    private static final int initNumOfLives = 3;
    public static final int TEXT_DIMS_X = 20;
    public static final int TEXT_DIMS_Y = 20;
    private static final Vector2 HEART_DIMENSIONS = new Vector2(20, 20);
    public static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
    private static final String WIN_STR = "You won";
    private static final String LOSE_STR = "You lost";
    private static final String PLAY_AGAIN_STR = " Play again?";
    private static final String BRICK_TITLE = "Brick";
    private static final Vector2 INITIAL_WINDOW_SIZE = new Vector2(700, 500);
    public static final Vector2 BALL_DIM = new Vector2(20, 20);
    public static final String HEART_IMG = "assets/heart.png";
    public static final String SOUND_FILE = "assets/Bubble5_4.wav";
    public static final String BRICK_IMG = "assets/brick.png";
    public static final String BALL_IMG = "assets/ball.png";
    public static final String PADDLE_IMG = "assets/paddle.png";
    public static final String BACKGROUND_IMG = "assets/DARK_BG2_small.jpeg";
    public static final int BRICK_DIST_FROM_BORDER = (5 + BORDER_WIDTH);
    public static final int MIN_DIST_EDGE_PADDLE = 10;
    private static final int INITIAL_FRAME_RATE = 80;
    private static final int NUMERIC_LIVES_PLACMENT = 25;
    private static final float LIVES_PLACEMENT = 25;
    public Ball ball;
    private final Vector2 windowDimensions;
    private WindowController windowController;
    private Counter livesCounter;
    private Counter brickCounter;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;

    public BrickerGameManager(java.lang.String windowTitle, danogl.util.Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;

    }

    /**
     * initializes the game
     *
     * @param imageReader      function to import the image
     * @param soundReader      the function that imports sound files for the game
     * @param inputListener    function to get input from the user
     * @param windowController the class to set the window graphics
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        livesCounter = new Counter(initNumOfLives);
        int numOfBricks = ROWS_OF_BRICKS * COLS_OF_BRICKS;
        brickCounter = new Counter(numOfBricks);
        windowController.setTargetFramerate(INITIAL_FRAME_RATE);
        makeDisplays();
    }

    /**
     * calls all of the graphic display functions to make the relevant graphics
     */
    private void makeDisplays() {
        makeBall();
        createPaddle(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30)); // todo decide where to put the make paddle function
        makeWalls();
        makeBackGround();
        makeBricks();
        makeLives();
        makeNumericLives();
    }

    /**
     * makes the live counter graphic
     */
    private void makeNumericLives() {
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(livesCounter,
                new Vector2(NUMERIC_LIVES_PLACMENT, windowDimensions.y() - HEART_DIMENSIONS.y() - BORDER_WIDTH),
                new Vector2(TEXT_DIMS_X, TEXT_DIMS_Y),
                gameObjects());
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);
    }

    /**
     * makes the lives graphics
     */
    private void makeLives() {
        Renderable livesWidget = imageReader.readImage(HEART_IMG, true);
        GameObject livesObject = new GraphicLifeCounter(
                new Vector2(LIVES_PLACEMENT, windowDimensions.y() - BORDER_WIDTH),
                HEART_DIMENSIONS,
                livesCounter,
                livesWidget,
                this.gameObjects(),
                initNumOfLives);
        this.gameObjects().addGameObject(livesObject, Layer.BACKGROUND);// todo might not need to be foreground
    }

    /**
     * makes the brick graphics
     */
    private void makeBricks() {
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(this.gameObjects(),
                this,
                this.imageReader,
                this.soundReader,
                this.inputListener,
                this.windowController,
                this.windowDimensions);
        Renderable brickImage = imageReader.readImage(BRICK_IMG, true);
        float brickWidth = (windowDimensions.x() - 2 * BRICK_DIST_FROM_BORDER -
                ((COLS_OF_BRICKS - 1))) / COLS_OF_BRICKS;
        BrickStrategyFactory factory = new BrickStrategyFactory(gameObjects(),
                this,
                imageReader,
                soundReader,
                inputListener,
                windowController,
                windowDimensions);
        for (int col = 0; col < COLS_OF_BRICKS; col++) {
            for (int row = 0; row < ROWS_OF_BRICKS; row++) {
                CollisionStrategy collisionStrategy = factory.getStrategy();
                GameObject brick = new Brick(
                        new Vector2(BORDER_WIDTH + brickWidth * col + 1, BORDER_WIDTH + BRICK_HEIGHT * row + 1),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage, collisionStrategy,
                        brickCounter);
                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * makes the ball graphics
     */
    private void makeBall() {
        Sound ballSound = soundReader.readSound(SOUND_FILE);
        Renderable ballImage = imageReader.readImage(BALL_IMG, true);
        ball = new Ball(Vector2.ZERO, BALL_DIM, ballImage, ballSound);
        ball.resetBall(windowDimensions.mult(0.5F));
        this.gameObjects().addGameObject(ball);
    }


    /**
     * makes the paddle graphic
     */
    private void createPaddle(Vector2 placement) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMG, true);
        GameObject paddle = new Paddle(Vector2.ZERO,
                PADDLE_DIMENSIONS,
                paddleImage,
                inputListener,
                windowDimensions,
                MIN_DIST_EDGE_PADDLE);
        paddle.setCenter(placement);
        this.gameObjects().addGameObject(paddle);
    }

    /**
     * makes the background for the game
     */
    private void makeBackGround() {
        GameObject backGround = new GameObject(Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage(BACKGROUND_IMG, true));
        this.gameObjects().addGameObject(backGround, Layer.BACKGROUND);
        backGround.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * makes the walls for the game
     */
    private void makeWalls() {
        this.gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()),
                null)); // left
        this.gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), null)); // top
        this.gameObjects().addGameObject(new GameObject(new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), null));// right
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkIfGameOver();
        checkOutOfBounds();
    }

    /**
     * checks what objects went out of bounds and gets rid of them
     */
    private void checkOutOfBounds() {
        for (GameObject object : gameObjects()) {
            if (object.getCenter().y() > windowDimensions.y() && object instanceof Puck) {
                gameObjects().removeGameObject(object);
            }
        }
    }

    /**
     * checks if the game is over
     */
    private void checkIfGameOver() {
        String prompt = "";
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            livesCounter.decrement();
            ball.resetBall(windowDimensions.mult(0.5F));
            ;
        }

        if (brickCounter.value() <= 0) {
            prompt = WIN_STR;
        } else if (livesCounter.value() <= 0) {
            prompt = LOSE_STR;
        }
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_STR;
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    public static void main(String[] args) {
        new BrickerGameManager(BRICK_TITLE, INITIAL_WINDOW_SIZE).run();
    }
}

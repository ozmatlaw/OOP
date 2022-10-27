package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * avatar game object for gameplay
 */
public class Avatar extends danogl.GameObject {
    private static final float AVATAR_SIZE = 100;
    private static final float JUMP_SPEED = 250;
    private static final float SPEED = 300;
    private static final float FALL_SPEED = 300;
    private static final float FLY_SPEED = 300;
    private static final float MAX_FALL_SPEED = 200;
    private static final int MAX_ENERGY = 200;
    private static final float DEFAULT_SPEED_BONUS = 1;
    private static final int WALK_ANIMATION_SPEED = 30;
    private static final int COUNTER_DEFAULT = 200;
    private static final float MAX_SPEED_BONUS = 10;
    private final Vector2 dimensions;
    private final UserInputListener inputListener;


    private static Renderable WALK_LEFT_IMG = null;
    private static Renderable WALK_RIGHT_IMG = null;
    private static Renderable STATIC_IMG = null;

    private static final String WALK_LEFT_PATH = "assets/player0.png";
    private static final String WALK_RIGHT_PATH = "assets/player1.png";
    private static final String STATIC_PATH = "assets/playerIdle.png";
    private boolean isWalking;
    private int walkAnimationCounter;
    private boolean isFlying;
    private boolean energyDepleted;
    private Counter energyCounter;
    private boolean redbullActive;
    private float speedBonus;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Renderable renderable,
                  danogl.gui.UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        isWalking = false;
        isFlying = false;
        redbullActive = false;
        energyDepleted = false;
        speedBonus = DEFAULT_SPEED_BONUS;
        walkAnimationCounter = 0;
        this.dimensions = dimensions;
        this.inputListener = inputListener;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(FALL_SPEED);
    }

    /**
     * loads the desired images for the avatar
     * @param imageReader reads the images for the GUI
     */
    private static void loadImg(danogl.gui.ImageReader imageReader) {
        WALK_LEFT_IMG = imageReader.readImage(WALK_LEFT_PATH, true);
        WALK_RIGHT_IMG = imageReader.readImage(WALK_RIGHT_PATH, true);
        STATIC_IMG = imageReader.readImage(STATIC_PATH, true);
    }


    /**
     * creats the avatar for the game
     *
     * @param gameObjects   ameObject collections
     * @param layer         desired layer for the avatar
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param inputListener reads the input from the user
     * @param imageReader   reads the images for the GUI
     * @return the avatar GameObject
     */
    public static Avatar create(danogl.collisions.GameObjectCollection gameObjects,
                                int layer,
                                danogl.util.Vector2 topLeftCorner,
                                danogl.gui.UserInputListener inputListener,
                                danogl.gui.ImageReader imageReader) {
        loadImg(imageReader);
        Avatar avatar = new Avatar(
                topLeftCorner.add(Vector2.UP.mult(2 * AVATAR_SIZE)), new Vector2(AVATAR_SIZE, AVATAR_SIZE),
                STATIC_IMG, inputListener);
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag("avatar");
        return avatar;
    }

    /**
     * sets the speed bonus for the sevenUp powerUp
     */
    public void sevenUpSpeed(float newSpeedBonus){
        if(speedBonus<MAX_SPEED_BONUS) {
            speedBonus *= newSpeedBonus;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        walkAnimationCounter++;
        walk();
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && !energyDepleted) {
            fly();
        }
        if (!inputListener.isKeyPressed(KeyEvent.VK_SPACE) && !inputListener.isKeyPressed(KeyEvent.VK_SHIFT))
        {
            isFlying = false;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            jump();
        }
        if (getVelocity().y() > MAX_FALL_SPEED)
        {
            setVelocity(new Vector2(getVelocity().x(), MAX_FALL_SPEED));
        }
        manageEnergy();
    }

    /**
     * manages the energy regeneration and depletion
     */
    private void manageEnergy() {
        if (energyDepleted && getVelocity().y() == 0) {
            energyDepleted = false;
        }
        else if (isFlying && !redbullActive) {
            energyCounter.decrement();
            if (energyCounter.value() <= 0) {
                energyDepleted = true;
                isFlying = false;
            }
        } else if (!energyDepleted && energyCounter.value() < MAX_ENERGY) {
            energyCounter.increment();
        }
    }


    /**
     * makes the avatar walk and sets the desired animation
     */
    private void walk() {
        if (isWalking) {
            walkAnimation();
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            moveLeft();
        } else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            moveRight();
        } else {
            dontMove();
        }
    }

    /**
     * gives to you or takes from you wings
     * @param value whether to give or take wings
     */
    public void setRedbullActive(boolean value){
        redbullActive = value;
    }

    /**
     * sets the desired animation for the current avatar status
     */
    private void walkAnimation() {
        if (walkAnimationCounter % WALK_ANIMATION_SPEED < WALK_ANIMATION_SPEED / 2) {
            this.renderer().setRenderable(WALK_RIGHT_IMG);
        } else {
            this.renderer().setRenderable(WALK_LEFT_IMG);
        }
    }


    /**
     * transitions the avatar into the moving flying form
     */
    private void fly() {
        isFlying = true;
        this.transform().setVelocityY(-FLY_SPEED);
    }

    /**
     * transitions the avatar into the moving left form
     */
    private void moveLeft() {
        this.transform().setVelocityX(-SPEED * speedBonus);
        this.renderer().setIsFlippedHorizontally(true);
        isWalking = true;
    }

    /**
     * transitions the avatar into the moving right form
     */
    private void moveRight() {

        this.transform().setVelocityX(SPEED * speedBonus);
        this.renderer().setIsFlippedHorizontally(false);
        isWalking = true;
    }

    /**
     * keeps the avatar in the standing form
     */
    private void dontMove() {
        this.transform().setVelocityX(0);
        this.renderer().setRenderable(STATIC_IMG);
        isWalking = false;
    }

    /**
     * makes the avatar jump
     */
    public void jump() {
        if (this.getVelocity() != Vector2.ZERO) {
            this.transform().setVelocity(Vector2.UP.mult(JUMP_SPEED));
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.setVelocity(Vector2.ZERO);
    }

    /**
     * sets the energy counter of the player
     * @param trackingEnergyCounter counter to keep track of player's energy
     */
    public void setEnergyCounter(Counter trackingEnergyCounter) {
        energyCounter = trackingEnergyCounter;
        energyCounter.reset();
        energyCounter.increaseBy(COUNTER_DEFAULT);
    }
}

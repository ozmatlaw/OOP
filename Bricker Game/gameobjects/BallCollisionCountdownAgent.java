package src.gameobjects;

import danogl.GameObject;

import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * counts how many collisions the ball had since initiation
 */
public class BallCollisionCountdownAgent extends GameObject {

    private final int countDownValue;
    private final ChangeCameraStrategy owner;
    private final Ball ball;
    private final int startCount;

    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue){
        super(Vector2.ZERO,Vector2.ZERO,null);
//        super(ball.getTopLeftCorner(),ball.getDimensions(),ball.renderer().getRenderable());
        this.ball = ball;
        this.owner = owner;
        this.countDownValue = countDownValue;
        this.startCount = ball.getCount();

    }

    /**
     * updates the game
     * @param deltaTime  frequency of update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(ball.getCount() - startCount > countDownValue){
            owner.turnOffCameraChange();
        }
    }
}

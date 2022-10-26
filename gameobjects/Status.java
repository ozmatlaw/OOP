package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Status extends GameObject {
    public static final Vector2 Speed = new Vector2(0,150);
    private final GameObjectCollection gameObjectCollection;

    public Status(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjectCollection = gameObjectCollection;
        setSpeed();
    }

    private void setSpeed() {
        setVelocity(Speed);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        updateStatus();
    }

    @Override
    public boolean shouldCollideWith(GameObject otherObject) {
        return otherObject instanceof Paddle;
    }

    public void updateStatus() {
        gameObjectCollection.removeGameObject(this);
    }

}


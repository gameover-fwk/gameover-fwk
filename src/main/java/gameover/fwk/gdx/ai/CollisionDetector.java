package gameover.fwk.gdx.ai;

import com.badlogic.gdx.math.Rectangle;

/**
 * Contract for a class which checks if a collision happens.
 */
public interface CollisionDetector {
    boolean checkCollision(Rectangle r, boolean onlyBlocking);
    boolean checkCollision(int tileX, int tileY, boolean onlyBlocking);
    boolean isDirect(Rectangle area, float x, float y, float targetX,
        float targetY, boolean onlyBlocking);
}

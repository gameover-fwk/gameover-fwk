package gameover.fwk.gdx.ai;

/**
 * Strategy to cimpute H in A* algorithm
 */
public interface HComputeStrategy {
    int h(int x, int y, int px, int py, CollisionDetector collisionDetector);
}

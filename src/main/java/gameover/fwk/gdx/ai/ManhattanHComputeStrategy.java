package gameover.fwk.gdx.ai;

/**
 * Classical Manhattan H computation strategy.
 */
public class ManhattanHComputeStrategy implements HComputeStrategy{

    @Override
    public int h(int x, int y, int px, int py, CollisionDetector collisionDetector) {
        return Math.abs(px-x)*10+Math.abs(py-y)*10;
    }
}

package gameover.fwk.gdx.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameover.fwk.utils.Vector2Pool;

/**
 * This class is an attempt to make a smart path finding. The algorithm first check
 * if the target can be reached directly from a direct path taking care of the area.
 * If it is not possible then an A* algorithm is apply with a smooth path finding
 */
public class SmartPathFinding {
    public AStar aStar;

    public SmartPathFinding(HComputeStrategy hComputeStrategy){
        aStar = new AStar(hComputeStrategy);
    }


    public Array<Vector2> findSmoothPath(Rectangle area, float x, float y, float targetX,
                                         float targetY, boolean findNearestPoint, CollisionDetector collisionDetector) {

        if(collisionDetector.isDirect(area, x, y, targetX, targetY, false)){
            Array<Vector2> ret = Vector2Pool.obtainAsArray(1);
            ret.get(0).set(targetX, targetY);
            return ret;
        }
        return aStar.findSmoothPath(area, x, y, targetX, targetY, findNearestPoint, collisionDetector);
    }
}

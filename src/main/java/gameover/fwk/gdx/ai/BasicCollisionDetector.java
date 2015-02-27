package gameover.fwk.gdx.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameover.fwk.utils.GeometryUtils;
import gameover.fwk.utils.Vector2Pool;

/**
 * Basic class for collision detection.
 */
public abstract class BasicCollisionDetector implements CollisionDetector{

  @Override
  public boolean isDirect(Rectangle area, float x, float y, float targetX,
      float targetY, boolean onlyWalls) {
    boolean isGoingTop = Math.signum(targetY-y)>=0f;
    boolean isGoingRight = Math.signum(targetX-x)>=0f;
    float diffX1 = isGoingRight ? area.width + area.x : area.x;
    float diffY1 = isGoingTop ? area.y : area.height + area.y;
    Array<Vector2> intersectionPoints = GeometryUtils.computeTiledIntersectionPoints(x + diffX1,
        y + diffY1, targetX + diffX1, targetY + diffY1);
    float diffX2 = isGoingRight ? area.x : area.width + area.x;
    float diffY2 = isGoingTop ? area.height + area.y : area.y;
    intersectionPoints.addAll(GeometryUtils.computeTiledIntersectionPoints(x+ diffX2,
        y+ diffY2, targetX+diffX2, targetY+diffY2));
    try {
      return !checkCollisions(intersectionPoints, onlyWalls);
    } finally {
      Vector2Pool.freeAll(intersectionPoints);
    }
  }

  private boolean checkCollisions(Array<Vector2> intersections, boolean onlyWalls) {
    for(Vector2 intersection : intersections){
      if(Math.floor(intersection.x)==intersection.x){
        if(checkCollision((int)intersection.x, (int)Math.floor(intersection.y), onlyWalls)
            || checkCollision((int)intersection.x-1, (int)Math.floor(intersection.y), onlyWalls)){
          return true;
        }
      }
      if(Math.floor(intersection.y)==intersection.y){
        if(checkCollision((int)Math.floor(intersection.x), (int)intersection.y, onlyWalls)
            || checkCollision((int)Math.floor(intersection.x), (int)intersection.y-1, onlyWalls)){
          return true;
        }
      }
    }
    return false;
  }

}

package gameover.fwk.gdx.ai;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameover.fwk.utils.GridPoint2Pool;
import gameover.fwk.utils.Vector2Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * A* implementation.
 */
public class AStar {

    private final HComputeStrategy hComputeStrategy;
    List<Point> opened = new ArrayList<Point>();
    List<Point> closed = new ArrayList<Point>();
    Point nearest;

    public AStar(HComputeStrategy hComputeStrategy){
        this.hComputeStrategy = hComputeStrategy;
    }

    /**
     * This is the classical method to call to compute an a* path.
     * It will return tiles by tiles the path to use to reach the target. The vector
     * Notice that the path returned is really a tileset path. If you need a smooth
     * path to reach target, prefer method #findSmoothPath which is compute a smooth path from
     * the path computed by this method.
     * @param x
     * @param y
     * @param tx
     * @param ty
     * @param findNearestPoint
     * @param collisionDetector
     * @return an array of tiles indices to reach path
     */
    public Array<GridPoint2> findPath(float x, float y, float tx, float ty, boolean findNearestPoint, CollisionDetector collisionDetector){
        Array<GridPoint2> ret = new Array<GridPoint2>();
        int ix = MathUtils.floor(x);
        int iy = MathUtils.floor(y);
        int itx = MathUtils.floor(tx);
        int ity = MathUtils.floor(ty);
        if(ix==itx && iy==ity){
            return ret;
        }
        opened.clear();
        closed.clear();
        Point p = addToOpenedIfElligible(ix, iy, ix, iy, itx, ity, collisionDetector, null, hComputeStrategy);
        if(p!=null){
            p = processPath(itx, ity, collisionDetector, p, hComputeStrategy);
            if(p!=null){
                return computeFinalPath(ix, iy, p);
            }
        }
        if(findNearestPoint && nearest!=null) {
            //System.out.println("nearest: "+nearest);
            return computeFinalPath(ix, iy, nearest);
        }
        return null;
    }

    public Array<Vector2> findSmoothPath(Rectangle area, float x, float y, float tx, float ty,
        boolean findNearestPoint, CollisionDetector collisionDetector) {
        Array<GridPoint2> path = findPath(x, y, tx, ty, findNearestPoint, collisionDetector);
        Array<Vector2> smoothPath = new Array<Vector2>();
        computePointForSmoothPathAuxRecursively(area, path, 0, MathUtils.floor(x), MathUtils.floor(y), smoothPath);
        if(path.size>0){
            GridPoint2 last = path.get(path.size - 1);
            if(MathUtils.floor(tx)==last.x && MathUtils.floor(ty)==last.y)
                smoothPath.add(Vector2Pool.obtain(tx, ty));
        }
        return smoothPath;
    }

    private void computePointForSmoothPathAuxRecursively(Rectangle area, Array<GridPoint2> path, int i, int previousTileX, int previousTileY,
        Array<Vector2> smoothPath) {
        if(i<path.size-1){
            GridPoint2 tile = path.get(i);
            GridPoint2 nextTile = path.get(i+1);
            boolean fromDiag = tile.x!=previousTileX && tile.y!=previousTileY;
            boolean fromLeft = tile.x>previousTileX;
            boolean toLeft = tile.x>nextTile.x;
            boolean fromBottom = tile.y>previousTileY;
            boolean toBottom = tile.y>nextTile.y;
            boolean fromRight = tile.x<previousTileX;
            boolean toRight = tile.x<nextTile.x;
            boolean fromEqualsX = tile.x == previousTileX;
            boolean toEqualsX = tile.x == nextTile.x;
            boolean fromTop = tile.y<previousTileY;
            boolean toTop = tile.y<nextTile.y;

            if(fromDiag){
                if(toEqualsX){
                    if(toTop){
                        smoothPath.add(fromLeft ? createBottomLeftPoint(area, tile) : createBottomRightPoint(area, tile));
                    } else {
                        smoothPath.add(fromLeft ? createTopLeftPoint(area, tile) : createTopRightPoint(area, tile));
                    }
                } else {
                    if(toRight){
                        smoothPath.add(fromBottom ? createBottomRightPoint(area, tile) : createTopRightPoint(area, tile));
                    } else {
                        smoothPath.add(fromBottom ? createBottomLeftPoint(area, tile) : createTopLeftPoint(area, tile));
                    }
                }
            } else {
                if(fromEqualsX){
                    if(fromTop){
                        smoothPath.add(toLeft ? createTopLeftPoint(area, tile) : createTopRightPoint(area, tile));
                    } else {
                        smoothPath.add(toLeft ? createBottomLeftPoint(area, tile) : createBottomRightPoint(area, tile));
                    }
                } else {
                    if(fromRight){
                        smoothPath.add(toBottom ? createBottomRightPoint(area, tile) : createTopRightPoint(area, tile));
                    } else {
                        smoothPath.add(toBottom ? createBottomLeftPoint(area, tile) : createTopLeftPoint(area, tile));
                    }
                }
            }
            computePointForSmoothPathAuxRecursively(area, path, i + 1, tile.x, tile.y, smoothPath);
        }
    }

    private Vector2 createBottomLeftPoint(Rectangle area, GridPoint2 tile){
        return Vector2Pool.obtain((float)tile.x-area.x, (float)tile.y-area.y);
    }

    private Vector2 createBottomRightPoint(Rectangle area, GridPoint2 tile){
        return Vector2Pool.obtain((float)tile.x+1f-(area.width+area.x), (float)tile.y-area.y);
    }

    private Vector2 createTopLeftPoint(Rectangle area, GridPoint2 tile){
        return Vector2Pool.obtain((float)tile.x-area.x, (float)tile.y+1f-(area.height+area.y));
    }

    private Vector2 createTopRightPoint(Rectangle area, GridPoint2 tile){
        return Vector2Pool.obtain((float)tile.x+1f-(area.width+area.x), (float)tile.y+1f-(area.height+area.y));
    }

    private Array<GridPoint2> computeFinalPath(int ix, int iy, Point p) {
        Array<GridPoint2> ret = new Array<GridPoint2>();
        for(int i=0, max = p.index; i<max; i++){
            ret.insert(0, GridPoint2Pool.obtain(p.x, p.y));
            p = p.ancestor;
        }
        ret.insert(0, GridPoint2Pool.obtain(ix, iy));
        for(int i=ret.size-2; i>0; i--){
            if(
                    (ret.get(i).x==ret.get(i-1).x && ret.get(i).x==ret.get(i+1).x)
                    ||
                    (ret.get(i).y==ret.get(i-1).y && ret.get(i).y==ret.get(i+1).y)
                    ||
                    (ret.get(i).x==ret.get(i-1).x+1 && ret.get(i).x+1==ret.get(i+1).x && ret.get(i).y==ret.get(i-1).y+1 && ret.get(i).y+1==ret.get(i+1).y)
                    ||
                    (ret.get(i).x==ret.get(i-1).x+1 && ret.get(i).x+1==ret.get(i+1).x && ret.get(i).y==ret.get(i-1).y-1 && ret.get(i).y-1==ret.get(i+1).y)
                    ||
                    (ret.get(i).x==ret.get(i-1).x-1 && ret.get(i).x-1==ret.get(i+1).x && ret.get(i).y==ret.get(i-1).y+1 && ret.get(i).y+1==ret.get(i+1).y)
                    ||
                    (ret.get(i).x==ret.get(i-1).x-1 && ret.get(i).x-1==ret.get(i+1).x && ret.get(i).y==ret.get(i-1).y-1 && ret.get(i).y-1==ret.get(i+1).y)
            )
                ret.removeIndex(i);
        }
        ret.removeIndex(0);
        return ret;
    }

    public boolean contains(int x, int y, List<Point> points){
        for(Point p : points){
            if(p.is(x, y))
                return true;
        }
        return false;
    }

    public Point find(int x, int y, List<Point> points){
        for(Point p : points){
            if(p.is(x, y))
                return p;
        }
        return null;
    }

    Point processPath(int tx, int ty, CollisionDetector collisionDetector,
                      Point ancestor, HComputeStrategy hComputeStrategy){
        int x = ancestor.x;
        int y = ancestor.y;
        //addToOpenedIfElligible(x-1, y-1, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        addToOpenedIfElligible(x-1, y, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        //addToOpenedIfElligible(x-1, y+1, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        addToOpenedIfElligible(x, y-1, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        addToOpenedIfElligible(x, y+1, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        //addToOpenedIfElligible(x+1, y-1, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        addToOpenedIfElligible(x+1, y, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        //addToOpenedIfElligible(x+1, y+1, x, y, tx, ty, collisionDetector, ancestor, hComputeStrategy);
        if(ancestor.is(tx, ty)){
            return ancestor;
        }
        opened.remove(ancestor);
        closed.add(ancestor);
        if(opened.size()>0){
            Point lowest = opened.get(0);
            for(int i=1; i<opened.size(); i++){
                if(opened.get(i).f()<lowest.f()){
                    lowest = opened.get(i);
                }
            }
            return processPath(tx, ty, collisionDetector, lowest, hComputeStrategy);
        } else {
            return null;
        }
    }

    Point addToOpenedIfElligible(int x, int y, int previousX, int previousY, int targetX, int targetY,
                                 CollisionDetector collisionDetector, Point ancestor, HComputeStrategy hComputeStrategy){
        Point p = null;
        if(find(x, y, closed)==null){
            //Rectangle r = RectanglePool.obtain(x+0.5f, y+0.5f, previousX+0.5f, previousY+0.5f);
            try{
                //if(!collisionDetector.checkCollision(r, false)){
                if(!collisionDetector.checkCollision(x, y, false)){
                    Point found = find(x, y, opened);
                    int g = 0;
                    if(ancestor!=null){
                        g = ancestor.x==x||ancestor.y==y?10:14;
                    }
                    int h = hComputeStrategy.h(x, y, targetX, targetY, collisionDetector);
                    if(found!=null){
                        p = found;
                        if(g+h<found.f()){
                            //better path to this point. update info on this point
                            found.g = g;
                            found.h = h;
                            found.ancestor = ancestor;
                        }
                    } else {
                        p = new Point(x, y, g, h, ancestor);
                        opened.add(p);
                    }
                    if(nearest==null || h<nearest.h || (h==nearest.h && g+h<nearest.f())){
                        //System.out.println("Change nearest from "+nearest+" to "+p);
                        nearest = p;
                    }
                }
            } finally {
                //RectanglePool.free(r);
            }
        }
        return p;
    }
}

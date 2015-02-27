package gameover.fwk.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameover.fwk.gdx.ai.Function2DPointComparator;

/**
 * This class group all utilities methods for geometry problematics.
 */
public final class GeometryUtils {
    private GeometryUtils(){

    }

    /**
     * Compute all intersections regarding tiles map ( grid of 1.0f square) between two points.
     * @param x1 first point x coordinate
     * @param y1 first point y coordinate
     * @param x2 second point x coordinate
     * @param y2 second point y coordinate
     * @return an array of Vector2 instances taken from Vector2Pool
     */
    public static Array<Vector2> computeTiledIntersectionPoints(float x1, float y1, float x2, float y2){
        Array<Vector2> ret = new Array<Vector2>();
        float diffX = x2-x1;
        float fX1 = com.badlogic.gdx.math.MathUtils.floor(x1);
        float diffY = y2-y1;
        float fY1 = com.badlogic.gdx.math.MathUtils.floor(y1);
        double alpha = Math.atan2(diffY, diffX);
        double angle = Math.abs(alpha % (Math.PI/2d));
        boolean inverseAxis = alpha>Math.PI/2d || alpha < -Math.PI/2d;
        float alphaTan = (float)Math.tan(angle);
        float xSign = Math.signum(diffX);
        float ySign = Math.signum(diffY);
        Array<Float> xs = new Array<Float>();
        Array<Float> ys = new Array<Float>();
        boolean finished = false;
        while(!finished){
            float x = xSign<0f?fX1-(fX1==x1?1:0)-xs.size:fX1+1+xs.size;
            if((xSign>0 && x>=x2) || (xSign<0 && x<=x2) || xSign==0)
                finished = true;
            else
                xs.add(x);
        }
        finished = false;
        while(!finished){
            float y = ySign<0f?fY1-(fY1==y1?1:0)-ys.size:fY1+1+ys.size;
            if((ySign>0 && y>=y2) || (ySign<0 && y<=y2) || ySign==0)
                finished = true;
            else
                ys.add(y);
        }
        for (float x : xs) {
            if (!inverseAxis)
                ret.add(Vector2Pool.obtain(x, computeYAux(x1, y1, x, ySign, alphaTan)));
            else
                ret.add(Vector2Pool.obtain(x, computeXAux(y1, x1, x, ySign, alphaTan)));
        }
        for (float y : ys) {
            if (!inverseAxis)
                ret.add(Vector2Pool.obtain(computeXAux(x1, y1, y, xSign, alphaTan), y));
            else
                ret.add(Vector2Pool.obtain(computeYAux(y1, x1, y, xSign, alphaTan), y));
        }
        ret.sort(new Function2DPointComparator(xSign, ySign));
        for(int i=ret.size-2; i>=0 && i+1<ret.size; ){
            if(ret.get(i).equals(ret.get(i+1)))
                ret.removeIndex(i);
            else
                i--;
        }
        return ret;
    }

    private static float computeYAux(float x1, float y1, float x2, float ySign, float alphaTan){
        return y1 + ySign * (alphaTan * (Math.abs(x2 - x1)));
    }

    private static float computeXAux(float x1, float y1, float y2, float xSign, float alphaTan){
        return x1 + (alphaTan!=0 ? xSign * (Math.abs(y2 - y1) / alphaTan) : 0f);
    }
}

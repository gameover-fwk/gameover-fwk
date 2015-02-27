package gameover.fwk.utils;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Utilities  for math computation where libgdx miss some methods
 */
public final class MathUtils {
    private MathUtils() {

    }

    /**
     * Compute a radian angle between two points
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return an angle
     */
    public static float computeAngle(float x1, float y1, float x2, float y2){
        return computeAngle(x2-x1, y2-y1);
    }

    /**
     * Compute a radian angle between two points
     * @param dx
     * @param dy
     * @return an angle
     */
    public static float computeAngle(float dx, float dy){
        Vector2 dv = Vector2Pool.obtain(dx, dy);
        try {
            return dv.angle();
        } finally {
            Vector2Pool.free(dv);
        }
    }

    /**
     * Create an arc as a polygon.
     *
     * @param x
     * @param y
     * @param radius
     * @param start
     * @param angle
     * @param segments
     *
     * @return
     */
    public static Polygon createArcAsPolygon(float x, float y, float radius, float start, float angle, int segments) {
        if (segments < 1) throw new IllegalArgumentException("arc need at least 1 segment");
        float theta = (2 * 3.1415926f * (angle / 360.0f)) / segments;
        float cos = com.badlogic.gdx.math.MathUtils.cos(theta);
        float sin = com.badlogic.gdx.math.MathUtils.sin(theta);
        float cx = radius * com.badlogic.gdx.math.MathUtils.cos(start * com.badlogic.gdx.math.MathUtils.degreesToRadians);
        float cy = radius * com.badlogic.gdx.math.MathUtils.sin(start * com.badlogic.gdx.math.MathUtils.degreesToRadians);

        float[] vertices = new float[segments * 2 + 2];
        vertices[vertices.length - 2] = x;
        vertices[vertices.length - 1] = y;
        for (int i = 0; i < segments; i++) {
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            vertices[i * 2] = x + cx;
            vertices[i * 2 + 1] = y + cy;
        }
        return new Polygon(vertices);
    }


    /** Checks whether the given point is in the polygon.
     * @param polygon The polygon vertices
     * @param point The point
     * @return true if the point is in the polygon */
    public static boolean isPointInPolygon (Array<Vector2> polygon, Vector2 point) {
        return isPointInPolygonAux(polygon.iterator(), polygon.peek(), point);
    }

    private static boolean isPointInPolygonAux(Iterator<Vector2> polygon, Vector2 lastVertice, Vector2 point) {
        boolean oddNodes = false;
        for (;polygon.hasNext();) {
            Vector2 vertice = polygon.next();
            if (vertice.y < point.y && lastVertice.y >= point.y || lastVertice.y < point.y
                    && vertice.y >= point.y) {
                if (vertice.x + (point.y - vertice.y) / (lastVertice.y - vertice.y)
                        * (lastVertice.x - vertice.x) < point.x) {
                    oddNodes = !oddNodes;
                }
            }
            lastVertice = vertice;
        }
        return oddNodes;
    }

    /**
     * Create an arc as a list of vertices
     */
    public static Array<Vector2> createArcAsListOfVertices(float x, float y, float radius, float start, float angle, int segments, boolean fromPool) {
        if (segments < 1) throw new IllegalArgumentException("arc need at least 1 segment");
        float theta = (2 * 3.1415926f * (angle / 360.0f)) / segments;
        float cos = com.badlogic.gdx.math.MathUtils.cos(theta);
        float sin = com.badlogic.gdx.math.MathUtils.sin(theta);
        float cx = radius * com.badlogic.gdx.math.MathUtils.cos(start * com.badlogic.gdx.math.MathUtils.degreesToRadians);
        float cy = radius * com.badlogic.gdx.math.MathUtils.sin(start * com.badlogic.gdx.math.MathUtils.degreesToRadians);

        Array<Vector2> ret;
        if (fromPool) {
            ret = Vector2Pool.obtainAsArray(segments + 1);
        } else {
            ret = new Array<Vector2>(false, segments + 1);
            for (int i = 0; i < segments + 1; i++) {
                ret.add(new Vector2());
            }
        }
        for (int i = 0; i < segments; i++) {
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            ret.get(i).set(x + cx, y + cy);
        }
        ret.get(segments).set(x, y);
        return ret;
    }
}

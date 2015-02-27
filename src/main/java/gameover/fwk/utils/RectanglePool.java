package gameover.fwk.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * A pool or Rectangle objects.
 */
public final class RectanglePool {

    private RectanglePool() {
    }

    private static Pool<Rectangle> $pool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    public static Rectangle obtain() {
        return $pool.obtain();
    }

    public static Rectangle obtain(Rectangle r) {
        return $pool.obtain().set(r);
    }

    public static void clear() {
        $pool.clear();
    }

    public static void freeAll(Array<Rectangle> rects) {
        $pool.freeAll(rects);
    }

    public static void free(Rectangle rect) {
        $pool.free(rect);
    }

    public static Rectangle obtain(Vector2 v1, Vector2 v2) {
        return obtain(v1.x, v1.y, v2.x, v2.y);
    }

    public static Rectangle obtain(float x1, float y1, float x2, float y2) {
        Rectangle r = obtain();
        r.x = Math.min(x1, x2);
        r.y = Math.min(y1, y2);
        r.width = Math.abs(x1 - x2);
        r.height = Math.abs(y1 - y2);
        return r;
    }

    public static Array<Rectangle> obtainAsArray(int nb) {
        Array<Rectangle> ret = new Array<Rectangle>(nb);
        for (int i = 0; i < nb; i++) {
            ret.add(obtain());
        }
        return ret;
    }
}


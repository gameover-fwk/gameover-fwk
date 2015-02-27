package gameover.fwk.utils;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * A pool or Rectangle objects.
 */
public final class GridPoint2Pool {

    private GridPoint2Pool() {
    }

    private static Pool<GridPoint2> $pool = new Pool<GridPoint2>() {
        @Override
        protected GridPoint2 newObject() {
            return new GridPoint2();
        }
    };

    public static GridPoint2 obtain() {
        return $pool.obtain();
    }

    public static GridPoint2 obtain(GridPoint2 gp2) {
        return $pool.obtain().set(gp2);
    }

    public static void clear() {
        $pool.clear();
    }

    public static void freeAll(Array<GridPoint2> gp2s) {
        $pool.freeAll(gp2s);
    }

    public static void free(GridPoint2 gp2) {
        $pool.free(gp2);
    }

    public static GridPoint2 obtain(int x, int y) {
        return $pool.obtain().set(x, y);
    }

    public static Array<GridPoint2> obtainAsArray(int nb) {
        Array<GridPoint2> ret = new Array<GridPoint2>(nb);
        for (int i = 0; i < nb; i++) {
            ret.add(obtain());
        }
        return ret;
    }
}


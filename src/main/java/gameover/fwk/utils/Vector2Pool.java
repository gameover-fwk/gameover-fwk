package gameover.fwk.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.List;

/**
 * A pool of Vector2 objects
 */
public final class Vector2Pool {

    private Vector2Pool() {
    }

    private static Pool<Vector2> $pool = new Pool<Vector2>() {
        @Override
        protected Vector2 newObject() {
            return new Vector2();
        }
    };

    public static Vector2 obtain() {
        return $pool.obtain();
    }

    public static Array<Vector2> obtainAsArray(int nb) {
        Array<Vector2> ret = new Array<Vector2>(nb);
        for (int i = 0; i < nb; i++) {
            ret.add(obtain());
        }
        return ret;
    }

    public static void clear() {
        $pool.clear();
    }

    public static void freeAll(Array<Vector2> vectors) {
        $pool.freeAll(vectors);
    }

    public static void freeAll(List<Vector2> vectors) {
        for (int i = 0; i < vectors.size(); i++) {
            $pool.free(vectors.get(i));
        }
    }

    public static void freeAll(Array<Vector2>[] vectors) {
        for (int i = 0; i < vectors.length; i++)
            $pool.freeAll(vectors[i]);
    }

    public static void free(Vector2 vector) {
        $pool.free(vector);
    }

    public static Vector2 obtain(float x, float y) {
        return obtain().set(x, y);
    }
}


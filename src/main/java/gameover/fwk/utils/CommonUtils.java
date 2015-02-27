package gameover.fwk.utils;

/**
 * Common utilities...
 */
public final class CommonUtils {

    private CommonUtils() {
    }

    public static <T> void copyArray(T[][] src, int srcPos, T[][] dest, int destPos, int length) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i + srcPos], 0, dest[i + destPos], 0, src[0].length);
        }
    }

    public static void copyFloatArray(float[][] src, int srcPos, float[][] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i + srcPos], 0, dest[i + destPos], 0, src[0].length);
        }
    }
}

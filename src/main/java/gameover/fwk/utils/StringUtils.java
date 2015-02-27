package gameover.fwk.utils;

/**
 * Utilities for String instance.
 */
public class StringUtils {
  public static String padRight(String s, int n) {
    return String.format("%1$-" + n + "s", s);
  }

  public static String padLeft(String s, int n) {
    return String.format("%1$" + n + "s", s);
  }

}

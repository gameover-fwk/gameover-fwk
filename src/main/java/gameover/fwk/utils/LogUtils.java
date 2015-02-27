package gameover.fwk.utils;

import com.badlogic.gdx.Gdx;

/**
 * Utilities for logging.
 */
public final class LogUtils {

  public enum Type {GUI, GAME, GFX, UTILS}

  public static void info(Type type, String message) {
    Gdx.app.log(type.name(), "["+System.nanoTime()+"] "+message);
  }

  public static void error(Type type, String message) {
    Gdx.app.error(type.name(), "[" + System.nanoTime() + "] " + message);
  }

  public static void error(Type type, String message, Throwable t) {
    Gdx.app.error(type.name(), "[" + System.nanoTime() + "] " + message, t);
  }

  public static void debug(Type type, String message) {
    Gdx.app.debug(type.name(), "[" + System.nanoTime() + "] " + message);
  }
}

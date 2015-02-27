package gameover.fwk.utils;

/**
 * This interface should be used by object that should clean some data before ending.
 */
public interface Disposable {

  /**
   * Clean all objects initialized by this objects.
   */
  void dispose();
}

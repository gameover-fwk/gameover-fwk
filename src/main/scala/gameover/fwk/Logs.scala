package gameover.fwk

import com.badlogic.gdx.Gdx

/**
 *
 * @author olmartin@expedia.com
 * @since 2015-07-01
 */
trait Logs {

  object LogType extends Enumeration {
    val GUI = Value("gui")
    val GAME = Value("game")
    val GFX = Value("gfx")
    val UTILS = Value("utils")
  }

  def logInfo(t: LogType.Value, message: String) {
    Gdx.app.log(t.toString, s"[${System.nanoTime}] $message")
  }

  def logError(t: LogType.Value, message: String) {
    Gdx.app.error(t.toString, s"[${System.nanoTime}] $message")
  }

  def logError(t: LogType.Value, message: String, thr: Throwable) {
    Gdx.app.error(t.toString, s"[${System.nanoTime}] $message", thr)
  }

  def logDebug(t: LogType.Value, message: String) {
    Gdx.app.debug(t.toString, s"[${System.nanoTime}] $message")
  }
}

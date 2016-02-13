package gameover.fwk.logging

import com.badlogic.gdx.Gdx

trait Logs {

  object LogType extends Enumeration {
    val GUI = Value("gui")
    val GAME = Value("game")
    val GFX = Value("gfx")
    val UTILS = Value("utils")
  }

  def logInfo(t: LogType.Value, message: String) {
    if (Gdx.app != null)
      Gdx.app.log(t.toString, s"[${System.nanoTime}] $message")
    else
      println(message)
  }

  def logError(t: LogType.Value, message: String) {
    if (Gdx.app != null)
      Gdx.app.error(t.toString, s"[${System.nanoTime}] $message")
    else
      sys.error(message)
  }

  def logError(t: LogType.Value, message: String, thr: Throwable) {
    if (Gdx.app != null)
      Gdx.app.error(t.toString, s"[${System.nanoTime}] $message", thr)
    else
      sys.error(message)
  }

  def logDebug(t: LogType.Value, message: String) {
    if (Gdx.app != null)
      Gdx.app.debug(t.toString, s"[${System.nanoTime}] $message")
    else
      println(message)
  }
}

package gameover.fwk.logging

import com.badlogic.gdx.Gdx

object Logs {

    fun logInfo(t: LogType, message: String) {
        if (Gdx.app != null)
            Gdx.app.log(t.code, "[${System.nanoTime()}] $message")
        else
            println(message)
    }

    fun logError(t: LogType, message: String) {
        if (Gdx.app != null)
            Gdx.app.error(t.code, "[${System.nanoTime()}] $message")
        else
            System.err.println(message)
    }

    fun logError(t: LogType, message: String, thr: Throwable) {
        if (Gdx.app != null)
            Gdx.app.error(t.code, "[${System.nanoTime()}] $message", thr)
        else {
            System.err.println(message)
            thr.printStackTrace()
        }
    }

    fun logDebug(t: LogType, message: String) {
        if (Gdx.app != null)
            Gdx.app.debug(t.code, "[${System.nanoTime()}] $message")
        else
            println(message)
    }

}

enum class LogType(val code: String) {
    GUI("gui"), GAME("game"), GFX("gfx"), UTILS("utils")
}
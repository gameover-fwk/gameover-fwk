package gameover.fwk.libgdx.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Button

/**
 * This helper class can be used to check if a key was just pressed. the key can be a keyboard key or a
 * {@link com.badlogic.gdx.scenes.scene2d.ui.Button} on a screen.
 */
object KeyHolder {
  def apply(keyCode: Int) {
    new KeyboardKeyHolder(keyCode)
  }

  def apply(button: Button) {
    new ButtonHolder(button)
  }
}

abstract private class Holder {
  var keyPressedStartTime: Float = .0f
  private var isPressed: Boolean = false

  def checkIfPressed: Boolean

  def updateState(stateTime: Float, checkStateChangeOnly: Boolean): Boolean = {
    val wasPressed: Boolean = isPressed
    isPressed = checkIfPressed
    if (isPressed) {
      if (!wasPressed) {
        keyPressedStartTime = stateTime
      }
    }
    else {
      keyPressedStartTime = -1
    }
    isPressed && (!checkStateChangeOnly || !wasPressed)
  }
}

private class KeyboardKeyHolder(keyCode: Int) extends Holder {
  override def checkIfPressed: Boolean = Gdx.input.isKeyPressed(keyCode)
}

private class ButtonHolder(button: Button) extends Holder {
  override def checkIfPressed: Boolean = button.isPressed
}

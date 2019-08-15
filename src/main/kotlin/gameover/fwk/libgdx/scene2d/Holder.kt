package gameover.fwk.libgdx.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Button

/**
 * This helper class can be used to check if a key was just pressed. the key can be a keyboard key or a
 * {@link com.badlogic.gdx.scenes.scene2d.ui.Button} on a screen.
 */
abstract class Holder {

    companion object {
        fun forKeyboard(keyCode: Int) : Holder = KeyboardKeyHolder(keyCode)
        fun forButton(button: Button) : Holder = ButtonHolder(button)
    }

    var keyPressedStartTime: Float = .0f
    private var isPressed: Boolean = false

    abstract fun checkIfPressed(): Boolean

    fun updateState(stateTime: Float, checkStateChangeOnly: Boolean): Boolean {
        val wasPressed = isPressed
        isPressed = checkIfPressed()
        if (isPressed) {
            if (!wasPressed) {
                keyPressedStartTime = stateTime
            }
        }
        else {
            keyPressedStartTime = -1f
        }
        return isPressed && (!checkStateChangeOnly || !wasPressed)
    }
}

private class KeyboardKeyHolder(val keyCode: Int) : Holder() {
    override fun checkIfPressed(): Boolean = Gdx.input.isKeyPressed(keyCode)
}

private class ButtonHolder(val button: Button) : Holder() {
    override fun checkIfPressed(): Boolean = button.isPressed
}

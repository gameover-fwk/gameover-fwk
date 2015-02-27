package gameover.fwk.gdx.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * This helper class can be used to check if a key was just pressed. the key can be a keyboard key or a {@link
 * com.badlogic.gdx.scenes.scene2d.ui.Button} on a screen.
 */
public class KeyHolder {
  public  float keyPressedStartTime;
  private Button button;
  private boolean isPressed = false;
  private int   keyCode;

  public KeyHolder(int keyCode) {
    this.keyCode = keyCode;
  }

  public KeyHolder(Button button) {
    this.button = button;
  }

  public boolean updateState(float stateTime, boolean checkStateChangeOnly) {
    boolean wasPressed = isPressed;
    isPressed = checkIfPressed();
    if (isPressed) {
      if (!wasPressed) {
        keyPressedStartTime = stateTime;
      }
    } else {
      keyPressedStartTime = -1;
    }
    return isPressed && (!checkStateChangeOnly || !wasPressed);
  }

  private boolean checkIfPressed() {
    return button != null ? button.isPressed() : Gdx.input.isKeyPressed(keyCode);
  }
}

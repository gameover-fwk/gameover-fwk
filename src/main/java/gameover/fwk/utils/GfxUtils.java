package gameover.fwk.utils;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Utilities for graphics
 */
public final class GfxUtils {

  private GfxUtils() {
  }

  /**
   * Take a screenshot of the screen.
   *
   * @return a Pixmap instance. Dont forget to close it after use.
   */
  public static Pixmap takeScreenShot() {
    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();
    Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, width, height);

        /* Reverse */
    ByteBuffer pixels = pixmap.getPixels();
    int amountOfBytes = width * height * 4;
    byte[] lines = new byte[amountOfBytes];
    int amountOfBytesPerLine = width * 4;
    for (int i = 0; i < height; i++) {
      pixels.position((height - i - 1) * amountOfBytesPerLine);
      pixels.get(lines, i * amountOfBytesPerLine, amountOfBytesPerLine);
    }
    pixels.clear();
    pixels.put(lines);
    return pixmap;
  }
  public static Texture loadTexture(String file) {
    FileHandle fileHandle = Gdx.files.internal(file);
    if (fileHandle != null && fileHandle.exists()) {
      return new Texture(fileHandle);
    }
    throw new IllegalArgumentException("Texture " + file + " not found");
  }

  public static NinePatch loadNinePatch(String file, int left, int right, int top, int bottom) {
    return new NinePatch(loadTexture(file), left, right, top, bottom);
  }

  public static Pixmap loadPixmap(String file) {
    FileHandle fileHandle = Gdx.files.internal(file);
    if (fileHandle != null && fileHandle.exists()) {
      return new Pixmap(fileHandle);
    }
    throw new IllegalArgumentException("Pixmap " + file + " not found");
  }

  public static Animation loadAnimation(String file, float frameDuration, int size) {
    return loadAnimation(Gdx.files.internal(file), frameDuration, size, size, null);
  }

  public static Animation loadAnimation(FileHandle animationFile, float frameDuration, int width, int height, Animation.PlayMode playMode) {
    if (animationFile != null && animationFile.exists()) {
      Texture texture = new Texture(animationFile);
      TextureRegion[][] regions = TextureRegion.split(texture, width, height);
      TextureRegion[] animArray = new TextureRegion[regions[0].length];
      for (int i = 0; i < regions[0].length; i++) {
        animArray[i] = regions[0][i];
      }
      Animation animation = new Animation(frameDuration, animArray);
      if (playMode != null) {
        animation.setPlayMode(playMode);
      }
      return animation;
    }
    throw new IllegalArgumentException("Animation " + animationFile + " not found");
  }

  public static Animation loadAnimation(FileHandle animationFile, float frameDuration, int width, int height, Animation.PlayMode playMode,
      int... index) {
    if (animationFile != null && animationFile.exists()) {
      Texture texture = new Texture(animationFile);
      TextureRegion[][] regions = TextureRegion.split(texture, width, height);
      TextureRegion[] animArray = new TextureRegion[index.length];
      for (int i : index) {
        animArray[i] = regions[0][i];
      }
      Animation animation = new Animation(frameDuration, animArray);
      if (playMode != null) {
        animation.setPlayMode(playMode);
      }
      return animation;
    }
    throw new IllegalArgumentException("Animation " + animationFile + " not found");
  }
}

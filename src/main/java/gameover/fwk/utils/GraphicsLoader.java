package gameover.fwk.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 * This class is used to managed all graphics resources, responsible to load them
 * and reference them through a key.
 */
public class GraphicsLoader implements Disposable{
  public static final String PNG = ".png";
  private final Map<String, Animation> animations;
  private final Map<String, Pixmap>    pixmaps;
  private final Map<String, Texture>   textures;
  private final Map<String, NinePatch> ninePatches;
  private       int                    loaded;

  /**
   * Create the graphics loader.
   */
  public GraphicsLoader() {
    this.animations = new HashMap<String, Animation>();
    this.pixmaps = new HashMap<String, Pixmap>();
    this.textures = new HashMap<String, Texture>();
    this.ninePatches = new HashMap<String, NinePatch>();
    loadGfx();
  }

  /**
   * Load all graphics from filesystem.
   */
  public void loadGfx() {
    this.loaded = 0;

    LogUtils.info(LogUtils.Type.GFX, "Scan images on file system...");


    FileHandle internal = Gdx.files.internal("");
    LogUtils.info(LogUtils.Type.GFX, "Scan internal storage " + internal);
    loadGfx(internal.list());

//    if (Gdx.files.isExternalStorageAvailable()) {
//      FileHandle external = Gdx.files.external("");
//      LogUtils.info(LogUtils.Type.GFX, "Scan external storage " + Gdx.files.getExternalStoragePath());
//      loadGfx(external.list());
//    }

    if (Gdx.files.isLocalStorageAvailable()) {
      FileHandle local = Gdx.files.local("");
      LogUtils.info(LogUtils.Type.GFX, "Scan local storage " + Gdx.files.getLocalStoragePath());
      loadGfx(local.list());
    }

    LogUtils.info(LogUtils.Type.GFX, "Finished loading images! Nb files loaded: "+loaded);
  }

  private void loadGfx(FileHandle[] fileHandles) {
    if(fileHandles!=null) {
      for (FileHandle fh : fileHandles) {
        if (fh.name().endsWith(".png")) {
          load(fh);
        }
        FileHandle[] list = fh.list();
        if (list!=null && list.length>0) {
          LogUtils.info(LogUtils.Type.GFX, "Scan " + fh);
          loadGfx(list);
        }
      }
    }
  }

  private void load(FileHandle fh) {
    String name = fh.name().substring(0, fh.name().indexOf(PNG));
    String[] idAndProperty = name.split("#");
    if(idAndProperty.length>1) {
      String id = idAndProperty[0];
      String[] properties = idAndProperty[1].split("[_]");
      int nbImages = Integer.parseInt(properties[0]);
      float frameDuration = Float.parseFloat(properties[1]+"f");
      Animation.PlayMode playMode = valueOfPlayMode(properties[2]);
      Pixmap pixmap = new Pixmap(fh);
      int w = pixmap.getWidth()/nbImages;
      int h = pixmap.getHeight();
      pixmap.dispose();
      Animation animation = GfxUtils.loadAnimation(fh, frameDuration, w, h, playMode);
      animations.put(id, animation);
      Gdx.app.log(getClass().getName(), "Load animation "+id);
      if(id.contains("_move_")) {
        animation = GfxUtils.loadAnimation(fh, frameDuration, w, h, playMode, 0);
        animations.put(id.replaceAll("[_]move[_]", "_stand_"), animation);
      }
    } else {
      idAndProperty = name.split("%");
      if(idAndProperty.length>1) {
        String id = idAndProperty[0];
        String[] props = idAndProperty[1].split("[_]");
        ninePatches.put(id, new NinePatch(new Texture(fh),
            Integer.parseInt(props[0]),
            Integer.parseInt(props[1]),
            Integer.parseInt(props[2]),
            Integer.parseInt(props[3])));
      } else {
        pixmaps.put(name, new Pixmap(fh));
        Gdx.app.log(getClass().getName(), "Load pixmap " + name);
        textures.put(name, new Texture(fh));
        Gdx.app.log(getClass().getName(), "Load texture " + name);
      }
    }
    loaded++;
  }

  private Animation.PlayMode valueOfPlayMode(String property) {
    if("N".equals(property)) {
      return Animation.PlayMode.NORMAL;
    } else {
      return Animation.PlayMode.LOOP;
    }
  }

  /**
   * Returns an animation from its id.
   * @param key the key for the animation
   * @return an animation
   */
  public Animation animation(String key) {
    return animations.get(key);
  }

  /**
   * Returns a pixmap from its id.
   * @param key the key for the pixmap
   * @return a pixmap
   */
  public Pixmap pixmap(String key) {
    return pixmaps.get(key);
  }

  public NinePatch ninePatch(String key) {
    return ninePatches.get(key);
  }

  @Override
  public void dispose() {
    for (Pixmap p : pixmaps.values()) {
      p.dispose();
    }
    pixmaps.clear();
    for(Texture t : textures.values()) {
      t.dispose();
    }
    textures.clear();
    animations.clear();
    ninePatches.clear();
  }

  public Texture texture(String key) {
    return this.textures.get(key);
  }

  public void reload() {
    dispose();
    loadGfx();
  }
}

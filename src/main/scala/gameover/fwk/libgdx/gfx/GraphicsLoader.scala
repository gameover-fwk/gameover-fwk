package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.{Animation, NinePatch, TextureRegion}
import com.badlogic.gdx.graphics.{Pixmap, Texture}
import gameover.fwk.libgdx.scene2d.Disposable
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.logging.Logs

import scala.collection.mutable

/**
 * This singleton is used to managed all graphics resources, responsible to load them
 * and reference them through a key. Images can use a pattern to specified addition information.
 *
 */
class GraphicsLoader() extends Disposable with Logs with LibGDXHelper {
  private val animations = new mutable.HashMap[String, Animation]()
  private val pixmaps = new mutable.HashMap[String, Pixmap]()
  private val textures = new mutable.HashMap[String, Texture]()
  private val ninePatches = new mutable.HashMap[String, NinePatch]()

  private val animationRegExp = """(\w*)#(\d*)_(\d*(.\d)?)_([LN])""".r
  private val ninePathRegExp = """(\w*)%(\d*)_(\d*)_(\d*)_(\d*)""".r

  loadGfx()

  def size() : Int = {
    animations.size + pixmaps.size + textures.size + ninePatches.size
  }

  def loadGfx() {
    var loaded = 0
    logInfo(LogType.GFX, "Scan images on file system...")
    val internal: FileHandle = Gdx.files.internal("")
    logInfo(LogType.GFX, "Scan internal storage " + internal)
    loadGfx(internal.list)

    //    if (Gdx.files.isExternalStorageAvailable()) {
    //      FileHandle external = Gdx.files.external("");
    //      LogUtils.info(LogUtils.Type.GFX, "Scan external storage " + Gdx.files.getExternalStoragePath());
    //      loadGfx(external.list());
    //    }

    if (Gdx.files.isLocalStorageAvailable) {
      val local: FileHandle = Gdx.files.local("")
      logInfo(LogType.GFX, "Scan local storage " + Gdx.files.getLocalStoragePath)
      loaded += loadGfx(local.list)
    }

    logInfo(LogType.GFX, "Finished loading images! Nb files loaded: " + loaded)
  }

  private def loadGfx(fileHandles: Array[FileHandle]): Int = {
    var loaded = 0
    if (fileHandles != null) {
      for (fh <- fileHandles) {
        if (fh.name.endsWith(".png")) {
          load(fh)
          loaded += 1
        }
        val list: Array[FileHandle] = fh.list
        if (list != null && list.length > 0) {
          logInfo(LogType.GFX, "Scan " + fh)
          loaded += loadGfx(list)
        }
      }
    }
    loaded
  }

  private def load(fh: FileHandle) {
    val fileName: String = fh.name.substring(0, fh.name.indexOf(".png"))
    fileName match {
      case animationRegExp(name, nbImages, frameDuration, _, playMode) =>
        registerAnimation(fh, name, nbImages.toInt, s"${frameDuration}f".toFloat, valueOfPlayMode(playMode))
      case ninePathRegExp(name, left, right, top, bottom) =>
        registerNinePatch(fh, name, left.toInt, right.toInt, top.toInt, bottom.toInt)
      case _ =>
        registerDefault(fh, fileName)
    }
  }

  def registerAnimation(fh: FileHandle, name: String, nbImages:Int, frameDuration: Float, playMode: Animation.PlayMode) {
    val pixmap: Pixmap = new Pixmap(fh)
    val w: Int = pixmap.getWidth / nbImages
    val h: Int = pixmap.getHeight
    pixmap.dispose()
    var animation: Animation = loadAnimation(fh, frameDuration, w, h, playMode)
    animations.put(name, animation)
    Gdx.app.log(getClass.getName, "Load animation " + name)
    if (name.contains("_move_")) {
      animation = loadAnimation(fh, frameDuration, w, h, playMode, 0)
      animations.put(name.replaceAll("[_]move[_]", "_stand_"), animation)
    }
  }

  def registerDefault(fh: FileHandle, name: String): Unit = {
    pixmaps.put(name, new Pixmap(fh))
    Gdx.app.log(getClass.getName, "Load pixmap " + name)
    textures.put(name, new Texture(fh))
    Gdx.app.log(getClass.getName, "Load texture " + name)
  }

  def registerNinePatch(fh: FileHandle, name: String, left: Int, right: Int, top: Int, bottom: Int) {
    ninePatches.put(name, new NinePatch(new Texture(fh), left, right, top, bottom))
  }

  private def valueOfPlayMode(property: String): Animation.PlayMode = {
    property match {
      case "N" => Animation.PlayMode.NORMAL
      case _ => Animation.PlayMode.LOOP
    }
  }

  def animation(key: String): Option[Animation] = animations.get(key)
  def pixmap(key: String): Option[Pixmap] = pixmaps.get(key)
  def texture(key: String): Option[Texture] = textures.get(key)
  def ninePatch(key: String): Option[NinePatch] = ninePatches.get(key)

  def dispose() {
    pixmaps.values.foreach(_.dispose())
    pixmaps.clear()
    textures.values.foreach(_.dispose())
    textures.clear()
    animations.clear()
    ninePatches.clear()
  }


  def reload() {
    dispose()
    loadGfx()
  }

  def loadTexture(file: String): Texture = {
    val fileHandle: FileHandle = Gdx.files.internal(file)
    if (fileHandle != null && fileHandle.exists) {
      return new Texture(fileHandle)
    }
    throw new IllegalArgumentException(s"Texture $file not found")
  }

  def loadNinePatch(file: String, left: Int, right: Int, top: Int, bottom: Int): NinePatch =
    new NinePatch(loadTexture(file), left, right, top, bottom)

  def loadPixmap(file: String): Pixmap = {
    val fileHandle: FileHandle = Gdx.files.internal(file)
    if (fileHandle != null && fileHandle.exists) {
      new Pixmap(fileHandle)
    } else throw new IllegalArgumentException(s"Pixmap $file not found")
  }

  def loadAnimation(file: String, frameDuration: Float, size: Int): Animation = loadAnimation(Gdx.files.internal(file), frameDuration, size, size, null)

  def loadAnimation(animationFile: FileHandle, frameDuration: Float, width: Int, height: Int, playMode: Animation.PlayMode): Animation = {
    if (animationFile != null && animationFile.exists) {
      val texture: Texture = new Texture(animationFile)
      val regions: Array[Array[TextureRegion]] = TextureRegion.split(texture, width, height)
      val animArray = new GdxArray[TextureRegion](regions(0).length)
      for (i <- regions(0).indices) {
        animArray.add(regions(0)(i))
      }
      val animation: Animation = new Animation(frameDuration, animArray)
      if (playMode != null) {
        animation.setPlayMode(playMode)
      }
      animation
    } else throw new IllegalArgumentException("Animation " + animationFile + " not found")
  }

  def loadAnimation(animationFile: FileHandle, frameDuration: Float, width: Int, height: Int, playMode: Animation.PlayMode, index: Int*): Animation = {
    if (animationFile != null && animationFile.exists) {
      val texture = new Texture(animationFile)
      val regions = TextureRegion.split(texture, width, height)
      val animArray = new GdxArray[TextureRegion](index.length)
      for (i <- index) {
        animArray.add(regions(0)(i))
      }
      val animation = new Animation(frameDuration, animArray)
      animation.setPlayMode(playMode)
      animation
    } else throw new IllegalArgumentException("Animation " + animationFile + " not found")
  }
}

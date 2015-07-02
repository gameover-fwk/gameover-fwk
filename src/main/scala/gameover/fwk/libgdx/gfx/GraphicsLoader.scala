package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.{Animation, NinePatch, TextureRegion}
import com.badlogic.gdx.graphics.{Pixmap, Texture}
import gameover.fwk.Logs
import gameover.fwk.libgdx.scene2d.Disposable
import gameover.fwk.libgdx.utils.LibGDXHelper

import scala.collection.mutable

/**
 * This singleton is used to managed all graphics resources, responsible to load them
 * and reference them through a key.
 */
object GraphicsLoader extends Disposable with LibGDXHelper with Logs {
  private val animations = new mutable.HashMap[String, Animation]()
  private val pixmaps = new mutable.HashMap[String, Pixmap]()
  private val textures = new mutable.HashMap[String, Texture]()
  private val ninePatches = new mutable.HashMap[String, NinePatch]()
  private var loaded: Int = 0

  loadGfx()

  def loadGfx() {
    this.loaded = 0
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
      loadGfx(local.list)
    }

    logInfo(LogType.GFX, "Finished loading images! Nb files loaded: " + loaded)
  }

  private def loadGfx(fileHandles: Array[FileHandle]) {
    if (fileHandles != null) {
      for (fh <- fileHandles) {
        if (fh.name.endsWith(".png")) {
          load(fh)
        }
        val list: Array[FileHandle] = fh.list
        if (list != null && list.length > 0) {
          logInfo(LogType.GFX, "Scan " + fh)
          loadGfx(list)
        }
      }
    }
  }

  private def load(fh: FileHandle) {
    val name: String = fh.name.substring(0, fh.name.indexOf(".png"))
    var idAndProperty: Array[String] = name.split("#")
    if (idAndProperty.length > 1) {
      val id: String = idAndProperty(0)
      val properties: Array[String] = idAndProperty(1).split("[_]")
      val nbImages: Int = properties(0).toInt
      val frameDuration: Float = (properties(1) + "f").toFloat
      val playMode: Animation.PlayMode = valueOfPlayMode(properties(2))
      val pixmap: Pixmap = new Pixmap(fh)
      val w: Int = pixmap.getWidth / nbImages
      val h: Int = pixmap.getHeight
      pixmap.dispose()
      var animation: Animation = loadAnimation(fh, frameDuration, w, h, playMode)
      animations.put(id, animation)
      Gdx.app.log(getClass.getName, "Load animation " + id)
      if (id.contains("_move_")) {
        animation = loadAnimation(fh, frameDuration, w, h, playMode, 0)
        animations.put(id.replaceAll("[_]move[_]", "_stand_"), animation)
      }
    }
    else {
      idAndProperty = name.split("%")
      if (idAndProperty.length > 1) {
        val id: String = idAndProperty(0)
        val props: Array[String] = idAndProperty(1).split("[_]")
        ninePatches.put(id, new NinePatch(new Texture(fh), props(0).toInt, props(1).toInt, props(2).toInt, props(3).toInt))
      }
      else {
        pixmaps.put(name, new Pixmap(fh))
        Gdx.app.log(getClass.getName, "Load pixmap " + name)
        textures.put(name, new Texture(fh))
        Gdx.app.log(getClass.getName, "Load texture " + name)
      }
    }
    loaded += 1
  }

  private def valueOfPlayMode(property: String): Animation.PlayMode = {
    property match {
      case "N" => Animation.PlayMode.NORMAL
      case _ => Animation.PlayMode.LOOP
    }
  }

  def animation(key: String): Animation = animations.getOrElse(key, null)
  def pixmap(key: String): Pixmap = pixmaps.getOrElse(key, null)
  def texture(key: String): Texture = textures.getOrElse(key, null)
  def ninePatch(key: String): NinePatch = ninePatches.getOrElse(key, null)

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
    throw new IllegalArgumentException("Texture " + file + " not found")
  }

  def loadNinePatch(file: String, left: Int, right: Int, top: Int, bottom: Int): NinePatch = new NinePatch(loadTexture(file), left, right, top, bottom)

  def loadPixmap(file: String): Pixmap = {
    val fileHandle: FileHandle = Gdx.files.internal(file)
    if (fileHandle != null && fileHandle.exists) {
      new Pixmap(fileHandle)
    } else throw new IllegalArgumentException("Pixmap " + file + " not found")
  }

  def loadAnimation(file: String, frameDuration: Float, size: Int): Animation = loadAnimation(Gdx.files.internal(file), frameDuration, size, size, null)

  def loadAnimation(animationFile: FileHandle, frameDuration: Float, width: Int, height: Int, playMode: Animation.PlayMode): Animation = {
    if (animationFile != null && animationFile.exists) {
      val texture: Texture = new Texture(animationFile)
      val regions: Array[Array[TextureRegion]] = TextureRegion.split(texture, width, height)
      val animArray = new GdxArray[TextureRegion](regions(0).length)
      for (i <- regions(0).indices) {
        animArray(i) = regions(0)(i)
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
        animArray(i) = regions(0)(i)
      }
      val animation = new Animation(frameDuration, animArray)
      animation.setPlayMode(playMode)
      animation
    } else throw new IllegalArgumentException("Animation " + animationFile + " not found")
  }
}

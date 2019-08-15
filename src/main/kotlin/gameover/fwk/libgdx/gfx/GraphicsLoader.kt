package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import gameover.fwk.libgdx.scene2d.Disposable
import gameover.fwk.libgdx.utils.GdxArray
import gameover.fwk.logging.LogType
import gameover.fwk.logging.Logs.logInfo


/**
 * This singleton is used to managed all graphics resources, responsible to load them
 * and reference them through a key. Images can use a pattern to specified addition information.
 *
 * Warning! Android is not supporting group in pattern
 */
class GraphicsLoader : Disposable {
    private val animations = HashMap<String, AnimationInfo>()
    private val pixmaps = HashMap<String, Pixmap>()
    private val textures = HashMap<String, Texture>()
    private val ninePatches = HashMap<String, NinePatch>()

    private val animationRegExp = """(?<name>\w*)(?:%(?<x>\d*)_(?<y>\d*)_(?<width>\d*)_(?<height>\d*))?#(?<nbImages>\d*)_(?<frameDuration>\d*(?:.\d*)?)_(?<playMode>[LN])""".toRegex()
    private val ninePatchRegExp = """(?<name>\w*)%(?<left>\d*)_(?<right>\d*)_(?<top>\d*)_(?<bottom>\d*)""".toRegex()

    init {
        loadGfx()
    }

    fun size() : Int = animations.size + pixmaps.size + textures.size + ninePatches.size

    private fun loadGfx() {
        var loaded = 0
        logInfo(LogType.GFX, "Scan images on file system...")
        val internal: FileHandle = Gdx.files.internal("")
        logInfo(LogType.GFX, "Scan internal storage $internal")
        loaded += loadGfx(internal.list(), true)

        if (Gdx.files.isLocalStorageAvailable) {
            val local: FileHandle = Gdx.files.local("")
            logInfo(LogType.GFX, "Scan local storage " + Gdx.files.localStoragePath)
            loaded += loadGfx(local.list(), false)
        }


        logInfo(LogType.GFX, "Finished loading images! Nb files loaded: $loaded")
    }


    private fun loadGfx(fileHandles: Array<FileHandle>, fixPath: Boolean): Int {
        var loaded = 0
        for (fh in fileHandles.filter { fh -> !fh.name().startsWith(".") }) {
            val fhl = if (fh.path().startsWith("/") && fixPath) Gdx.files.internal(fh.path().substring(1)) else fh
            if (fhl.name().endsWith(".png")) {
                if (fhl.exists()) {
                    load(fhl)
                    loaded += 1
                } else {
                    logInfo(LogType.GFX, "Ignore $fhl because it does not 'exist'")
                }
            }
            val list: Array<FileHandle> = fhl.list()
            if (list.isNotEmpty()) {
                logInfo(LogType.GFX, "Scan $fhl")
                loaded += loadGfx(list, fixPath)
            }
        }
        return loaded
    }

    private fun load(fh: FileHandle) {
        val filename = fh.name().substring(0, fh.name().indexOf(".png"))
        val a = animationRegExp.matchEntire(filename)
        if (a != null) {
            val x = a.groups["x"]?.value
            val y = a.groups["y"]?.value
            val w = a.groups["width"]?.value
            val h = a.groups["height"]?.value
            val area: Rectangle? = if (x != null && y != null && w != null && h != null) {
                Rectangle(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
            } else {
                null
            }
            registerAnimation(fh, a.groups["name"]!!.value, area, a.groups["nbImages"]!!.value.toInt(), a.groups["frameDuration"]?.value?.toFloat() ?: 1.0f, valueOfPlayMode(a.groups["playMode"]!!.value))
        } else {
            val np = ninePatchRegExp.matchEntire(filename)
            if (np != null) {
                val l = np.groups["left"]!!.value
                val r = np.groups["right"]!!.value
                val t = np.groups["top"]!!.value
                val b = np.groups["bottom"]!!.value
                registerNinePatch(fh, np.groups["name"]!!.value, l.toInt(), r.toInt(), t.toInt(), b.toInt())
            } else {
                registerDefault(fh, filename)
            }
        }
    }

    fun registerAnimation(fh: FileHandle, name: String, optionalArea: Rectangle?, nbImages:Int, frameDuration: Float, playMode: Animation.PlayMode) {
        val pixmap = Pixmap(fh)
        val w: Int = pixmap.width / nbImages
        val h: Int = pixmap.height
        pixmap.dispose()
        val animation = loadAnimation(fh, frameDuration, w, h, playMode)
        animations[name] = AnimationInfo(name, animation, optionalArea)
        logInfo(LogType.GFX, "Load animation $name")
        if (name.contains("_move_")) {
            val standAnimation = loadAnimation(fh, frameDuration, w, h, playMode, intArrayOf(0))
            val standName = name.replace("_move_", "_stand_")
            animations[standName] = AnimationInfo(standName, standAnimation, optionalArea)
        }
    }

    private fun registerDefault(fh: FileHandle, name: String) {
        pixmaps[name] = Pixmap(fh)
        logInfo(LogType.GFX, "Load pixmap $name")
        textures[name] = Texture(fh)
        logInfo(LogType.GFX, "Load texture $name")
    }

    fun registerNinePatch(fh: FileHandle, name: String, left: Int, right: Int, top: Int, bottom: Int) {
        ninePatches[name] = NinePatch(Texture(fh), left, right, top, bottom)
    }

    private fun valueOfPlayMode(property: String): Animation.PlayMode {
        return when (property){
            "N" -> Animation.PlayMode.NORMAL
            else -> Animation.PlayMode.LOOP
        }
    }

    fun animation(key: String): AnimationInfo? = animations[key]
    fun pixmap(key: String): Pixmap? = pixmaps[key]
    fun texture(key: String): Texture? = textures[key]
    fun ninePatch(key: String): NinePatch? = ninePatches[key]

    override fun dispose() {
        pixmaps.values.forEach { p -> p.dispose() }
        pixmaps.clear()
        textures.values.forEach { p -> p.dispose() }
        textures.clear()
        animations.clear()
        ninePatches.clear()
    }


    fun reload() {
        dispose()
        loadGfx()
    }

    fun loadTexture(file: String): Texture {
        val fileHandle: FileHandle = Gdx.files.internal(file)
        if (fileHandle.exists()) {
            return Texture(fileHandle)
        }
        throw IllegalArgumentException("Texture $file not found")
    }

    fun loadNinePatch(file: String, left: Int, right: Int, top: Int, bottom: Int): NinePatch = NinePatch(loadTexture(file), left, right, top, bottom)

    fun loadPixmap(file: String): Pixmap {
        val fileHandle: FileHandle = Gdx.files.internal(file)
        if (fileHandle.exists()) {
            return Pixmap(fileHandle)
        } else throw IllegalArgumentException("Pixmap $file not found")
    }

    fun loadAnimation(file: String, frameDuration: Float, size: Int): Animation<TextureRegion> = loadAnimation(Gdx.files.internal(file), frameDuration, size, size, Animation.PlayMode.LOOP)

    fun loadAnimation(animationFile: FileHandle, frameDuration: Float, width: Int, height: Int, playMode: Animation.PlayMode): Animation<TextureRegion> {
        if (animationFile.exists()) {
            val texture = Texture(animationFile)
            val regions = TextureRegion.split(texture, width, height)
            val animArray = GdxArray<TextureRegion>(regions[0])
            val animation = Animation<TextureRegion>(frameDuration, animArray)
            animation.playMode = playMode
            return animation
        } else throw IllegalArgumentException("Animation $animationFile not found")
    }

    fun loadAnimation(animationFile: FileHandle, frameDuration: Float, width: Int, height: Int, playMode: Animation.PlayMode, indexes: IntArray): Animation<TextureRegion> {
        if (animationFile.exists()) {
            val texture = Texture(animationFile)
            val regions = TextureRegion.split(texture, width, height)
            val animArray = GdxArray<TextureRegion>(regions[0].filterIndexed { i, _ -> i in indexes }.toTypedArray())
            val animation = Animation<TextureRegion>(frameDuration, animArray)
            animation.playMode = playMode
            return animation
        } else throw IllegalArgumentException("Animation $animationFile not found")
    }
}

data class AnimationInfo(val id: String, val anim: Animation<TextureRegion>, val optionalArea: Rectangle?)

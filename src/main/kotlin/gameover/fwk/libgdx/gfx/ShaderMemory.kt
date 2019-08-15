package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import gameover.fwk.collection.head
import gameover.fwk.collection.tail

object ShaderMemory {
    private val shadersPerSpriteBatches = HashMap<SpriteBatch, List<ShaderProgram>>()

    private fun findShaders(batch: SpriteBatch): List<ShaderProgram>? {
        return shadersPerSpriteBatches[batch]
    }

    fun switchShader(batch: SpriteBatch, newShader: ShaderProgram): ShaderProgram? {
        var shaders = findShaders(batch)
        var old: ShaderProgram? = null
        if (shaders != null && shaders.isNotEmpty()) {
            old = shaders.head
            shaders = shaders.tail
        }
        shaders = gameover.fwk.collection.listAppend(newShader, shaders)
        shadersPerSpriteBatches[batch] = shaders
        batch.shader = newShader
        return old
    }

    fun setShader(batch: SpriteBatch, newShader: ShaderProgram) {
        val shaders = findShaders(batch)
        shadersPerSpriteBatches[batch] = gameover.fwk.collection.listAppend(newShader, shaders)
        batch.shader = newShader
    }

    fun removeCurrentShader(batch: SpriteBatch) {
        var shaders = findShaders(batch)
        if (shaders != null && shaders.isNotEmpty()) {
            shaders = shaders.tail
            shadersPerSpriteBatches[batch] = shaders
            if (shaders.isNotEmpty())
                batch.shader = shaders.head
        }
    }

    fun clearShader(batch: SpriteBatch) : List<ShaderProgram>? {
        val shaders = shadersPerSpriteBatches[batch]
        shadersPerSpriteBatches.remove(batch)
        batch.shader = null
        return shaders
    }
}
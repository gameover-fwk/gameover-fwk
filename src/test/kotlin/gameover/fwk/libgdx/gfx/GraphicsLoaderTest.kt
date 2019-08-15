package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.libgdx.GdxTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GraphicsLoaderTest {
    init {
        GdxTest.initializeLibGdx()
    }
    private var graphicsLoader = GraphicsLoader()

    @Test
    fun `scanning files`(){
        assertThat(graphicsLoader.size()).isGreaterThan(0)
    }

    @Test
    fun `load properly an image as a pixmap and as a texture`() {
        assertThat(graphicsLoader.pixmap("pad_area")).isNotNull
        assertThat(graphicsLoader.texture("pad_area")).isNotNull
    }

    @Test
    fun `load a nine patch with information from filename`() {
        val ninePatch = graphicsLoader.ninePatch("bar_window")
        assertThat(ninePatch).isNotNull
    }

    @Test
    fun `load animation with an area the nb of frames and the duration`() {
        val ai = graphicsLoader.animation("animwitharea_move_right")
        assertThat(ai).isNotNull
        assertThat(ai?.anim?.frameDuration).isEqualTo(0.08f)
        assertThat(ai?.anim?.keyFrames).hasSize(4)
        assertThat(ai?.optionalArea).isNotNull
        assertThat(ai?.optionalArea).isEqualTo(Rectangle(5f, 5f, 3f, 3f))

    }

    @Test
    fun `load anim without area but nb of frames and the duration`() {
        val ai = graphicsLoader.animation("name")
        assertThat(ai).isNotNull
        assertThat(ai?.anim?.frameDuration).isEqualTo(0.12f)
        assertThat(ai?.anim?.keyFrames).hasSize(7)
        assertThat(ai?.optionalArea).isNull()
    }

    @Test
    fun `create a 'stand' animation from a 'move' animation containing the first frame only`() {
        val move = graphicsLoader.animation("animwitharea_move_right")
        assertThat(move).isNotNull
        val stand = graphicsLoader.animation("animwitharea_stand_right")
        assertThat(stand?.anim?.keyFrames).hasSize(1)
        val td1 = stand?.anim!!.keyFrames[0].texture.textureData
        td1.prepare()
        val td2 = move?.anim!!.keyFrames[0].texture.textureData
        td2.prepare()
        try {
            assertThat(td1.consumePixmap().pixels).isEqualTo(td2.consumePixmap().pixels)
        } finally {
            td1.disposePixmap()
            td2.disposePixmap()
        }
    }
}
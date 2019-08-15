package gameover.fwk.collection

import gameover.fwk.libgdx.utils.GdxArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CollectionsTest {

    @Nested
    inner class GdxArrayTest {

        @Test
        fun `should be able to do tail on a GdxArray`() {
            val gdxa = GdxArray<Int>()
            for (i in 1..10)
                gdxa.add(i)
            assertThat(gdxa).hasSize(10)

            val gdxa2 = gdxa.tail
            assertThat(gdxa2).hasSize(9)
            assertThat(gdxa2.get(0)).isEqualTo(2)
        }

        @Test
        fun `should be able to do head on a GdxArray`() {
            val gdxa = GdxArray<Int>()
            for (i in 1..10)
                gdxa.add(i)

            val h = gdxa.head
            assertThat(h).isEqualTo(1)
        }

        @Test
        fun `should be able to do firstOrNull() on a GdxArray`() {
            val gdxa = GdxArray<Int>()
            val h = gdxa.firstOrNull()
            assertThat(h).isNull()

            for (i in 1..10)
                gdxa.add(i)
            val h2 = gdxa.firstOrNull()
            assertThat(h2).isEqualTo(1)

        }
    }

    @Nested
    inner class IterableTest {

        @Test
        fun `should be able to do tail on an iterable`() {
            val en: Iterable<Int> = listOf(1, 2 , 3)

            val en2: Iterable<Int> = en.tail
            assertThat(en2).hasSize(2)
            assertThat(en2.first()).isEqualTo(2)
        }

        @Test
        fun `should be able to do head on a GdxArray`() {
            val en: Iterable<Int> = listOf(1, 2 , 3)

            val h = en.head
            assertThat(h).isEqualTo(1)
        }
    }

}
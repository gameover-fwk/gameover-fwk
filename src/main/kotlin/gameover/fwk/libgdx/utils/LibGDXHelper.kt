package gameover.fwk.libgdx.utils

import com.badlogic.gdx.math.GridPoint2

typealias GdxArray<T> = com.badlogic.gdx.utils.Array<T>

object LibGDXHelper {
    fun toString(gridPoint2: GridPoint2): String = "[${gridPoint2.x},${gridPoint2.y}]"

    fun toString(gridPoint2s: Array<GridPoint2>): String = "[${gridPoint2s.map { gp -> toString(gp) }.joinToString { ", " }}]"
}
package gameover.fwk.collection

import gameover.fwk.libgdx.utils.GdxArray

val <T> GdxArray<T>.tail: GdxArray<T>
    get() {
        removeIndex(0)
        return this
    }

val <T> GdxArray<T>.head: T
    get() = first()

val <T> Iterable<T>.tail: Iterable<T>
    get() = drop(1)

fun <T> GdxArray<T>.firstOrNull(): T? = if (size == 0) null else first()

val <T> Iterable<T>.head: T
    get() = first()

val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()

fun <T> listAppend(e: T, list: List<T>?) : List<T> {
    val l2 = arrayListOf(e)
    list?.let { t -> l2.addAll(t) }
    return l2
}

fun <T> listOf(elt: T, acc: List<T>) : List<T> {
    val l = ArrayList<T>()
    l.add(elt)
    acc.forEach { l.add(it) }
    return l
}

fun <T> listOf(acc: List<T>, elt: T) : List<T> {
    val l = ArrayList<T>()
    acc.forEach { l.add(it) }
    l.add(elt)
    return l
}

object Arrays {
    //Dont know what to do
   /* def copyArray[T](src: Array[Array[T]], srcPos: Int, dest: Array[Array[T]], destPos: Int, length: Int) {
        for (i <- 0 to length) {
            Array.copy(src(i), srcPos, dest(i), destPos, src(0).length)
        }
    }*/
    /*fun <T> copyArray(src: Array<Array<T>>, srcPos: Int, dest: Array<Array<T>>, destPos: Int, length: Int) {
        for (i in 0 until length) {
            Array.copy(src[i], srcPos, dest[i], destPos, src[0].size)
        }
    }*/
}
package com.ced.commons.util

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun <E> MutableList<E>.addOrReplace(element: E) {
    if (this.contains(element)) {
        set(indexOf(element), element)
    } else {
        add(element)
    }
}
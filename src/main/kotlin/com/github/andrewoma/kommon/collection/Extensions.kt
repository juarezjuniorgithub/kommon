/*
 * Copyright (c) 2015 Andrew O'Malley
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.andrewoma.kommon.collection

import kotlin.support.AbstractIterator
import java.util.ArrayList
import java.util.HashMap

public fun <K, V> Map<K, V>.plus(other: Map<K, V>): Map<K, V> {
    val result = this.toLinkedMap()
    result.putAll(other)
    return result
}

public fun <T> Stream<T>.chunked(size: Int): Stream<List<T>> {
    val iterator = this.iterator()

    return object : Stream<List<T>> {
        override fun iterator() = object : AbstractIterator<List<T>>() {
            override fun computeNext() {
                val next = ArrayList<T>(size)
                while (iterator.hasNext() && next.size() < size) {
                    next.add(iterator.next())
                }
                if (next.isEmpty()) done() else setNext(next)
            }
        }
    }
}

/**
 * Creates a HashMap with a capacity to handle 'size' elements without requiring internal resizing.
 * It does this by adding roughly 30% extra to the size to allow for the default 0.75 load factor
 */
public fun <K, V> hashMapOfExpectedSize(size: Int): HashMap<K, V> = HashMap(capacity(size))

fun capacity(size: Int) = (size.toLong() + (size / 3) + 1).let {
    if (it > Integer.MAX_VALUE) Integer.MAX_VALUE else it.toInt()
}
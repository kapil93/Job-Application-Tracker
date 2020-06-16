package com.kapil.presentation.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * A wrapper class for live data items which provides the option to consume each item emitted  by it
 * exactly once.
 *
 * This class makes it possible to use LiveData as a lifecycle aware notifier instead of a data
 * holder.
 */
class ShortLivedItem<T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }
}

/**
 * Creates an observer which passes the item contained by ShortLivedItem object to the itemHandler
 * function only if the item has not been handled yet.
 */
fun <T> LiveData<ShortLivedItem<T>>.observeShortLivedData(
    owner: LifecycleOwner,
    itemHandler: (T) -> Unit
) = observe(owner, Observer { if (!it.hasBeenHandled) itemHandler(it.getContentIfNotHandled()!!) })
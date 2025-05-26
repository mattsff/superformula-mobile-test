package com.qrapp.presentation.utils.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Used for providing
 * Coroutine Dispatchers
 * and helper in testing
 */
interface DispatcherProvider {
    fun default(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}

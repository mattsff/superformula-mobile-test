package com.qrapp.presentation.screen

import com.qrapp.presentation.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider : DispatcherProvider {
    override fun default(): CoroutineDispatcher = StandardTestDispatcher()
    override fun main(): CoroutineDispatcher = UnconfinedTestDispatcher()
    override fun io(): CoroutineDispatcher = UnconfinedTestDispatcher()
    override fun unconfined(): CoroutineDispatcher = UnconfinedTestDispatcher()
}

package org.patifiner.client.core

import kotlinx.coroutines.CoroutineDispatcher

interface PtfDispatchers {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
}

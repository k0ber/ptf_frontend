package org.patifiner.client.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

suspend fun SnackbarHostState.showError(message: String) = showSnackbar(message = message, duration = SnackbarDuration.Long)

fun ComponentContext.componentScope() = CoroutineScope(
    SupervisorJob() + Dispatchers.Main.immediate // todo: Default mb? avoid switching context to often
).also { lifecycle.doOnDestroy { it.cancel() } }

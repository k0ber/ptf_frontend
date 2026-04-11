package org.patifiner.client.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModel
import org.koin.compose.LocalKoinScopeContext
import org.koin.compose.koinInject
import org.koin.compose.scope.rememberKoinScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.scope.Scope
import org.patifiner.client.root.login.data.SessionManager

@OptIn(KoinExperimentalAPI::class)
@Composable
inline fun <reified T : ViewModel> authScopedViewModel(): T {
    val sessionManager: SessionManager = koinInject()

    val scope: Scope = sessionManager.authScope
        ?: error("Cannot resolve authScopedViewModel<${T::class.simpleName}>. No active session scope.")

    return koinViewModel<T>(scope = scope)
}

package org.patifiner.client.core

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.scope.Scope
import org.patifiner.client.root.login.data.SessionManager

@Composable
@OptIn(KoinExperimentalAPI::class)
inline fun <reified T : ViewModel> authScopedViewModel(): T {
    val sessionManager: SessionManager = koinInject()

    val scope: Scope = sessionManager.authScope
        ?: error("Cannot resolve authScopedViewModel<${T::class.simpleName}>. No active session scope.")

    val viewModelKey = "${T::class.simpleName}_${sessionManager.userId}"

    return koinViewModel<T>(
        scope = scope,
        key = viewModelKey
    )
}

package org.patifiner.client.login.ui

import androidx.compose.runtime.Immutable


@Immutable
data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
)

@Immutable
sealed class LoginScreenEvent() {
    object FocusOnPassword : LoginScreenEvent()
    class Error(val message: String) : LoginScreenEvent()
}

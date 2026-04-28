package org.patifiner.client.core

import org.orbitmvi.orbit.syntax.Syntax

sealed interface ScreenStatus {
    data object Idle : ScreenStatus
    data object Loading : ScreenStatus
    data class Error(val message: String) : ScreenStatus

    val isLoading: Boolean get() = this is Loading
}

interface StatusState {
    val status: ScreenStatus
    fun copyWithStatus(status: ScreenStatus): StatusState
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <S : Any, SE : Any, T> Syntax<S, SE>.execute(
    crossinline block: suspend () -> Result<T>,
    crossinline onSuccess: suspend Syntax<S, SE>.(T) -> Unit,
    noinline errorFactory: ((String) -> SE)?, // todo: default error handler?
) {
    val statusState = state as? StatusState
        ?: error("State ${state::class} must implement StatusState")

    if (statusState.status.isLoading) return

    reduce { (state as StatusState).copyWithStatus(ScreenStatus.Loading) as S }

    block().fold(
        onSuccess = { data ->
            reduce { (state as StatusState).copyWithStatus(ScreenStatus.Idle) as S }
            onSuccess(data)
        },
        onFailure = { throwable ->
            val msg = throwable.message ?: "Unknown error"
            reduce { (state as StatusState).copyWithStatus(ScreenStatus.Error(msg)) as S }
            errorFactory?.let { factory -> postSideEffect(factory(msg)) }
        }
    )
}

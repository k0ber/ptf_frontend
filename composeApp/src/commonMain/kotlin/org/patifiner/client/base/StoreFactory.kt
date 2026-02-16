package org.patifiner.client.base

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import kotlinx.coroutines.launch

interface BaseState<out S : BaseState<S>> {
    val isLoading: Boolean
    fun withLoading(isLoading: Boolean): S
}

typealias StateReducer<S> = S.() -> S

fun <S : Any> genericReducer(): Reducer<S, StateReducer<S>> = Reducer { msg -> this.msg() }

inline fun <Intent : Any, reified State : BaseState<State>, Label : Any> StoreFactory.createDefault(
    initialState: State,
    noinline executorFactory: () -> Executor<Intent, Nothing, State, StateReducer<State>, Label>
): Store<Intent, State, Label> = create(
    name = State::class.simpleName + "Store",
    initialState = initialState,
    executorFactory = executorFactory,
    reducer = genericReducer()
)

fun <S : BaseState<S>, L : Any> CoroutineExecutorScope<S, StateReducer<S>, *, L>.execute(
    useCase: suspend () -> Result<*>,
    onSuccess: (suspend () -> Unit)? = null,
    errorFactory: ((String) -> L)? = null
) {
    if (state().isLoading) return

    launch {
        dispatch { withLoading(true) }

        useCase()
            .onSuccess { onSuccess?.invoke() }
            .onFailure { throwable ->
                val msg = throwable.toUserMessage()
                errorFactory?.let { publish(it(msg)) }
            }

        dispatch { withLoading(false) }
    }
}
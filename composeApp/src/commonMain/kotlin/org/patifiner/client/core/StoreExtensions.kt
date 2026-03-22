package org.patifiner.client.core

import com.arkivanov.mvikotlin.core.store.Bootstrapper
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

inline fun <Intent : Any, Action : Any, reified State : BaseState<State>, Label : Any> StoreFactory.createDefault(
    initialState: State,
    bootstrapper: Bootstrapper<Action>? = null,
    noinline executorFactory: () -> Executor<Intent, Action, State, StateReducer<State>, Label>
): Store<Intent, State, Label> = create(
    name = State::class.simpleName + "Store",
    initialState = initialState,
    executorFactory = executorFactory,
    reducer = genericReducer(),
    bootstrapper = bootstrapper
)

fun <S : BaseState<S>, L : Any, T> CoroutineExecutorScope<S, StateReducer<S>, *, L>.execute(
    useCase: suspend () -> Result<T>,
    onSuccessData: (S.(T) -> S)? = null,
    onSuccess: ((T) -> Unit)? = null,
    errorFactory: ((String) -> L)? = null
) {
    if (state().isLoading) return

    launch {
        dispatch { withLoading(true) }

        useCase()
            .onSuccess { data ->
                onSuccessData?.let { reducerWithData -> dispatch { reducerWithData(data) } }
                onSuccess?.invoke(data) // are two lambdas for success necessary?
            }
            .onFailure { throwable ->
                errorFactory?.let { publish(it(throwable.message ?: "Unknown error")) }
            }

        dispatch { withLoading(false) }
    }
}

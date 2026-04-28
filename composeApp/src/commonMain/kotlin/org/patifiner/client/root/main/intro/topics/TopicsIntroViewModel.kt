package org.patifiner.client.root.main.intro.topics

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.root.main.intro.IntroViewModel

@Stable
class TopicsIntroViewModel(
    private val introViewModel: IntroViewModel
) : ViewModel(), ContainerHost<TopicsIntroState, TopicsIntroSideEffect> {

    override val container: Container<TopicsIntroState, TopicsIntroSideEffect> = container(TopicsIntroState())

    fun changeSearch(q: String) = intent {
        reduce { state.copy(q = q) }
    }

    fun onNext() {
        introViewModel.nextStep()
    }
}

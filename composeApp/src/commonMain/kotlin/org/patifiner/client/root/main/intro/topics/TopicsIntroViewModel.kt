package org.patifiner.client.root.main.intro.topics

import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.main.intro.IntroViewModel

class TopicsIntroViewModel(
    store: TopicsIntroStore,
    private val introViewModel: IntroViewModel
) : PtfViewModel<TopicsIntroIntent, TopicsIntroState, TopicsIntroLabel>(store) {
    fun onNext() {
        introViewModel.nextStep()
    }
}

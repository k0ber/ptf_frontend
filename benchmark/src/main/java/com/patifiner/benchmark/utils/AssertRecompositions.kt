package com.patifiner.benchmark.utils

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import org.patifiner.client.base.RecompositionCountKey

fun SemanticsNodeInteraction.assertRecompositionCount(
    expected: Int
): SemanticsNodeInteraction {
    assert(
        SemanticsMatcher.expectValue(
            RecompositionCountKey,
            expected
        )
    )
    return this
}

fun SemanticsNodeInteraction.assertRecompositionCountInRange(
    range: IntRange
): SemanticsNodeInteraction {
    val count = fetchSemanticsNode().config[RecompositionCountKey]
    assert(count in range) {
        "Recomposition count ($count) is not in the expected range $range"
    }
    return this
}

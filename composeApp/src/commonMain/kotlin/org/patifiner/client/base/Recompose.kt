package org.patifiner.client.base

import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics

val RecompositionCountKey = SemanticsPropertyKey<Int>("RecompositionCount")

var SemanticsPropertyReceiver.recompositionCount by RecompositionCountKey

fun Modifier.trackCompositions(name: String? = null): Modifier =
    this.then(name?.let { Modifier.testTag(it) } ?: Modifier)
        .composed {
            val recompositions = remember { mutableStateOf(0) }

            SideEffect {
                recompositions.value++
                PtfLog.d { "$name recomposition #${recompositions.value}" }
            }

            Modifier.semantics {
                recompositionCount = recompositions.value
            }
        }

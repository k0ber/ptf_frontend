package org.patifiner.client.design

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.views.GradientBackground

@Composable
fun PtfScreen(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    GradientBackground {
        Column(
            modifier = modifier.scrollableScreen(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
        ) {
            content()
        }
    }
}

fun Modifier.scrollableScreen(scrollState: ScrollState) = this.fillMaxSize()
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .statusBarsPadding()
    .navigationBarsPadding()
    .imePadding()
    .verticalScroll(scrollState)

fun Modifier.screen() = this.fillMaxSize()
    .imePadding()
    .statusBarsPadding()
    .navigationBarsPadding()

fun ColumnScope.centeredField() = Modifier.align(Alignment.CenterHorizontally)
    .requiredWidthIn(max = 400.dp)
    .wrapContentWidth(Alignment.CenterHorizontally)

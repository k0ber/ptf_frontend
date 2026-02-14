package org.patifiner.client.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.views.Chip

@Composable
fun DesignDemo() {
    val sp = LocalSpacing.current
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = sp.lg.dp)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(sp.lg.dp)
    ) {
        // Типографика
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Display Large", style = MaterialTheme.typography.displayLarge)
            Text("Headline", style = MaterialTheme.typography.headlineMedium)
            Text("Title", style = MaterialTheme.typography.titleMedium)
            Text("Body", style = MaterialTheme.typography.bodyLarge)
            Text("Label", style = MaterialTheme.typography.labelLarge)
        }

        // Кнопки
        Row(
            horizontalArrangement = Arrangement.spacedBy(sp.md.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            PrimaryButton(onClick = {}) { Text("Primary") }
//            GhostButton(onClick = {}) { Text("Ghost") }
        }

        // Чипы интересов
        Text("Интересы", style = MaterialTheme.typography.titleMedium)
        FlowRowWrap {
            val interests = listOf("Kotlin", "Compose", "Running", "Art", "Gaming", "Coffee", "Travel")
            val selected = remember { mutableStateListOf<String>() }
            interests.forEach { tag ->
                Chip(
                    text = tag,
                    selected = tag in selected,
                    onClick = {
                        if (tag in selected) selected.remove(tag) else selected.add(tag)
                    },
                    modifier = Modifier.padding(end = sp.sm.dp)
                )
            }
        }

        // Подложки и цвета
        Row(
            horizontalArrangement = Arrangement.spacedBy(sp.sm.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorSwatch("Primary", MaterialTheme.colorScheme.primary)
            ColorSwatch("Surface", MaterialTheme.colorScheme.surface)
            ColorSwatch("Error", MaterialTheme.colorScheme.error)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(sp.md.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            PrimaryButton(onClick = { showError = !showError }) { Text("Toggle Error") }
        }
        Spacer(Modifier.height(sp.xxl.dp))
    }
}

@Composable
private fun FlowRowWrap(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun ColorSwatch(name: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(48.dp)
                .background(color, shape = MaterialTheme.shapes.small)
        )
        Spacer(Modifier.height(6.dp))
        Text(name, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview
@Composable
fun DesignDemoLightPreview() {
    AppTheme(forceDarkMode = false) { DesignDemo() }
}

@Preview
@Composable
fun DesignDemoDarkPreview() {
    AppTheme(forceDarkMode = true) { DesignDemo() }
}

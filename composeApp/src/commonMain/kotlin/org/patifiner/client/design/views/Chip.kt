package org.patifiner.client.design.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.AppTheme

@Composable
fun Chip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface( // hack to remove internal paddings
        modifier = modifier.height(28.dp),
        color = Color.Transparent
    ) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            label = { Text(text) },
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            border = if (!selected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                selectedLabelColor = MaterialTheme.colorScheme.primary
            ),
        )
    }
}

@Preview
@Composable
fun ChipsLightPreview() {
    AppTheme(forceDarkMode = false) {
        Row {
            Chip(text = "Interest Chip", selected = false, onClick = {})
            Chip(text = "Interest Chip", selected = true, onClick = {})
        }
    }
}

@Preview
@Composable
fun ChipsDarkPreview() {
    AppTheme(forceDarkMode = true) {
        Row {
            Chip(text = "Interest Chip", selected = false, onClick = {})
            Chip(text = "Interest Chip", selected = true, onClick = {})
        }
    }
}

package org.patifiner.client.design.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val IcArrowLeft: ImageVector
    get() {
        if (_icArrowLeft != null) return _icArrowLeft!!

        _icArrowLeft = ImageVector.Builder(
            name = "IcArrowLeft",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFE3E3E3)),
                pathFillType = NonZero
            ) {
                moveTo(560.0f, 720.0f)
                lineTo(320.0f, 480.0f)
                lineToRelative(240.0f, -240.0f)
                lineToRelative(56.0f, 56.0f)
                lineToRelative(-184.0f, 184.0f)
                lineToRelative(184.0f, 184.0f)
                lineToRelative(-56.0f, 56.0f)
                close()
            }
        }.build()
        return _icArrowLeft!!
    }

private var _icArrowLeft: ImageVector? = null

@Preview
@Composable
private fun IcArrowLeftPreview() {
    Box(Modifier.padding(12.dp)) { Image(IcArrowLeft, null) }
}

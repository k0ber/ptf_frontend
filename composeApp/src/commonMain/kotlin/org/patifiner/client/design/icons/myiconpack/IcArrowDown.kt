package org.patifiner.client.design.icons.myiconpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.Unit
import org.patifiner.client.design.icons.MyIconPack

public val MyIconPack.IcArrowDown: ImageVector
    get() {
        if (_icArrowDown != null) {
            return _icArrowDown!!
        }
        _icArrowDown = Builder(name = "IcArrowDown", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFFe3e3e3)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(480.0f, 616.0f)
                lineTo(240.0f, 376.0f)
                lineToRelative(56.0f, -56.0f)
                lineToRelative(184.0f, 184.0f)
                lineToRelative(184.0f, -184.0f)
                lineToRelative(56.0f, 56.0f)
                lineToRelative(-240.0f, 240.0f)
                close()
            }
        }
        .build()
        return _icArrowDown!!
    }

private var _icArrowDown: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIconPack.IcArrowDown, contentDescription = "")
    }
}

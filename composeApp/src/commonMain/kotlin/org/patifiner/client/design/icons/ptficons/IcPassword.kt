package org.patifiner.client.design.icons.ptficons

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.icons.PtfIcons
import kotlin.Unit

public val PtfIcons.IcPassword: ImageVector
    get() {
        if (_icPassword != null) {
            return _icPassword!!
        }
        _icPassword = Builder(name = "IcPassword", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFFe3e3e3)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(80.0f, 760.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(800.0f)
                verticalLineToRelative(80.0f)
                lineTo(80.0f, 760.0f)
                close()
                moveTo(126.0f, 518.0f)
                lineTo(74.0f, 488.0f)
                lineTo(108.0f, 428.0f)
                lineTo(40.0f, 428.0f)
                verticalLineToRelative(-60.0f)
                horizontalLineToRelative(68.0f)
                lineToRelative(-34.0f, -58.0f)
                lineToRelative(52.0f, -30.0f)
                lineToRelative(34.0f, 58.0f)
                lineToRelative(34.0f, -58.0f)
                lineToRelative(52.0f, 30.0f)
                lineToRelative(-34.0f, 58.0f)
                horizontalLineToRelative(68.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-68.0f)
                lineToRelative(34.0f, 60.0f)
                lineToRelative(-52.0f, 30.0f)
                lineToRelative(-34.0f, -60.0f)
                lineToRelative(-34.0f, 60.0f)
                close()
                moveTo(446.0f, 518.0f)
                lineTo(394.0f, 488.0f)
                lineTo(428.0f, 428.0f)
                horizontalLineToRelative(-68.0f)
                verticalLineToRelative(-60.0f)
                horizontalLineToRelative(68.0f)
                lineToRelative(-34.0f, -58.0f)
                lineToRelative(52.0f, -30.0f)
                lineToRelative(34.0f, 58.0f)
                lineToRelative(34.0f, -58.0f)
                lineToRelative(52.0f, 30.0f)
                lineToRelative(-34.0f, 58.0f)
                horizontalLineToRelative(68.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-68.0f)
                lineToRelative(34.0f, 60.0f)
                lineToRelative(-52.0f, 30.0f)
                lineToRelative(-34.0f, -60.0f)
                lineToRelative(-34.0f, 60.0f)
                close()
                moveTo(766.0f, 518.0f)
                lineTo(714.0f, 488.0f)
                lineTo(748.0f, 428.0f)
                horizontalLineToRelative(-68.0f)
                verticalLineToRelative(-60.0f)
                horizontalLineToRelative(68.0f)
                lineToRelative(-34.0f, -58.0f)
                lineToRelative(52.0f, -30.0f)
                lineToRelative(34.0f, 58.0f)
                lineToRelative(34.0f, -58.0f)
                lineToRelative(52.0f, 30.0f)
                lineToRelative(-34.0f, 58.0f)
                horizontalLineToRelative(68.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-68.0f)
                lineToRelative(34.0f, 60.0f)
                lineToRelative(-52.0f, 30.0f)
                lineToRelative(-34.0f, -60.0f)
                lineToRelative(-34.0f, 60.0f)
                close()
            }
        }
        .build()
        return _icPassword!!
    }

private var _icPassword: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = PtfIcons.IcPassword, contentDescription = "")
    }
}

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
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.icons.PtfIcons
import kotlin.Unit

public val PtfIcons.IcEmail: ImageVector
    get() {
        if (_icEmail != null) {
            return _icEmail!!
        }
        _icEmail = Builder(name = "IcEmail", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFFe3e3e3)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(480.0f, 880.0f)
                quadToRelative(-83.0f, 0.0f, -156.0f, -31.5f)
                reflectiveQuadTo(197.0f, 763.0f)
                quadToRelative(-54.0f, -54.0f, -85.5f, -127.0f)
                reflectiveQuadTo(80.0f, 480.0f)
                quadToRelative(0.0f, -83.0f, 31.5f, -156.0f)
                reflectiveQuadTo(197.0f, 197.0f)
                quadToRelative(54.0f, -54.0f, 127.0f, -85.5f)
                reflectiveQuadTo(480.0f, 80.0f)
                quadToRelative(83.0f, 0.0f, 156.0f, 31.5f)
                reflectiveQuadTo(763.0f, 197.0f)
                quadToRelative(54.0f, 54.0f, 85.5f, 127.0f)
                reflectiveQuadTo(880.0f, 480.0f)
                verticalLineToRelative(58.0f)
                quadToRelative(0.0f, 59.0f, -40.5f, 100.5f)
                reflectiveQuadTo(740.0f, 680.0f)
                quadToRelative(-35.0f, 0.0f, -66.0f, -15.0f)
                reflectiveQuadToRelative(-52.0f, -43.0f)
                quadToRelative(-29.0f, 29.0f, -65.5f, 43.5f)
                reflectiveQuadTo(480.0f, 680.0f)
                quadToRelative(-83.0f, 0.0f, -141.5f, -58.5f)
                reflectiveQuadTo(280.0f, 480.0f)
                quadToRelative(0.0f, -83.0f, 58.5f, -141.5f)
                reflectiveQuadTo(480.0f, 280.0f)
                quadToRelative(83.0f, 0.0f, 141.5f, 58.5f)
                reflectiveQuadTo(680.0f, 480.0f)
                verticalLineToRelative(58.0f)
                quadToRelative(0.0f, 26.0f, 17.0f, 44.0f)
                reflectiveQuadToRelative(43.0f, 18.0f)
                quadToRelative(26.0f, 0.0f, 43.0f, -18.0f)
                reflectiveQuadToRelative(17.0f, -44.0f)
                verticalLineToRelative(-58.0f)
                quadToRelative(0.0f, -134.0f, -93.0f, -227.0f)
                reflectiveQuadToRelative(-227.0f, -93.0f)
                quadToRelative(-134.0f, 0.0f, -227.0f, 93.0f)
                reflectiveQuadToRelative(-93.0f, 227.0f)
                quadToRelative(0.0f, 134.0f, 93.0f, 227.0f)
                reflectiveQuadToRelative(227.0f, 93.0f)
                horizontalLineToRelative(200.0f)
                verticalLineToRelative(80.0f)
                lineTo(480.0f, 880.0f)
                close()
                moveTo(480.0f, 600.0f)
                quadToRelative(50.0f, 0.0f, 85.0f, -35.0f)
                reflectiveQuadToRelative(35.0f, -85.0f)
                quadToRelative(0.0f, -50.0f, -35.0f, -85.0f)
                reflectiveQuadToRelative(-85.0f, -35.0f)
                quadToRelative(-50.0f, 0.0f, -85.0f, 35.0f)
                reflectiveQuadToRelative(-35.0f, 85.0f)
                quadToRelative(0.0f, 50.0f, 35.0f, 85.0f)
                reflectiveQuadToRelative(85.0f, 35.0f)
                close()
            }
        }
        .build()
        return _icEmail!!
    }

private var _icEmail: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = PtfIcons.IcEmail, contentDescription = "")
    }
}

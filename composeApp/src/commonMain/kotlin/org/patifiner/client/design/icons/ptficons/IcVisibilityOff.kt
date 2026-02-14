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

public val PtfIcons.IcVisibilityOff: ImageVector
    get() {
        if (_icVisibilityOff != null) {
            return _icVisibilityOff!!
        }
        _icVisibilityOff = Builder(name = "IcVisibilityOff", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFFe3e3e3)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(644.0f, 532.0f)
                lineToRelative(-58.0f, -58.0f)
                quadToRelative(9.0f, -47.0f, -27.0f, -88.0f)
                reflectiveQuadToRelative(-93.0f, -32.0f)
                lineToRelative(-58.0f, -58.0f)
                quadToRelative(17.0f, -8.0f, 34.5f, -12.0f)
                reflectiveQuadToRelative(37.5f, -4.0f)
                quadToRelative(75.0f, 0.0f, 127.5f, 52.5f)
                reflectiveQuadTo(660.0f, 460.0f)
                quadToRelative(0.0f, 20.0f, -4.0f, 37.5f)
                reflectiveQuadTo(644.0f, 532.0f)
                close()
                moveTo(772.0f, 658.0f)
                lineTo(714.0f, 602.0f)
                quadToRelative(38.0f, -29.0f, 67.5f, -63.5f)
                reflectiveQuadTo(832.0f, 460.0f)
                quadToRelative(-50.0f, -101.0f, -143.5f, -160.5f)
                reflectiveQuadTo(480.0f, 240.0f)
                quadToRelative(-29.0f, 0.0f, -57.0f, 4.0f)
                reflectiveQuadToRelative(-55.0f, 12.0f)
                lineToRelative(-62.0f, -62.0f)
                quadToRelative(41.0f, -17.0f, 84.0f, -25.5f)
                reflectiveQuadToRelative(90.0f, -8.5f)
                quadToRelative(151.0f, 0.0f, 269.0f, 83.5f)
                reflectiveQuadTo(920.0f, 460.0f)
                quadToRelative(-23.0f, 59.0f, -60.5f, 109.5f)
                reflectiveQuadTo(772.0f, 658.0f)
                close()
                moveTo(792.0f, 904.0f)
                lineTo(624.0f, 738.0f)
                quadToRelative(-35.0f, 11.0f, -70.5f, 16.5f)
                reflectiveQuadTo(480.0f, 760.0f)
                quadToRelative(-151.0f, 0.0f, -269.0f, -83.5f)
                reflectiveQuadTo(40.0f, 460.0f)
                quadToRelative(21.0f, -53.0f, 53.0f, -98.5f)
                reflectiveQuadToRelative(73.0f, -81.5f)
                lineTo(56.0f, 168.0f)
                lineToRelative(56.0f, -56.0f)
                lineToRelative(736.0f, 736.0f)
                lineToRelative(-56.0f, 56.0f)
                close()
                moveTo(222.0f, 336.0f)
                quadToRelative(-29.0f, 26.0f, -53.0f, 57.0f)
                reflectiveQuadToRelative(-41.0f, 67.0f)
                quadToRelative(50.0f, 101.0f, 143.5f, 160.5f)
                reflectiveQuadTo(480.0f, 680.0f)
                quadToRelative(20.0f, 0.0f, 39.0f, -2.5f)
                reflectiveQuadToRelative(39.0f, -5.5f)
                lineToRelative(-36.0f, -38.0f)
                quadToRelative(-11.0f, 3.0f, -21.0f, 4.5f)
                reflectiveQuadToRelative(-21.0f, 1.5f)
                quadToRelative(-75.0f, 0.0f, -127.5f, -52.5f)
                reflectiveQuadTo(300.0f, 460.0f)
                quadToRelative(0.0f, -11.0f, 1.5f, -21.0f)
                reflectiveQuadToRelative(4.5f, -21.0f)
                lineToRelative(-84.0f, -82.0f)
                close()
                moveTo(541.0f, 429.0f)
                close()
                moveTo(390.0f, 504.0f)
                close()
            }
        }
        .build()
        return _icVisibilityOff!!
    }

private var _icVisibilityOff: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = PtfIcons.IcVisibilityOff, contentDescription = "")
    }
}

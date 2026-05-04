package org.patifiner.client.design.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcExplore: ImageVector
    get() {
        if (_icExplore != null) {
            return _icExplore!!
        }
        _icExplore = ImageVector.Builder(
            name = "IcExplore", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 960f, viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(160f, 840f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(80f, 760f)
                verticalLineToRelative(-280f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(280f)
                horizontalLineToRelative(360f)
                verticalLineToRelative(80f)
                lineTo(160f, 840f)
                close()
                moveTo(320f, 680f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(240f, 600f)
                verticalLineToRelative(-280f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(280f)
                horizontalLineToRelative(360f)
                verticalLineToRelative(80f)
                lineTo(320f, 680f)
                close()
                moveTo(480f, 520f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(400f, 440f)
                verticalLineToRelative(-240f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(480f, 120f)
                horizontalLineToRelative(320f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(880f, 200f)
                verticalLineToRelative(240f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(800f, 520f)
                lineTo(480f, 520f)
                close()
                moveTo(480f, 440f)
                horizontalLineToRelative(320f)
                verticalLineToRelative(-160f)
                lineTo(480f, 280f)
                verticalLineToRelative(160f)
                close()
            }
        }.build()

        return _icExplore!!
    }

@Suppress("ObjectPropertyName")
private var _icExplore: ImageVector? = null

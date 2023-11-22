package com.team13.fooriend.ui.fooriendicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.FooriendIcon

public val FooriendIcon.Home: ImageVector
    get() {
        if (_home != null) {
            return _home!!
        }
        _home = Builder(name = "Home", defaultWidth = 212.0.dp, defaultHeight = 206.25.dp,
                viewportWidth = 212.0f, viewportHeight = 206.25f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(205.616f, 84.717f)
                lineToRelative(-88.955f, -77.062f)
                curveToRelative(-0.298f, -0.254f, -0.656f, -0.472f, -1.059f, -0.639f)
                curveToRelative(-5.693f, -4.32f, -13.557f, -4.119f, -19.049f, 0.639f)
                lineToRelative(-88.956f, 77.053f)
                curveToRelative(-6.376f, 5.519f, -7.181f, 15.313f, -1.784f, 21.839f)
                curveToRelative(3.411f, 4.128f, 9.069f, 4.075f, 13.408f, 3.2f)
                verticalLineToRelative(82.834f)
                curveToRelative(0.0f, 6.253f, 5.02f, 11.344f, 11.195f, 11.344f)
                horizontalLineToRelative(53.586f)
                curveToRelative(2.414f, 0.0f, 4.374f, -1.96f, 4.374f, -4.373f)
                verticalLineToRelative(-72.242f)
                horizontalLineToRelative(36.471f)
                verticalLineToRelative(72.242f)
                curveToRelative(0.0f, 2.413f, 1.959f, 4.373f, 4.372f, 4.373f)
                horizontalLineToRelative(53.587f)
                curveToRelative(6.175f, 0.0f, 11.195f, -5.091f, 11.195f, -11.344f)
                verticalLineToRelative(-82.834f)
                curveToRelative(4.355f, 0.857f, 9.996f, 0.919f, 13.407f, -3.2f)
                curveTo(212.797f, 100.022f, 211.992f, 90.218f, 205.616f, 84.717f)
                close()
                moveTo(200.666f, 100.967f)
                curveToRelative(-0.9f, 1.058f, -5.563f, 0.48f, -9.288f, -1.146f)
                curveToRelative(-1.355f, -0.595f, -2.93f, -0.454f, -4.146f, 0.351f)
                curveToRelative(-1.233f, 0.805f, -1.985f, 2.187f, -1.985f, 3.655f)
                verticalLineToRelative(88.746f)
                curveToRelative(0.0f, 1.435f, -1.103f, 2.598f, -2.449f, 2.598f)
                horizontalLineToRelative(-49.214f)
                verticalLineToRelative(-72.242f)
                curveToRelative(0.0f, -2.414f, -1.959f, -4.373f, -4.373f, -4.373f)
                horizontalLineToRelative(-45.217f)
                curveToRelative(-2.414f, 0.0f, -4.373f, 1.959f, -4.373f, 4.373f)
                verticalLineToRelative(72.242f)
                horizontalLineToRelative(-49.214f)
                curveToRelative(-1.347f, 0.0f, -2.449f, -1.163f, -2.449f, -2.598f)
                lineTo(27.958f, 103.826f)
                curveToRelative(0.0f, -1.478f, -0.744f, -2.859f, -1.985f, -3.664f)
                curveToRelative(-1.233f, -0.796f, -2.799f, -0.919f, -4.155f, -0.342f)
                curveToRelative(-3.306f, 1.461f, -8.334f, 2.283f, -9.279f, 1.138f)
                curveToRelative(-2.388f, -2.887f, -2.046f, -7.207f, 0.77f, -9.647f)
                lineToRelative(88.956f, -77.061f)
                curveToRelative(2.432f, -2.099f, 5.956f, -2.099f, 8.388f, 0.0f)
                curveToRelative(0.314f, 0.28f, 0.674f, 0.507f, 1.05f, 0.674f)
                lineToRelative(88.186f, 76.405f)
                curveTo(202.713f, 93.76f, 203.053f, 98.08f, 200.666f, 100.967f)
                lineTo(200.666f, 100.967f)
                close()
            }
        }
        .build()
        return _home!!
    }

private var _home: ImageVector? = null

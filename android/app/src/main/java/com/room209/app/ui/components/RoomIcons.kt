package com.room209.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.room209.app.ui.theme.AccentPrimary
import com.room209.app.ui.theme.TextPrimary

// Bespoke 1.5px Linear Vector Iconography (Strict No-Emoji Standard)
@Composable
fun IconHome(modifier: Modifier = Modifier.size(22.dp), color: Color = TextPrimary, strokeWidth: Dp = 1.5.dp) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.15f, h * 0.45f)
            lineTo(w * 0.5f, h * 0.15f)
            lineTo(w * 0.85f, h * 0.45f)
            lineTo(w * 0.85f, h * 0.85f)
            lineTo(w * 0.15f, h * 0.85f)
            close()
        }
        drawPath(path, color, style = stroke)
        drawRect(
            color = color,
            topLeft = Offset(w * 0.38f, h * 0.55f),
            size = Size(w * 0.24f, h * 0.30f),
            style = stroke
        )
    }
}

@Composable
fun IconFeed(modifier: Modifier = Modifier.size(22.dp), color: Color = TextPrimary, strokeWidth: Dp = 1.5.dp) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.15f, h * 0.15f),
            size = Size(w * 0.7f, h * 0.7f),
            cornerRadius = CornerRadius(w * 0.15f, h * 0.15f),
            style = stroke
        )
        drawLine(color, Offset(w * 0.3f, h * 0.38f), Offset(w * 0.7f, h * 0.38f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.3f, h * 0.52f), Offset(w * 0.62f, h * 0.52f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.3f, h * 0.66f), Offset(w * 0.48f, h * 0.66f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
fun IconPlus(modifier: Modifier = Modifier.size(22.dp), color: Color = Color.White, strokeWidth: Dp = 2.dp) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawLine(color, Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.75f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.25f, h * 0.5f), Offset(w * 0.75f, h * 0.5f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
fun IconPlans(modifier: Modifier = Modifier.size(22.dp), color: Color = TextPrimary, strokeWidth: Dp = 1.5.dp) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.15f, h * 0.22f),
            size = Size(w * 0.7f, h * 0.65f),
            cornerRadius = CornerRadius(w * 0.12f, h * 0.12f),
            style = stroke
        )
        drawLine(color, Offset(w * 0.32f, h * 0.12f), Offset(w * 0.32f, h * 0.25f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.68f, h * 0.12f), Offset(w * 0.68f, h * 0.25f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.15f, h * 0.42f), Offset(w * 0.85f, h * 0.42f), strokeWidth = strokeWidth.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
fun IconFun(modifier: Modifier = Modifier.size(22.dp), color: Color = TextPrimary, strokeWidth: Dp = 1.5.dp) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.15f)
            lineTo(w * 0.62f, h * 0.38f)
            lineTo(w * 0.88f, h * 0.42f)
            lineTo(w * 0.68f, h * 0.62f)
            lineTo(w * 0.74f, h * 0.88f)
            lineTo(w * 0.5f, h * 0.75f)
            lineTo(w * 0.26f, h * 0.88f)
            lineTo(w * 0.32f, h * 0.62f)
            lineTo(w * 0.12f, h * 0.42f)
            lineTo(w * 0.38f, h * 0.38f)
            close()
        }
        drawPath(path, color, style = stroke)
    }
}

@Composable
fun IconProfile(modifier: Modifier = Modifier.size(22.dp), color: Color = TextPrimary, strokeWidth: Dp = 1.5.dp) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val w = size.width
        val h = size.height
        drawCircle(color, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.36f), style = stroke)
        val arcPath = Path().apply {
            moveTo(w * 0.2f, h * 0.85f)
            quadraticBezierTo(w * 0.5f, h * 0.55f, w * 0.8f, h * 0.85f)
        }
        drawPath(arcPath, color, style = stroke)
    }
}

@Composable
fun IconMoon(modifier: Modifier = Modifier.size(20.dp), color: Color = AccentPrimary, strokeWidth: Dp = 1.5.dp) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.65f, h * 0.15f)
            cubicTo(w * 0.35f, h * 0.20f, w * 0.15f, h * 0.45f, w * 0.20f, h * 0.75f)
            cubicTo(w * 0.25f, h * 0.95f, w * 0.50f, h * 1.00f, w * 0.75f, h * 0.85f)
            cubicTo(w * 0.50f, h * 0.75f, w * 0.45f, h * 0.40f, w * 0.65f, h * 0.15f)
            close()
        }
        drawPath(path, color, style = stroke)
    }
}

@Composable
fun IconCheck(modifier: Modifier = Modifier.size(16.dp), color: Color = Color.White, strokeWidth: Dp = 2.dp) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.2f, h * 0.52f)
            lineTo(w * 0.42f, h * 0.75f)
            lineTo(w * 0.82f, h * 0.25f)
        }
        drawPath(path, color, style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

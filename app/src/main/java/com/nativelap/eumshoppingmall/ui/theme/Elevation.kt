package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class EumShadowToken(
    val color: Color,
    val offsetX: androidx.compose.ui.unit.Dp,
    val offsetY: androidx.compose.ui.unit.Dp,
    val blurRadius: androidx.compose.ui.unit.Dp,
    val spread: androidx.compose.ui.unit.Dp,
)

object EumShadows {
    val level1 =
        EumShadowToken(
            color = Color(0x0F111111),
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
        )
    val level2 =
        EumShadowToken(
            color = Color(0x14111111),
            offsetX = 0.dp,
            offsetY = 4.dp,
            blurRadius = 16.dp,
            spread = 0.dp,
        )
    val level3 =
        EumShadowToken(
            color = Color(0x29111111),
            offsetX = 0.dp,
            offsetY = 12.dp,
            blurRadius = 32.dp,
            spread = (-4).dp,
        )
    val drawer =
        EumShadowToken(
            color = Color(0x29111111),
            offsetX = 8.dp,
            offsetY = 0.dp,
            blurRadius = 32.dp,
            spread = (-4).dp,
        )
    val sheet =
        EumShadowToken(
            color = Color(0x29111111),
            offsetX = 0.dp,
            offsetY = (-8).dp,
            blurRadius = 32.dp,
            spread = (-4).dp,
        )
}

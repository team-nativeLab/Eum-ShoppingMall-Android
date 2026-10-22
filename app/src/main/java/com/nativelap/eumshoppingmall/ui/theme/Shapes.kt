package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object EumRadius {
    val x4 = 4.dp
    val x6 = 6.dp
    val x8 = 8.dp
    val x10 = 10.dp
    val x12 = 12.dp
    val capsule = 999.dp
}

val EumShapes =
    Shapes(
        extraSmall = RoundedCornerShape(EumRadius.x4),
        small = RoundedCornerShape(EumRadius.x6),
        medium = RoundedCornerShape(EumRadius.x8),
        large = RoundedCornerShape(EumRadius.x12),
        extraLarge = RoundedCornerShape(EumRadius.x12),
    )

val EumCapsuleShape = RoundedCornerShape(EumRadius.capsule)

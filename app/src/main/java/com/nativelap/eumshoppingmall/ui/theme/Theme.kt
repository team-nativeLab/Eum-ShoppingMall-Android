package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/** The Figma file defines Light values only; system and dynamic palettes are intentionally disabled. */
@Composable
fun EumShoppingMallTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalEumColorTokens provides LightEumColorTokens) {
        MaterialTheme(
            colorScheme = EumLightColorScheme,
            typography = Typography,
            shapes = EumShapes,
            content = content,
        )
    }
}

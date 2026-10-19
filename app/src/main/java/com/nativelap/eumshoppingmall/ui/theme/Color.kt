package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Semantic colors from the Figma HF Color collection (Light mode). */
@Immutable
data class EumColorTokens(
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val background: Color,
    val backgroundSubtle: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val line: Color,
    val lineStrong: Color,
    val lineControl: Color,
    val inverse: Color,
    val inverseSecondary: Color,
    val dim: Color,
    val pressedDark: Color,
    val toast: Color,
    val placeholderGlyph: Color,
    val danger: Color,
    val dangerTint: Color,
    val success: Color,
    val successTint: Color,
    val warning: Color,
    val warningTint: Color,
    val info: Color,
    val infoTint: Color,
    val heartIdle: Color,
    val heartActive: Color,
    val chartBase: Color,
    val chartUp: Color,
)

val LightEumColorTokens =
    EumColorTokens(
        textPrimary = Color(0xFF171B19),
        textSecondary = Color(0xFF48534D),
        textTertiary = Color(0xFF65716A),
        textDisabled = Color(0xFF7D8982),
        background = Color(0xFFFAFBFA),
        backgroundSubtle = Color(0xFFF6F8F6),
        surface = Color(0xFFFFFFFF),
        surfaceSecondary = Color(0xFFF1F4F2),
        line = Color(0xFFD7DED9),
        lineStrong = Color(0xFF89938D),
        lineControl = Color(0xFF75817A),
        inverse = Color(0xFFFFFFFF),
        inverseSecondary = Color(0xFFF1F4F2),
        dim = Color(0x7A111111),
        pressedDark = Color(0xFF333333),
        toast = Color(0xFF222222),
        placeholderGlyph = Color(0xFFA6B0AA),
        danger = Color(0xFFC5221F),
        dangerTint = Color(0xFFFDECEA),
        success = Color(0xFF1E7A3C),
        successTint = Color(0xFFE8F3EC),
        warning = Color(0xFF8A5A00),
        warningTint = Color(0xFFFFF4E0),
        info = Color(0xFF1A5DC8),
        infoTint = Color(0xFFEAF1FB),
        heartIdle = Color(0xFF65716A),
        heartActive = Color(0xFFE5484D),
        chartBase = Color(0xFF8F8F8F),
        chartUp = Color(0xFFCC2E2E),
    )

val LocalEumColorTokens = staticCompositionLocalOf { LightEumColorTokens }

val MaterialTheme.eumColors: EumColorTokens
    @Composable
    @ReadOnlyComposable
    get() = LocalEumColorTokens.current

val EumLightColorScheme: ColorScheme =
    lightColorScheme(
        primary = LightEumColorTokens.info,
        onPrimary = LightEumColorTokens.inverse,
        primaryContainer = LightEumColorTokens.infoTint,
        onPrimaryContainer = LightEumColorTokens.info,
        secondary = LightEumColorTokens.textSecondary,
        onSecondary = LightEumColorTokens.inverse,
        secondaryContainer = LightEumColorTokens.surfaceSecondary,
        onSecondaryContainer = LightEumColorTokens.textPrimary,
        tertiary = LightEumColorTokens.success,
        onTertiary = LightEumColorTokens.inverse,
        tertiaryContainer = LightEumColorTokens.successTint,
        onTertiaryContainer = LightEumColorTokens.success,
        error = LightEumColorTokens.danger,
        onError = LightEumColorTokens.inverse,
        errorContainer = LightEumColorTokens.dangerTint,
        onErrorContainer = LightEumColorTokens.danger,
        background = LightEumColorTokens.background,
        onBackground = LightEumColorTokens.textPrimary,
        surface = LightEumColorTokens.surface,
        onSurface = LightEumColorTokens.textPrimary,
        surfaceVariant = LightEumColorTokens.surfaceSecondary,
        onSurfaceVariant = LightEumColorTokens.textSecondary,
        outline = LightEumColorTokens.lineControl,
        outlineVariant = LightEumColorTokens.lineStrong,
        scrim = LightEumColorTokens.dim,
        inverseSurface = LightEumColorTokens.toast,
        inverseOnSurface = LightEumColorTokens.inverse,
        surfaceTint = LightEumColorTokens.info,
    )

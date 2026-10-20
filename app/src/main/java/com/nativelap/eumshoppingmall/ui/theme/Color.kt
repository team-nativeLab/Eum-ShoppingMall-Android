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
    val heroScrimStart: Color,
    val heroScrimEnd: Color,
    val splashScrimStart: Color,
    val splashScrimMiddle: Color,
    val splashScrimEnd: Color,
    val imagePlaceholderStart: Color,
    val imagePlaceholderEnd: Color,
    val ratingStar: Color,
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
)

val LightEumColorTokens =
    EumColorTokens(
        textPrimary = Color(0xFF111111),
        textSecondary = Color(0xFF555555),
        textTertiary = Color(0xFF707070),
        textDisabled = Color(0xFFB3B3B3),
        background = Color(0xFFFFFFFF),
        backgroundSubtle = Color(0xFFFAFAFA),
        surface = Color(0xFFF5F5F5),
        surfaceSecondary = Color(0xFFEBEBEB),
        line = Color(0xFFEBEBEB),
        lineStrong = Color(0xFFD4D4D4),
        lineControl = Color(0xFF949494),
        inverse = Color(0xFFFFFFFF),
        inverseSecondary = Color(0xB8FFFFFF),
        dim = Color(0x7A111111),
        heroScrimStart = Color(0x99121212),
        heroScrimEnd = Color(0x0D121212),
        splashScrimStart = Color(0x260D0D0F),
        splashScrimMiddle = Color(0x400D0D0F),
        splashScrimEnd = Color(0x800D0D0F),
        imagePlaceholderStart = Color(0xFFF2F2F2),
        imagePlaceholderEnd = Color(0xFFE8E8E8),
        ratingStar = Color(0xFFF5B301),
        pressedDark = Color(0xFF333333),
        toast = Color(0xFF222222),
        placeholderGlyph = Color(0xFFD6D6D6),
        danger = Color(0xFFC5221F),
        dangerTint = Color(0xFFFDECEA),
        success = Color(0xFF1E7A3C),
        successTint = Color(0xFFE8F3EC),
        warning = Color(0xFF8A5A00),
        warningTint = Color(0xFFFFF4E0),
        info = Color(0xFF1A5DC8),
        infoTint = Color(0xFFEAF1FB),
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
        surface = LightEumColorTokens.background,
        onSurface = LightEumColorTokens.textPrimary,
        surfaceVariant = LightEumColorTokens.surface,
        onSurfaceVariant = LightEumColorTokens.textSecondary,
        outline = LightEumColorTokens.lineControl,
        outlineVariant = LightEumColorTokens.lineStrong,
        scrim = LightEumColorTokens.dim,
        inverseSurface = LightEumColorTokens.toast,
        inverseOnSurface = LightEumColorTokens.inverse,
        surfaceTint = LightEumColorTokens.info,
    )

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
    val lineNeutral: Color,
    val lineStrong: Color,
    val lineControl: Color,
    val inverse: Color,
    val inverseSecondary: Color,
    // 기본 강조색(Figma primary #111). 기존 info(파랑)를 대체했고, info 토큰은 사용처가 없어 제거했다.
    val primary: Color,
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
    val heartIdle: Color,
    val heartActive: Color,
    val ratingStarOutline: Color,
    val chartBase: Color,
    val chartUp: Color,
    // Figma A-46 주소 검색 값은 기존 토큰과 정확히 일치하지 않아 신규 semantic 토큰으로 둔다.
    val addressMuted: Color,
    val addressPlaceholder: Color,
    val addressRoad: Color,
    val addressDivider: Color,
    // Figma 체크박스 체크 상태는 흑백 톤이라 Material 기본 primary(파란색) 대신 쓴다. 체크 상태 Figma 값은 미확인 상태에서 textPrimary와 같은 검정 계열로 둔다.
    val checkboxChecked: Color,
    // Figma 상품 카드(179:743) 이미지 박스 회색은 기존 placeholder 토큰과 달라 신규 토큰으로 둔다.
    val productImageBackground: Color,
    // Figma 상품 카드 견적 배지 테두리(#8C8C8C)는 기존 토큰과 달라 신규 토큰으로 둔다.
    val productBadgeBorder: Color,
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
        lineNeutral = Color(0xFFE5E5E5),
        lineStrong = Color(0xFF89938D),
        lineControl = Color(0xFF75817A),
        inverse = Color(0xFFFFFFFF),
        inverseSecondary = Color(0xFFF1F4F2),
        primary = Color(0xFF111111),
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
        placeholderGlyph = Color(0xFFA6B0AA),
        danger = Color(0xFFC5221F),
        dangerTint = Color(0xFFFDECEA),
        success = Color(0xFF1E7A3C),
        successTint = Color(0xFFE8F3EC),
        warning = Color(0xFF8A5A00),
        warningTint = Color(0xFFFFF4E0),
        heartIdle = Color(0xFF65716A),
        heartActive = Color(0xFFE5484D),
        ratingStarOutline = Color(0xFFA96F16),
        chartBase = Color(0xFF8F8F8F),
        chartUp = Color(0xFFCC2E2E),
        addressMuted = Color(0xFF666666),
        addressPlaceholder = Color(0xFFA0A0A0),
        addressRoad = Color(0xFF1F2126),
        addressDivider = Color(0xFFEEEEEE),
        checkboxChecked = Color(0xFF171B19),
        productImageBackground = Color(0xFFEEEEEE),
        productBadgeBorder = Color(0xFF8C8C8C),
    )

val LocalEumColorTokens = staticCompositionLocalOf { LightEumColorTokens }

val MaterialTheme.eumColors: EumColorTokens
    @Composable
    @ReadOnlyComposable
    get() = LocalEumColorTokens.current

val EumLightColorScheme: ColorScheme =
    lightColorScheme(
        primary = LightEumColorTokens.primary,
        onPrimary = LightEumColorTokens.inverse,
        // 파랑 계열 컨테이너를 중립 표면으로 바꿔 강조 색이 파랑으로 남지 않게 한다.
        primaryContainer = LightEumColorTokens.surfaceSecondary,
        onPrimaryContainer = LightEumColorTokens.textPrimary,
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
        // 상단 tonal elevation 틴트는 중립 흰색으로 둔다. 파랑 틴트가 시트·다이얼로그에 번지지 않게 한다.
        surfaceTint = LightEumColorTokens.surface,
        // 기본 M3 surfaceContainer 값(보라 계열)이 ModalBottomSheet·AlertDialog에 새지 않도록 명시한다.
        surfaceContainerLowest = LightEumColorTokens.surface,
        surfaceContainerLow = LightEumColorTokens.surface,
        surfaceContainer = LightEumColorTokens.surface,
        surfaceContainerHigh = LightEumColorTokens.surface,
        surfaceContainerHighest = LightEumColorTokens.surfaceSecondary,
        surfaceBright = LightEumColorTokens.surface,
        surfaceDim = LightEumColorTokens.backgroundSubtle,
    )

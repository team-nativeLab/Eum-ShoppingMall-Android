package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/** Typography styles from the Figma app page. Figma letter spacing is stored as a percentage. */
object EumTypographyTokens {
    private val fontFamily = FontFamily.Default

    val webDisplayL = style(44, 56, FontWeight.Bold, -0.015.em)
    val webBodyXl = style(18, 30, FontWeight.Normal, 0.em)
    val display = style(32, 42, FontWeight.Bold, -0.025.em)
    val h1 = style(26, 34, FontWeight.Bold, -0.025.em)
    val h2 = style(22, 30, FontWeight.Bold, -0.02.em)
    val h3 = style(18, 26, FontWeight.Bold, -0.015.em)
    val title = style(16, 24, FontWeight.Medium, -0.01.em)
    val bodyL = style(16, 26, FontWeight.Normal, -0.01.em)
    val bodyLMedium = style(16, 26, FontWeight.Medium, -0.01.em)
    val bodyM = style(15, 24, FontWeight.Normal, -0.005.em)
    val bodyS = style(14, 20, FontWeight.Normal, -0.005.em)
    val bodySMedium = style(14, 20, FontWeight.Medium, -0.005.em)
    val labelL = style(16, 24, FontWeight.Medium, -0.005.em)
    val labelM = style(14, 20, FontWeight.Medium, -0.005.em)
    val labelS = style(12, 16, FontWeight.Medium, 0.em)
    val labelXs = style(11, 14, FontWeight.Medium, 0.em)
    val labelXsMedium = style(11, 14, FontWeight.Medium, 0.em)
    val caption = style(12, 16, FontWeight.Normal, 0.em)
    val captionMedium = style(12, 16, FontWeight.Medium, 0.em)
    val priceL = style(24, 32, FontWeight.Bold, -0.015.em)
    val priceM = style(16, 22, FontWeight.Bold, -0.005.em)
    val overline = style(11, 16, FontWeight.Medium, 0.16.em)

    private fun style(
        fontSize: Int,
        lineHeight: Int,
        fontWeight: FontWeight,
        letterSpacing: androidx.compose.ui.unit.TextUnit,
    ) = TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = letterSpacing,
    )
}

val Typography =
    Typography(
        displayLarge = EumTypographyTokens.webDisplayL,
        displayMedium = EumTypographyTokens.display,
        displaySmall = EumTypographyTokens.h1,
        headlineLarge = EumTypographyTokens.h1,
        headlineMedium = EumTypographyTokens.h2,
        headlineSmall = EumTypographyTokens.h3,
        titleLarge = EumTypographyTokens.title,
        titleMedium = EumTypographyTokens.labelL,
        titleSmall = EumTypographyTokens.labelM,
        bodyLarge = EumTypographyTokens.bodyL,
        bodyMedium = EumTypographyTokens.bodyM,
        bodySmall = EumTypographyTokens.bodyS,
        labelLarge = EumTypographyTokens.labelL,
        labelMedium = EumTypographyTokens.labelM,
        labelSmall = EumTypographyTokens.labelXs,
    )

package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.common.EumCtaButtonHeight
import com.nativelap.eumshoppingmall.ui.common.EumDialogButtonHeight
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class EumDesignTokenTest {
    private val tokens = LightEumColorTokens

    @Test
    fun neutralColors_matchFigmaVariables() {
        assertEquals(Color(0xFFFAFAFA), tokens.background)
        assertEquals(Color(0xFF171717), tokens.textPrimary)
        assertEquals(Color(0xFF4A4A4A), tokens.textSecondary)
        assertEquals(Color(0xFF666666), tokens.textTertiary)
        assertEquals(Color(0xFFE5E5E5), tokens.line)
        assertEquals(Color(0xFF8C8C8C), tokens.lineStrong)
        assertEquals(Color(0xFFA6A6A6), tokens.placeholderGlyph)
        assertEquals(Color(0xFFF2F2F2), tokens.surfaceSecondary)
        assertEquals(Color(0xFF707070), tokens.textDisabled)
    }

    @Test
    fun additionalColors_matchFigmaVariables() {
        assertEquals(Color(0xFF777777), tokens.lineControl)
        assertEquals(Color(0xFFF2F2F2), tokens.inverseSecondary)
        assertEquals(Color(0xFF666666), tokens.heartIdle)
        assertEquals(Color(0xFFC5221F), tokens.danger)
        assertEquals(Color(0xFFFDECEA), tokens.dangerTint)
        assertEquals(Color(0xFF171717), tokens.checkboxChecked)
        assertEquals(Color(0xFFE7F5F1), tokens.accentTint)
    }

    @Test
    fun ctaButtonHeight_isFixedAt56dp() {
        assertEquals(56.dp, EumCtaButtonHeight)
    }

    @Test
    fun backgroundSubtle_matchesFigmaVariable() {
        assertEquals(Color(0xFFFAFAFA), tokens.backgroundSubtle)
    }

    @Test
    fun dialogButtonHeight_isFixedAt52dp() {
        assertEquals(52.dp, EumDialogButtonHeight)
    }

    @Test
    fun radiusTokens_includeEleven_andFourteen() {
        assertEquals(11.dp, EumRadius.x11)
        assertEquals(14.dp, EumRadius.x14)
    }

    @Test
    fun labelS_is12Over16Medium() {
        assertEquals(12f, EumTypographyTokens.labelS.fontSize.value)
        assertEquals(16f, EumTypographyTokens.labelS.lineHeight.value)
        assertEquals(FontWeight.Medium, EumTypographyTokens.labelS.fontWeight)
    }

    @Test
    fun typographyLabelSmall_mapsToLabelS() {
        assertEquals(EumTypographyTokens.labelS, Typography.labelSmall)
    }

    @Test
    fun colorsXml_matchesColorTokens() {
        val workingDirectory = System.getProperty("user.dir").orEmpty()
        val colorsXmlFile = listOf(
            File(workingDirectory, "src/main/res/values/colors.xml"),
            File(workingDirectory, "app/src/main/res/values/colors.xml"),
        ).firstOrNull { it.exists() }
        val colorsXml = checkNotNull(colorsXmlFile) { "colors.xml을 찾지 못했습니다. user.dir=$workingDirectory" }.readText()
        fun xmlColor(name: String): Color {
            val colorMatch = checkNotNull(Regex("name=\"$name\">#([0-9A-Fa-f]{8})<").find(colorsXml)) { "colors.xml에 $name 이 없습니다." }
            val hex = colorMatch.groupValues[1]
            return Color(hex.toLong(16))
        }
        assertEquals(tokens.textPrimary, xmlColor("hf_text_1"))
        assertEquals(tokens.textSecondary, xmlColor("hf_text_2"))
        assertEquals(tokens.textTertiary, xmlColor("hf_text_3"))
        assertEquals(tokens.textDisabled, xmlColor("hf_text_disabled"))
        assertEquals(tokens.background, xmlColor("hf_bg"))
        assertEquals(tokens.backgroundSubtle, xmlColor("hf_bg_subtle"))
        assertEquals(tokens.surfaceSecondary, xmlColor("hf_surface_2"))
        assertEquals(tokens.line, xmlColor("hf_line"))
        assertEquals(tokens.lineStrong, xmlColor("hf_line_strong"))
        assertEquals(tokens.lineControl, xmlColor("hf_line_control"))
        assertEquals(tokens.inverseSecondary, xmlColor("hf_inverse_2"))
        assertEquals(tokens.placeholderGlyph, xmlColor("hf_ph_glyph"))
        assertEquals(tokens.heartIdle, xmlColor("hf_heart_idle"))
    }
}

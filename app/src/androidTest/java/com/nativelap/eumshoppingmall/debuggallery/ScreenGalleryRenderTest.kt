package com.nativelap.eumshoppingmall.debuggallery

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Rule
import org.junit.Test

class ScreenGalleryRenderTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun openSourceLicensesEntry_rendersWithoutCrash() {
        val licensesEntry = checkNotNull(GalleryRegistry.find("A-57"))
        composeRule.setContent {
            EumShoppingMallTheme {
                ScreenGalleryDetailScreen(
                    entry = licensesEntry,
                    widthDp = 390,
                    fontScale = 1.0f,
                    onWidthChange = {},
                    onFontScaleChange = {},
                    onBackClick = {},
                )
            }
        }
        composeRule.onNodeWithText(licensesEntry.title).assertIsDisplayed()
    }
}

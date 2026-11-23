package com.nativelap.eumshoppingmall.ui.quote

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteRequestRoute
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuoteRequestProductErrorSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun tappingSubmitWithZeroProductsShowsProductRequiredError() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val submitButtonText = context.getString(R.string.quote_submit_label)
        val productRequiredErrorText = context.getString(R.string.quote_product_required_error)

        composeRule.setContent {
            EumShoppingMallTheme {
                QuoteRequestRoute(
                    uiModel = QuoteRequestUiModel(products = emptyList()),
                    pickedProductId = null,
                    productById = { productId -> error("상품이 없으므로 조회되지 않아야 합니다: $productId") },
                    onBackClick = {},
                    onAddProductClick = {},
                    onPickedProductConsumed = {},
                    onSubmit = {},
                    onUnavailableAction = {},
                )
            }
        }

        composeRule.onAllNodesWithText(productRequiredErrorText).assertCountEquals(0)

        composeRule.onNodeWithText(submitButtonText).performClick()

        composeRule.onNodeWithText(productRequiredErrorText).assertIsDisplayed()
    }
}

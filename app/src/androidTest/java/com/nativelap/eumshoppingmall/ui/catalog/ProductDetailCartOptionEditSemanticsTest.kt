package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDetailCartOptionEditSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun editSheetShowsFixedQuantityWithoutRemoveOrStepperControls() {
        val option = CatalogOptionUiModel("option-1", "표준형", quantity = 3, priceLabel = null, unitPriceWon = 0L)
        val group = CatalogOptionGroupUiModel("group-1", "옵션", listOf(option))
        val uiModel = CatalogProductDetailUiModel(
            product = CatalogProductUiModel("product-1", "상품", CatalogPriceUiModel.Available("₩1,000", unitPriceWon = 1_000)),
            optionGroups = listOf(group),
            selectedOptions = listOf(CatalogSelectedOptionUiModel(group.id, option)),
            optionsSheetVisible = true,
            isCartOptionEdit = true,
        )

        composeRule.setContent {
            EumShoppingMallTheme {
                CatalogProductDetailScreen(uiModel = uiModel, onEvent = {})
            }
        }

        composeRule.onAllNodesWithText("옵션 변경").assertCountEquals(2)
        composeRule.onAllNodesWithText("표준형").assertCountEquals(1)
        composeRule.onAllNodesWithContentDescription("표준형 옵션 삭제").assertCountEquals(0)
        composeRule.onAllNodesWithContentDescription("수량 줄이기").assertCountEquals(0)
        composeRule.onAllNodesWithContentDescription("수량 늘리기").assertCountEquals(0)
    }

    @Test
    fun selectingOptionInvokesReplacementCallbackExactlyOnce() {
        val replacements = mutableListOf<Pair<String, CartLine>>()
        var backClicks = 0
        composeRule.setContent {
            EumShoppingMallTheme {
                editRoute(
                    onBackClick = { backClicks++ },
                    onReplaceCartLine = { lineId, replacement ->
                        replacements.add(lineId to replacement)
                        true
                    },
                )
            }
        }

        composeRule.onNodeWithText("선택").performClick()
        composeRule.onNodeWithText("대체 옵션").performClick()
        composeRule.onAllNodesWithText("옵션 변경")[1].performClick()
        composeRule.waitUntil(5_000) { replacements.isNotEmpty() }

        assertEquals(1, replacements.size)
        assertEquals("product-1/old-option", replacements.single().first)
        assertEquals("new-option", replacements.single().second.optionId)
        assertEquals(3, replacements.single().second.quantity)
        assertEquals(1, backClicks)
    }

    @Test
    fun closingThenGoingBackNeverInvokesReplacementCallback() {
        val replacements = mutableListOf<Pair<String, CartLine>>()
        var backClicks = 0
        composeRule.setContent {
            EumShoppingMallTheme {
                editRoute(
                    onBackClick = { backClicks++ },
                    onReplaceCartLine = { lineId, replacement ->
                        replacements.add(lineId to replacement)
                        true
                    },
                )
            }
        }

        composeRule.onNodeWithContentDescription("닫기").performClick()
        composeRule.onNodeWithContentDescription("뒤로").performClick()

        assertEquals(0, replacements.size)
        assertEquals(1, backClicks)
    }

    @Composable
    private fun editRoute(
        onBackClick: () -> Unit,
        onReplaceCartLine: suspend (String, CartLine) -> Boolean,
    ) {
        val sourceLine = CartLine(
            productId = "product-1",
            productName = "상품",
            optionId = "old-option",
            optionLabel = "기존 옵션",
            quantity = 3,
            optionExtraWon = 0,
            productPriceLabel = "₩1,000",
            isQuoteRequired = false,
            productUnitPriceWon = 1_000,
        )
        val options = listOf(
            CatalogOptionUiModel("old-option", "기존 옵션", 1, null, 0),
            CatalogOptionUiModel("new-option", "대체 옵션", 1, "₩500", 500),
        )
        CatalogProductDetailRoute(
            uiModel = CatalogProductDetailUiModel(
                product = CatalogProductUiModel("product-1", "상품", CatalogPriceUiModel.Available("₩1,000", unitPriceWon = 1_000)),
                optionGroups = listOf(CatalogOptionGroupUiModel("group-1", "유형", options)),
            ),
            onBackClick = onBackClick,
            onCartClick = {},
            onBuyNowClick = {},
            onQuoteRequestClick = {},
            onProductClick = {},
            isLoggedIn = true,
            onLoginRequired = {},
            onAllReviewsClick = {},
            onReviewWriteClick = {},
            onQuestionWriteClick = {},
            onAddToCart = { error("편집 흐름에서 일반 담기를 호출하면 안 됩니다") },
            editSourceLineId = sourceLine.lineId(),
            editSourceLine = sourceLine,
            onReplaceCartLine = onReplaceCartLine,
            cartAddOutcome = null,
            onCartAddOutcomeChange = {},
        )
    }
}

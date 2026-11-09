package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatalogSearchResultsRouteTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun popularSearchOnEmptyResultsOpensPopularSearchesInsteadOfGoingBack() {
        var popularSearchClickCount = 0
        var backClickCount = 0
        composeRule.setContent {
            EumShoppingMallTheme {
                CatalogSearchResultsRoute(
                    uiModel = CatalogSearchResultsUiModel(
                        query = "없는 상품",
                        resultCount = 0,
                        sortLabel = "",
                    ),
                    selectedTab = MainNavigationTab.Home,
                    cartItemCount = 0,
                    onTabSelected = {},
                    onBackClick = { backClickCount++ },
                    onPopularSearchClick = { popularSearchClickCount++ },
                    onSearch = {},
                    onProductClick = {},
                    onQuoteRequestClick = {},
                )
            }
        }

        composeRule.onNodeWithText(POPULAR_SEARCH_BUTTON_TEXT).performClick()

        assertEquals(1, popularSearchClickCount)
        assertEquals(0, backClickCount)
    }

    private companion object {
        const val POPULAR_SEARCH_BUTTON_TEXT = "인기 검색어 보기"
    }
}

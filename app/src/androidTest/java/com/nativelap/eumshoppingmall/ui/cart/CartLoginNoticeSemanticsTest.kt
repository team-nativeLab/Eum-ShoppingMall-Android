package com.nativelap.eumshoppingmall.ui.cart

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.ui.cart.view.CartScreen
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartLoginNoticeSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginNoticeIsDisplayedWhenUserIsNotLoggedIn() {
        composeRule.setContent {
            EumShoppingMallTheme {
                CartScreen(uiModel = CartUiModel(), onEvent = {}, isLoggedIn = false)
            }
        }

        composeRule.onNodeWithText(LOGIN_NOTICE_TEXT).assertIsDisplayed()
    }

    @Test
    fun loginNoticeIsNotDisplayedWhenUserIsLoggedIn() {
        composeRule.setContent {
            EumShoppingMallTheme {
                CartScreen(uiModel = CartUiModel(), onEvent = {}, isLoggedIn = true)
            }
        }

        composeRule.onAllNodesWithText(LOGIN_NOTICE_TEXT).assertCountEquals(0)
    }

    private companion object {
        const val LOGIN_NOTICE_TEXT = "로그인하면 장바구니가 저장됩니다."
    }
}

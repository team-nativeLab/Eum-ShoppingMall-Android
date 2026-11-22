package com.nativelap.eumshoppingmall.ui.orders

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimRequestFormRoute
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.screendata.SampleShoppingScreenDataProvider
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClaimRequestQuantityRestorationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectedQuantityAndTotalSurviveSavedStateRestoreAndRespectBoundaries() {
        val restorationTester = StateRestorationTester(composeRule)
        restorationTester.setContent {
            EumShoppingMallTheme {
                ClaimRequestFormRoute(
                    uiModel = SampleShoppingScreenDataProvider.claimForm("order-2", OrderRequestType.Return),
                    onBackClick = {},
                    onSubmit = {},
                    onUnavailableAction = {},
                )
            }
        }

        composeRule.onNodeWithText("2개").assertExists()
        composeRule.onNodeWithContentDescription("수량 줄이기").performClick()
        composeRule.onNodeWithText("1개").assertExists()
        composeRule.onNodeWithContentDescription("수량 줄이기").performClick()
        composeRule.onNodeWithText("1개").assertExists()

        restorationTester.emulateSavedInstanceStateRestore()

        composeRule.onNodeWithText("1개").assertExists()
        composeRule.onNodeWithContentDescription("수량 늘리기").performClick()
        composeRule.onNodeWithText("2개").assertExists()
        composeRule.onNodeWithContentDescription("수량 늘리기").performClick()
        composeRule.onNodeWithText("2개").assertExists()
    }

    @Test
    fun invalidOrderShowsNoticeAndDisablesSubmission() {
        composeRule.setContent {
            EumShoppingMallTheme {
                ClaimRequestFormRoute(
                    uiModel = SampleShoppingScreenDataProvider.claimForm("missing-order", OrderRequestType.Return),
                    onBackClick = {},
                    onSubmit = {},
                    onUnavailableAction = {},
                )
            }
        }

        composeRule.onNodeWithText("주문 정보를 확인할 수 없습니다. 주문 내역에서 다시 선택해 주세요.").assertExists()
        composeRule.onNodeWithText("반품 신청하기").assertIsNotEnabled()
    }
}

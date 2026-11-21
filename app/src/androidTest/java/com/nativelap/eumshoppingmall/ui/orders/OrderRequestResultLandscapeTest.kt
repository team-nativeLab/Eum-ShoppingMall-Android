package com.nativelap.eumshoppingmall.ui.orders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestResultScreen
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrderRequestResultLandscapeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun localMockResultAndDemoNoticeRemainScrollableInLandscapeHeight() {
        val resultStatusDescription = "데모 신청입니다. 실제 저장이나 서버 전송은 되지 않았습니다. 신청 품목: 안전화 2개"

        composeRule.setContent {
            EumShoppingMallTheme {
                Box(Modifier.requiredSize(width = 720.dp, height = 320.dp)) {
                    OrderRequestResultScreen(
                        uiModel = OrderRequestResultUiModel("LOCAL-MOCK", "데모 신청", resultStatusDescription),
                        onEvent = { _ -> },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }

        composeRule.onNodeWithText("LOCAL-MOCK").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("데모 결과는 현재 화면에서만 확인할 수 있으며 신청 내역에 저장되지 않습니다.").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText(resultStatusDescription).performScrollTo().assertIsDisplayed()
    }
}

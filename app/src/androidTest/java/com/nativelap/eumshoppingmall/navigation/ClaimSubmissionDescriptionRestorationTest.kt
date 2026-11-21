package com.nativelap.eumshoppingmall.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClaimSubmissionDescriptionRestorationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun localDemoSubmissionDescriptionSurvivesSavedStateRestore() {
        val restorationTester = StateRestorationTester(composeRule)
        lateinit var summaries: MutableState<Map<String, String>>
        restorationTester.setContent {
            EumShoppingMallTheme {
                summaries = rememberClaimSubmissionDescriptions()
                Text(summaries.value["order-2"] ?: "신청 상세 없음")
            }
        }

        composeRule.runOnIdle {
            summaries.value = summaries.value + ("order-2" to "데모 신청입니다. 안전화 2개")
        }
        composeRule.onNodeWithText("데모 신청입니다. 안전화 2개").assertExists()

        restorationTester.emulateSavedInstanceStateRestore()

        composeRule.onNodeWithText("데모 신청입니다. 안전화 2개").assertExists()
    }
}

package com.nativelap.eumshoppingmall.ui.account

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.ui.account.view.AccountAddressFormScreen
import com.nativelap.eumshoppingmall.ui.account.view.AccountProfileScreen
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressFormUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileEditUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountUiSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun profileFieldExposesItsVisibleLabelAsFieldDescription() {
        composeRule.setContent {
            EumShoppingMallTheme {
                AccountProfileScreen(
                    uiModel = AccountProfileEditUiModel(profile = previewProfile()),
                    isPasswordVerification = false,
                    onEvent = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("이름").assertIsDisplayed()
    }

    @Test
    fun addressInputExposesItsVisibleLabelOnlyOnceToAccessibility() {
        composeRule.setContent {
            EumShoppingMallTheme {
                AccountAddressFormScreen(
                    uiModel = AccountAddressFormUiModel(address = previewAddress(), isEditMode = false),
                    onEvent = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("배송지명").assertIsDisplayed()
        // 라벨 Text는 clearAndSetSemantics로 텍스트 시맨틱을 비우고, 필드 contentDescription이 유일한 읽기 대상이다.
        composeRule.onAllNodesWithText("배송지명", useUnmergedTree = true).assertCountEquals(0)
        composeRule.onAllNodesWithContentDescription("배송지명", useUnmergedTree = true).assertCountEquals(1)
    }

    @Test
    fun consentLabelAndCheckboxShareToggleSemantics() {
        val changedValues = mutableListOf<Boolean>()
        composeRule.setContent {
            EumShoppingMallTheme {
                AccountProfileScreen(
                    uiModel = AccountProfileEditUiModel(
                        profile = previewProfile().copy(isSmsConsentEnabled = false),
                    ),
                    isPasswordVerification = false,
                    onEvent = { event ->
                        if (event is AccountProfileScreenEvent.SmsConsentChanged) {
                            changedValues += event.enabled
                        }
                    },
                )
            }
        }

        // 수신 동의 행은 LazyColumn 하단에 있어 노드를 구성하려면 스크롤이 필요하다.
        // 화면에 스크롤 노드가 둘 이상일 수 있어, 첫 항목 텍스트를 가진 스크롤 노드로 대상을 특정한다.
        composeRule
            .onNode(hasScrollAction() and hasAnyDescendant(hasText("기본 정보")))
            .performScrollToNode(hasText("SMS 수신 동의"))
        composeRule.onNodeWithText("SMS 수신 동의")
            .assertIsToggleable()
            .performClick()

        composeRule.runOnIdle {
            assertEquals(listOf(true), changedValues)
        }
    }

    private fun previewAddress() = AccountAddressUiModel(
        id = "address-1",
        label = "집",
        recipient = "홍길동",
        phoneNumber = "010-0000-0000",
        postalCode = "00000",
        address = "서울",
        addressDetail = "1층",
        deliveryRequest = "",
        isDefault = true,
    )

    private fun previewProfile() = AccountProfileUiModel(
        name = "홍길동",
        email = "example@email.com",
        phoneNumber = "010-0000-0000",
        companyName = "회사명",
        businessRegistrationNumber = "000-00-00000",
        department = "안전관리팀",
        isBusinessMember = true,
        isSmsConsentEnabled = true,
        isEmailConsentEnabled = false,
    )
}

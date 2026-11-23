package com.nativelap.eumshoppingmall.ui.checkout

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentScreen
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckoutPaymentBlockReasonSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun tappingAgreementBlockReasonMovesFocusToAgreementRow() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val blockReasonText = context.getString(R.string.checkout_payment_block_agreement)
        val agreementRowText = context.getString(R.string.checkout_order_agreement)

        composeRule.setContent {
            EumShoppingMallTheme {
                CheckoutPaymentScreen(
                    uiModel = checkoutModel(paymentMethod = CheckoutPaymentMethod.CreditCard, isAgreementAccepted = false),
                    onEvent = {},
                )
            }
        }

        composeRule.onNodeWithText(blockReasonText).performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            isAgreementRowFocused(agreementRowText)
        }
        composeRule.onNode(isToggleable() and hasText(agreementRowText)).assertIsFocused()
    }

    @Test
    fun nonAgreementBlockReasonHasNoClickAction() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val blockReasonText = context.getString(R.string.checkout_payment_block_method)

        composeRule.setContent {
            EumShoppingMallTheme {
                CheckoutPaymentScreen(
                    uiModel = checkoutModel(paymentMethod = null, isAgreementAccepted = true),
                    onEvent = {},
                )
            }
        }

        composeRule.onNodeWithText(blockReasonText).assertHasNoClickAction()
    }

    private fun isAgreementRowFocused(agreementRowText: String): Boolean = runCatching {
        composeRule.onNode(isToggleable() and hasText(agreementRowText)).assertIsFocused()
    }.isSuccess

    private fun checkoutModel(
        paymentMethod: CheckoutPaymentMethod?,
        isAgreementAccepted: Boolean,
    ) = CheckoutUiModel(
        address = CheckoutAddressUiModel(
            recipientName = "홍길동",
            contactLabel = "010-0000-0000",
            addressLabel = "서울특별시 중구 세종대로 110",
        ),
        deliveryMethodLabel = "택배 배송",
        paymentMethod = paymentMethod,
        receiptType = CheckoutReceiptType.None,
        paymentSummary = CheckoutPaymentSummaryUiModel(
            productAmountLabel = "113,000원",
            shippingFeeLabel = "무료",
            discountLabel = "0원",
            finalAmountLabel = "113,000원",
            shippingFeeWon = 0L,
            finalAmountWon = 113_000L,
        ),
        isOrderAgreementAccepted = isAgreementAccepted,
    )
}

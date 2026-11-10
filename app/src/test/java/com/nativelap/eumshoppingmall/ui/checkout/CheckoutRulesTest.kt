package com.nativelap.eumshoppingmall.ui.checkout

import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentBlockReason
import com.nativelap.eumshoppingmall.ui.checkout.view.checkoutPaymentBlockReason
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.shippingFeeText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CheckoutRulesTest {
    @Test
    fun missingPaymentMethodIsTheFirstBlockReasonEvenWhenOtherRulesFailToo() {
        val reason = checkoutPaymentBlockReason(
            model(paymentMethod = null, agreed = false, receiptType = CheckoutReceiptType.CashReceipt, phone = "abc"),
        )

        assertEquals(CheckoutPaymentBlockReason.NoPaymentMethod, reason)
    }

    @Test
    fun unacceptedAgreementIsReportedBeforeInvalidReceipt() {
        val reason = checkoutPaymentBlockReason(
            model(paymentMethod = CheckoutPaymentMethod.entries.first(), agreed = false, receiptType = CheckoutReceiptType.CashReceipt, phone = "abc"),
        )

        assertEquals(CheckoutPaymentBlockReason.AgreementNotAccepted, reason)
    }

    @Test
    fun invalidCashReceiptPhoneIsReportedWhenEverythingElseIsFine() {
        val reason = checkoutPaymentBlockReason(
            model(paymentMethod = CheckoutPaymentMethod.entries.first(), agreed = true, receiptType = CheckoutReceiptType.CashReceipt, phone = "abc"),
        )

        assertEquals(CheckoutPaymentBlockReason.InvalidReceiptDetails, reason)
    }

    @Test
    fun completeInputHasNoBlockReason() {
        assertNull(
            checkoutPaymentBlockReason(
                model(paymentMethod = CheckoutPaymentMethod.entries.first(), agreed = true, receiptType = CheckoutReceiptType.None),
            ),
        )
    }

    @Test
    fun zeroWonShippingFeeShowsFreeLabel() {
        assertEquals("무료", summary(shippingFeeWon = 0L).shippingFeeText(freeLabel = "무료", unknownLabel = "확인 필요"))
    }

    @Test
    fun positiveShippingFeeShowsAmount() {
        assertEquals("₩3,000", summary(shippingFeeWon = 3_000L).shippingFeeText(freeLabel = "무료", unknownLabel = "확인 필요"))
    }

    @Test
    fun amountDerivesTheLabelEvenWhenSuppliedLabelDisagrees() {
        val mismatched = summary(shippingFeeWon = 3_000L, shippingFeeLabel = "₩9,999")

        assertEquals("₩3,000", mismatched.shippingFeeText(freeLabel = "무료", unknownLabel = "확인 필요"))
    }

    @Test
    fun unknownAmountFallsBackToSuppliedLabel() {
        assertEquals("₩3,000", summary(shippingFeeWon = null).shippingFeeText(freeLabel = "무료", unknownLabel = "확인 필요"))
    }

    @Test
    fun unknownAmountAndBlankLabelShowsUnknownNotice() {
        val blank = summary(shippingFeeWon = null, shippingFeeLabel = "")

        assertEquals("확인 필요", blank.shippingFeeText(freeLabel = "무료", unknownLabel = "확인 필요"))
    }

    private fun summary(
        shippingFeeWon: Long?,
        shippingFeeLabel: String = "₩3,000",
    ) = CheckoutPaymentSummaryUiModel(
        productAmountLabel = "₩18,000",
        shippingFeeLabel = shippingFeeLabel,
        discountLabel = "₩0",
        finalAmountLabel = "₩21,000",
        shippingFeeWon = shippingFeeWon,
    )

    private fun model(
        paymentMethod: CheckoutPaymentMethod?,
        agreed: Boolean,
        receiptType: CheckoutReceiptType = CheckoutReceiptType.None,
        phone: String = "",
    ) = CheckoutUiModel(
        address = CheckoutAddressUiModel(
            recipientName = "수령인",
            contactLabel = "010-0000-0000",
            addressLabel = "서울",
        ),
        deliveryMethodLabel = "일반 배송",
        paymentMethod = paymentMethod,
        receiptType = receiptType,
        receiptDetails = CheckoutReceiptDetailsUiModel(
            type = CheckoutReceiptDetailsType.CashReceiptPhone,
            phoneNumber = phone,
        ),
        paymentSummary = summary(shippingFeeWon = 3_000L),
        isOrderAgreementAccepted = agreed,
    )
}

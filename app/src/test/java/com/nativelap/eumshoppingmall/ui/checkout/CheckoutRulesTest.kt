package com.nativelap.eumshoppingmall.ui.checkout

import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentBlockReason
import com.nativelap.eumshoppingmall.ui.checkout.view.checkoutContentWidthForViewport
import com.nativelap.eumshoppingmall.ui.checkout.view.checkoutPaymentBlockReason
import com.nativelap.eumshoppingmall.ui.checkout.view.checkoutPaymentCanComplete
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutOrderLine
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutOrderSnapshot
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutOrderSource
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.withReceiptDetailsTypeSelection
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.withReceiptTypeSelection
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.shippingFeeText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import androidx.compose.ui.unit.dp

class CheckoutRulesTest {
    @Test
    fun landscapeCheckoutContentWidthIsCappedWhilePhoneWidthIsPreserved() {
        assertEquals(520.dp, checkoutContentWidthForViewport(960.dp))
        assertEquals(400.dp, checkoutContentWidthForViewport(400.dp))
    }

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
    fun defaultReceiptSelectionIsNotRequestedAndIsValid() {
        val defaultModel = model(
            paymentMethod = CheckoutPaymentMethod.entries.first(),
            agreed = true,
        ).copy(receiptDetails = CheckoutReceiptDetailsUiModel())

        assertEquals(CheckoutReceiptType.None, defaultModel.receiptType)
        assertNull(checkoutPaymentBlockReason(defaultModel))
    }

    @Test
    fun explicitCashReceiptWithNoDetailTypeIsBlocked() {
        val cashReceiptWithoutDetails = model(
            paymentMethod = CheckoutPaymentMethod.entries.first(),
            agreed = true,
            receiptType = CheckoutReceiptType.CashReceipt,
        ).copy(receiptDetails = CheckoutReceiptDetailsUiModel())

        assertEquals(CheckoutPaymentBlockReason.InvalidReceiptDetails, checkoutPaymentBlockReason(cashReceiptWithoutDetails))
    }

    @Test
    fun validCashReceiptPhoneIsAccepted() {
        val cashReceipt = model(
            paymentMethod = CheckoutPaymentMethod.entries.first(),
            agreed = true,
            receiptType = CheckoutReceiptType.CashReceipt,
            phone = "010-1234-5678",
        )

        assertNull(checkoutPaymentBlockReason(cashReceipt))
    }

    @Test
    fun switchingReceiptTypesClearsOldDetailsAndOffersTheNewTypeSelection() {
        val cashReceipt = model(
            paymentMethod = CheckoutPaymentMethod.entries.first(),
            agreed = true,
            receiptType = CheckoutReceiptType.CashReceipt,
            phone = "010-1234-5678",
        )
        val taxInvoice = cashReceipt.withReceiptTypeSelection(CheckoutReceiptType.TaxInvoice)

        assertEquals(CheckoutReceiptDetailsType.None, taxInvoice.receiptDetails.type)
        assertEquals("", taxInvoice.receiptDetails.phoneNumber)
        assertEquals("", taxInvoice.receiptDetails.registrationNumber)
        assertEquals("", taxInvoice.receiptDetails.businessName)
        val taxDetails = taxInvoice.withReceiptDetailsTypeSelection(CheckoutReceiptDetailsType.ManualBusinessInformation)
        assertEquals(CheckoutReceiptDetailsType.ManualBusinessInformation, taxDetails.receiptDetails.type)

        val businessInformation = taxDetails.copy(
            receiptDetails = taxDetails.receiptDetails.copy(
                registrationNumber = "1234567890",
                businessName = "이음건설",
                representativeName = "홍길동",
                email = "buyer@example.com",
            ),
        )
        val cashReceiptAgain = businessInformation.withReceiptTypeSelection(CheckoutReceiptType.CashReceipt)

        assertEquals(CheckoutReceiptDetailsType.None, cashReceiptAgain.receiptDetails.type)
        assertEquals("", cashReceiptAgain.receiptDetails.registrationNumber)
        assertEquals("", cashReceiptAgain.receiptDetails.businessName)
        assertEquals("", cashReceiptAgain.receiptDetails.representativeName)
        assertEquals("", cashReceiptAgain.receiptDetails.email)
        val cashDetails = cashReceiptAgain.withReceiptDetailsTypeSelection(CheckoutReceiptDetailsType.CashReceiptPhone)
        assertEquals(CheckoutReceiptDetailsType.CashReceiptPhone, cashDetails.receiptDetails.type)
        assertEquals("", cashDetails.receiptDetails.phoneNumber)
    }

    @Test
    fun koreanMobileNumberValidationUsesPrefixesSeparatorsAndLengthBoundaries() {
        listOf(
            "01012345678",
            "010-1234-5678",
            "010 1234 5678",
            "0111234567",
            "011-123-4567",
            "011 123 4567",
            "011-1234-5678",
            "011 1234 5678",
            "0161234567",
            "0171234567",
            "0181234567",
            "0191234567",
            "01912345678",
        ).forEach { phone ->
            assertNull(checkoutPaymentBlockReason(cashReceiptModel(phone)))
        }
        listOf("1234567890", "01212345678", "0101234567", "010123456789", "016123456", "0161234567890").forEach { phone ->
            assertEquals(CheckoutPaymentBlockReason.InvalidReceiptDetails, checkoutPaymentBlockReason(cashReceiptModel(phone)))
        }
    }

    @Test
    fun koreanMobileNumberRejectsEmptyPartialNonAsciiAndMisplacedHyphens() {
        listOf(
            "",
            "010-123",
            "٠١٠-١٢٣٤-٥٦٧٨",
            "-010-1234-5678",
            "010--1234-5678",
            "010-1234-5678-",
            "010  1234 5678",
            "010-1234 5678",
            "010 1234-5678",
        ).forEach { phone ->
            assertEquals(CheckoutPaymentBlockReason.InvalidReceiptDetails, checkoutPaymentBlockReason(cashReceiptModel(phone)))
        }
    }

    @Test
    fun unconfirmedOrderAmountBlocksPaymentBeforeOtherRules() {
        val unknownAmountModel = model(
            paymentMethod = CheckoutPaymentMethod.entries.first(),
            agreed = true,
            receiptType = CheckoutReceiptType.None,
        ).copy(paymentSummary = summary(shippingFeeWon = null).copy(finalAmountWon = null))

        assertEquals(CheckoutPaymentBlockReason.UnconfirmedOrderAmount, checkoutPaymentBlockReason(unknownAmountModel))
    }

    @Test
    fun paymentCompletionHandlerUsesTheSamePaymentValidationAsTheButton() {
        val confirmedSnapshot = CheckoutOrderSnapshot(
            lines = listOf(CheckoutOrderLine("helmet", "안전모", "기본", 1, 18_000L)),
            source = CheckoutOrderSource.SavedCart,
            cartLineIdsToRemove = setOf("helmet/default"),
        )
        val validUiModel = model(
            paymentMethod = CheckoutPaymentMethod.entries.first(),
            agreed = true,
            receiptType = CheckoutReceiptType.None,
        )

        assertTrue(checkoutPaymentCanComplete(confirmedSnapshot, validUiModel))
        assertFalse(checkoutPaymentCanComplete(confirmedSnapshot, validUiModel.copy(
            paymentSummary = validUiModel.paymentSummary.copy(finalAmountWon = 20_000L),
        )))
        assertFalse(checkoutPaymentCanComplete(confirmedSnapshot, validUiModel.copy(paymentMethod = null)))
        assertFalse(checkoutPaymentCanComplete(confirmedSnapshot, validUiModel.copy(isOrderAgreementAccepted = false)))
        assertFalse(checkoutPaymentCanComplete(confirmedSnapshot, validUiModel.copy(
            receiptType = CheckoutReceiptType.CashReceipt,
            receiptDetails = CheckoutReceiptDetailsUiModel(
                type = CheckoutReceiptDetailsType.CashReceiptPhone,
                phoneNumber = "invalid",
            ),
        )))
        assertTrue(checkoutPaymentCanComplete(confirmedSnapshot, validUiModel.copy(
            receiptType = CheckoutReceiptType.CashReceipt,
            receiptDetails = CheckoutReceiptDetailsUiModel(
                type = CheckoutReceiptDetailsType.CashReceiptPhone,
                phoneNumber = "010-1234-5678",
            ),
        )))
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
        finalAmountWon = 21_000L,
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

    private fun cashReceiptModel(phone: String) = model(
        paymentMethod = CheckoutPaymentMethod.entries.first(),
        agreed = true,
        receiptType = CheckoutReceiptType.CashReceipt,
        phone = phone,
    )
}

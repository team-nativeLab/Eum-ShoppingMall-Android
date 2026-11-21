package com.nativelap.eumshoppingmall.ui.orders.view

import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestReason
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.createOrderRequestSubmission
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.localClaimResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.orderRequestSubmissionSummary
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.selectedClaimAmount
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.updatedClaimQuantity
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.updatedClaimSelection
import com.nativelap.eumshoppingmall.ui.screendata.SampleShoppingScreenDataProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class ClaimRoutesTest {
    @Test
    fun beforeShippingStartsAsCancel() {
        listOf(OrderStatus.WaitingPayment, OrderStatus.PaymentComplete, OrderStatus.Preparing, null).forEach { status ->
            assertEquals(OrderRequestType.Cancel, defaultClaimType(status))
        }
    }

    @Test
    fun shippingOrDeliveredStartsAsReturn() {
        assertEquals(OrderRequestType.Return, defaultClaimType(OrderStatus.Shipping))
        assertEquals(OrderRequestType.Return, defaultClaimType(OrderStatus.Delivered))
    }

    @Test
    fun claimQuantityStaysWithinOrderedQuantity() {
        val product = OrderRequestProductUiModel("p", "상품", "", "", "1개", true, orderQuantity = 3, quantity = 1, unitPriceWon = 1000)

        assertEquals(1, updatedClaimQuantity(product, -1).quantity)
        assertEquals(2, updatedClaimQuantity(product, 1).quantity)
        assertEquals(3, updatedClaimQuantity(product.copy(quantity = 3), 1).quantity)
        assertEquals(1000L, selectedClaimAmount(listOf(updatedClaimQuantity(product.copy(quantity = 2), -1)), setOf("p")))
        assertEquals(3000L, updatedClaimQuantity(product.copy(quantity = 2), 1).quantity * product.unitPriceWon)
    }

    @Test
    fun debugClaimFormUsesTheSelectedOrderProductAndRejectsUnknownOrders() {
        val helmet = SampleShoppingScreenDataProvider.claimForm("order-1", OrderRequestType.Return)
        val boots = SampleShoppingScreenDataProvider.claimForm("order-2", OrderRequestType.Cancel)
        val unknown = SampleShoppingScreenDataProvider.claimForm("missing-order", OrderRequestType.Return)

        val helmetDetail = SampleShoppingScreenDataProvider.orderDetail("order-1")
        val bootsDetail = SampleShoppingScreenDataProvider.orderDetail("order-2")
        val selectableOrders = SampleShoppingScreenDataProvider.claimOrders().orders.associateBy { it.id }
        val historyOrders = SampleShoppingScreenDataProvider.orderHistory().orders.associateBy { it.id }
        val claimHistory = SampleShoppingScreenDataProvider.claimHistory().items.associateBy { it.orderId }
        val helmetClaimDetail = SampleShoppingScreenDataProvider.claimDetail(claimHistory.getValue("order-1").id)
        val bootsClaimDetail = SampleShoppingScreenDataProvider.claimDetail(claimHistory.getValue("order-2").id)

        assertOrderMapping(helmet, helmetDetail.products.single(), selectableOrders.getValue("order-1").products.single(), "세이프가드 ABS 안전모", "사이즈 L", 1, 18_000)
        assertOrderMapping(boots, bootsDetail.products.single(), selectableOrders.getValue("order-2").products.single(), "경량 안전화 4인치", "사이즈 270", 2, 54_000)
        assertEquals("₩108,000 · 2개", historyOrders.getValue("order-2").product.priceAndQuantityLabel)
        assertEquals(bootsDetail.products.single().priceAndQuantityLabel, historyOrders.getValue("order-2").product.priceAndQuantityLabel)
        assertEquals("SAMPLE-0001", claimHistory.getValue("order-1").orderNumber)
        assertEquals("세이프가드 ABS 안전모", claimHistory.getValue("order-1").productName)
        assertEquals("사이즈 L", claimHistory.getValue("order-1").productOptionLabel)
        assertEquals("₩18,000 · 1개", claimHistory.getValue("order-1").priceAndQuantityLabel)
        assertEquals("SAMPLE-0002", claimHistory.getValue("order-2").orderNumber)
        assertEquals("경량 안전화 4인치", claimHistory.getValue("order-2").productName)
        assertEquals("사이즈 270", claimHistory.getValue("order-2").productOptionLabel)
        assertEquals("₩108,000 · 2개", claimHistory.getValue("order-2").priceAndQuantityLabel)
        assertEquals("REQ-0001", helmetClaimDetail.requestNumber)
        assertEquals(OrderRequestType.Return, helmetClaimDetail.requestType)
        assertEquals(OrderRequestStatus.Requested, helmetClaimDetail.status)
        assertEquals("세이프가드 ABS 안전모 · 사이즈 L 1개", helmetClaimDetail.productSummaryLabel)
        assertEquals("REQ-0002", bootsClaimDetail.requestNumber)
        assertEquals(OrderRequestType.Exchange, bootsClaimDetail.requestType)
        assertEquals(OrderRequestStatus.Rejected, bootsClaimDetail.status)
        assertEquals("경량 안전화 4인치 · 사이즈 270 2개", bootsClaimDetail.productSummaryLabel)
        assertEquals("₩108,000", bootsDetail.payment.productAmountLabel)
        assertEquals("₩111,000", bootsDetail.payment.totalAmountLabel)
        assertEquals(emptyList<OrderRequestProductUiModel>(), unknown.products)
        assertEquals(false, unknown.isOrderValid)
        assertEquals(false, unknown.canSubmit)
        assertEquals("", unknown.collectionAddressLabel)
        assertEquals("", unknown.collectionContactLabel)
        assertEquals("", unknown.paymentMethodLabel)
        assertEquals(emptyList<OrderDetailProductUiModel>(), SampleShoppingScreenDataProvider.orderDetail("missing-order").products)
    }

    @Test
    fun submissionContainsOnlySelectedProductsAndCurrentQuantities() {
        val products = listOf(
            OrderRequestProductUiModel("a", "상품 A", "", "", "1개", true, orderQuantity = 2, quantity = 2, unitPriceWon = 1500),
            OrderRequestProductUiModel("b", "상품 B", "", "", "1개", true, orderQuantity = 1, quantity = 1, unitPriceWon = 3000),
        )
        val submission = createOrderRequestSubmission(OrderRequestType.Return, products, setOf("a"), OrderRequestReason.ChangedMind)

        assertEquals(listOf("a"), submission?.products?.map { it.id })
        assertEquals(3000L, submission?.amountWon)
        assertEquals("상품 A 2개", submission?.let(::orderRequestSubmissionSummary))
        assertEquals(null, createOrderRequestSubmission(OrderRequestType.Return, products, emptySet(), OrderRequestReason.ChangedMind))
        assertEquals(3000L, selectedClaimAmount(products, setOf("a")))
        assertEquals(emptySet<String>(), updatedClaimSelection(setOf("a"), "a", false))
        assertEquals(setOf("a"), updatedClaimSelection(emptySet(), "a", true))

        val selectedButUnavailable = products.map { product -> if (product.id == "a") product.copy(isSelectable = false) else product }
        assertEquals(0L, selectedClaimAmount(selectedButUnavailable, setOf("a")))
        assertEquals(null, createOrderRequestSubmission(OrderRequestType.Return, selectedButUnavailable, setOf("a"), OrderRequestReason.ChangedMind))

        val unavailableSelectedProduct = products[1].copy(isSelectable = false)
        val allSelectedProducts = listOf(products[0], unavailableSelectedProduct)
        val eligibleSubmission = createOrderRequestSubmission(OrderRequestType.Return, allSelectedProducts, setOf("a", "b"), OrderRequestReason.ChangedMind)
        assertEquals(3000L, selectedClaimAmount(allSelectedProducts, setOf("a", "b")))
        assertEquals(eligibleSubmission?.amountWon, selectedClaimAmount(allSelectedProducts, setOf("a", "b")))
        assertEquals(listOf("a"), eligibleSubmission?.products?.map { it.id })
    }

    @Test
    fun restoredLocalClaimResultNeverFallsBackToProviderSampleConfirmation() {
        val restoredResult = localClaimResultUiModel(null, "데모 상세 정보가 복원되지 않았습니다. 실제 신청은 저장되거나 전송되지 않았습니다.")
        val submittedResult = localClaimResultUiModel("데모 신청입니다. 안전화 2개", "복원 안내")

        assertEquals("LOCAL-MOCK", restoredResult.requestNumber)
        assertEquals("데모 신청", restoredResult.statusLabel)
        assertEquals("데모 상세 정보가 복원되지 않았습니다. 실제 신청은 저장되거나 전송되지 않았습니다.", restoredResult.statusDescription)
        assertEquals("데모 신청입니다. 안전화 2개", submittedResult.statusDescription)
        assertEquals(false, restoredResult.statusDescription.contains("REQ-0001"))
    }

    private fun assertOrderMapping(
        form: OrderRequestFormUiModel,
        detail: OrderDetailProductUiModel,
        selectionProduct: OrderRequestProductUiModel,
        expectedName: String,
        expectedOption: String,
        expectedQuantity: Int,
        expectedUnitPrice: Long,
    ) {
        val product = form.products.single()
        assertEquals(expectedName, product.name)
        assertEquals(expectedName, detail.name)
        assertEquals(expectedName, selectionProduct.name)
        assertEquals(expectedOption, product.optionLabel)
        assertEquals(expectedOption, detail.optionLabel)
        assertEquals(expectedOption, selectionProduct.optionLabel)
        assertEquals(expectedQuantity, product.quantity)
        assertEquals(expectedQuantity, product.orderQuantity)
        assertEquals(expectedQuantity, selectionProduct.quantity)
        assertEquals(expectedQuantity, selectionProduct.orderQuantity)
        assertEquals(expectedUnitPrice, product.unitPriceWon)
    }
}

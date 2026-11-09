package com.nativelap.eumshoppingmall.ui.orders

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestDetailScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestFormScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestHistoryScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestHubScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestOrderSelectionScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestRejectionScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestResultScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestWithdrawalCompleteScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestWithdrawalConfirmScreen
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryEmptyState
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestOrderSelectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestReason
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.RequestableOrderUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val sampleRequestProduct = OrderRequestProductUiModel(
    id = "helmet",
    name = "세이프가드 ABS 안전모 제품명이 길어질 때 두 줄까지 표시됩니다",
    optionLabel = "흰색 · 사이즈 L",
    priceAndQuantityLabel = "₩00,000 · 1개",
    quantityLabel = "1개",
    isSelected = true,
)

private val sampleFormProducts = listOf(
    OrderRequestProductUiModel(
        id = "form-helmet-01",
        name = "제품명",
        optionLabel = "옵션명 · 주문 0개",
        priceAndQuantityLabel = "₩00,000 · 1개",
        quantityLabel = "1개",
        isSelected = true,
    ),
    OrderRequestProductUiModel(
        id = "form-helmet-02",
        name = "제품명",
        optionLabel = "옵션명 · 주문 0개",
        priceAndQuantityLabel = "₩00,000 · 1개",
        quantityLabel = "1개",
        isSelected = false,
    ),
)

private val sampleOrderRequestForm = OrderRequestFormUiModel(
    type = OrderRequestType.Return,
    products = sampleFormProducts,
    reasons = OrderRequestReason.entries,
    selectedReason = OrderRequestReason.ProductDefective,
    reasonDetails = "",
    productAmountLabel = "신청 후 안내",
    feeLabel = "정책에 따라 안내",
    expectedAmountLabel = "신청 후 안내",
    paymentMethodLabel = "결제 수단과 동일",
    collectionAddressLabel = "주문 시 입력한 배송지",
    collectionContactLabel = "주문 시 입력한 연락처",
    canSubmit = true,
)

private val sampleSelectionProduct = OrderRequestProductUiModel(
    id = "selection-helmet",
    name = "제품명",
    optionLabel = "",
    priceAndQuantityLabel = "₩00,000 · 1개",
    quantityLabel = "1개",
    isSelected = false,
)

// Figma 신청 내역 목록(879:45202)의 네 가지 상태: 철회완료, 회수중, 교환완료, 반려.
private val sampleRequestHistoryItems = listOf(
    OrderRequestHistoryItemUiModel(
        id = "request-01",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Return,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Withdrawn,
        expectedAmountLabel = "₩00,000",
    ),
    OrderRequestHistoryItemUiModel(
        id = "request-02",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Return,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Collecting,
        expectedAmountLabel = "₩00,000",
    ),
    OrderRequestHistoryItemUiModel(
        id = "request-03",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Exchange,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Completed,
    ),
    OrderRequestHistoryItemUiModel(
        id = "request-04",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Cancel,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Rejected,
        hasRejectionReason = true,
    ),
)

// Figma 신청 상세(879:44641)와 철회 완료 상세(879:45279) 샘플. 값은 Figma 표기를 그대로 쓴다.
private val sampleRequestDetail = OrderRequestDetailUiModel(
    requestNumber = "A-2026-001",
    requestType = OrderRequestType.Return,
    requestDateLabel = "2026.10.19",
    status = OrderRequestStatus.Requested,
    statusDescription = "담당자가 신청 내용을 확인한 뒤 처리합니다.",
    productSummaryLabel = "경량 안전모 · 블랙 / 1개",
    reasonSummaryLabel = "단순 변심",
    collectionAddressLabel = "서울특별시 강남구 테헤란로 427",
    canWithdraw = true,
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 신청 허브")
@Composable
private fun RequestHubPreview() {
    EumShoppingMallTheme { OrderRequestHubScreen(onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 주문 선택")
@Composable
private fun RequestOrderSelectionPreview() {
    val orders = listOf(
        RequestableOrderUiModel("order-01", "0000.00.00", "0000000000", OrderStatus.WaitingPayment, listOf(sampleSelectionProduct), true),
        RequestableOrderUiModel("order-02", "0000.00.00", "0000000000", OrderStatus.PaymentComplete, listOf(sampleSelectionProduct), true),
        RequestableOrderUiModel("order-03", "0000.00.00", "0000000000", OrderStatus.Shipping, listOf(sampleSelectionProduct), true),
        RequestableOrderUiModel("order-04", "0000.00.00", "0000000000", OrderStatus.Delivered, listOf(sampleSelectionProduct), true),
    )
    EumShoppingMallTheme { OrderRequestOrderSelectionScreen(OrderRequestOrderSelectionUiModel(periodLabel = "최근 3개월", periods = listOf("최근 3개월", "최근 6개월", "최근 1년"), orders = orders), onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 신청 주문 없음")
@Composable
private fun RequestOrderEmptyPreview() {
    EumShoppingMallTheme { OrderRequestOrderSelectionScreen(OrderRequestOrderSelectionUiModel(periodLabel = "최근 3개월"), onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 필터 결과 없음")
@Composable
private fun RequestOrderNoMatchingPreview() {
    EumShoppingMallTheme {
        OrderRequestOrderSelectionScreen(
            OrderRequestOrderSelectionUiModel(
                filter = OrderHistoryFilter.Shipping,
                periodLabel = "최근 3개월",
                emptyState = OrderHistoryEmptyState.NoMatchingOrders,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 반품 신청")
@Composable
private fun ReturnRequestPreview() {
    EumShoppingMallTheme { OrderRequestFormScreen(sampleOrderRequestForm, onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 교환 신청")
@Composable
private fun ExchangeRequestPreview() {
    EumShoppingMallTheme { OrderRequestFormScreen(sampleOrderRequestForm.copy(type = OrderRequestType.Exchange, paymentMethodLabel = "신용카드"), onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 취소 신청")
@Composable
private fun CancelRequestPreview() {
    EumShoppingMallTheme { OrderRequestFormScreen(sampleOrderRequestForm.copy(type = OrderRequestType.Cancel), onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 신청 내역")
@Composable
private fun RequestHistoryPreview() {
    EumShoppingMallTheme { OrderRequestHistoryScreen(OrderRequestHistoryUiModel(items = sampleRequestHistoryItems), onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19le 철회 후 신청 내역 빈 상태")
@Composable
private fun RequestHistoryEmptyPreview() {
    EumShoppingMallTheme { OrderRequestHistoryScreen(OrderRequestHistoryUiModel(hasNoRequests = true), onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 신청 유형 필터 결과 없음")
@Composable
private fun RequestHistoryNoMatchingPreview() {
    EumShoppingMallTheme {
        OrderRequestHistoryScreen(
            OrderRequestHistoryUiModel(filter = OrderRequestHistoryFilter.Exchange),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-51 신청 상세")
@Composable
private fun RequestDetailPreview() {
    EumShoppingMallTheme { OrderRequestDetailScreen(sampleRequestDetail, onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-51 신청 상세 · 철회 완료")
@Composable
private fun RequestDetailCancelledPreview() {
    EumShoppingMallTheme {
        OrderRequestDetailScreen(
            sampleRequestDetail.copy(
                status = OrderRequestStatus.Withdrawn,
                statusDescription = "철회 완료 · 신청이 정상적으로 취소됐습니다.",
                canWithdraw = false,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-50 신청 접수")
@Composable
private fun RequestCompletePreview() {
    EumShoppingMallTheme { OrderRequestResultScreen(OrderRequestResultUiModel("A-2026-001", "신청이 접수됐어요", "담당자가 확인 후 처리합니다."), onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-53 신청 철회 완료")
@Composable
private fun WithdrawalCompletePreview() {
    EumShoppingMallTheme {
        OrderRequestWithdrawalCompleteScreen(
            OrderRequestResultUiModel("A-2026-001", "철회", "A-2026-001 반품 신청을 철회했습니다. 신청 내역에서 상태를 확인할 수 있습니다."),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-54 반려 사유")
@Composable
private fun RejectionReasonPreview() {
    EumShoppingMallTheme {
        OrderRequestRejectionScreen(
            OrderRequestRejectionUiModel(
                requestNumber = "A-2026-001",
                productSummaryLabel = "경량 안전모 · 블랙 / 1개",
                requestReasonLabel = "단순 변심",
                statusDescription = "반려 · 신청이 처리되지 않았습니다.",
                rejectionReasonLabel = "반려 사유는 신청 내역에서 확인해 주세요.",
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-52 신청 철회 확인")
@Composable
private fun WithdrawalConfirmPreview() {
    EumShoppingMallTheme { OrderRequestWithdrawalConfirmScreen("A-2026-001", onEvent = {}) }
}

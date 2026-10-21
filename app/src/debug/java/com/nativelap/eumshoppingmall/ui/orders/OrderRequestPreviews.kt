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
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val sampleRequestProduct = OrderRequestProductUiModel(
    id = "helmet",
    name = "세이프가드 ABS 안전모 제품명이 길어질 때 두 줄까지 표시됩니다",
    optionLabel = "흰색 · 사이즈 L",
    priceAndQuantityLabel = "₩00,000 · 1개",
    quantityLabel = "1개",
    isSelected = true,
)

private val sampleOrderRequestForm = OrderRequestFormUiModel(
    type = OrderRequestType.Return,
    products = listOf(sampleRequestProduct),
    reasons = OrderRequestReason.entries,
    selectedReason = OrderRequestReason.ChangedMind,
    reasonDetails = "사이즈가 맞지 않아 반품을 신청합니다.",
    productAmountLabel = "₩00,000",
    feeLabel = "₩0",
    expectedAmountLabel = "₩00,000",
    paymentMethodLabel = "신용카드",
    collectionAddressLabel = "(00000) 주소 상세주소",
    collectionContactLabel = "홍길동 · 010-0000-0000",
    canSubmit = true,
)

private val sampleRequestHistoryItem = OrderRequestHistoryItemUiModel(
    id = "request-01",
    requestDateLabel = "2026.10.20",
    requestType = OrderRequestType.Return,
    orderNumber = "0000000000",
    productName = sampleRequestProduct.name,
    priceAndQuantityLabel = sampleRequestProduct.priceAndQuantityLabel,
    status = OrderRequestStatus.Processing,
    expectedAmountLabel = "₩00,000",
    canWithdraw = true,
)

private val sampleRequestDetail = OrderRequestDetailUiModel(
    requestNumber = "RQ-00000001",
    requestType = OrderRequestType.Return,
    requestDateLabel = "2026.10.20",
    status = OrderRequestStatus.Processing,
    statusDescription = "상품 회수를 준비하고 있습니다.",
    productSummaryLabel = "${sampleRequestProduct.name}\n${sampleRequestProduct.optionLabel} · ${sampleRequestProduct.priceAndQuantityLabel}",
    reasonSummaryLabel = "단순 변심\n사이즈가 맞지 않아 반품을 신청합니다.",
    collectionAddressLabel = "홍길동 · 010-0000-0000\n(00000) 주소 상세주소",
    canWithdraw = true,
)

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 신청 허브")
@Composable
private fun RequestHubPreview() {
    EumShoppingMallTheme { OrderRequestHubScreen(onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 주문 선택")
@Composable
private fun RequestOrderSelectionPreview() {
    val order = RequestableOrderUiModel("order-01", "2026.10.20", "0000000000", OrderStatus.Delivered, listOf(sampleRequestProduct), true)
    EumShoppingMallTheme { OrderRequestOrderSelectionScreen(OrderRequestOrderSelectionUiModel(periodLabel = "최근 3개월", periods = listOf("최근 3개월", "최근 6개월", "최근 1년"), orders = listOf(order)), onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 신청 주문 없음")
@Composable
private fun RequestOrderEmptyPreview() {
    EumShoppingMallTheme { OrderRequestOrderSelectionScreen(OrderRequestOrderSelectionUiModel(periodLabel = "최근 3개월"), onEvent = {}) }
}

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

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 반품 신청")
@Composable
private fun ReturnRequestPreview() {
    EumShoppingMallTheme { OrderRequestFormScreen(sampleOrderRequestForm, onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 교환 신청")
@Composable
private fun ExchangeRequestPreview() {
    EumShoppingMallTheme { OrderRequestFormScreen(sampleOrderRequestForm.copy(type = OrderRequestType.Exchange, exchangeOptionLabel = "검정 · 사이즈 270"), onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 취소 신청")
@Composable
private fun CancelRequestPreview() {
    EumShoppingMallTheme { OrderRequestFormScreen(sampleOrderRequestForm.copy(type = OrderRequestType.Cancel), onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19 신청 내역")
@Composable
private fun RequestHistoryPreview() {
    EumShoppingMallTheme { OrderRequestHistoryScreen(OrderRequestHistoryUiModel(items = listOf(sampleRequestHistoryItem)), onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-19 신청 내역 없음")
@Composable
private fun RequestHistoryEmptyPreview() {
    EumShoppingMallTheme { OrderRequestHistoryScreen(OrderRequestHistoryUiModel(hasNoRequests = true), onEvent = {}) }
}

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

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-51 신청 상세")
@Composable
private fun RequestDetailPreview() {
    EumShoppingMallTheme { OrderRequestDetailScreen(sampleRequestDetail, onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-50 신청 접수")
@Composable
private fun RequestCompletePreview() {
    EumShoppingMallTheme { OrderRequestResultScreen(OrderRequestResultUiModel("RQ-00000001", "신청이 접수됐어요", "신청 내용 확인 후 순차적으로 안내해 드릴게요."), onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-53 신청 철회 완료")
@Composable
private fun WithdrawalCompletePreview() {
    EumShoppingMallTheme { OrderRequestWithdrawalCompleteScreen(OrderRequestResultUiModel("RQ-00000001", "철회", "신청이 철회되었습니다."), onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-54 반려 사유")
@Composable
private fun RejectionReasonPreview() {
    EumShoppingMallTheme { OrderRequestRejectionScreen(OrderRequestRejectionUiModel("RQ-00000001", sampleRequestProduct.name, "단순 변심", "신청하신 내용을 확인했습니다.", "상품 포장 훼손으로 반품이 어렵습니다."), onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-52 신청 철회 확인")
@Composable
private fun WithdrawalConfirmPreview() {
    EumShoppingMallTheme { OrderRequestWithdrawalConfirmScreen("RQ-00000001", "처리 중", onEvent = {}) }
}

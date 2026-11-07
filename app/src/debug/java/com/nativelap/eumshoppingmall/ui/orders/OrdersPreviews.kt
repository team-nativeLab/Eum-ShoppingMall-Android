package com.nativelap.eumshoppingmall.ui.orders

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.orders.view.DeliveryTrackingScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderDetailScreen
import com.nativelap.eumshoppingmall.ui.orders.view.OrderHistoryScreen
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryProgressStage
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTimelineItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailAddressUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryAction
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryEmptyState
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderPaymentBreakdownUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewHistoryModel = OrderHistoryUiModel(
    filter = OrderHistoryFilter.All,
    periodLabel = "최근 3개월",
    periods = listOf("최근 3개월", "최근 6개월", "최근 1년"),
    orders = listOf(
        OrderHistoryItemUiModel(
            id = "waiting",
            orderDateLabel = "2026.10.20",
            orderNumber = "0000000000",
            status = OrderStatus.WaitingPayment,
            product = OrderHistoryProductUiModel("안전모 제품명이 길어질 때 두 줄까지 자연스럽게 표시", "₩00,000 · 1개", "입금 기한 2026.10.22까지"),
            paymentDeadlineLabel = "입금 기한 2026.10.22까지",
            actions = listOf(OrderHistoryAction.Cancel),
        ),
        OrderHistoryItemUiModel(
            id = "paid",
            orderDateLabel = "2026.10.18",
            orderNumber = "0000000001",
            status = OrderStatus.PaymentComplete,
            product = OrderHistoryProductUiModel("안전 장갑", "₩00,000 · 1개", "제품명 외 1건"),
            actions = listOf(OrderHistoryAction.Cancel),
        ),
        OrderHistoryItemUiModel(
            id = "shipping",
            orderDateLabel = "2026.10.15",
            orderNumber = "0000000002",
            status = OrderStatus.Shipping,
            product = OrderHistoryProductUiModel("절연 안전화", "₩00,000 · 1개"),
            actions = listOf(OrderHistoryAction.TrackDelivery, OrderHistoryAction.BuyAgain),
        ),
        OrderHistoryItemUiModel(
            id = "delivered",
            orderDateLabel = "2026.10.10",
            orderNumber = "0000000003",
            status = OrderStatus.Delivered,
            product = OrderHistoryProductUiModel("방진 마스크", "₩00,000 · 1개"),
            actions = listOf(OrderHistoryAction.TrackDelivery, OrderHistoryAction.BuyAgain),
        ),
    ),
)

private val previewOrderDetailModel = OrderDetailUiModel(
    orderDateLabel = "2026.10.20",
    orderNumber = "0000000000",
    products = listOf(
        OrderDetailProductUiModel("helmet", "세이프가드 ABS 안전모", "옵션명 · 사이즈 L", "₩00,000 · 1개", OrderStatus.Shipping),
        OrderDetailProductUiModel("shoes", "경량 안전화 4인치", "옵션명 · 사이즈 270", "₩00,000 · 1개", OrderStatus.Shipping),
    ),
    address = OrderDetailAddressUiModel("홍길동", "010-0000-0000", "(00000) 주소 상세주소\n상세 주소가 길어지는 경우에도 줄바꿈됩니다.", "배송 요청사항"),
    payment = OrderPaymentBreakdownUiModel("₩00,000", "₩0", "-₩0", "-0원", "₩00,000", "카드명"),
    expectedPointsLabel = "0원",
)

private val previewDeliveryModel = DeliveryTrackingUiModel(
    status = OrderStatus.Shipping,
    expectedArrivalLabel = "0000.00.00",
    carrierName = "택배사명",
    trackingNumber = "0000000000",
    progressStage = DeliveryProgressStage.InTransit,
    timeline = listOf(
        DeliveryTimelineItemUiModel("배송중", "처리 위치 · 0000.00.00 00:00", isCurrent = true),
        DeliveryTimelineItemUiModel("배송중", "처리 위치 · 0000.00.00 00:00"),
        DeliveryTimelineItemUiModel("간선 상차", "처리 위치 · 0000.00.00 00:00"),
        DeliveryTimelineItemUiModel("집화 처리", "처리 위치 · 0000.00.00 00:00"),
        DeliveryTimelineItemUiModel("상품 인수", "처리 위치 · 0000.00.00 00:00"),
    ),
    productsSummaryLabel = "제품명 외 0건",
    noticeLabel = "택배사 사정에 따라 정보가 늦게 반영될 수 있습니다.",
)

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-16 주문 내역")
@Composable
private fun OrderHistoryPreview() {
    EumShoppingMallTheme { OrderHistoryScreen(previewHistoryModel, onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-16 주문 없음")
@Composable
private fun OrderHistoryEmptyPreview() {
    EumShoppingMallTheme {
        OrderHistoryScreen(
            previewHistoryModel.copy(orders = emptyList(), emptyState = OrderHistoryEmptyState.NoOrders),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-16 필터 결과 없음")
@Composable
private fun OrderHistoryNoMatchingPreview() {
    EumShoppingMallTheme {
        OrderHistoryScreen(
            previewHistoryModel.copy(
                filter = OrderHistoryFilter.Shipping,
                orders = emptyList(),
                emptyState = OrderHistoryEmptyState.NoMatchingOrders,
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-17 좁은 주문 상세")
@Composable
private fun OrderDetailNarrowPreview() {
    EumShoppingMallTheme { OrderDetailScreen(previewOrderDetailModel, onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-18 배송 조회")
@Composable
private fun DeliveryTrackingPreview() {
    EumShoppingMallTheme { DeliveryTrackingScreen(previewDeliveryModel, onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-18 배송 이력 없음")
@Composable
private fun DeliveryTrackingEmptyPreview() {
    EumShoppingMallTheme {
        DeliveryTrackingScreen(previewDeliveryModel.copy(timeline = emptyList()), onEvent = {})
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-18 배송 정보 없음")
@Composable
private fun DeliveryTrackingMissingInfoPreview() {
    EumShoppingMallTheme {
        DeliveryTrackingScreen(
            previewDeliveryModel.copy(
                expectedArrivalLabel = "",
                carrierName = "",
                trackingNumber = "",
                timeline = emptyList(),
            ),
            onEvent = {},
        )
    }
}

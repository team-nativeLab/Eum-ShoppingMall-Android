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
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewHistoryModel = OrderHistoryUiModel(
    filter = OrderHistoryFilter.All,
    periodLabel = "최근 3개월",
    periods = listOf("최근 3개월", "최근 6개월", "최근 1년"),
    orders = listOf(
        OrderHistoryItemUiModel(
            id = "waiting",
            orderDateLabel = "0000.00.00",
            orderNumber = "0000000000",
            status = OrderStatus.WaitingPayment,
            product = OrderHistoryProductUiModel("제품명", "₩00,000 · 1개"),
            paymentDeadlineLabel = "입금 기한 0000.00.00까지",
            actions = listOf(OrderHistoryAction.Cancel),
        ),
        OrderHistoryItemUiModel(
            id = "paid",
            orderDateLabel = "0000.00.00",
            orderNumber = "0000000000",
            status = OrderStatus.PaymentComplete,
            product = OrderHistoryProductUiModel("제품명", "₩00,000 · 1개", "제품명 외 0건"),
            actions = listOf(OrderHistoryAction.Cancel),
        ),
        OrderHistoryItemUiModel(
            id = "shipping",
            orderDateLabel = "0000.00.00",
            orderNumber = "0000000000",
            status = OrderStatus.Shipping,
            product = OrderHistoryProductUiModel("제품명", "₩00,000 · 1개"),
            actions = listOf(OrderHistoryAction.TrackDelivery, OrderHistoryAction.BuyAgain),
        ),
        OrderHistoryItemUiModel(
            id = "delivered",
            orderDateLabel = "0000.00.00",
            orderNumber = "0000000000",
            status = OrderStatus.Delivered,
            product = OrderHistoryProductUiModel("제품명", "₩00,000 · 1개"),
            actions = listOf(OrderHistoryAction.TrackDelivery, OrderHistoryAction.BuyAgain),
        ),
    ),
)

private val previewOrderDetailModel = OrderDetailUiModel(
    orderDateLabel = "0000.00.00",
    orderNumber = "0000000000",
    products = listOf(
        OrderDetailProductUiModel("helmet", "제품명", "옵션명", "₩00,000 · 1개", OrderStatus.Shipping),
        OrderDetailProductUiModel("shoes", "제품명", "옵션명", "₩00,000 · 1개", OrderStatus.Shipping),
    ),
    address = OrderDetailAddressUiModel("홍길동", "010-0000-0000", "(00000) 주소 상세주소", "배송 요청사항"),
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

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-16 주문 내역")
@Composable
private fun OrderHistoryPreview() {
    EumShoppingMallTheme { OrderHistoryScreen(previewHistoryModel, onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-16e 주문 내역 빈 상태")
@Composable
private fun OrderHistoryEmptyPreview() {
    EumShoppingMallTheme {
        OrderHistoryScreen(
            previewHistoryModel.copy(orders = emptyList(), emptyState = OrderHistoryEmptyState.NoOrders),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
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

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-17 좁은 주문 상세")
@Composable
private fun OrderDetailNarrowPreview() {
    EumShoppingMallTheme { OrderDetailScreen(previewOrderDetailModel, onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-18 배송 조회")
@Composable
private fun DeliveryTrackingPreview() {
    EumShoppingMallTheme { DeliveryTrackingScreen(previewDeliveryModel, onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 760, fontScale = 1.2f, name = "A-18 배송 이력 없음")
@Composable
private fun DeliveryTrackingEmptyPreview() {
    EumShoppingMallTheme {
        DeliveryTrackingScreen(previewDeliveryModel.copy(timeline = emptyList()), onEvent = {})
    }
}

@ResponsiveUiPreview
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

package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryProgressStage
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTimelineItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailAddressUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryAction
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderPaymentBreakdownUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestReason
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val sampleHistoryOrders = listOf(
    OrderHistoryItemUiModel(
        id = "order-1",
        orderDateLabel = "0000.00.00",
        orderNumber = "0000000000",
        status = OrderStatus.Shipping,
        product = OrderHistoryProductUiModel("안전모 제품명 외 1건", "₩00,000 · 1개", "상품 2개"),
        actions = listOf(OrderHistoryAction.TrackDelivery, OrderHistoryAction.BuyAgain),
    ),
    OrderHistoryItemUiModel(
        id = "order-2",
        orderDateLabel = "0000.00.00",
        orderNumber = "0000000001",
        status = OrderStatus.WaitingPayment,
        product = OrderHistoryProductUiModel("작업용 보호 장갑 제품명", "₩00,000 · 2개"),
        paymentDeadlineLabel = "결제 기한 0000.00.00",
        actions = listOf(OrderHistoryAction.Cancel),
    ),
)

private val sampleDetailProducts = listOf(
    OrderDetailProductUiModel("p1", "안전모 제품명", "옵션: 흰색 / L", "₩00,000 · 1개", OrderStatus.Shipping),
    OrderDetailProductUiModel("p2", "작업용 보호 장갑 제품명", "옵션: 기본", "₩00,000 · 2개", OrderStatus.Shipping),
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.3f, name = "A-16 주문 내역 · 320dp · 글꼴 1.3")
@Composable
private fun OrderHistoryPreview() {
    EumShoppingMallTheme {
        OrderHistoryScreen(
            OrderHistoryUiModel(periodLabel = "최근 3개월", periods = listOf("최근 3개월"), orders = sampleHistoryOrders),
            {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 1000, fontScale = 1.3f, name = "A-17 주문 상세 · 320dp · 글꼴 1.3")
@Composable
private fun OrderDetailPreview() {
    EumShoppingMallTheme {
        OrderDetailScreen(
            OrderDetailUiModel(
                orderDateLabel = "0000.00.00",
                orderNumber = "0000000000",
                products = sampleDetailProducts,
                address = OrderDetailAddressUiModel("홍길동", "010-0000-0000", "주소 00시 00구 00로 00", "문 앞에 놓아주세요"),
                payment = OrderPaymentBreakdownUiModel("₩00,000", "₩0", "-₩0", "-0원", "₩00,000", "신용카드"),
                expectedPointsLabel = "0원",
            ),
            {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 1000, fontScale = 1.3f, name = "A-18 배송 조회 · 320dp · 글꼴 1.3")
@Composable
private fun DeliveryTrackingPreview() {
    EumShoppingMallTheme {
        DeliveryTrackingScreen(
            DeliveryTrackingUiModel(
                status = OrderStatus.Shipping,
                expectedArrivalLabel = "0월 0일 도착 예정",
                carrierName = "택배사",
                trackingNumber = "000000000000",
                progressStage = DeliveryProgressStage.InTransit,
                timeline = listOf(
                    DeliveryTimelineItemUiModel("배송 중", "00 터미널 · 0000.00.00 00:00", isCurrent = true),
                    DeliveryTimelineItemUiModel("상품 인수", "00 지점 · 0000.00.00 00:00"),
                ),
                productsSummaryLabel = "안전모 제품명 외 1건",
                noticeLabel = "배송 일정은 택배사 사정에 따라 변경될 수 있습니다.",
            ),
            {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 1200, fontScale = 1.3f, name = "A-19 반품 신청 · 320dp · 글꼴 1.3")
@Composable
private fun OrderRequestReturnFormPreview() {
    EumShoppingMallTheme {
        OrderRequestFormScreen(
            OrderRequestFormUiModel(
                type = OrderRequestType.Return,
                products = listOf(
                    OrderRequestProductUiModel("p1", "안전모 제품명", "옵션: 흰색 / L", "₩00,000 · 1개", "1", isSelected = true),
                    OrderRequestProductUiModel("p2", "작업용 보호 장갑 제품명", "옵션: 기본", "₩00,000 · 2개", "2", isSelected = false),
                ),
                reasons = OrderRequestReason.entries,
                selectedReason = OrderRequestReason.ChangedMind,
                photoCount = 1,
                collectionContactLabel = "홍길동 010-0000-0000",
                collectionAddressLabel = "주소 00시 00구 00로 00",
                productAmountLabel = "₩00,000",
                feeLabel = "₩0",
                expectedAmountLabel = "₩00,000",
                paymentMethodLabel = "신용카드",
            ),
            {},
        )
    }
}

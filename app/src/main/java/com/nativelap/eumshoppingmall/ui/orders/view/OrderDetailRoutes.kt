package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailUiModel

/** 주문 상세(A-17). 명세서·영수증 내려받기는 파일 연동 전이라 연결하지 않는다. */
@Composable
fun OrderDetailRoute(
    uiModel: OrderDetailUiModel,
    onBackClick: () -> Unit,
    onTrackDeliveryClick: () -> Unit,
    onExchangeReturnClick: () -> Unit,
) {
    var isProductsExpanded by rememberSaveable { mutableStateOf(uiModel.isProductsExpanded) }
    OrderDetailScreen(
        uiModel = uiModel.copy(isProductsExpanded = isProductsExpanded),
        onEvent = { event ->
            when (event) {
                OrderDetailScreenEvent.BackClicked -> onBackClick()

                is OrderDetailScreenEvent.ProductsExpandedChanged -> isProductsExpanded = event.isExpanded

                OrderDetailScreenEvent.DeliveryTrackingClicked -> onTrackDeliveryClick()

                OrderDetailScreenEvent.ExchangeReturnRequestClicked -> onExchangeReturnClick()

                OrderDetailScreenEvent.StatementDownloadClicked,
                OrderDetailScreenEvent.ReceiptViewClicked,
                -> Unit
            }
        },
    )
}

/** 배송 조회(A-18). 운송장 번호 복사는 클립보드 연동 전이라 연결하지 않는다. */
@Composable
fun DeliveryTrackingRoute(
    uiModel: DeliveryTrackingUiModel,
    onBackClick: () -> Unit,
) {
    DeliveryTrackingScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                DeliveryTrackingScreenEvent.BackClicked -> onBackClick()
                DeliveryTrackingScreenEvent.TrackingNumberCopyClicked -> Unit
            }
        },
    )
}

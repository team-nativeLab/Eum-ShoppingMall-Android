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

/** 주문 상세(A-17). 파일 작업은 지원 안내를 표시한다. */
@Composable
fun OrderDetailRoute(
    uiModel: OrderDetailUiModel,
    onBackClick: () -> Unit,
    onTrackDeliveryClick: () -> Unit,
    onExchangeReturnClick: () -> Unit,
    onUnavailableAction: () -> Unit,
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
                -> onUnavailableAction()
            }
        },
    )
}

/** 배송 조회(A-18). 운송장 복사 기능은 지원 안내를 표시한다. */
@Composable
fun DeliveryTrackingRoute(
    uiModel: DeliveryTrackingUiModel,
    onBackClick: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    DeliveryTrackingScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                DeliveryTrackingScreenEvent.BackClicked -> onBackClick()
                DeliveryTrackingScreenEvent.TrackingNumberCopyClicked -> onUnavailableAction()
            }
        },
    )
}

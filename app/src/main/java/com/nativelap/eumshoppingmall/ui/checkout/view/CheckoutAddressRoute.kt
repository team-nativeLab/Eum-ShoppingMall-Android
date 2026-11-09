package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel

/** 주문하기 1 배송지(A-08). 배송 요청 선택 메뉴는 화면 내부 상태로 연다. */
@Composable
fun CheckoutAddressRoute(
    uiModel: CheckoutUiModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    var checkoutUiModel by remember(uiModel) { mutableStateOf(uiModel) }
    CheckoutAddressScreen(
        uiModel = checkoutUiModel,
        onEvent = { event ->
            when (event) {
                CheckoutScreenEvent.BackClicked -> onBackClick()

                CheckoutScreenEvent.NextClicked -> onNextClick()

                CheckoutScreenEvent.DeliveryRequestPickerToggled -> {
                    checkoutUiModel = checkoutUiModel.copy(
                        isDeliveryRequestPickerVisible = !checkoutUiModel.isDeliveryRequestPickerVisible,
                    )
                }

                is CheckoutScreenEvent.DeliveryRequestSelected -> {
                    checkoutUiModel = checkoutUiModel.copy(
                        deliveryRequestLabel = event.request,
                        isDeliveryRequestPickerVisible = false,
                    )
                }

                is CheckoutScreenEvent.ProductsExpandedChanged -> {
                    checkoutUiModel = checkoutUiModel.copy(productsExpanded = event.isExpanded)
                }

                // 배송지 변경(A-24·A-46)은 결과 반환이 필요해 후속 작업에서 연결한다. 나머지는 결제 화면 이벤트다.
                else -> Unit
            }
        },
    )
}

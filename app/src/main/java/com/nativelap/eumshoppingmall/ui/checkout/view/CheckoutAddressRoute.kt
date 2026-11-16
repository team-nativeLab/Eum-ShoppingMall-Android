package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel

/** 주문하기 1 배송지(A-08). 배송 요청 선택 메뉴는 화면 내부 상태로 연다. */
@Composable
fun CheckoutAddressRoute(
    uiModel: CheckoutUiModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onAddressChangeClick: () -> Unit = {},
    pickedAddressId: String? = null,
    addressLabelById: (addressId: String) -> String? = { null },
    onPickedAddressConsumed: () -> Unit = {},
) {
    // 주소 검색에 다녀오는 동안 이 화면은 composition에서 빠지므로 입력은 저장 가능한 값으로 보관한다.
    // 고른 주소는 ID만 저장하고 표시용 주소는 매번 ID로 다시 찾는다.
    var selectedAddressId by rememberSaveable { mutableStateOf<String?>(null) }
    var deliveryRequestLabel by rememberSaveable { mutableStateOf(uiModel.deliveryRequestLabel) }
    var isDeliveryRequestPickerVisible by rememberSaveable { mutableStateOf(uiModel.isDeliveryRequestPickerVisible) }
    var productsExpanded by rememberSaveable { mutableStateOf(uiModel.productsExpanded) }
    LaunchedEffect(pickedAddressId) {
        if (pickedAddressId != null) {
            selectedAddressId = pickedAddressId
            onPickedAddressConsumed()
        }
    }
    val selectedAddressLabel = selectedAddressId?.let(addressLabelById)
    val checkoutUiModel = uiModel.copy(
        address = if (selectedAddressLabel == null) {
            uiModel.address
        } else {
            uiModel.address.copy(addressLabel = selectedAddressLabel)
        },
        deliveryRequestLabel = deliveryRequestLabel,
        isDeliveryRequestPickerVisible = isDeliveryRequestPickerVisible,
        productsExpanded = productsExpanded,
    )
    CheckoutAddressScreen(
        uiModel = checkoutUiModel,
        onEvent = { event ->
            when (event) {
                CheckoutScreenEvent.BackClicked -> onBackClick()

                CheckoutScreenEvent.NextClicked -> onNextClick()

                CheckoutScreenEvent.AddressChangeClicked -> onAddressChangeClick()

                CheckoutScreenEvent.DeliveryRequestPickerToggled -> {
                    isDeliveryRequestPickerVisible = !isDeliveryRequestPickerVisible
                }

                is CheckoutScreenEvent.DeliveryRequestSelected -> {
                    deliveryRequestLabel = event.request
                    isDeliveryRequestPickerVisible = false
                }

                is CheckoutScreenEvent.ProductsExpandedChanged -> productsExpanded = event.isExpanded

                // 나머지는 결제 화면 이벤트다.
                else -> Unit
            }
        },
    )
}

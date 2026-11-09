package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartScreenEvent
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

@Composable
fun CartRoute(
    uiModel: CartUiModel,
    isLoggedIn: Boolean,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onSearchClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onQuoteRequestClick: () -> Unit,
) {
    // 장바구니 저장소가 생기기 전까지 선택·수량·삭제는 이 화면에 머무는 동안만 유지한다.
    var cartItems by remember(uiModel) { mutableStateOf(uiModel.items) }
    CartScreen(
        uiModel = uiModel.copy(items = cartItems),
        isLoggedIn = isLoggedIn,
        cartItemCount = cartItemCount,
        onEvent = { event ->
            when (event) {
                CartScreenEvent.SearchClicked -> onSearchClick()

                is CartScreenEvent.BottomTabSelected -> onTabSelected(event.tab)

                is CartScreenEvent.SelectAllChanged -> {
                    cartItems = cartItems.map { item ->
                        if (item.type == CartItemType.Orderable && !item.isSoldOut) {
                            item.copy(isSelected = event.isSelected)
                        } else {
                            item
                        }
                    }
                }

                is CartScreenEvent.ItemSelectionChanged -> {
                    cartItems = cartItems.map { item ->
                        if (item.id == event.itemId) item.copy(isSelected = event.isSelected) else item
                    }
                }

                CartScreenEvent.DeleteSelectedClicked -> {
                    cartItems = cartItems.filterNot { item -> item.type == CartItemType.Orderable && item.isSelected }
                }

                is CartScreenEvent.ItemRemoved -> cartItems = cartItems.filterNot { item -> item.id == event.itemId }

                is CartScreenEvent.QuantityChanged -> {
                    cartItems = cartItems.map { item ->
                        if (item.id == event.itemId) item.copy(quantity = event.quantity.coerceAtLeast(1)) else item
                    }
                }

                CartScreenEvent.QuoteRequestClicked -> onQuoteRequestClick()

                CartScreenEvent.CheckoutClicked -> onCheckoutClick()

                // 옵션 변경 시트는 이번 범위가 아니다.
                is CartScreenEvent.OptionChangeClicked -> Unit
            }
        },
    )
}

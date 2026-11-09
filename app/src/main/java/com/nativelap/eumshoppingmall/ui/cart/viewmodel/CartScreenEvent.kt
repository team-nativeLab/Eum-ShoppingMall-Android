package com.nativelap.eumshoppingmall.ui.cart.viewmodel

import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

sealed interface CartScreenEvent {
    data object SearchClicked : CartScreenEvent
    data class BottomTabSelected(val tab: MainNavigationTab) : CartScreenEvent
    data class SelectAllChanged(val isSelected: Boolean) : CartScreenEvent
    data class ItemSelectionChanged(val itemId: String, val isSelected: Boolean) : CartScreenEvent
    data object DeleteSelectedClicked : CartScreenEvent
    data class ItemRemoved(val itemId: String) : CartScreenEvent
    data class QuantityChanged(val itemId: String, val quantity: Int) : CartScreenEvent
    data class OptionChangeClicked(val itemId: String) : CartScreenEvent
    data object QuoteRequestClicked : CartScreenEvent
    data object CheckoutClicked : CartScreenEvent
}

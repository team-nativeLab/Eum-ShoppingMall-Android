package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface DeliveryTrackingScreenEvent {
    data object BackClicked : DeliveryTrackingScreenEvent
    data object TrackingNumberCopyClicked : DeliveryTrackingScreenEvent
}

package com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel

data class OrderCompleteUiModel(
    val orderNumber: String,
    val paymentAmountLabel: String,
    val paymentMethodLabel: String,
    val addressLabel: String,
    val isVirtualAccount: Boolean = false,
    val bankLabel: String? = null,
    val accountNumber: String? = null,
    val depositorLabel: String? = null,
    val depositAmountLabel: String? = null,
    val depositDeadlineLabel: String? = null,
)

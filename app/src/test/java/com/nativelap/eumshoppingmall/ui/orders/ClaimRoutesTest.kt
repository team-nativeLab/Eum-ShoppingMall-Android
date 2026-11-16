package com.nativelap.eumshoppingmall.ui.orders.view

import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class ClaimRoutesTest {
    @Test
    fun beforeShippingStartsAsCancel() {
        listOf(OrderStatus.WaitingPayment, OrderStatus.PaymentComplete, OrderStatus.Preparing, null).forEach { status ->
            assertEquals(OrderRequestType.Cancel, defaultClaimType(status))
        }
    }

    @Test
    fun shippingOrDeliveredStartsAsReturn() {
        assertEquals(OrderRequestType.Return, defaultClaimType(OrderStatus.Shipping))
        assertEquals(OrderRequestType.Return, defaultClaimType(OrderStatus.Delivered))
    }
}

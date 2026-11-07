package com.nativelap.eumshoppingmall.ui.account

import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressListUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressUiModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountAddressListUiModelTest {
    @Test
    fun allowsAddingAddressWhenListIsBelowPositiveLimit() {
        val uiModel = AccountAddressListUiModel(addresses = emptyList(), maximumAddressCount = 1)

        assertTrue(uiModel.canAddAddress)
    }

    @Test
    fun blocksAddingAddressWhenLimitIsZero() {
        val uiModel = AccountAddressListUiModel(addresses = emptyList(), maximumAddressCount = 0)

        assertFalse(uiModel.canAddAddress)
    }

    @Test
    fun blocksAddingAddressWhenListIsAtOrAboveLimit() {
        val address = AccountAddressUiModel(
            id = "address-1",
            label = "기본 배송지",
            recipient = "홍길동",
            phoneNumber = "010-0000-0000",
            postalCode = "00000",
            address = "서울시",
            addressDetail = "",
            deliveryRequest = "",
            isDefault = true,
        )

        assertFalse(
            AccountAddressListUiModel(addresses = listOf(address), maximumAddressCount = 1)
                .canAddAddress,
        )
        assertFalse(
            AccountAddressListUiModel(addresses = listOf(address), maximumAddressCount = 0)
                .canAddAddress,
        )
    }
}

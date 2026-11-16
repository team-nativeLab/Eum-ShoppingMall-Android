package com.nativelap.eumshoppingmall.ui.addresssearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * 주소 검색(A-46). 검색어는 개인정보일 수 있어 저장 상태에 넣지 않고 고른 결과 ID만 저장 상태로 보관하며, 결과 목록은 [searchAddresses]가 매번 만든다.
 * 확정하면 주소 ID 하나만 [onAddressConfirmed]로 넘기며 주소 문자열은 호출부로 보내지 않는다.
 */
@Composable
fun AddressSearchRoute(
    searchAddresses: (query: String) -> AddressSearchUiModel,
    onBackClick: () -> Unit,
    onAddressConfirmed: (addressId: String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var selectedAddressId by rememberSaveable { mutableStateOf<String?>(null) }
    AddressSearchScreen(
        uiModel = remember(query) { searchAddresses(query) }.copy(query = query, selectedAddressId = selectedAddressId),
        onEvent = { event ->
            when (event) {
                AddressSearchScreenEvent.BackClicked -> onBackClick()

                is AddressSearchScreenEvent.QueryChanged -> {
                    query = event.query
                    selectedAddressId = null
                }

                is AddressSearchScreenEvent.ResultSelected -> selectedAddressId = event.addressId

                is AddressSearchScreenEvent.AddressSelectionConfirmed -> onAddressConfirmed(event.addressId)
            }
        },
    )
}

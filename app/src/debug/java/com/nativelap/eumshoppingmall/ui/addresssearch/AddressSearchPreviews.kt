package com.nativelap.eumshoppingmall.ui.addresssearch

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val addressPreviewResults = listOf(
    AddressSearchResultUiModel("preview-1", "경기 성남시 분당구 판교역로 235", "경기 성남시 분당구 백현동 532"),
    AddressSearchResultUiModel("preview-2", "경기 성남시 분당구 판교역로 240", "경기 성남시 분당구 삼평동 670"),
    AddressSearchResultUiModel("preview-3", "경기 성남시 분당구 대왕판교로 660", "경기 성남시 분당구 삼평동 670"),
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AddressSearchResultsPreview() {
    EumShoppingMallTheme {
        AddressSearchScreen(
            AddressSearchUiModel("", addressPreviewResults, null, hasSearched = true),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Composable
private fun AddressSearchEmptyPreview() {
    EumShoppingMallTheme {
        AddressSearchScreen(AddressSearchUiModel("존재하지 않는 주소", emptyList(), null, hasSearched = true), onEvent = {})
    }
}

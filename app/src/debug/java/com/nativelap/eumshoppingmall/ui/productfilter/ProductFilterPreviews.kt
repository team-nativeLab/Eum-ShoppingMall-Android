package com.nativelap.eumshoppingmall.ui.productfilter

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductFilterScreenPreview() {
    EumShoppingMallTheme {
        ProductFilterScreen(
            ProductFilterUiModel(
                categories = listOf("전체", "안전모", "안전화"),
                selectedCategory = "전체",
                selectedPriceRange = null,
                selectedSort = ProductFilterSort.Recommended,
            ),
            onEvent = {},
        )
    }
}

package com.nativelap.eumshoppingmall.ui.productreview

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

@ResponsiveUiPreview
@Composable
private fun ProductReviewWritePreview() = EumShoppingMallTheme {
    ProductReviewWriteScreen(
        ProductReviewWriteUiModel(
            brand = "세이프가드",
            product = "ABS 안전모 (내관 조절형)",
            option = "화이트 · 조절형",
            reviewer = "김*현",
            rating = 0,
            content = "",
            canSubmit = false,
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun ProductReviewWriteEmptyPreview() = EumShoppingMallTheme {
    ProductReviewWriteScreen(
        ProductReviewWriteUiModel("세이프가드", "ABS 안전모 (내관 조절형)", "화이트 · 조절형", "김*현"),
        onEvent = {},
    )
}

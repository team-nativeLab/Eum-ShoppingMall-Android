package com.nativelap.eumshoppingmall.ui.productreview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewProductReviews = listOf(
    ProductReviewItemUiModel(
        id = "review-preview-001",
        authorName = "이*현",
        isPurchaseVerified = true,
        dateLabel = "2026.10.20",
        rating = 5,
        optionLabel = "화이트 · 조절형",
        body = "가볍고 튼튼해요. 내관 조절이 쉬워 현장에서 오래 착용해도 편합니다.",
        photos = listOf(
            ProductReviewPhotoUiModel("preview-photo-001", R.drawable.ic_image, isPlaceholder = true),
            ProductReviewPhotoUiModel("preview-photo-002", R.drawable.ic_image, isPlaceholder = true),
        ),
    ),
    ProductReviewItemUiModel(
        id = "review-preview-002",
        authorName = "박*은",
        isPurchaseVerified = true,
        dateLabel = "2026.10.12",
        rating = 4,
        optionLabel = "화이트",
        body = "마감이 깔끔하고 착용감이 안정적입니다. 배송도 빠르게 도착했습니다.",
        photos = emptyList(),
    ),
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductReviewScreenPreview() {
    EumShoppingMallTheme {
        ProductReviewScreen(
            uiModel = ProductReviewUiModel(
                productId = "preview-safety-helmet",
                productName = "ABS 안전모 (내관 조절형)",
                brandName = "세이프가드",
                variantLabel = "화이트 · 조절형",
                rating = 4.7f,
                reviewCountLabel = "3개",
                reviews = previewProductReviews,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "리뷰 없는 상태")
@Composable
private fun ProductReviewEmptyPreview() {
    EumShoppingMallTheme {
        ProductReviewScreen(
            uiModel = ProductReviewUiModel(
                productId = "preview-safety-helmet",
                productName = "ABS 안전모 (내관 조절형)",
                brandName = "세이프가드",
                variantLabel = "화이트 · 조절형",
                rating = 0f,
                reviewCountLabel = "0개",
                reviews = emptyList(),
            ),
            onEvent = {},
        )
    }
}

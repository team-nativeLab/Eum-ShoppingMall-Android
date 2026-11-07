package com.nativelap.eumshoppingmall.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val homePreviewRecommendedProducts = listOf(
    HomeProductUiModel(
        id = "recommended-helmet",
        name = "세이프가드 ABS 안전모",
        priceLabel = "₩18,000",
        ratingLabel = "4.7 (3)",
        ratingValue = 4.7f,
        originalPriceLabel = "₩22,000",
        discountRateLabel = "18%",
    ),
    HomeProductUiModel(
        id = "recommended-boots",
        name = "경량 안전화 4인치",
        priceLabel = "₩54,000",
        ratingLabel = "4.7 (6)",
        ratingValue = 4.7f,
    ),
    HomeProductUiModel(
        id = "recommended-extinguisher",
        name = "분말 소화기 3.3kg",
        priceLabel = "₩32,000",
        ratingLabel = "5.0 (1)",
        ratingValue = 5f,
        originalPriceLabel = "₩35,000",
        discountRateLabel = "8%",
    ),
)

private val homePreviewBestSellerProducts = listOf(
    HomeProductUiModel(
        id = "best-helmet",
        name = "세이프가드 ABS 안전모",
        priceLabel = "₩18,000",
        ratingLabel = "4.7 (3)",
        ratingValue = 4.7f,
        originalPriceLabel = "₩22,000",
        discountRateLabel = "18%",
    ),
    HomeProductUiModel(
        id = "best-boots",
        name = "경량 안전화 4인치",
        priceLabel = "₩54,000",
        ratingLabel = "4.7 (6)",
        ratingValue = 4.7f,
    ),
    HomeProductUiModel(
        id = "best-extinguisher",
        name = "분말 소화기 3.3kg",
        priceLabel = "₩32,000",
        ratingLabel = "5.0 (1)",
        ratingValue = 5f,
        originalPriceLabel = "₩35,000",
        discountRateLabel = "8%",
    ),
    HomeProductUiModel(
        id = "best-gloves",
        name = "방한 작업 장갑",
        priceLabel = "₩12,000",
        ratingLabel = "평점 없음",
        isSoldOut = true,
    ),
)

@Preview(name = "A-02 Home · Figma content", showBackground = true, widthDp = 390, heightDp = 852)
@Composable
private fun HomeFigmaPreview() {
    EumShoppingMallTheme {
        HomeRoute(
            recommendedProducts = homePreviewRecommendedProducts,
            bestSellerProducts = homePreviewBestSellerProducts,
            cartItemCount = 3,
        )
    }
}

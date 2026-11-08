package com.nativelap.eumshoppingmall.ui.benefits

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesScreen
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsScreen
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

@ResponsiveUiPreview
@Composable
private fun FavoritesEmptyPreview() = EumShoppingMallTheme {
    FavoritesScreen(FavoritesUiModel(), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun RecentProductsEmptyPreview() = EumShoppingMallTheme {
    RecentProductsScreen(RecentProductsUiModel(), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun PointsEmptyPreview() = EumShoppingMallTheme {
    PointsScreen(PointsScreenUiModel(availableBalanceLabel = "0원", expiringBalanceLabel = "0원", usageNotice = "적립금은 사용 기한이 지나면 소멸됩니다."), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun PointsHistoryPreview() = EumShoppingMallTheme {
    PointsScreen(
        PointsScreenUiModel(
            availableBalanceLabel = "12,000원",
            expiringBalanceLabel = "1,000원",
            history = listOf(PointHistoryUiModel("earned", "상품 구매 적립", "2026.11.01", "주문번호 0000000000", "+1,000원", "2027.11.01 소멸")),
            usageNotice = "적립금은 사용 기한이 지나면 소멸됩니다.",
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun CouponAvailablePreview() = EumShoppingMallTheme {
    CouponScreen(
        CouponScreenUiModel(
            pointsBalanceLabel = "0원",
            availableCoupons = listOf(CouponUiModel("coupon", "10%", "신규 회원 할인", "5만원 이상 구매 시", "2026.12.31까지", applicableLabel = "전체 상품")),
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun CouponUsedExpiredEmptyPreview() = EumShoppingMallTheme {
    CouponScreen(
        CouponScreenUiModel(
            pointsBalanceLabel = "0원",
            availableCoupons = listOf(CouponUiModel("coupon", "10%", "신규 회원 할인", "5만원 이상 구매 시", "2026.12.31까지", applicableLabel = "전체 상품")),
            selectedTab = CouponTab.UsedExpired,
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun CouponEmptyPreview() = EumShoppingMallTheme {
    CouponScreen(CouponScreenUiModel(pointsBalanceLabel = "0원"), onEvent = {})
}

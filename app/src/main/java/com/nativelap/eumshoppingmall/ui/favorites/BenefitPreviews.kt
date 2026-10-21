package com.nativelap.eumshoppingmall.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.benefits.CouponScreen
import com.nativelap.eumshoppingmall.ui.benefits.CouponScreenUiModel
import com.nativelap.eumshoppingmall.ui.benefits.CouponUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointHistoryUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointsScreen
import com.nativelap.eumshoppingmall.ui.benefits.PointsScreenUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.recent.RecentProductGroupUiModel
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsScreen
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val sampleProducts = listOf(
    CatalogProductUiModel("helmet", "안전모 제품명", CatalogPriceUiModel.Available("₩00,000"), subtitle = "평점 없음", isFavorite = true),
    CatalogProductUiModel("gloves", "작업용 보호 장갑 제품명", CatalogPriceUiModel.QuoteRequired, subtitle = "평점 없음", badgeLabel = "견적 상품", isFavorite = true),
    CatalogProductUiModel("boots", "안전화", CatalogPriceUiModel.Available("₩00,000"), isFavorite = true, isSoldOut = true),
    CatalogProductUiModel("vest", "안전 조끼", CatalogPriceUiModel.Available("₩00,000"), isFavorite = true),
)

private val sampleCoupons = listOf(
    CouponUiModel("coupon-1", "00%", "쿠폰명", "₩00,000 이상 구매 시 · 최대 ₩00,000 할인", "0000.00.00까지", "D-0", "적용 상품"),
    CouponUiModel("coupon-2", "₩00,000", "배송비 쿠폰", "₩00,000 이상 구매 시 · 최대 ₩00,000 할인", "0000.00.00까지", applicableLabel = "배송비"),
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-20 찜한 상품")
@Composable
private fun FavoritesPreview() {
    EumShoppingMallTheme { FavoritesScreen(FavoritesUiModel(sampleProducts, "최근 찜한 순"), {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-20e 찜 빈 상태")
@Composable
private fun FavoritesEmptyPreview() {
    EumShoppingMallTheme { FavoritesScreen(FavoritesUiModel(), {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-21 최근 본 상품")
@Composable
private fun RecentProductsPreview() {
    EumShoppingMallTheme {
        RecentProductsScreen(
            RecentProductsUiModel(
                groups = listOf(RecentProductGroupUiModel("0000.00.00", sampleProducts)),
                retentionNotice = "최근 본 상품은 최대 30개까지 보관합니다.",
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-21e 최근 본 상품 빈 상태")
@Composable
private fun RecentProductsEmptyPreview() {
    EumShoppingMallTheme { RecentProductsScreen(RecentProductsUiModel(), {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-22 쿠폰함")
@Composable
private fun CouponsPreview() {
    EumShoppingMallTheme { CouponScreen(CouponScreenUiModel(pointsBalanceLabel = "0원", availableCoupons = sampleCoupons), {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-22e 쿠폰 없음")
@Composable
private fun CouponsEmptyPreview() {
    EumShoppingMallTheme { CouponScreen(CouponScreenUiModel(pointsBalanceLabel = "0원"), {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-22p 적립금")
@Composable
private fun PointsPreview() {
    EumShoppingMallTheme {
        PointsScreen(
            PointsScreenUiModel(
                availableBalanceLabel = "0원",
                expiringBalanceLabel = "0원",
                history = listOf(
                    PointHistoryUiModel("order-1", "주문 적립", "0000.00.00", "주문번호 0000000000", "+0원", "만료 0000.00.00"),
                    PointHistoryUiModel("order-2", "주문 사용", "0000.00.00", "주문번호 0000000000", "-0원"),
                ),
                usageNotice = "적립금은 결제 시 1원 단위로 사용할 수 있습니다.",
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-22pe 적립 내역 없음")
@Composable
private fun PointsEmptyPreview() {
    EumShoppingMallTheme {
        PointsScreen(PointsScreenUiModel("0원", "0원", usageNotice = "적립금은 결제 시 1원 단위로 사용할 수 있습니다."), {})
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.3f, name = "찜 좁은 화면·큰 글자")
@Composable
private fun FavoritesAdaptivePreview() {
    EumShoppingMallTheme { FavoritesScreen(FavoritesUiModel(sampleProducts, "최근 찜한 순"), {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.3f, name = "최근 본 상품 좁은 화면·큰 글자")
@Composable
private fun RecentAdaptivePreview() {
    EumShoppingMallTheme {
        RecentProductsScreen(
            RecentProductsUiModel(
                groups = listOf(RecentProductGroupUiModel("0000.00.00", sampleProducts)),
                retentionNotice = "최근 본 상품은 최대 30개까지 보관합니다.",
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.3f, name = "쿠폰 좁은 화면·큰 글자")
@Composable
private fun CouponsAdaptivePreview() {
    EumShoppingMallTheme { CouponScreen(CouponScreenUiModel(pointsBalanceLabel = "₩1,234원", availableCoupons = sampleCoupons), {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.3f, name = "적립금 좁은 화면·큰 글자")
@Composable
private fun PointsAdaptivePreview() {
    EumShoppingMallTheme {
        PointsScreen(
            PointsScreenUiModel(
                availableBalanceLabel = "₩1,234,567원",
                expiringBalanceLabel = "₩1,234원",
                history = listOf(PointHistoryUiModel("long", "주문 취소 후 적립금이 복원되었습니다", "0000.00.00", "주문번호 000000000000", "+0원", "만료 0000.00.00")),
                usageNotice = "적립금은 결제 시 1원 단위로 사용할 수 있습니다.",
            ),
            {},
        )
    }
}

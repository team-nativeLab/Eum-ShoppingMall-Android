package com.nativelap.eumshoppingmall.ui.benefits

data class CouponUiModel(
    val id: String,
    val discountLabel: String,
    val name: String,
    val conditionLabel: String,
    val expiryLabel: String,
    val badgeLabel: String? = null,
    val applicableLabel: String,
)

data class CouponScreenUiModel(
    val pointsBalanceLabel: String,
    val availableCoupons: List<CouponUiModel> = emptyList(),
    val usedExpiredCoupons: List<CouponUiModel> = emptyList(),
    val availableCouponCount: Int = availableCoupons.size,
    val usedExpiredCouponCount: Int = usedExpiredCoupons.size,
    val selectedTab: CouponTab = CouponTab.Available,
    val registeredCode: String = "",
) {
    val selectedCoupons: List<CouponUiModel>
        get() = when (selectedTab) {
            CouponTab.Available -> availableCoupons
            CouponTab.UsedExpired -> usedExpiredCoupons
        }

    fun countFor(tab: CouponTab): Int = when (tab) {
        CouponTab.Available -> availableCouponCount
        CouponTab.UsedExpired -> usedExpiredCouponCount
    }
}

enum class CouponTab {
    Available,
    UsedExpired,
}

sealed interface CouponScreenEvent {
    data object BackClicked : CouponScreenEvent
    data class CodeChanged(val code: String) : CouponScreenEvent
    data object RegisterClicked : CouponScreenEvent
    data class TabSelected(val tab: CouponTab) : CouponScreenEvent
    data class CouponClicked(val couponId: String) : CouponScreenEvent
    data object BrowseProductsClicked : CouponScreenEvent
    data object PointsClicked : CouponScreenEvent
}

data class PointHistoryUiModel(
    val id: String,
    val title: String,
    val dateLabel: String,
    val orderNumberLabel: String,
    val amountLabel: String,
    val expiryLabel: String? = null,
)

data class PointsScreenUiModel(
    val availableBalanceLabel: String,
    val expiringBalanceLabel: String,
    val selectedFilter: PointsFilter = PointsFilter.All,
    val history: List<PointHistoryUiModel> = emptyList(),
    val usageNotice: String,
)

enum class PointsFilter {
    All,
    Earned,
    Used,
    Expired,
}

sealed interface PointsScreenEvent {
    data object BackClicked : PointsScreenEvent
    data class FilterSelected(val filter: PointsFilter) : PointsScreenEvent
    data object ShopClicked : PointsScreenEvent
}

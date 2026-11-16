package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeCategoryUiModel

/** 앱 화면에서 함께 사용하는 카테고리 탐색 항목. 서버 카테고리 ID가 아닌 화면 흐름용 ID다. */
object CatalogCategoryDefinitions {
    val all: List<CatalogCategoryUiModel> = listOf(
        CatalogCategoryUiModel("protective", R.string.home_category_protective_equipment, R.drawable.ic_hard_hat),
        CatalogCategoryUiModel("uniform", R.string.home_category_workwear, R.drawable.ic_shirt),
        CatalogCategoryUiModel("traffic", R.string.home_category_traffic_safety, R.drawable.ic_traffic_cone),
        CatalogCategoryUiModel("fire", R.string.home_category_fire_safety, R.drawable.ic_fire_extinguisher),
        CatalogCategoryUiModel("industrial", R.string.home_category_industrial_supplies, R.drawable.ic_briefcase_medical),
        CatalogCategoryUiModel("security", R.string.home_category_security, R.drawable.ic_cctv),
        CatalogCategoryUiModel("living", R.string.home_category_home_safety, R.drawable.ic_leaf),
        CatalogCategoryUiModel("custom", R.string.home_category_custom_orders, R.drawable.ic_package),
    )

    fun homeItems(): List<HomeCategoryUiModel> = all.map { category ->
        HomeCategoryUiModel(category.id, category.labelResId, category.iconResId)
    }
}

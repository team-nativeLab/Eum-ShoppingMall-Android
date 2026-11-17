package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeCategoryUiModel

/** 앱 화면에서 함께 사용하는 카테고리 탐색 항목. 서버 카테고리 ID가 아닌 화면 흐름용 ID다. */
object CatalogCategoryDefinitions {
    val all: List<CatalogCategoryUiModel> = listOf(
        CatalogCategoryUiModel(
            id = "protective",
            labelResId = R.string.home_category_protective_equipment,
            iconResId = R.drawable.ic_hard_hat,
            descriptionResId = null,
        ),
        CatalogCategoryUiModel(
            id = "uniform",
            labelResId = R.string.home_category_workwear,
            iconResId = R.drawable.ic_shirt,
            descriptionResId = null,
        ),
        CatalogCategoryUiModel(
            id = "traffic",
            labelResId = R.string.home_category_traffic_safety,
            iconResId = R.drawable.ic_traffic_cone,
            descriptionResId = null,
        ),
        CatalogCategoryUiModel(
            id = "fire",
            labelResId = R.string.home_category_fire_safety,
            iconResId = R.drawable.ic_fire_extinguisher,
            descriptionResId = null,
        ),
        CatalogCategoryUiModel("industrial", R.string.home_category_industrial_supplies, R.drawable.ic_briefcase_medical),
        CatalogCategoryUiModel("security", R.string.home_category_security, R.drawable.ic_cctv),
        CatalogCategoryUiModel("living", R.string.home_category_home_safety, R.drawable.ic_leaf),
        CatalogCategoryUiModel("custom", R.string.home_category_custom_orders, R.drawable.ic_package),
    )

    fun homeItems(): List<HomeCategoryUiModel> = all.map { category ->
        HomeCategoryUiModel(
            id = category.id,
            labelResId = category.labelResId,
            iconResId = category.iconResId,
            descriptionResId = category.descriptionResId,
        )
    }
}

package com.nativelap.eumshoppingmall.ui.catalog

import androidx.annotation.StringRes
import com.nativelap.eumshoppingmall.R
import kotlinx.serialization.Serializable

/**
 * 상품 목록이 시작할 기본 상품 원본. 홈 섹션의 "전체 보기"에서 넘어온다.
 *
 * All은 카테고리 목록(기존 전체 상품)이고, 나머지는 홈 섹션과 같은 상품 원본을 쓴다.
 */
@Serializable
enum class ProductListPreset {
    All,
    Best,
    Discount,
    Today,
    Recommended,
}

/** 목록 상단 제목 리소스. All은 카테고리 제목을 쓰므로 null이다. */
@StringRes
fun ProductListPreset.titleResId(): Int? = when (this) {
    ProductListPreset.All -> null
    ProductListPreset.Best -> R.string.product_list_title_best
    ProductListPreset.Discount -> R.string.product_list_title_discount
    ProductListPreset.Today -> R.string.product_list_title_today
    ProductListPreset.Recommended -> R.string.product_list_title_recommended
}

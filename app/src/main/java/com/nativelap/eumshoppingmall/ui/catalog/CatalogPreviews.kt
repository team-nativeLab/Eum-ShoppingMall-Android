package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewProducts = listOf(
    CatalogProductUiModel(
        id = "preview-helmet",
        name = "안전모 제품명",
        subtitle = "산업용 안전모",
        price = CatalogPriceUiModel.Available("₩00,000", "₩00,000", "10%"),
        ratingLabel = "4.8",
        reviewCountLabel = "(128)",
        badgeLabel = "BEST",
    ),
    CatalogProductUiModel(
        id = "preview-glove",
        name = "작업용 보호 장갑 제품명",
        subtitle = "내절단 기능성 장갑",
        price = CatalogPriceUiModel.QuoteRequired,
        ratingLabel = "4.6",
        reviewCountLabel = "(42)",
    ),
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "검색 결과 populated")
@Composable
private fun CatalogSearchResultsPreview() {
    EumShoppingMallTheme {
        CatalogSearchResultsScreen(
            uiModel = CatalogSearchResultsUiModel(
                query = "안전모",
                filters = listOf("전체", "안전모", "보호구"),
                selectedFilter = "전체",
                resultCount = 24,
                sortLabel = "인기순",
                products = previewProducts,
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "상품 상세")
@Composable
private fun CatalogProductDetailPreview() {
    EumShoppingMallTheme {
        CatalogProductDetailScreen(
            uiModel = CatalogProductDetailUiModel(
                product = previewProducts.first(),
                galleryImageResIds = listOf(null, null, null, null),
                shippingRows = listOf(
                    CatalogKeyValueUiModel("배송", "3일 이내 출고"),
                    CatalogKeyValueUiModel("배송비", "3,000원"),
                ),
                specifications = listOf(CatalogKeyValueUiModel("색상", "백색")),
                returnRows = listOf(CatalogKeyValueUiModel("반품 안내", "수령 후 7일 이내 접수")),
                reviewCount = 128,
                questionCount = 12,
                reviews = listOf(CatalogReviewUiModel("구매자", "5.0", "2026.10.20", "제품이 튼튼하고 배송도 빨랐습니다.")),
                questions = listOf(CatalogQuestionUiModel("사이즈 선택은 어떻게 하나요?", "상세 사이즈표를 확인해 주세요.", "2026.10.20")),
                relatedProducts = previewProducts,
                optionGroups = listOf(
                    CatalogOptionGroupUiModel(
                        id = "size",
                        label = "사이즈 선택",
                        options = listOf(CatalogOptionUiModel("size-m", "M", 1, "₩00,000")),
                        selectedOptionId = "size-m",
                    ),
                ),
                selectedOptions = listOf(CatalogOptionUiModel("size-m", "M", 1, "₩00,000")),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "견적 상품 옵션 시트")
@Composable
private fun CatalogQuoteOptionsPreview() {
    EumShoppingMallTheme {
        CatalogProductDetailScreen(
            uiModel = CatalogProductDetailUiModel(
                product = previewProducts.last(),
                optionsSheetVisible = true,
                optionGroups = listOf(
                    CatalogOptionGroupUiModel(
                        id = "size",
                        label = "사이즈 선택",
                        options = listOf(CatalogOptionUiModel("size-l", "L", 2, null)),
                        selectedOptionId = "size-l",
                    ),
                ),
                selectedOptions = listOf(CatalogOptionUiModel("size-l", "L", 2, null)),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "검색 결과 없음")
@Composable
private fun CatalogSearchEmptyPreview() {
    EumShoppingMallTheme {
        CatalogSearchEmptyScreen(query = "검색어", popularSearchLabel = "안전모", onEvent = {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "브랜드 목록")
@Composable
private fun CatalogBrandListPreview() {
    val brands = listOf(
        CatalogBrandUiModel("alpha", "브랜드 이름", "24", letterGroup = "A"),
        CatalogBrandUiModel("beta", "다른 브랜드", "12", letterGroup = "B"),
    )
    EumShoppingMallTheme {
        CatalogBrandListScreen(
            uiModel = CatalogBrandListUiModel(
                featuredBrand = brands.first(),
                letters = listOf("A", "B"),
                brandsByLetter = mapOf("A" to listOf(brands.first()), "B" to listOf(brands.last())),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "브랜드 상세")
@Composable
private fun CatalogBrandDetailPreview() {
    val brand = CatalogBrandUiModel("preview-brand", "브랜드 이름", "24")
    EumShoppingMallTheme {
        CatalogBrandDetailScreen(
            uiModel = CatalogBrandDetailUiModel(
                brand = brand,
                description = "브랜드 소개 문구가 표시되는 영역입니다.",
                informationRows = listOf(CatalogKeyValueUiModel("원산지", "대한민국")),
                products = previewProducts,
                otherBrands = listOf(CatalogBrandUiModel("other", "다른 브랜드", "12")),
                categories = listOf("전체", "안전모", "보호구"),
                sortLabel = "인기순",
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "카테고리 목록")
@Composable
private fun CatalogCategoryPreview() {
    EumShoppingMallTheme {
        CatalogCategoryScreen(
            uiModel = CatalogCategoryScreenUiModel(
                categories = listOf(
                    CatalogCategoryUiModel("safety", "안전모", R.drawable.ic_hard_hat),
                    CatalogCategoryUiModel("uniform", "작업복", R.drawable.ic_shirt),
                    CatalogCategoryUiModel("safety-tools", "안전용품", R.drawable.ic_traffic_cone),
                ),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "상품 목록")
@Composable
private fun CatalogProductListPreview() {
    EumShoppingMallTheme {
        CatalogProductListScreen(
            uiModel = CatalogProductListUiModel(
                title = "개인보호장비",
                categories = listOf("전체", "안전모", "보호구", "작업복"),
                selectedCategory = "전체",
                resultCountLabel = "총 24개",
                sortLabel = "인기순",
                products = previewProducts,
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "검색 홈")
@Composable
private fun CatalogSearchPreview() {
    EumShoppingMallTheme {
        CatalogSearchScreen(
            uiModel = CatalogSearchUiModel(
                recentTerms = listOf("안전모", "작업 장갑", "보안경", "안전화"),
                popularTerms = listOf("안전모", "안전화", "작업복", "보안경", "장갑", "마스크"),
            ),
            onEvent = {},
        )
    }
}

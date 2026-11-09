package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
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
    CatalogProductUiModel(
        id = "preview-sold-out",
        name = "품절 상품 긴 이름이 여러 줄에 표시되는 예시 제품",
        subtitle = "재입고 알림 신청 가능",
        price = CatalogPriceUiModel.Available("₩00,000"),
        isSoldOut = true,
    ),
)

// A-05 상품 상세 Figma(179:938) 기준 샘플. 관련 상품도 같은 프레임의 항목을 쓴다.
private val productDetailFigmaRelatedProducts = listOf(
    CatalogProductUiModel(
        id = "related-boots",
        name = "경량 안전화 4인치",
        price = CatalogPriceUiModel.Available("₩54,000"),
        ratingLabel = "4.7",
        reviewCountLabel = "(6)",
    ),
    CatalogProductUiModel(
        id = "related-belt",
        name = "전신 안전벨트",
        price = CatalogPriceUiModel.QuoteRequired,
    ),
    CatalogProductUiModel(
        id = "related-coating-glove",
        name = "코팅 장갑 10컬렉",
        price = CatalogPriceUiModel.QuoteRequired,
        ratingLabel = "4.0",
        reviewCountLabel = "(1)",
    ),
)

// A-04 상품 목록 Figma(179:743) 기준 샘플.
private val productListFigmaProducts = listOf(
    CatalogProductUiModel(
        id = "list-safety-helmet",
        name = "세이프가드 ABS 안전모",
        price = CatalogPriceUiModel.Available("₩18,000", "₩22,000", "18%"),
        ratingLabel = "4.7",
        reviewCountLabel = "(3)",
    ),
    CatalogProductUiModel(
        id = "list-boots",
        name = "경량 안전화 4인치",
        price = CatalogPriceUiModel.Available("₩54,000"),
        ratingLabel = "4.7",
        reviewCountLabel = "(6)",
    ),
    CatalogProductUiModel(
        id = "list-coating-glove",
        name = "코팅 장갑 10컬렉",
        price = CatalogPriceUiModel.QuoteRequired,
        ratingLabel = "4.0",
        reviewCountLabel = "(1)",
        badgeLabel = "견적 상품",
    ),
    CatalogProductUiModel(
        id = "list-extinguisher",
        name = "분말 소화기 3.3kg",
        price = CatalogPriceUiModel.Available("₩32,000", "₩35,000", "8%"),
        ratingLabel = "5.0",
        reviewCountLabel = "(1)",
        isSoldOut = true,
    ),
)

// A-06b 검색 결과 Figma(180:1511) 기준 샘플. 제품명·가격은 Figma의 자리표시 값을 그대로 쓴다.
private val searchResultFigmaProducts = listOf(
    CatalogProductUiModel(
        id = "search-placeholder-1",
        name = "제품명",
        price = CatalogPriceUiModel.Available("₩18,000", "₩22,000", "18%"),
    ),
    CatalogProductUiModel(
        id = "search-placeholder-2",
        name = "제품명",
        price = CatalogPriceUiModel.Available("₩00,000"),
    ),
    CatalogProductUiModel(
        id = "search-placeholder-3",
        name = "제품명",
        price = CatalogPriceUiModel.Available("₩00,000"),
    ),
    CatalogProductUiModel(
        id = "search-placeholder-4",
        name = "제품명",
        price = CatalogPriceUiModel.QuoteRequired,
        badgeLabel = "견적 상품",
    ),
)

// A-33 브랜드 상세 Figma(616:13032) 기준 샘플. 자리표시 값은 Figma와 같다.
private const val SEARCH_PREVIEW_RESULT_COUNT = 24

// 미리보기 샘플 브랜드. 제품 수는 Int로 두고 표시 문자열은 여기서만 만든다.
private fun previewBrand(id: String, name: String, productCount: Int, letterGroup: String? = null) = CatalogBrandUiModel(
    id = id,
    name = name,
    productCountLabel = "제품 ${productCount}개",
    letterGroup = letterGroup,
    productCount = productCount,
)

private val brandDetailFigmaProducts = listOf(
    CatalogProductUiModel(id = "brand-placeholder-1", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000")),
    CatalogProductUiModel(id = "brand-placeholder-2", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000")),
    CatalogProductUiModel(id = "brand-placeholder-3", name = "제품명", price = CatalogPriceUiModel.QuoteRequired, badgeLabel = "견적 상품"),
    CatalogProductUiModel(id = "brand-placeholder-4", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000")),
    CatalogProductUiModel(id = "brand-placeholder-5", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000"), isSoldOut = true),
    CatalogProductUiModel(id = "brand-placeholder-6", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000")),
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "검색 결과 populated")
@Composable
private fun CatalogSearchResultsPreview() {
    EumShoppingMallTheme {
        CatalogSearchResultsScreen(
            uiModel = CatalogSearchResultsUiModel(
                query = "안전모",
                filters = listOf("전체 $SEARCH_PREVIEW_RESULT_COUNT", "개인보호장비 12", "작업복/유니폼 8", "교통·도로 안전 4"),
                selectedFilter = "전체 $SEARCH_PREVIEW_RESULT_COUNT",
                resultCount = SEARCH_PREVIEW_RESULT_COUNT,
                sortLabel = "추천순",
                products = searchResultFigmaProducts,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "상품 상세")
@Composable
private fun CatalogProductDetailPreview() {
    EumShoppingMallTheme {
        CatalogProductDetailScreen(
            uiModel = CatalogProductDetailUiModel(
                product = CatalogProductUiModel(
                    id = "preview-abs-helmet",
                    name = "ABS 안전모 (내관 조절형)",
                    subtitle = "세이프가드",
                    price = CatalogPriceUiModel.Available("₩18,000", "₩22,000", "18%"),
                    ratingLabel = "4.7",
                    reviewCountLabel = "리뷰 3개",
                ),
                description = "가볍고 튼튼한 ABS 안전모입니다.\n내관 조절형으로 편하게 착용할 수 있습니다.",
                galleryImageResIds = listOf(null, null, null, null, null),
                shippingRows = listOf(
                    CatalogKeyValueUiModel("배송", "일반 배송 (2~3일) · 무료"),
                    CatalogKeyValueUiModel("인증", "KCs 안전인증"),
                    CatalogKeyValueUiModel("제조사", "세이프가드"),
                ),
                specifications = listOf(
                    CatalogKeyValueUiModel("모델명", "SG-H100"),
                    CatalogKeyValueUiModel("소재", "ABS"),
                    CatalogKeyValueUiModel("사이즈", "내관 조절형"),
                    CatalogKeyValueUiModel("안전인증", "KCs 안전인증"),
                    CatalogKeyValueUiModel("원산지", "대한민국"),
                    CatalogKeyValueUiModel("제조사", "세이프가드"),
                ),
                returnRows = listOf(
                    CatalogKeyValueUiModel("교환·반품", "수령 후 7일 이내 접수"),
                    CatalogKeyValueUiModel("반품 배송비", "편도 ₩3,000"),
                ),
                reviewCount = 3,
                questionCount = 0,
                reviews = listOf(CatalogReviewUiModel("이*현", "5.0", "2026.10.20", "가볍고 튼튼해요. 내관 조절이 쉬워 현장에서 오래 착용해도 편합니다.")),
                relatedProducts = productDetailFigmaRelatedProducts,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
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
                        options = listOf(
                            CatalogOptionUiModel("size-l", "사이즈 L", 1, null),
                            CatalogOptionUiModel("size-xl", "사이즈 XL", 1, null),
                        ),
                    ),
                ),
                selectedOptions = listOf(
                    CatalogSelectedOptionUiModel("size", CatalogOptionUiModel("size-l", "사이즈 L", 1, null)),
                    CatalogSelectedOptionUiModel("size", CatalogOptionUiModel("size-xl", "사이즈 XL", 1, null)),
                ),
            ),
            onEvent = {},
        )
    }
}

// A-05s 옵션 바텀시트 Figma(180:1142) 기준 샘플. 사이즈 L·XL이 선택된 상태다.
@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "일반 상품 옵션 선택")
@Composable
private fun CatalogUnselectedOptionsPreview() {
    EumShoppingMallTheme {
        CatalogProductDetailScreen(
            uiModel = CatalogProductDetailUiModel(
                product = CatalogProductUiModel(
                    id = "preview-abs-helmet",
                    name = "ABS 안전모 (내관 조절형)",
                    subtitle = "세이프가드",
                    price = CatalogPriceUiModel.Available("₩00,000", "₩22,000", "18%"),
                ),
                optionsSheetVisible = true,
                optionGroups = listOf(
                    CatalogOptionGroupUiModel(
                        id = "size",
                        label = "사이즈",
                        options = listOf(
                            CatalogOptionUiModel("size-l", "사이즈 L", 1, "₩00,000"),
                            CatalogOptionUiModel("size-xl", "사이즈 XL", 1, "₩00,000"),
                        ),
                    ),
                ),
                selectedOptions = listOf(
                    CatalogSelectedOptionUiModel("size", CatalogOptionUiModel("size-l", "사이즈 L", 1, "₩00,000")),
                    CatalogSelectedOptionUiModel("size", CatalogOptionUiModel("size-xl", "사이즈 XL", 1, "₩00,000")),
                ),
                totalPriceLabel = "₩00,000",
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "검색 결과 없음")
@Composable
private fun CatalogSearchEmptyPreview() {
    EumShoppingMallTheme {
        CatalogSearchEmptyScreen(query = "방폭 헬멧", onEvent = {})
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "브랜드 목록")
@Composable
private fun CatalogBrandListPreview() {
    val featuredBrand = previewBrand("featured", "브랜드 A", 12)
    val brandsByLetter = mapOf(
        "ㄱ" to listOf(
            previewBrand("brand-a", "브랜드 A", 12, letterGroup = "ㄱ"),
            previewBrand("brand-b", "브랜드 B", 8, letterGroup = "ㄱ"),
            previewBrand("brand-c", "브랜드 C", 5, letterGroup = "ㄱ"),
            previewBrand("brand-d", "브랜드 D", 3, letterGroup = "ㄱ"),
        ),
        "ㄷ" to listOf(
            previewBrand("brand-e", "브랜드 E", 7, letterGroup = "ㄷ"),
            previewBrand("brand-f", "브랜드 F", 4, letterGroup = "ㄷ"),
            previewBrand("brand-g", "브랜드 G", 9, letterGroup = "ㄷ"),
        ),
        "A-Z" to listOf(
            previewBrand("brand-h", "브랜드 H", 6, letterGroup = "A-Z"),
            previewBrand("brand-i", "브랜드 I", 2, letterGroup = "A-Z"),
        ),
    )
    EumShoppingMallTheme {
        CatalogBrandListScreen(
            uiModel = CatalogBrandListUiModel(
                featuredBrand = featuredBrand,
                featuredDescription = "브랜드 한 줄 소개가 들어갑니다.",
                letters = listOf("ㄱ", "ㄷ", "A-Z"),
                brandsByLetter = brandsByLetter,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "브랜드 상세")
@Composable
private fun CatalogBrandDetailPreview() {
    val brand = previewBrand("preview-brand", "브랜드 A", 12)
    EumShoppingMallTheme {
        CatalogBrandDetailScreen(
            uiModel = CatalogBrandDetailUiModel(
                brand = brand,
                description = "브랜드 한 줄 소개가 들어갑니다. 서버에서 받은 소개 문구를 최대 세 줄까지 표시합니다.",
                storyTitle = "브랜드 스토리 제목이 들어갑니다.",
                storyBody = "브랜드 소개 본문이 들어갑니다. 서버에서 받은 브랜드 소개 문구를 표시하며, 브랜드의 시작, 주력 분야, 현장에서의 쓰임, 인증과 품질 관리, 납품 이력처럼 담당자가 신뢰를 확인할 수 있는 내용을 담습니다.",
                informationRows = listOf(
                    CatalogKeyValueUiModel("주력 분야", "분야명"),
                    CatalogKeyValueUiModel("주요 인증", "인증명 0개"),
                    CatalogKeyValueUiModel("원산지", "원산지"),
                ),
                products = brandDetailFigmaProducts,
                otherBrands = listOf(
                    previewBrand("brand-b", "브랜드 B", 8),
                    previewBrand("brand-c", "브랜드 C", 5),
                    previewBrand("brand-d", "브랜드 D", 3),
                ),
                categories = listOf("전체", "개인보호장비", "작업복/유니폼", "교통·도로 안전"),
                selectedCategory = "전체",
                sortLabel = "추천순",
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "카테고리 목록")
@Composable
private fun CatalogCategoryPreview() {
    EumShoppingMallTheme {
        CatalogCategoryScreen(
            uiModel = CatalogCategoryScreenUiModel(
                categories = listOf(
                    CatalogCategoryUiModel("protective", "개인보호장비", R.drawable.ic_hard_hat),
                    CatalogCategoryUiModel("uniform", "작업복/유니폼", R.drawable.ic_shirt),
                    CatalogCategoryUiModel("traffic", "교통·도로 안전", R.drawable.ic_traffic_cone),
                    CatalogCategoryUiModel("fire", "소방·방재용품", R.drawable.ic_fire_extinguisher),
                    CatalogCategoryUiModel("industrial", "산업 현장용품", R.drawable.ic_briefcase_medical),
                    CatalogCategoryUiModel("security", "보안·감시", R.drawable.ic_cctv),
                    CatalogCategoryUiModel("living", "생활 안전용품", R.drawable.ic_leaf),
                    CatalogCategoryUiModel("custom", "맞춤 제작/납품", R.drawable.ic_package),
                ),
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "상품 목록")
@Composable
private fun CatalogProductListPreview() {
    EumShoppingMallTheme {
        CatalogProductListScreen(
            uiModel = CatalogProductListUiModel(
                title = "개인보호장비",
                categories = listOf("전체", "안전모", "보안경", "안전장갑", "방진마스크"),
                selectedCategory = "전체",
                resultCountLabel = "총 40개",
                sortLabel = "추천순",
                products = productListFigmaProducts,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "상품 목록 리스트·품절 상품")
@Composable
private fun CatalogProductListRowsPreview() {
    EumShoppingMallTheme {
        CatalogProductListScreen(
            uiModel = CatalogProductListUiModel(
                title = "개인보호장비",
                categories = listOf("전체", "안전모", "보호구", "작업복"),
                selectedCategory = "전체",
                resultCountLabel = "총 24개",
                sortLabel = "인기순",
                products = previewProducts,
                isListLayout = true,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "상품 목록 빈 상태")
@Composable
private fun CatalogProductListEmptyPreview() {
    EumShoppingMallTheme {
        CatalogProductListScreen(
            uiModel = CatalogProductListUiModel(
                title = "개인보호장비",
                sortLabel = "인기순",
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.2f, name = "좁은 화면 상품 목록")
@Composable
private fun CatalogProductListNarrowPreview() {
    EumShoppingMallTheme {
        CatalogProductListScreen(
            uiModel = CatalogProductListUiModel(
                title = "개인보호장비",
                categories = listOf("전체", "안전모", "보호구", "작업복"),
                selectedCategory = "전체",
                resultCountLabel = "총 24개",
                sortLabel = "인기순",
                products = previewProducts,
                isListLayout = true,
            ),
            onEvent = {},
        )
    }
}

// A-06 검색 홈 Figma(180:1374) 기준 샘플. 인기 검색어 10개는 Figma의 자리표시 문구를 그대로 쓴다.
@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "검색 홈")
@Composable
private fun CatalogSearchPreview() {
    EumShoppingMallTheme {
        CatalogSearchScreen(
            uiModel = CatalogSearchUiModel(
                recentTerms = listOf("안전모", "작업복", "안전장갑", "교통안전봉", "소화기"),
                popularTerms = List(10) { "인기 검색어" },
                popularUpdatedAtLabel = "00:00",
            ),
            onEvent = {},
        )
    }
}

package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountMenuRowUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountOrderStatusUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountShortcutUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.addresssearch.AddressSearchResultUiModel
import com.nativelap.eumshoppingmall.ui.addresssearch.AddressSearchUiModel
import com.nativelap.eumshoppingmall.ui.benefits.CouponScreenUiModel
import com.nativelap.eumshoppingmall.ui.benefits.CouponUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointHistoryUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointsScreenUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CATALOG_ALL_CATEGORY_ID
import com.nativelap.eumshoppingmall.ui.catalog.CatalogBrandDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogBrandUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryChipUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryDefinitions
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryScreenUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogKeyValueUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionGroupUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogReviewUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchResultsUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchUiModel
import com.nativelap.eumshoppingmall.ui.catalog.cartShippingFeeWon
import com.nativelap.eumshoppingmall.ui.catalog.formatWonLabel
import com.nativelap.eumshoppingmall.ui.catalog.productShippingFeeLabel
import com.nativelap.eumshoppingmall.ui.catalog.productShippingFeeValue
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutProductUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.events.EventDetailUiModel
import com.nativelap.eumshoppingmall.ui.events.EventUiModel
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryProgressStage
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTimelineItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailAddressUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryAction
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderPaymentBreakdownUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestOrderSelectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestReason
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.RequestableOrderUiModel
import com.nativelap.eumshoppingmall.ui.productreview.ProductReviewItemUiModel
import com.nativelap.eumshoppingmall.ui.productreview.ProductReviewUiModel
import com.nativelap.eumshoppingmall.ui.productreview.ProductReviewWriteUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteDetailItemUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryListItemUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.recent.RecentProductGroupUiModel
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsUiModel
import com.nativelap.eumshoppingmall.ui.support.CustomerSupportFaqUiModel
import com.nativelap.eumshoppingmall.ui.support.CustomerSupportUiModel
import com.nativelap.eumshoppingmall.ui.support.InquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.support.InquiryHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.support.InquiryStatus
import com.nativelap.eumshoppingmall.ui.support.NoticeDetailUiModel
import com.nativelap.eumshoppingmall.ui.support.NoticeItemUiModel
import com.nativelap.eumshoppingmall.ui.support.OneToOneInquiryUiModel

/**
 * debug 빌드에서 화면 흐름(F1~F7)을 눈으로 따라갈 수 있게 하는 고정 샘플.
 *
 * 실제 상품·가격이 아니며 release 소스셋에는 포함되지 않는다. 로그인 상태는 이 제공자가 아니라 로그인 세션이 소유하며,
 * 모든 빌드에서 초기값은 비로그인이다(debug는 mock 로그인으로 전환한다).
 */
object SampleShoppingScreenDataProvider : ShoppingScreenDataProvider {
    override val enablesDebugCartFallback: Boolean = true
    private const val QUOTE_ONLY_PRODUCT_ID = "sample-belt"

    // 상위 카테고리(categoryId)와 세부 카테고리(subcategoryId)를 나눠 둔다. 목록 칩은 세부, 홈 베스트 탭은 상위를 쓴다.
    // 베스트 순위는 상품 속성이 아니라 홈 베스트 섹션의 표시 순서로 정한다(목 데이터에는 두지 않는다).
    private val sampleProducts = listOf(
        CatalogProductUiModel(
            id = "sample-helmet",
            name = "세이프가드 ABS 안전모",
            subtitle = "샘플 브랜드 A",
            price = CatalogPriceUiModel.Available(
                currentLabel = "₩18,000",
                originalLabel = "₩22,000",
                discountLabel = "18%",
                unitPriceWon = 18_000L,
            ),
            ratingLabel = "4.7",
            ratingValue = 4.7f,
            reviewCountLabel = "(3)",
            specLabel = "1개",
            shippingLabel = "당일 출고",
            benefitLabels = listOf("쿠폰 적용"),
            categoryId = "protective",
            subcategoryId = "helmet",
            hasOptions = false,
        ),
        CatalogProductUiModel(
            id = "sample-boots",
            name = "경량 안전화 4인치",
            subtitle = "샘플 브랜드 A",
            price = CatalogPriceUiModel.Available(currentLabel = "₩54,000", unitPriceWon = 54_000L),
            ratingLabel = "4.7",
            ratingValue = 4.7f,
            reviewCountLabel = "(6)",
            specLabel = "10켤레",
            shippingLabel = "무료배송",
            categoryId = "protective",
            subcategoryId = "boots",
            hasOptions = true,
        ),
        CatalogProductUiModel(
            id = QUOTE_ONLY_PRODUCT_ID,
            name = "전신 안전벨트",
            subtitle = "샘플 브랜드 B",
            price = CatalogPriceUiModel.QuoteRequired,
            specLabel = "1개",
            shippingLabel = "배송 문의",
            badgeLabel = "견적 상품",
            categoryId = "protective",
            subcategoryId = "belt",
            hasOptions = false,
        ),
        CatalogProductUiModel(
            id = "sample-extinguisher",
            name = "분말 소화기 3.3kg",
            subtitle = "샘플 브랜드 B",
            price = CatalogPriceUiModel.Available(
                currentLabel = "₩32,000",
                originalLabel = "₩35,000",
                discountLabel = "8%",
                unitPriceWon = 32_000L,
            ),
            specLabel = "3.3kg",
            shippingLabel = "당일 출고",
            categoryId = "fire",
            subcategoryId = "extinguisher",
            hasOptions = false,
        ),
    ).map(::withShippingPolicy)

    // 카테고리 탭이 빈 목록으로 막히지 않도록 나머지 6개 카테고리에 상품을 하나씩 둔다.
    // 홈 섹션(추천·오늘의 상품·베스트)은 sampleProducts만 쓰므로 이 목록은 홈 화면 내용을 바꾸지 않는다.
    private val sampleCategoryProducts = listOf(
        CatalogProductUiModel(
            id = "sample-workwear",
            name = "현장 작업복 상의 (네이비)",
            subtitle = "샘플 브랜드 A",
            price = CatalogPriceUiModel.Available(currentLabel = "₩29,000", unitPriceWon = 29_000L),
            specLabel = "1벌",
            shippingLabel = "무료배송",
            categoryId = "uniform",
            subcategoryId = "workwear",
            hasOptions = false,
        ),
        CatalogProductUiModel(
            id = "sample-traffic-cone",
            name = "교통 안전콘 70cm",
            subtitle = "샘플 브랜드 B",
            price = CatalogPriceUiModel.Available(currentLabel = "₩12,000", unitPriceWon = 12_000L),
            specLabel = "1개",
            shippingLabel = "당일 출고",
            categoryId = "traffic",
            subcategoryId = "cone",
            hasOptions = false,
        ),
        CatalogProductUiModel(
            id = "sample-gloves",
            name = "내전 작업 장갑",
            subtitle = "샘플 브랜드 A",
            price = CatalogPriceUiModel.Available(currentLabel = "₩4,500", unitPriceWon = 4_500L),
            specLabel = "1켤레",
            shippingLabel = "당일 출고",
            categoryId = "industrial",
            subcategoryId = "gloves",
            hasOptions = false,
        ),
        CatalogProductUiModel(
            id = "sample-cctv",
            name = "실내 CCTV 카메라",
            subtitle = "샘플 브랜드 B",
            price = CatalogPriceUiModel.Available(currentLabel = "₩89,000", unitPriceWon = 89_000L),
            specLabel = "1대",
            shippingLabel = "무료배송",
            categoryId = "security",
            subcategoryId = "cctv",
            hasOptions = false,
        ),
        CatalogProductUiModel(
            id = "sample-door-lock",
            name = "아동 보호 도어락 잠금장치",
            subtitle = "샘플 브랜드 A",
            price = CatalogPriceUiModel.Available(currentLabel = "₩15,000", unitPriceWon = 15_000L),
            specLabel = "1개",
            shippingLabel = "무료배송",
            categoryId = "living",
            subcategoryId = "home-safety",
            hasOptions = false,
        ),
        CatalogProductUiModel(
            id = "sample-custom-sign",
            name = "맞춤 제작 안내 표지판",
            subtitle = "샘플 브랜드 B",
            price = CatalogPriceUiModel.QuoteRequired,
            specLabel = "1개",
            shippingLabel = "배송 문의",
            badgeLabel = "견적 상품",
            categoryId = "custom",
            subcategoryId = "sign",
            hasOptions = false,
        ),
    ).map(::withShippingPolicy)

    // 상품 목록(카테고리·전체)과 상품 상세 조회가 쓰는 전체 목록. 홈 섹션은 이 목록을 쓰지 않는다.
    private val sampleCatalogProducts = sampleProducts + sampleCategoryProducts

    private val sampleSubcategoryLabels = mapOf(
        "helmet" to "안전모",
        "boots" to "안전화",
        "belt" to "안전벨트",
        "extinguisher" to "소화기",
        "workwear" to "작업복",
        "cone" to "교통 콘",
        "gloves" to "작업 장갑",
        "cctv" to "CCTV",
        "home-safety" to "생활 안전",
        "sign" to "맞춤 표지판",
    )

    // 배송 문구는 무료배송 기준 규칙(ShippingPolicy)에서만 만든다. 견적 상품은 기존 "배송 문의"를 그대로 둔다.
    // 당일 출고 문구는 운영 조건이 확정될 때까지 사용자 화면에 노출하지 않는다(A). 리소스·샘플 값은 되살리기 쉽게 남긴다.
    private fun withShippingPolicy(product: CatalogProductUiModel): CatalogProductUiModel {
        val price = product.price as? CatalogPriceUiModel.Available ?: return product
        return product.copy(shippingLabel = productShippingFeeLabel(price.unitPriceWon))
    }

    private val sampleBrands = listOf(
        CatalogBrandUiModel(id = "brand-a", name = "샘플 브랜드 A", productCountLabel = "2개 상품", letterGroup = "A", productCount = 2),
        CatalogBrandUiModel(id = "brand-b", name = "샘플 브랜드 B", productCountLabel = "1개 상품", letterGroup = "B", productCount = 1),
    )

    private val sampleAddresses = listOf(
        AddressSearchResultUiModel("addr-1", "서울특별시 샘플구 샘플로 1", "샘플동 1-1"),
        AddressSearchResultUiModel("addr-2", "경기도 샘플시 샘플대로 2", "샘플동 2-2"),
    )

    private val sampleClaimProducts = listOf(
        OrderRequestProductUiModel(
            id = "claim-product-1",
            name = "경량 안전화 4인치",
            optionLabel = "사이즈 270",
            priceAndQuantityLabel = "₩54,000 · 1개",
            quantityLabel = "1개",
            isSelected = true,
        ),
    )

    private val sampleProfile = AccountProfileUiModel(
        name = "샘플 사용자",
        email = "sample@example.com",
        phoneNumber = "010-0000-0000",
        companyName = "샘플 건설",
        businessRegistrationNumber = "000-00-00000",
        department = "구매팀",
        isBusinessMember = true,
        isSmsConsentEnabled = false,
        isEmailConsentEnabled = false,
    )

    // 추천과 오늘의 상품은 서로 겹치지 않는 목 데이터를 쓴다.
    override fun homeRecommendedProducts(): List<HomeProductUiModel> = sampleProducts.take(2).map(::toHomeProduct)

    override fun homeTodayProducts(): List<HomeProductUiModel> = sampleProducts.drop(2).map(::toHomeProduct)

    override fun homeBestSellerProducts(): List<HomeProductUiModel> = sampleProducts.map(::toHomeProduct)

    override fun categoryScreen(): CatalogCategoryScreenUiModel = CatalogCategoryScreenUiModel(
        categories = CatalogCategoryDefinitions.all,
        brands = sampleBrands,
    )

    // 상위 카테고리로 목록을 거르고, 칩은 그 안의 세부 카테고리로 만든다. 칩 개수의 합은 "전체" 개수와 같다.
    override fun productList(categoryId: String?): CatalogProductListUiModel {
        val products = sampleCatalogProducts.filter { product -> categoryId == null || product.categoryId == categoryId }
        val subcategoryChips = products.mapNotNull { product -> product.subcategoryId }.distinct().map { subcategoryId ->
            CatalogCategoryChipUiModel(
                id = subcategoryId,
                label = sampleSubcategoryLabels.getValue(subcategoryId),
                count = products.count { product -> product.subcategoryId == subcategoryId },
            )
        }
        return CatalogProductListUiModel(
            title = "전체 상품",
            // 상단 제목은 카테고리 타일과 같은 라벨 리소스를 쓴다(F2).
            titleResId = categoryId?.let { id -> CatalogCategoryDefinitions.all.firstOrNull { category -> category.id == id }?.labelResId },
            categoryChips = listOf(
                CatalogCategoryChipUiModel(id = CATALOG_ALL_CATEGORY_ID, label = "전체", count = products.size),
            ) + subcategoryChips,
            sortLabel = "인기순",
            products = products,
        )
    }

    override fun searchScreen(): CatalogSearchUiModel = CatalogSearchUiModel(
        recentTerms = listOf("안전모", "안전화"),
        popularTerms = listOf("안전모", "소화기", "안전벨트", "안전장갑"),
        popularUpdatedAtLabel = "11.17 09:00",
    )

    // 검색어가 상품명에 포함되면 결과, 아니면 0건(A-06z)으로 분기된다.
    override fun searchResults(query: String): CatalogSearchResultsUiModel {
        val matchedProducts = sampleProducts.filter { product -> product.name.contains(query.trim()) }
        return CatalogSearchResultsUiModel(
            query = query,
            filters = listOf("전체", "개인보호장비"),
            selectedFilter = "전체",
            resultCount = matchedProducts.size,
            sortLabel = "인기순",
            products = matchedProducts,
        )
    }

    override fun productDetail(productId: String): CatalogProductDetailUiModel {
        val product = sampleCatalogProducts.firstOrNull { it.id == productId } ?: sampleProducts.first()
        val isQuoteOnly = product.price is CatalogPriceUiModel.QuoteRequired
        return CatalogProductDetailUiModel(
            product = product,
            galleryImageResIds = listOf(null, null, null),
            shippingRows = listOf(
                CatalogKeyValueUiModel("배송비", productShippingFeeValue((product.price as? CatalogPriceUiModel.Available)?.unitPriceWon) ?: "배송 문의"),
            ),
            specifications = listOf(CatalogKeyValueUiModel("소재", "샘플")),
            reviewCount = 1,
            reviews = listOf(CatalogReviewUiModel("샘플 구매자", "★ 5", "샘플 일자", "샘플 리뷰입니다.")),
            relatedProducts = sampleProducts.filterNot { it.id == product.id },
            optionGroups = if (product.hasOptions != true) {
                emptyList()
            } else {
                listOf(
                    CatalogOptionGroupUiModel(
                        id = "size",
                        label = "사이즈",
                        options = listOf(
                            CatalogOptionUiModel("size-l", "사이즈 L", 1, if (isQuoteOnly) null else "₩0", unitPriceWon = if (isQuoteOnly) null else 0L),
                            CatalogOptionUiModel("size-xl", "사이즈 XL", 1, if (isQuoteOnly) null else "₩0", unitPriceWon = if (isQuoteOnly) null else 0L),
                        ),
                    ),
                )
            },
        )
    }

    override fun cart(): CartUiModel = CartUiModel(
        items = listOf(
            CartItemUiModel(
                id = "cart-helmet",
                name = "세이프가드 ABS 안전모",
                optionLabel = "사이즈 L",
                quantity = 1,
                unitPriceLabel = "₩18,000",
                totalPriceLabel = "₩18,000",
                isSelected = true,
                lineTotalWon = 18_000L,
                unitPriceWon = 18_000L,
            ),
            CartItemUiModel(
                id = "cart-belt",
                name = "전신 안전벨트",
                optionLabel = "사이즈 XL",
                quantity = 2,
                unitPriceLabel = "가격 문의",
                totalPriceLabel = "가격 문의",
                type = CartItemType.QuoteRequired,
            ),
        ),
        productAmountLabel = "₩18,000",
        shippingFeeLabel = formatWonLabel(cartShippingFeeWon(18_000L)),
        shippingFeeWon = cartShippingFeeWon(18_000L),
        expectedPaymentLabel = "₩21,000",
    )

    override fun checkout(): CheckoutUiModel = CheckoutUiModel(
        address = CheckoutAddressUiModel(
            recipientName = "샘플 사용자",
            contactLabel = "010-0000-0000",
            addressLabel = "샘플시 샘플구 샘플로 1",
            isDefaultAddress = true,
        ),
        deliveryRequestOptions = listOf("문 앞에 놓아 주세요", "배송 전 연락 바랍니다"),
        deliveryMethodLabel = "일반 배송 (2~3일)",
        products = listOf(
            CheckoutProductUiModel(
                id = "cart-helmet",
                name = "세이프가드 ABS 안전모",
                optionAndQuantityLabel = "사이즈 L · 1개",
                priceLabel = "₩18,000",
            ),
        ),
        pointsAvailableLabel = "0원",
        paymentSummary = CheckoutPaymentSummaryUiModel(
            productAmountLabel = "₩18,000",
            shippingFeeLabel = "₩3,000",
            discountLabel = "₩0",
            finalAmountLabel = "₩21,000",
            shippingFeeWon = 3_000L,
        ),
    )

    override fun orderComplete(): OrderCompleteUiModel = OrderCompleteUiModel(
        orderNumber = "SAMPLE-0001",
        paymentAmountLabel = "₩21,000",
        paymentMethodLabel = "신용카드",
        addressLabel = "샘플시 샘플구 샘플로 1",
    )

    override fun drawerProfile(): AccountProfileUiModel = sampleProfile

    override fun myPage(): MyPageUiModel = MyPageUiModel(
        isLoggedIn = isLoggedIn,
        profile = sampleProfile,
        shortcuts = listOf(
            AccountShortcutUiModel("orders", "주문내역"),
            AccountShortcutUiModel("favorites", "찜한 상품"),
            AccountShortcutUiModel("recent", "최근 본 상품"),
            AccountShortcutUiModel("coupons", "쿠폰함"),
            AccountShortcutUiModel("points", "적립금"),
        ),
        orderStatuses = listOf(
            AccountOrderStatusUiModel("입금대기", "0"),
            AccountOrderStatusUiModel("결제완료", "0"),
            AccountOrderStatusUiModel("배송준비", "0"),
            AccountOrderStatusUiModel("배송중", "0"),
            AccountOrderStatusUiModel("배송완료", "0"),
        ),
        orderRows = listOf(
            AccountMenuRowUiModel("returns", "취소·반품·교환"),
            AccountMenuRowUiModel("quotes", "견적 문의 내역"),
        ),
        supportRows = listOf(
            AccountMenuRowUiModel("inquiries", "1:1 문의 내역"),
            AccountMenuRowUiModel("customer", "고객센터"),
            AccountMenuRowUiModel("notices", "공지사항"),
        ),
        recentProducts = sampleProducts.take(2).map { it.name },
    )

    override fun quoteRequest(productId: String?): QuoteRequestUiModel = QuoteRequestUiModel(
        products = sampleProducts.filter { it.id == productId },
    )

    override fun quoteComplete(): QuoteCompleteUiModel = QuoteCompleteUiModel(
        inquiryNumber = "Q-SAMPLE-0001",
        responseTimeLabel = "영업일 기준 1~2일",
        requestTypeLabel = "맞춤 제작",
        companyName = "샘플 건설",
        contactName = "샘플 사용자",
        productsSummary = "전신 안전벨트 외",
        preferredDeliveryDate = "협의",
    )

    override fun quoteHistory(): QuoteHistoryUiModel = QuoteHistoryUiModel(
        inquiries = listOf(
            QuoteInquiryListItemUiModel(
                id = "quote-1",
                dateLabel = "샘플 일자",
                inquiryNumber = "Q-SAMPLE-0001",
                title = "안전벨트 대량 구매 문의",
                productsSummary = "전신 안전벨트 외 1건",
                status = QuoteInquiryStatus.Consulting,
            ),
        ),
    )

    override fun quoteInquiryDetail(inquiryId: String): QuoteInquiryDetailUiModel = QuoteInquiryDetailUiModel(
        title = "안전벨트 대량 구매 문의",
        dateLabel = "샘플 일자",
        inquiryNumber = "Q-SAMPLE-0001",
        status = QuoteInquiryStatus.Consulting,
        companyName = "샘플 건설",
        contactName = "샘플 사용자",
        contactPhone = "010-0000-0000",
        contactEmail = "sample@example.com",
        items = listOf(QuoteDetailItemUiModel("item-1", "전신 안전벨트", "20개")),
    )

    override fun orderHistory(): OrderHistoryUiModel = OrderHistoryUiModel(
        periodLabel = "최근 3개월",
        orders = listOf(
            OrderHistoryItemUiModel(
                id = "order-1",
                orderDateLabel = "샘플 일자",
                orderNumber = "SAMPLE-0001",
                status = OrderStatus.Shipping,
                product = OrderHistoryProductUiModel(
                    name = "세이프가드 ABS 안전모",
                    priceAndQuantityLabel = "₩18,000 · 1개",
                ),
                actions = listOf(OrderHistoryAction.TrackDelivery),
            ),
            OrderHistoryItemUiModel(
                id = "order-2",
                orderDateLabel = "샘플 일자",
                orderNumber = "SAMPLE-0002",
                status = OrderStatus.PaymentComplete,
                product = OrderHistoryProductUiModel(
                    name = "경량 안전화 4인치",
                    priceAndQuantityLabel = "₩54,000 · 1개",
                ),
                actions = listOf(OrderHistoryAction.Cancel),
            ),
        ),
    )

    override fun favorites(): FavoritesUiModel = FavoritesUiModel(
        products = sampleProducts.take(2),
        sortLabel = "최근 찜한 순",
    )

    override fun recentProducts(): RecentProductsUiModel = RecentProductsUiModel(
        groups = listOf(RecentProductGroupUiModel("샘플 일자", sampleProducts.take(2))),
    )

    override fun coupons(): CouponScreenUiModel = CouponScreenUiModel(
        pointsBalanceLabel = "0원",
        availableCoupons = listOf(
            CouponUiModel(
                id = "coupon-1",
                discountLabel = "10%",
                name = "샘플 쿠폰",
                conditionLabel = "5만원 이상 구매 시",
                expiryLabel = "샘플 기한",
                applicableLabel = "전체 상품",
            ),
        ),
    )

    override fun points(): PointsScreenUiModel = PointsScreenUiModel(
        availableBalanceLabel = "12,000원",
        expiringBalanceLabel = "1,000원",
        history = listOf(
            PointHistoryUiModel(
                id = "earned",
                title = "상품 구매 적립",
                dateLabel = "2026.11.01",
                orderNumberLabel = "주문번호 0000000000",
                amountLabel = "+1,000원",
                expiryLabel = "2027.11.01 소멸",
            ),
        ),
        usageNotice = "적립금은 사용 기한이 지나면 소멸됩니다.",
    )

    override fun inquiryHistory(): List<InquiryHistoryItemUiModel> = listOf(
        InquiryHistoryItemUiModel(
            id = "q1",
            title = "현장 납품 가능 일정을 알고 싶습니다",
            categoryLabel = "배송 문의",
            dateLabel = "2026.11.05",
            status = InquiryStatus.Waiting,
            body = "경기도 현장 납품 일정 문의",
        ),
        InquiryHistoryItemUiModel(
            id = "q2",
            title = "견적서 발급 방법을 알고 싶습니다",
            categoryLabel = "견적 문의",
            dateLabel = "2026.11.04",
            status = InquiryStatus.Answered,
            body = "사업자 견적서 발급 절차 문의",
            answer = "견적 문의 화면에서 사업자 정보를 입력하면 발급됩니다.",
        ),
    )

    override fun customerSupport(): CustomerSupportUiModel = CustomerSupportUiModel(
        phoneNumber = "0000-0000",
        businessHours = "평일 09:00~18:00",
        query = "",
        categories = listOf("전체", "주문/배송"),
        selectedCategory = "전체",
        faqs = listOf(CustomerSupportFaqUiModel("faq-1", "배송은 얼마나 걸리나요?", "샘플 답변입니다.")),
        expandedFaqIds = emptySet(),
    )

    override fun notices(): List<NoticeItemUiModel> = listOf(
        NoticeItemUiModel("notice-1", "샘플 공지사항", "샘플 일자", isPinned = true),
    )

    override fun events(): List<EventUiModel> = listOf(
        EventUiModel("event-1", "진행 중", "샘플 이벤트", R.drawable.ic_ticket),
    )

    override fun noticeDetail(noticeId: String): NoticeDetailUiModel = NoticeDetailUiModel(
        title = "샘플 공지사항",
        dateLabel = "샘플 일자",
        content = "샘플 공지 본문입니다.",
        isPinned = true,
    )

    override fun eventDetail(eventId: String): EventDetailUiModel = EventDetailUiModel(
        title = "샘플 이벤트",
        period = "샘플 기간",
        benefit = "샘플 혜택 안내",
        participation = "샘플 참여 방법",
        notice = "샘플 유의사항",
    )

    override fun inquiryDetail(inquiryId: String): InquiryDetailUiModel = inquiryHistory()
        .firstOrNull { inquiry -> inquiry.id == inquiryId }
        ?.let { inquiry ->
            InquiryDetailUiModel(
                title = inquiry.title,
                statusLabel = if (inquiry.status == InquiryStatus.Answered) "답변 완료" else "답변 대기",
                dateLabel = inquiry.dateLabel,
                typeLabel = inquiry.categoryLabel,
                orderNumber = "SAMPLE-0001",
                content = inquiry.body,
                answerDateLabel = inquiry.answer?.let { inquiry.dateLabel },
                answer = inquiry.answer,
            )
        }
        ?: super.inquiryDetail(inquiryId)

    override fun inquiryForm(): OneToOneInquiryUiModel = OneToOneInquiryUiModel(
        inquiryType = "배송 문의",
        title = "",
        content = "",
        attachmentLabel = "파일 첨부",
    )

    // TODO: 서버 연동 시 주문 ID 체계를 통일한다. 지금은 주문 완료가 주문 번호를 ID로 넘기므로 번호와 ID를 모두 받는다.
    override fun orderDetail(orderId: String): OrderDetailUiModel = if (orderId !in knownOrderIds) {
        super.orderDetail(orderId)
    } else {
        sampleOrderDetail()
    }

    private val knownOrderIds = setOf("order-1", "order-2", "SAMPLE-0001", "SAMPLE-0002")

    private fun sampleOrderDetail(): OrderDetailUiModel = OrderDetailUiModel(
        orderDateLabel = "샘플 일자",
        orderNumber = "SAMPLE-0001",
        products = listOf(
            OrderDetailProductUiModel(
                id = "detail-product-1",
                name = "세이프가드 ABS 안전모",
                optionLabel = "사이즈 L",
                priceAndQuantityLabel = "₩18,000 · 1개",
                status = OrderStatus.Shipping,
            ),
        ),
        address = OrderDetailAddressUiModel(
            recipientName = "샘플 사용자",
            contactLabel = "010-0000-0000",
            addressLabel = "서울특별시 샘플구 샘플로 1",
            requestLabel = "문 앞에 놓아 주세요",
        ),
        payment = OrderPaymentBreakdownUiModel(
            productAmountLabel = "₩18,000",
            shippingFeeLabel = "₩3,000",
            couponDiscountLabel = "-₩0",
            pointsUsedLabel = "-₩0",
            totalAmountLabel = "₩21,000",
            paymentMethodLabel = "신용카드",
        ),
        expectedPointsLabel = "180P",
    )

    override fun deliveryTracking(orderId: String): DeliveryTrackingUiModel = if (orderId !in knownOrderIds) {
        super.deliveryTracking(orderId)
    } else {
        sampleDeliveryTracking()
    }

    private fun sampleDeliveryTracking(): DeliveryTrackingUiModel = DeliveryTrackingUiModel(
        status = OrderStatus.Shipping,
        expectedArrivalLabel = "샘플 도착 예정일",
        carrierName = "샘플 택배",
        trackingNumber = "0000-0000-0000",
        progressStage = DeliveryProgressStage.InTransit,
        timeline = listOf(
            DeliveryTimelineItemUiModel("배송 중", "샘플 터미널 · 샘플 시각", isCurrent = true),
            DeliveryTimelineItemUiModel("집하 완료", "샘플 지점 · 샘플 시각"),
        ),
        productsSummaryLabel = "세이프가드 ABS 안전모 1개",
        noticeLabel = "샘플 배송 안내입니다.",
    )

    override fun claimOrders(): OrderRequestOrderSelectionUiModel = OrderRequestOrderSelectionUiModel(
        periods = listOf("최근 3개월", "최근 6개월"),
        periodLabel = "최근 3개월",
        orders = listOf(
            RequestableOrderUiModel(
                id = "order-1",
                orderDateLabel = "샘플 일자",
                orderNumber = "SAMPLE-0001",
                status = OrderStatus.Shipping,
                products = sampleClaimProducts,
                canSelectForRequest = true,
            ),
            RequestableOrderUiModel(
                id = "order-2",
                orderDateLabel = "샘플 일자",
                orderNumber = "SAMPLE-0002",
                status = OrderStatus.PaymentComplete,
                products = sampleClaimProducts,
                canSelectForRequest = true,
            ),
        ),
    )

    override fun claimForm(orderId: String, requestType: OrderRequestType): OrderRequestFormUiModel = OrderRequestFormUiModel(
        type = requestType,
        products = sampleClaimProducts,
        reasons = listOf(
            OrderRequestReason.ChangedMind,
            OrderRequestReason.ProductDefective,
            OrderRequestReason.WrongProduct,
            OrderRequestReason.Other,
        ),
        collectionAddressLabel = "서울특별시 샘플구 샘플로 1",
        collectionContactLabel = "010-0000-0000",
        productAmountLabel = "₩54,000",
        feeLabel = "₩0",
        expectedAmountLabel = "₩54,000",
        paymentMethodLabel = "신용카드",
    )

    override fun claimResult(requestId: String): OrderRequestResultUiModel = if (requestId !in knownRequestIds) {
        super.claimResult(requestId)
    } else {
        sampleClaimResult()
    }

    private fun sampleClaimResult(): OrderRequestResultUiModel = OrderRequestResultUiModel(
        requestNumber = "REQ-0001",
        statusLabel = "신청 접수",
        statusDescription = "샘플 신청이 접수되었습니다.",
    )

    override fun claimWithdrawalResult(requestId: String): OrderRequestResultUiModel = OrderRequestResultUiModel(
        requestNumber = "REQ-0001",
        statusLabel = "철회 완료",
        statusDescription = "샘플 신청이 철회되었습니다.",
    )

    override fun claimHistory(): OrderRequestHistoryUiModel = OrderRequestHistoryUiModel(
        items = listOf(
            OrderRequestHistoryItemUiModel(
                id = "request-1",
                requestDateLabel = "샘플 일자",
                requestType = OrderRequestType.Return,
                orderNumber = "SAMPLE-0001",
                productName = "경량 안전화 4인치",
                priceAndQuantityLabel = "₩54,000 · 1개",
                status = OrderRequestStatus.Requested,
                canWithdraw = true,
            ),
            OrderRequestHistoryItemUiModel(
                id = "request-2",
                requestDateLabel = "샘플 일자",
                requestType = OrderRequestType.Exchange,
                orderNumber = "SAMPLE-0002",
                productName = "세이프가드 ABS 안전모",
                priceAndQuantityLabel = "₩18,000 · 1개",
                status = OrderRequestStatus.Rejected,
                hasRejectionReason = true,
            ),
        ),
    )

    // TODO: 신청 ID 체계를 통일한다. 신청 제출은 서버가 없어 주문 ID를 임시 신청 ID로 넘기므로 주문 ID도 받는다.
    override fun claimDetail(requestId: String): OrderRequestDetailUiModel = if (requestId !in knownRequestIds) {
        super.claimDetail(requestId)
    } else {
        sampleClaimDetail()
    }

    private val knownRequestIds = setOf("request-1", "request-2", "order-1", "order-2")

    private fun sampleClaimDetail(): OrderRequestDetailUiModel = OrderRequestDetailUiModel(
        requestNumber = "REQ-0001",
        requestType = OrderRequestType.Return,
        requestDateLabel = "샘플 일자",
        status = OrderRequestStatus.Requested,
        statusDescription = "샘플 신청이 접수되었습니다.",
        productSummaryLabel = "경량 안전화 4인치 1개",
        reasonSummaryLabel = "단순 변심",
        collectionAddressLabel = "서울특별시 샘플구 샘플로 1",
        canWithdraw = true,
    )

    override fun claimRejection(requestId: String): OrderRequestRejectionUiModel = OrderRequestRejectionUiModel(
        requestNumber = "REQ-0002",
        productSummaryLabel = "세이프가드 ABS 안전모 1개",
        requestReasonLabel = "단순 변심",
        statusDescription = "샘플 신청이 반려되었습니다.",
        rejectionReasonLabel = "샘플 반려 사유입니다.",
    )

    // 검색어가 비어 있으면 아직 검색 전 상태이고, 검색어가 있으면 샘플 주소 중 포함하는 것만 보여 준다.
    override fun addressSearch(query: String): AddressSearchUiModel = AddressSearchUiModel(
        query = query,
        results = if (query.isBlank()) {
            emptyList()
        } else {
            sampleAddresses.filter { address -> address.roadAddress.contains(query.trim()) || address.lotAddress.contains(query.trim()) }
        },
        selectedAddressId = null,
        hasSearched = query.isNotBlank(),
    )

    override fun addressLabel(addressId: String): String? = sampleAddresses.firstOrNull { address -> address.id == addressId }?.roadAddress

    override fun brandDetail(brandId: String): CatalogBrandDetailUiModel = CatalogBrandDetailUiModel(
        brand = sampleBrands.firstOrNull { brand -> brand.id == brandId } ?: sampleBrands.first(),
        description = "샘플 브랜드 소개입니다.",
        informationRows = listOf(CatalogKeyValueUiModel("원산지", "샘플")),
        products = sampleProducts.take(2),
        otherBrands = sampleBrands.filterNot { brand -> brand.id == brandId },
        selectedCategory = "전체",
        categories = listOf("전체", "안전모"),
        sortLabel = "인기순",
    )

    override fun productReviews(productId: String): ProductReviewUiModel = ProductReviewUiModel(
        productId = productId,
        productName = sampleProducts.firstOrNull { product -> product.id == productId }?.name ?: sampleProducts.first().name,
        brandName = "샘플 브랜드 A",
        variantLabel = "사이즈 L",
        rating = 4.5f,
        reviewCountLabel = "리뷰 1개",
        reviews = listOf(
            ProductReviewItemUiModel(
                id = "review-1",
                authorName = "샘플 구매자",
                isPurchaseVerified = true,
                dateLabel = "샘플 일자",
                rating = 5,
                optionLabel = "사이즈 L",
                body = "샘플 리뷰입니다.",
                photos = emptyList(),
            ),
        ),
    )

    override fun reviewWrite(productId: String): ProductReviewWriteUiModel = ProductReviewWriteUiModel(
        brand = "샘플 브랜드 A",
        product = sampleProducts.firstOrNull { product -> product.id == productId }?.name ?: sampleProducts.first().name,
        option = "사이즈 L",
        reviewer = "샘플 사용자",
    )

    private fun toHomeProduct(product: CatalogProductUiModel): HomeProductUiModel {
        val price = product.price as? CatalogPriceUiModel.Available
        return HomeProductUiModel(
            id = product.id,
            name = product.name,
            priceLabel = price?.currentLabel ?: "가격 문의",
            ratingLabel = product.ratingLabel?.let { rating ->
                listOfNotNull(rating, product.reviewCountLabel).joinToString(" ")
            },
            ratingValue = product.ratingValue ?: product.ratingLabel?.toFloatOrNull(),
            originalPriceLabel = price?.originalLabel,
            discountRateLabel = price?.discountLabel,
            imageResId = product.imageResId,
            imageDescription = product.imageDescription,
            isSoldOut = product.isSoldOut,
            isPurchaseUnavailable = price == null,
            categoryId = product.categoryId,
            badgeLabel = product.badgeLabel,
            unitPriceWon = price?.unitPriceWon,
            hasOptions = product.hasOptions,
            specLabel = product.specLabel,
            shippingLabel = product.shippingLabel,
            benefitLabels = product.benefitLabels,
        )
    }
}

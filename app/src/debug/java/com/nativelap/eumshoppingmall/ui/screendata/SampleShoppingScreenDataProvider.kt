package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountMenuRowUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountOrderStatusUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountShortcutUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.benefits.CouponScreenUiModel
import com.nativelap.eumshoppingmall.ui.benefits.CouponUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointHistoryUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointsScreenUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryScreenUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogKeyValueUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionGroupUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchResultsUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutProductUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.events.EventUiModel
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryAction
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
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
import com.nativelap.eumshoppingmall.ui.support.InquiryHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.support.InquiryStatus
import com.nativelap.eumshoppingmall.ui.support.NoticeItemUiModel

/**
 * debug 빌드에서 화면 흐름(F1~F7)을 눈으로 따라갈 수 있게 하는 고정 샘플.
 *
 * 실제 상품·가격이 아니며 release 소스셋에는 포함되지 않는다. 로그인 후 화면을 확인할 수 있도록 로그인 상태로 둔다.
 */
object SampleShoppingScreenDataProvider : ShoppingScreenDataProvider {
    // 로그인 후 화면(마이 바로가기 등)을 실행 중에 확인하기 위한 debug 전용 값이다. 실제 인증 상태가 아니며 release는 비로그인 그대로다.
    override val isLoggedIn: Boolean = true

    private const val QUOTE_ONLY_PRODUCT_ID = "sample-belt"

    private val sampleProducts = listOf(
        CatalogProductUiModel(
            id = "sample-helmet",
            name = "세이프가드 ABS 안전모",
            price = CatalogPriceUiModel.Available(
                currentLabel = "₩18,000",
                originalLabel = "₩22,000",
                discountLabel = "18%",
                unitPriceWon = 18_000L,
            ),
            ratingLabel = "4.7",
            reviewCountLabel = "(3)",
        ),
        CatalogProductUiModel(
            id = "sample-boots",
            name = "경량 안전화 4인치",
            price = CatalogPriceUiModel.Available(currentLabel = "₩54,000", unitPriceWon = 54_000L),
            ratingLabel = "4.7",
            reviewCountLabel = "(6)",
        ),
        CatalogProductUiModel(
            id = QUOTE_ONLY_PRODUCT_ID,
            name = "전신 안전벨트",
            price = CatalogPriceUiModel.QuoteRequired,
        ),
        CatalogProductUiModel(
            id = "sample-extinguisher",
            name = "분말 소화기 3.3kg",
            price = CatalogPriceUiModel.Available(
                currentLabel = "₩32,000",
                originalLabel = "₩35,000",
                discountLabel = "8%",
                unitPriceWon = 32_000L,
            ),
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

    override fun homeRecommendedProducts(): List<HomeProductUiModel> = sampleProducts.take(3).map(::toHomeProduct)

    override fun homeBestSellerProducts(): List<HomeProductUiModel> = sampleProducts.map(::toHomeProduct)

    override fun categoryScreen(): CatalogCategoryScreenUiModel = CatalogCategoryScreenUiModel(
        categories = listOf(
            CatalogCategoryUiModel("protective", "개인보호장비", R.drawable.ic_hard_hat),
            CatalogCategoryUiModel("uniform", "작업복/유니폼", R.drawable.ic_shirt),
            CatalogCategoryUiModel("traffic", "교통·도로 안전", R.drawable.ic_traffic_cone),
            CatalogCategoryUiModel("fire", "소방·방재용품", R.drawable.ic_fire_extinguisher),
        ),
    )

    override fun productList(categoryId: String?): CatalogProductListUiModel = CatalogProductListUiModel(
        title = if (categoryId == null) "전체 상품" else "개인보호장비",
        categories = listOf("전체", "안전모", "안전화", "안전벨트"),
        selectedCategory = "전체",
        resultCountLabel = "${sampleProducts.size}개 상품",
        sortLabel = "인기순",
        products = sampleProducts,
    )

    override fun searchScreen(): CatalogSearchUiModel = CatalogSearchUiModel(
        recentTerms = listOf("안전모", "안전화"),
        popularTerms = listOf("안전모", "소화기", "안전벨트", "없는상품"),
        popularUpdatedAtLabel = "샘플 기준",
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
        val product = sampleProducts.firstOrNull { it.id == productId } ?: sampleProducts.first()
        val isQuoteOnly = product.price is CatalogPriceUiModel.QuoteRequired
        return CatalogProductDetailUiModel(
            product = product,
            galleryImageResIds = listOf(null, null, null),
            shippingRows = listOf(CatalogKeyValueUiModel("배송비", "₩3,000")),
            specifications = listOf(CatalogKeyValueUiModel("소재", "샘플")),
            relatedProducts = sampleProducts.filterNot { it.id == product.id },
            optionGroups = listOf(
                CatalogOptionGroupUiModel(
                    id = "size",
                    label = "사이즈",
                    options = listOf(
                        CatalogOptionUiModel("size-l", "사이즈 L", 1, if (isQuoteOnly) null else "₩0", unitPriceWon = if (isQuoteOnly) null else 0L),
                        CatalogOptionUiModel("size-xl", "사이즈 XL", 1, if (isQuoteOnly) null else "₩0", unitPriceWon = if (isQuoteOnly) null else 0L),
                    ),
                ),
            ),
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
        shippingFeeLabel = "₩3,000",
        shippingFeeWon = 3_000L,
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

    private fun toHomeProduct(product: CatalogProductUiModel): HomeProductUiModel {
        val price = product.price as? CatalogPriceUiModel.Available
        return HomeProductUiModel(
            id = product.id,
            name = product.name,
            priceLabel = price?.currentLabel ?: "가격 문의",
            originalPriceLabel = price?.originalLabel,
            discountRateLabel = price?.discountLabel,
            isPurchaseUnavailable = price == null,
        )
    }
}

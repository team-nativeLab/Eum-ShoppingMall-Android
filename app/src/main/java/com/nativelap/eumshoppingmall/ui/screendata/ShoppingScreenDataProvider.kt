package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.addresssearch.AddressSearchUiModel
import com.nativelap.eumshoppingmall.ui.benefits.CouponScreenUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointsScreenUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogBrandDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogBrandUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryDefinitions
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryScreenUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchResultsUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchUiModel
import com.nativelap.eumshoppingmall.ui.catalog.ProductListPreset
import com.nativelap.eumshoppingmall.ui.catalog.titleResId
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroUiModel
import com.nativelap.eumshoppingmall.ui.events.EventDetailUiModel
import com.nativelap.eumshoppingmall.ui.events.EventUiModel
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeCompanyInfoUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryProgressStage
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailAddressUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryEmptyState
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderPaymentBreakdownUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestOrderSelectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import com.nativelap.eumshoppingmall.ui.productreview.ProductReviewUiModel
import com.nativelap.eumshoppingmall.ui.productreview.ProductReviewWriteUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsUiModel
import com.nativelap.eumshoppingmall.ui.support.CustomerSupportUiModel
import com.nativelap.eumshoppingmall.ui.support.InquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.support.InquiryHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.support.NoticeDetailUiModel
import com.nativelap.eumshoppingmall.ui.support.NoticeItemUiModel
import com.nativelap.eumshoppingmall.ui.support.OneToOneInquiryUiModel

/**
 * 서버·ViewModel이 연결되기 전까지 화면에 넣을 UiModel을 공급한다.
 *
 * 서버 카테고리 ID가 필요 없는 탐색 메뉴 메타데이터는 공통 정의를 쓰고, 그 외 기본 데이터는 비어 있다.
 * 샘플 상품·가격 등은 debug 소스셋의 구현에만 두어 release 빌드에 포함되지 않게 한다.
 * 빌드 타입별 구현은 [createShoppingScreenDataProvider]가 고른다.
 */
interface ShoppingScreenDataProvider {
    /** True only when the debug provider intentionally offers local sample cart rows for an empty store. */
    val enablesDebugCartFallback: Boolean
        get() = false

    // 로그인 성공 경로가 아직 없으므로 기본은 비로그인이다.
    val isLoggedIn: Boolean
        get() = false

    fun homeRecommendedProducts(): List<HomeProductUiModel> = emptyList()

    fun homeTodayProducts(): List<HomeProductUiModel> = emptyList()

    fun homeBestSellerProducts(): List<HomeProductUiModel> = emptyList()

    fun homeDiscountProducts(): List<HomeProductUiModel> = emptyList()

    fun homeCompanyIntro(): HomeCompanyInfoUiModel = HomeCompanyInfoUiModel()

    fun categoryScreen(): CatalogCategoryScreenUiModel = CatalogCategoryScreenUiModel(
        categories = CatalogCategoryDefinitions.all,
    )

    /**
     * preset이 고른 기본 상품 원본. 홈 섹션과 같은 상품을 같은 순서로 준다. All은 카테고리 전체 상품이다.
     * 기본 구현은 빈 목록이며, release 빌드에서는 목록이 비어 빈 상태 문구가 나온다.
     */
    fun presetProducts(preset: ProductListPreset): List<CatalogProductUiModel> = emptyList()

    fun productList(
        categoryId: String?,
        preset: ProductListPreset = ProductListPreset.All,
    ): CatalogProductListUiModel = CatalogProductListUiModel(
        title = "",
        titleResId = preset.titleResId(),
        sortLabel = "",
        products = productListSourceFor(preset, categoryId, this),
    )

    fun searchScreen(): CatalogSearchUiModel = CatalogSearchUiModel()

    fun searchResults(query: String): CatalogSearchResultsUiModel = CatalogSearchResultsUiModel(
        query = query,
        resultCount = 0,
        sortLabel = "",
    )

    fun productDetail(productId: String): CatalogProductDetailUiModel = CatalogProductDetailUiModel(
        product = CatalogProductUiModel(
            id = productId,
            name = "",
            price = CatalogPriceUiModel.QuoteRequired,
        ),
    )

    fun cart(): CartUiModel = CartUiModel()

    fun checkout(): CheckoutUiModel = CheckoutUiModel(
        address = CheckoutAddressUiModel(
            recipientName = "",
            contactLabel = "",
            addressLabel = "",
        ),
        deliveryMethodLabel = "",
        paymentSummary = CheckoutPaymentSummaryUiModel(
            productAmountLabel = "",
            shippingFeeLabel = "",
            discountLabel = "",
            finalAmountLabel = "",
        ),
    )

    fun orderComplete(): OrderCompleteUiModel = OrderCompleteUiModel(
        orderNumber = "",
        paymentAmountLabel = "",
        paymentMethodLabel = "",
        addressLabel = "",
    )

    fun drawerProfile(): AccountProfileUiModel = EmptyAccountProfile

    fun myPage(): MyPageUiModel = MyPageUiModel(
        isLoggedIn = isLoggedIn,
        profile = drawerProfile(),
        shortcuts = emptyList(),
        orderStatuses = emptyList(),
        orderRows = emptyList(),
        supportRows = emptyList(),
        recentProducts = emptyList(),
    )

    fun quoteRequest(productId: String?): QuoteRequestUiModel = QuoteRequestUiModel()

    fun quoteComplete(): QuoteCompleteUiModel = QuoteCompleteUiModel(
        inquiryNumber = "",
        responseTimeLabel = "",
        requestTypeLabel = "",
        companyName = "",
        contactName = "",
        productsSummary = "",
        preferredDeliveryDate = "",
    )

    fun quoteHistory(): QuoteHistoryUiModel = QuoteHistoryUiModel()

    fun quoteInquiryDetail(inquiryId: String): QuoteInquiryDetailUiModel = QuoteInquiryDetailUiModel(
        title = "",
        dateLabel = "",
        inquiryNumber = "",
        status = QuoteInquiryStatus.Received,
        companyName = "",
        contactName = "",
        contactPhone = "",
        contactEmail = "",
    )

    fun orderHistory(): OrderHistoryUiModel = OrderHistoryUiModel(
        periodLabel = "",
        emptyState = OrderHistoryEmptyState.NoOrders,
    )

    fun favorites(): FavoritesUiModel = FavoritesUiModel()

    fun recentProducts(): RecentProductsUiModel = RecentProductsUiModel()

    fun coupons(): CouponScreenUiModel = CouponScreenUiModel(pointsBalanceLabel = "")

    fun points(): PointsScreenUiModel = PointsScreenUiModel(
        availableBalanceLabel = "",
        expiringBalanceLabel = "",
        usageNotice = "",
    )

    fun inquiryHistory(): List<InquiryHistoryItemUiModel> = emptyList()

    fun customerSupport(): CustomerSupportUiModel = CustomerSupportUiModel(
        phoneNumber = "",
        businessHours = "",
        query = "",
        categories = emptyList(),
        selectedCategory = "",
        faqs = emptyList(),
        expandedFaqIds = emptySet(),
    )

    fun notices(): List<NoticeItemUiModel> = emptyList()

    fun events(): List<EventUiModel> = emptyList()

    fun noticeDetail(noticeId: String): NoticeDetailUiModel = NoticeDetailUiModel(title = "", dateLabel = "", content = "")

    fun eventDetail(eventId: String): EventDetailUiModel = EventDetailUiModel(
        title = "",
        period = "",
        benefit = "",
        participation = "",
        notice = "",
    )

    fun inquiryDetail(inquiryId: String): InquiryDetailUiModel = InquiryDetailUiModel(
        title = "",
        statusLabel = "",
        dateLabel = "",
        typeLabel = "",
        orderNumber = "",
        content = "",
    )

    fun inquiryForm(): OneToOneInquiryUiModel = OneToOneInquiryUiModel(
        selectedType = null,
        orderOptions = emptyList(),
        selectedOrderId = null,
        title = "",
        content = "",
        isReplyNotificationOn = false,
    )

    fun orderDetail(orderId: String): OrderDetailUiModel = OrderDetailUiModel(
        orderDateLabel = "",
        orderNumber = "",
        products = emptyList(),
        address = OrderDetailAddressUiModel(recipientName = "", contactLabel = "", addressLabel = "", requestLabel = ""),
        payment = OrderPaymentBreakdownUiModel(
            productAmountLabel = "",
            shippingFeeLabel = "",
            couponDiscountLabel = "",
            pointsUsedLabel = "",
            totalAmountLabel = "",
            paymentMethodLabel = "",
        ),
        expectedPointsLabel = "",
    )

    fun deliveryTracking(orderId: String): DeliveryTrackingUiModel = DeliveryTrackingUiModel(
        status = OrderStatus.PaymentComplete,
        expectedArrivalLabel = "",
        carrierName = "",
        trackingNumber = "",
        progressStage = DeliveryProgressStage.PaymentComplete,
        productsSummaryLabel = "",
        noticeLabel = "",
    )

    fun claimOrders(): OrderRequestOrderSelectionUiModel = OrderRequestOrderSelectionUiModel(periodLabel = "")

    fun claimForm(orderId: String, requestType: OrderRequestType): OrderRequestFormUiModel = OrderRequestFormUiModel(
        type = requestType,
        products = emptyList(),
        reasons = emptyList(),
        productAmountLabel = "",
        feeLabel = "",
        expectedAmountLabel = "",
        paymentMethodLabel = "",
    )

    fun claimResult(requestId: String): OrderRequestResultUiModel = OrderRequestResultUiModel(
        requestNumber = "",
        statusLabel = "",
        statusDescription = "",
    )

    fun claimWithdrawalResult(requestId: String): OrderRequestResultUiModel = claimResult(requestId)

    fun claimHistory(): OrderRequestHistoryUiModel = OrderRequestHistoryUiModel(hasNoRequests = true)

    fun claimDetail(requestId: String): OrderRequestDetailUiModel = OrderRequestDetailUiModel(
        requestNumber = "",
        requestType = OrderRequestType.Cancel,
        requestDateLabel = "",
        status = OrderRequestStatus.Requested,
        statusDescription = "",
        productSummaryLabel = "",
        reasonSummaryLabel = "",
        collectionAddressLabel = "",
        canWithdraw = false,
    )

    fun claimRejection(requestId: String): OrderRequestRejectionUiModel = OrderRequestRejectionUiModel(
        requestNumber = "",
        productSummaryLabel = "",
        requestReasonLabel = "",
        statusDescription = "",
        rejectionReasonLabel = "",
    )

    fun addressSearch(query: String): AddressSearchUiModel = AddressSearchUiModel(
        query = query,
        results = emptyList(),
        selectedAddressId = null,
        hasSearched = query.isNotBlank(),
    )

    /** 주소 검색 결과 ID를 배송지에 표시할 도로명 주소로 바꾼다. 모르는 ID면 null이다. */
    fun addressLabel(addressId: String): String? = null

    fun brandDetail(brandId: String): CatalogBrandDetailUiModel = CatalogBrandDetailUiModel(
        brand = CatalogBrandUiModel(id = brandId, name = "", productCountLabel = ""),
        description = "",
        sortLabel = "",
    )

    fun productReviews(productId: String): ProductReviewUiModel = ProductReviewUiModel(
        productId = productId,
        productName = "",
        brandName = null,
        variantLabel = null,
        rating = 0f,
        reviewCountLabel = "",
        reviews = emptyList(),
    )

    fun reviewWrite(productId: String): ProductReviewWriteUiModel = ProductReviewWriteUiModel(
        brand = "",
        product = "",
        option = "",
        reviewer = "",
    )

    fun companyIntro(): CompanyIntroUiModel = CompanyIntroUiModel(
        businessAreas = emptyList(),
        history = emptyList(),
        certifications = emptyList(),
        partners = emptyList(),
        address = "",
        phone = "",
        email = "",
    )
}

/** 데이터 원천이 없을 때 쓰는 빈 구현. release 빌드가 사용한다. */
object EmptyShoppingScreenDataProvider : ShoppingScreenDataProvider

private val EmptyAccountProfile = AccountProfileUiModel(
    name = "",
    email = "",
    phoneNumber = "",
    companyName = "",
    businessRegistrationNumber = "",
    department = "",
    isBusinessMember = false,
    isSmsConsentEnabled = false,
    isEmailConsentEnabled = false,
)

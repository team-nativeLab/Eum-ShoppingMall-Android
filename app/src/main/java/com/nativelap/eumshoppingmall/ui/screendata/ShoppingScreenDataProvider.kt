package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.benefits.CouponScreenUiModel
import com.nativelap.eumshoppingmall.ui.benefits.PointsScreenUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryScreenUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchResultsUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroUiModel
import com.nativelap.eumshoppingmall.ui.events.EventUiModel
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryEmptyState
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsUiModel
import com.nativelap.eumshoppingmall.ui.support.CustomerSupportUiModel
import com.nativelap.eumshoppingmall.ui.support.InquiryHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.support.NoticeItemUiModel

/**
 * 서버·ViewModel이 연결되기 전까지 화면에 넣을 UiModel을 공급한다.
 *
 * 기본 구현은 모두 빈 값이다. 샘플 상품·가격 등은 debug 소스셋의 구현에만 두어 release 빌드에 포함되지 않게 한다.
 * 빌드 타입별 구현은 [createShoppingScreenDataProvider]가 고른다.
 */
interface ShoppingScreenDataProvider {
    // 로그인 성공 경로가 아직 없으므로 기본은 비로그인이다.
    val isLoggedIn: Boolean
        get() = false

    fun homeRecommendedProducts(): List<HomeProductUiModel> = emptyList()

    fun homeBestSellerProducts(): List<HomeProductUiModel> = emptyList()

    fun categoryScreen(): CatalogCategoryScreenUiModel = CatalogCategoryScreenUiModel()

    fun productList(categoryId: String?): CatalogProductListUiModel = CatalogProductListUiModel(
        title = "",
        sortLabel = "",
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

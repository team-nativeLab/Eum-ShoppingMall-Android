package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.nativelap.eumshoppingmall.ui.account.view.AccountDrawerRoute
import com.nativelap.eumshoppingmall.ui.account.view.MyPageRoute
import com.nativelap.eumshoppingmall.ui.auth.view.AuthLoginRoute
import com.nativelap.eumshoppingmall.ui.benefits.CouponRoute
import com.nativelap.eumshoppingmall.ui.benefits.PointsRoute
import com.nativelap.eumshoppingmall.ui.cart.view.CartRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchResultsRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchRoute
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutAddressRoute
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentRoute
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroRoute
import com.nativelap.eumshoppingmall.ui.events.EventListRoute
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesRoute
import com.nativelap.eumshoppingmall.ui.home.HomeRoute
import com.nativelap.eumshoppingmall.ui.ordercomplete.view.OrderCompleteRoute
import com.nativelap.eumshoppingmall.ui.orders.view.OrderHistoryRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteCompleteRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteHistoryRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteInquiryDetailRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteRequestRoute
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsRoute
import com.nativelap.eumshoppingmall.ui.screendata.ShoppingScreenDataProvider
import com.nativelap.eumshoppingmall.ui.support.CustomerSupportRoute
import com.nativelap.eumshoppingmall.ui.support.InquiryHistoryRoute
import com.nativelap.eumshoppingmall.ui.support.NoticeListRoute

/**
 * 앱의 단일 NavDisplay. 하단 탭 5개가 각자 back stack을 갖고, 드로어는 Dialog scene으로 띄운다.
 *
 * 스킬 기본 구조(로그인 전 Auth NavDisplay / 로그인 후 Main NavDisplay 분리) 대신 단일 NavDisplay를 쓴다.
 * 이 앱은 비로그인 상태에서도 홈·검색·장바구니를 쓰고 로그인은 보호된 화면 앞에서만 요구하므로, 로그인(A-13)을
 * 현재 탭 위에 [EumDestination.Login]으로 push하고 성공 시 원래 목적지로 돌아오는 구조(F4)가 IA와 맞다.
 *
 * 옵션 시트(A-05s)·배송 요청 메뉴 등 화면 안 시트·다이얼로그는 아직 각 Route의 내부 상태로 연다(후속 작업에서 overlay로 이전).
 */
@Composable
fun EumMainNavDisplay(
    cartItemCount: Int,
    screenDataProvider: ShoppingScreenDataProvider,
    modifier: Modifier = Modifier,
) {
    val navigationState = rememberEumMainNavigationState()
    val navigator = remember(navigationState) { EumMainNavigator(navigationState) }
    val quoteProductSelection = rememberEumQuoteProductSelection()
    val currentCartItemCount by rememberUpdatedState(cartItemCount)
    val entryProvider = remember(navigationState, navigator, screenDataProvider, quoteProductSelection) {
        entryProvider<NavKey> {
            entry<EumDestination.Home> {
                HomeRoute(
                    recommendedProducts = remember { screenDataProvider.homeRecommendedProducts() },
                    bestSellerProducts = remember { screenDataProvider.homeBestSellerProducts() },
                    cartItemCount = currentCartItemCount,
                    selectedTab = MainNavigationTab.Home,
                    onSearch = { query ->
                        if (query.isBlank()) {
                            navigator.navigateToTab(MainNavigationTab.Search)
                        } else {
                            navigator.push(EumDestination.SearchResults(query.trim()))
                        }
                    },
                    onProductClick = { product -> navigator.push(EumDestination.ProductDetail(product.id)) },
                    onQuoteClick = { navigator.push(EumDestination.QuoteRequest()) },
                    onMenuClick = { navigator.push(EumDestination.Drawer) },
                    onSeeAllProductsClick = { navigator.push(EumDestination.ProductList()) },
                    onSeeRecommendedProductsClick = { navigator.push(EumDestination.ProductList()) },
                    onNavigationSelected = navigator::navigateToTab,
                )
            }
            entry<EumDestination.Categories> {
                CatalogCategoryRoute(
                    uiModel = remember { screenDataProvider.categoryScreen() },
                    selectedTab = MainNavigationTab.Categories,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onCategoryClick = { categoryId -> navigator.push(EumDestination.ProductList(categoryId)) },
                )
            }
            entry<EumDestination.Search> {
                CatalogSearchRoute(
                    uiModel = remember { screenDataProvider.searchScreen() },
                    selectedTab = MainNavigationTab.Search,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onSearch = { query -> navigator.push(EumDestination.SearchResults(query)) },
                    onCancel = { navigator.goBack() },
                )
            }
            entry<EumDestination.Cart> {
                CartRoute(
                    uiModel = remember { screenDataProvider.cart() },
                    isLoggedIn = screenDataProvider.isLoggedIn,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onCheckoutClick = { navigator.push(EumDestination.CheckoutAddress) },
                    onQuoteRequestClick = { navigator.push(EumDestination.QuoteRequest()) },
                )
            }
            entry<EumDestination.MyPage> {
                MyPageRoute(
                    uiModel = remember { screenDataProvider.myPage() },
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onLoginClick = { navigator.push(EumDestination.Login()) },
                    onDrawerClick = { navigator.push(EumDestination.Drawer) },
                    onShortcutClick = { shortcutId ->
                        EumMenuDestinations.myPageShortcut(shortcutId)?.let(navigator::push)
                    },
                    onMenuRowClick = { rowId ->
                        EumMenuDestinations.myPageMenuRow(rowId)?.let(navigator::push)
                    },
                )
            }
            entry<EumDestination.Drawer>(
                metadata = DialogSceneStrategy.dialog(
                    DialogProperties(
                        usePlatformDefaultWidth = false,
                        decorFitsSystemWindows = false,
                    ),
                ),
            ) {
                AccountDrawerRoute(
                    profile = remember { screenDataProvider.drawerProfile() },
                    isLoggedIn = screenDataProvider.isLoggedIn,
                    onCloseClick = { navigator.goBack() },
                    onMenuClick = { menuId ->
                        EumMenuDestinations.drawerMenu(menuId)?.let(navigator::replaceTop)
                    },
                )
            }
            entry<EumDestination.ProductList> { destination ->
                CatalogProductListRoute(
                    uiModel = remember(destination) { screenDataProvider.productList(destination.categoryId) },
                    selectedTab = LocalEumOwnerTab.current,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onBackClick = { navigator.goBack() },
                    onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                )
            }
            entry<EumDestination.SearchResults> { destination ->
                CatalogSearchResultsRoute(
                    uiModel = remember(destination) { screenDataProvider.searchResults(destination.query) },
                    selectedTab = LocalEumOwnerTab.current,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onBackClick = { navigator.goBack() },
                    onPopularSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onSearch = { query -> navigator.replaceTop(EumDestination.SearchResults(query)) },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onQuoteRequestClick = { navigator.push(EumDestination.QuoteRequest()) },
                )
            }
            entry<EumDestination.ProductDetail> { destination ->
                CatalogProductDetailRoute(
                    uiModel = remember(destination) { screenDataProvider.productDetail(destination.productId) },
                    onBackClick = { navigator.goBack() },
                    onCartClick = { navigator.navigateToTab(MainNavigationTab.Cart) },
                    onBuyNowClick = { navigator.push(EumDestination.CheckoutAddress) },
                    onQuoteRequestClick = { productId -> navigator.push(EumDestination.QuoteRequest(productId)) },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                )
            }
            entry<EumDestination.CheckoutAddress> {
                CheckoutAddressRoute(
                    uiModel = remember { screenDataProvider.checkout() },
                    onBackClick = { navigator.goBack() },
                    onNextClick = { navigator.push(EumDestination.CheckoutPayment) },
                )
            }
            entry<EumDestination.CheckoutPayment> {
                CheckoutPaymentRoute(
                    uiModel = remember { screenDataProvider.checkout() },
                    onBackClick = { navigator.goBack() },
                    onPaymentClick = {
                        // 주문 완료 뒤 뒤로가기로 결제 단계에 다시 들어가지 않도록 A-08·A-09를 걷어 낸다.
                        navigator.popWhileAndPush(EumDestination.OrderComplete) { navKey ->
                            navKey is EumDestination.CheckoutAddress || navKey is EumDestination.CheckoutPayment
                        }
                    },
                )
            }
            entry<EumDestination.OrderComplete> {
                OrderCompleteRoute(
                    uiModel = remember { screenDataProvider.orderComplete() },
                    onContinueShoppingClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onCloseClick = { navigator.goBack() },
                )
            }
            entry<EumDestination.QuoteRequest> { destination ->
                QuoteRequestRoute(
                    uiModel = remember(destination) { screenDataProvider.quoteRequest(destination.productId) },
                    pickedProductId = quoteProductSelection.pendingProductId,
                    productById = remember { { productId -> screenDataProvider.productDetail(productId).product } },
                    onBackClick = { navigator.goBack() },
                    onAddProductClick = { navigator.push(EumDestination.QuoteProductPicker) },
                    onPickedProductConsumed = quoteProductSelection::consume,
                    onSubmit = {
                        // 완료 뒤 뒤로가기로 작성 폼에 다시 들어가지 않도록 A-15를 걷어 낸다.
                        navigator.popWhileAndPush(EumDestination.QuoteComplete) { navKey ->
                            navKey is EumDestination.QuoteRequest
                        }
                    },
                )
            }
            entry<EumDestination.QuoteProductPicker> {
                CatalogProductListRoute(
                    uiModel = remember { screenDataProvider.productList(categoryId = null) },
                    selectedTab = LocalEumOwnerTab.current,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onBackClick = { navigator.goBack() },
                    onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onProductClick = { productId ->
                        // 상세로 가지 않고 바로 폼에 담는다. 퇴장 애니메이션 중 두 번째 탭이 폼까지 닫거나
                        // 소비되지 않는 ID를 남기지 않도록, 선택 화면이 맨 위여서 pop에 성공했을 때만 ID를 맡긴다.
                        if (navigator.popIfTop(EumDestination.QuoteProductPicker)) {
                            quoteProductSelection.select(productId)
                        }
                    },
                )
            }
            entry<EumDestination.QuoteComplete> {
                QuoteCompleteRoute(
                    uiModel = remember { screenDataProvider.quoteComplete() },
                    onCloseClick = { navigator.goBack() },
                    onContinueShoppingClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onViewInquiriesClick = { navigator.replaceTop(EumDestination.QuoteHistory) },
                )
            }
            entry<EumDestination.QuoteHistory> {
                QuoteHistoryRoute(
                    uiModel = remember { screenDataProvider.quoteHistory() },
                    onBackClick = { navigator.goBack() },
                    onInquiryClick = { inquiryId -> navigator.push(EumDestination.QuoteInquiryDetail(inquiryId)) },
                    onNewInquiryClick = { navigator.push(EumDestination.QuoteRequest()) },
                )
            }
            entry<EumDestination.QuoteInquiryDetail> { destination ->
                QuoteInquiryDetailRoute(
                    uiModel = remember(destination) { screenDataProvider.quoteInquiryDetail(destination.inquiryId) },
                    onBackClick = { navigator.goBack() },
                    onRequestAgainClick = { navigator.push(EumDestination.QuoteRequest()) },
                )
            }
            entry<EumDestination.OrderHistory> {
                OrderHistoryRoute(
                    uiModel = remember { screenDataProvider.orderHistory() },
                    onBackClick = { navigator.goBack() },
                    onBrowseProductsClick = { navigator.navigateToTab(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.Favorites> {
                FavoritesRoute(
                    uiModel = remember { screenDataProvider.favorites() },
                    onBackClick = { navigator.goBack() },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onBrowseProductsClick = { navigator.navigateToTab(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.RecentProducts> {
                RecentProductsRoute(
                    uiModel = remember { screenDataProvider.recentProducts() },
                    onBackClick = { navigator.goBack() },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onBrowseProductsClick = { navigator.navigateToTab(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.Coupons> {
                CouponRoute(
                    uiModel = remember { screenDataProvider.coupons() },
                    onBackClick = { navigator.goBack() },
                    onBrowseProductsClick = { navigator.navigateToTab(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.Points> {
                PointsRoute(
                    uiModel = remember { screenDataProvider.points() },
                    onBackClick = { navigator.goBack() },
                    onShopClick = { navigator.navigateToTab(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.InquiryHistory> {
                InquiryHistoryRoute(
                    items = remember { screenDataProvider.inquiryHistory() },
                    onBackClick = { navigator.goBack() },
                )
            }
            entry<EumDestination.CustomerSupport> {
                CustomerSupportRoute(
                    uiModel = remember { screenDataProvider.customerSupport() },
                    onBackClick = { navigator.goBack() },
                )
            }
            entry<EumDestination.NoticeList> {
                NoticeListRoute(
                    notices = remember { screenDataProvider.notices() },
                    onBackClick = { navigator.goBack() },
                )
            }
            entry<EumDestination.EventList> {
                EventListRoute(
                    events = remember { screenDataProvider.events() },
                    onBackClick = { navigator.goBack() },
                )
            }
            entry<EumDestination.CompanyIntro> {
                CompanyIntroRoute(
                    uiModel = remember { screenDataProvider.companyIntro() },
                    onBackClick = { navigator.goBack() },
                )
            }
            entry<EumDestination.Login> {
                AuthLoginRoute(onCloseClick = { navigator.goBack() })
            }
        }
    }
    NavDisplay(
        entries = navigationState.rememberNavEntries(entryProvider),
        modifier = modifier,
        sceneStrategies = listOf(
            DialogSceneStrategy(),
            SinglePaneSceneStrategy(),
        ),
        transitionSpec = EumNavTransitions.transitionSpec,
        popTransitionSpec = EumNavTransitions.popTransitionSpec,
        predictivePopTransitionSpec = EumNavTransitions.predictivePopTransitionSpec,
        onBack = { navigator.goBack() },
    )
}

package com.nativelap.eumshoppingmall.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.account.view.AccountDrawerRoute
import com.nativelap.eumshoppingmall.ui.account.view.MyPageRoute
import com.nativelap.eumshoppingmall.ui.auth.createAccountService
import com.nativelap.eumshoppingmall.ui.auth.createLoginAuthenticator
import com.nativelap.eumshoppingmall.ui.auth.view.AuthLoginRoute
import com.nativelap.eumshoppingmall.ui.benefits.CouponRoute
import com.nativelap.eumshoppingmall.ui.benefits.PointsRoute
import com.nativelap.eumshoppingmall.ui.cart.view.CartRoute
import com.nativelap.eumshoppingmall.ui.catalog.CartAddOutcome
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchResultsRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchRoute
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutAddressRoute
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentRoute
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroRoute
import com.nativelap.eumshoppingmall.ui.events.EventListRoute
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesRoute
import com.nativelap.eumshoppingmall.ui.home.HomeRoute
import com.nativelap.eumshoppingmall.ui.ordercomplete.view.OrderCompleteRoute
import com.nativelap.eumshoppingmall.ui.orders.view.OrderHistoryRoute
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteCompleteRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteHistoryRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteInquiryDetailRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteRequestRoute
import com.nativelap.eumshoppingmall.ui.recent.RecentProductsRoute
import com.nativelap.eumshoppingmall.ui.screendata.ShoppingScreenDataProvider
import com.nativelap.eumshoppingmall.ui.splash.SplashOverlay
import com.nativelap.eumshoppingmall.ui.support.CustomerSupportRoute
import com.nativelap.eumshoppingmall.ui.support.InquiryHistoryRoute
import com.nativelap.eumshoppingmall.ui.support.NoticeListRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

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
    savedCartLines: List<CartLine>,
    screenDataProvider: ShoppingScreenDataProvider,
    onAddToCart: suspend (addedLines: List<CartLine>) -> Unit,
    onCartQuantityChanged: suspend (lineId: String, quantity: Int) -> Unit,
    onCartItemsRemoved: suspend (lineIds: Set<String>) -> Unit,
    onCartLinesRestored: suspend (lines: List<CartLine>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationState = rememberEumMainNavigationState()
    val navigator = remember(navigationState) { EumMainNavigator(navigationState) }
    val quoteProductSelection = rememberEumQuoteProductSelection()
    val loginSession = rememberEumLoginSession()
    val loginAuthenticator = remember { createLoginAuthenticator() }
    val accountService = remember { createAccountService() }
    val addressSelection = rememberEumAddressSelection()
    val entryContext = remember(navigator, loginSession, screenDataProvider) {
        EumEntryContext(navigator, loginSession, screenDataProvider)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val signupWelcomeMessage = stringResource(R.string.flow_signup_welcome)
    // 결제 직후 장바구니 제거는 결제 화면이 사라져도 끝나야 하므로 화면 밖 scope에서 실행한다.
    val orderScope = rememberCoroutineScope()
    val currentOnCartItemsRemoved by rememberUpdatedState(onCartItemsRemoved)
    val currentCartItemCount by rememberUpdatedState(cartItemCount)
    val currentSavedCartLines by rememberUpdatedState(savedCartLines)
    // 담기 결과는 Route가 아니라 여기서 보관한다. Route가 사라져도 결과가 남아 다음 화면 구성에서 안내된다.
    var cartAddOutcome by rememberSaveable(stateSaver = CartAddOutcome.Saver) { mutableStateOf<CartAddOutcome?>(null) }
    val currentCartAddOutcome by rememberUpdatedState(cartAddOutcome)

    // 맨 위가 인증 화면일 때만 세션을 갱신하고 이동한다. 늦게 도착한 성공 콜백이 세션만 바꾸는 불일치를 막는다.
    fun completeAuthentication(): Boolean {
        val topKey = navigationState.currentBackStack.lastOrNull() ?: return false
        if (!isAuthDestination(topKey)) return false
        // 세션을 먼저 갱신한 뒤 이동해, 도착한 화면이 로그인 상태로 그려지게 한다.
        loginSession.markLoggedIn()
        navigator.completeLogin()
        return true
    }
    val entryProvider = remember(navigationState, navigator, screenDataProvider, quoteProductSelection, addressSelection, entryContext, loginSession, loginAuthenticator, accountService, onAddToCart) {
        entryProvider<NavKey> {
            entry<EumDestination.Home> {
                HomeRoute(
                    categories = remember { com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryDefinitions.homeItems() },
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
                    onCategoryClick = { category -> navigator.push(destinationForCategory(category.id)) },
                    onHeroAction = { action -> navigator.push(destinationForHomeHeroAction(action)) },
                    onQuoteClick = { navigator.push(EumDestination.QuoteRequest()) },
                    onMenuClick = { navigator.push(EumDestination.Drawer) },
                    onSeeAllProductsClick = { navigator.push(EumDestination.ProductList()) },
                    onSeeRecommendedProductsClick = { navigator.push(EumDestination.ProductList()) },
                    isFavoriteAllowed = loginSession.isLoggedIn,
                    onFavoriteBlocked = entryContext::openLogin,
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
                    onCategoryClick = { categoryId -> navigator.push(destinationForCategory(categoryId)) },
                    onBrandClick = { brandId -> navigator.push(EumDestination.BrandDetail(brandId)) },
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
                    // 저장된 장바구니 줄이 있으면 그것을 먼저 보여 주고, 없을 때만 provider의 기본 목록을 쓴다.
                    savedLines = currentSavedCartLines,
                    fallback = remember { screenDataProvider.cart() },
                    onQuantityChanged = onCartQuantityChanged,
                    onItemsRemoved = onCartItemsRemoved,
                    onCartLinesRestored = onCartLinesRestored,
                    isLoggedIn = loginSession.isLoggedIn,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onCheckoutClick = { orderedLineIds ->
                        entryContext.open(EumDestination.CheckoutAddress(orderedLineIds))
                    },
                    // 배너에서는 돌아갈 곳이 지금 장바구니이므로 보류 목적지 없이 로그인만 연다.
                    onLoginClick = { navigator.push(EumDestination.Login()) },
                    onQuoteRequestClick = { navigator.push(EumDestination.QuoteRequest()) },
                    onOptionChangeClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
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
                        EumMenuDestinations.myPageShortcut(shortcutId)?.let(entryContext::open)
                    },
                    onMenuRowClick = { rowId ->
                        EumMenuDestinations.myPageMenuRow(rowId)?.let(entryContext::open)
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
                    isLoggedIn = loginSession.isLoggedIn,
                    onLoginRequired = entryContext::openLogin,
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
                    isLoggedIn = loginSession.isLoggedIn,
                    onLoginRequired = entryContext::openLogin,
                )
            }
            entry<EumDestination.ProductDetail> { destination ->
                CatalogProductDetailRoute(
                    uiModel = remember(destination) { screenDataProvider.productDetail(destination.productId) },
                    onBackClick = { navigator.goBack() },
                    onCartClick = { navigator.navigateToTab(MainNavigationTab.Cart) },
                    onBuyNowClick = {
                        entryContext.open(EumDestination.CheckoutAddress())
                    },
                    onQuoteRequestClick = { productId -> navigator.push(EumDestination.QuoteRequest(productId)) },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    isLoggedIn = loginSession.isLoggedIn,
                    onLoginRequired = entryContext::openLogin,
                    onAllReviewsClick = { productId -> navigator.push(EumDestination.ProductReviews(productId)) },
                    onReviewWriteClick = { productId -> entryContext.open(EumDestination.ReviewWrite(productId)) },
                    onQuestionWriteClick = { productId -> entryContext.open(EumDestination.ProductInquiryWrite(productId)) },
                    onAddToCart = onAddToCart,
                    cartAddOutcome = currentCartAddOutcome,
                    onCartAddOutcomeChange = { outcome -> cartAddOutcome = outcome },
                )
            }
            entry<EumDestination.CheckoutAddress> { destination ->
                CheckoutAddressRoute(
                    uiModel = remember { screenDataProvider.checkout() },
                    onBackClick = { navigator.goBack() },
                    onNextClick = {
                        navigator.push(EumDestination.CheckoutPayment(destination.orderedCartLineIds))
                    },
                    onAddressChangeClick = { navigator.push(EumDestination.AddressSearch) },
                    pickedAddressId = addressSelection.pendingAddressId,
                    addressLabelById = screenDataProvider::addressLabel,
                    onPickedAddressConsumed = addressSelection::consume,
                )
            }
            entry<EumDestination.CheckoutPayment> { destination ->
                CheckoutPaymentRoute(
                    uiModel = remember { screenDataProvider.checkout() },
                    onBackClick = { navigator.goBack() },
                    onPaymentClick = {
                        // 주문 완료 뒤 뒤로가기로 결제 단계에 다시 들어가지 않도록 A-08·A-09를 걷어 낸다.
                        val isPaymentCompleted = navigator.popWhileAndPushIfTop(
                            topDestination = destination,
                            destination = EumDestination.OrderComplete,
                        ) { navKey ->
                            navKey is EumDestination.CheckoutAddress || navKey is EumDestination.CheckoutPayment
                        }
                        // 이동은 제거 완료를 기다리지 않는다. 연타된 두 번째 결제는 맨 위가 달라 여기에 오지 않는다.
                        if (isPaymentCompleted && destination.orderedCartLineIds.isNotEmpty()) {
                            val orderedLineIds = destination.orderedCartLineIds.toSet()
                            orderScope.launch {
                                try {
                                    currentOnCartItemsRemoved(orderedLineIds)
                                } catch (exception: CancellationException) {
                                    throw exception
                                } catch (exception: Exception) {
                                    // 저장 실패는 주문 완료를 되돌리지 않는다. 장바구니에 남은 줄은 사용자가 직접 지울 수 있다.
                                }
                            }
                        }
                    },
                )
            }
            entry<EumDestination.OrderComplete> {
                // A-10의 뒤로가기·X·쇼핑 계속은 결제 직전 화면이 아니라 홈으로 간다(F1). 연타는 맨 위가 OrderComplete일 때만 처리한다.
                fun closeOrderComplete() {
                    navigator.closeTopAndOpenTabRootIfTop(EumDestination.OrderComplete, MainNavigationTab.Home)
                }

                val orderCompleteUiModel = remember { screenDataProvider.orderComplete() }
                OrderCompleteRoute(
                    uiModel = orderCompleteUiModel,
                    onContinueShoppingClick = ::closeOrderComplete,
                    onCloseClick = ::closeOrderComplete,
                    // 서버 연동 전이라 주문 ID가 없어 주문 번호를 주문 ID로 쓴다.
                    onViewOrderDetailsClick = {
                        entryContext.open(EumDestination.OrderDetail(orderCompleteUiModel.orderNumber))
                    },
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
                    isLoggedIn = loginSession.isLoggedIn,
                    onLoginRequired = entryContext::openLogin,
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
                    onViewInquiriesClick = { navigator.replaceTop(gateIfProtected(EumDestination.QuoteHistory, loginSession.isLoggedIn)) },
                )
            }
            entry<EumDestination.QuoteHistory> {
                QuoteHistoryRoute(
                    uiModel = remember { screenDataProvider.quoteHistory() },
                    onBackClick = { navigator.goBack() },
                    onInquiryClick = { inquiryId -> entryContext.open(EumDestination.QuoteInquiryDetail(inquiryId)) },
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
                    onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onOrderDetailsClick = { orderId -> entryContext.open(EumDestination.OrderDetail(orderId)) },
                    onTrackDeliveryClick = { orderId -> entryContext.open(EumDestination.DeliveryTracking(orderId)) },
                    onCancelClick = { orderId ->
                        entryContext.open(EumDestination.ClaimRequest(orderId, OrderRequestType.Cancel.name))
                    },
                    onCancelReturnClick = { entryContext.open(EumDestination.ClaimHub) },
                )
            }
            entry<EumDestination.Favorites> {
                FavoritesRoute(
                    uiModel = remember { screenDataProvider.favorites() },
                    onBackClick = { navigator.goBack() },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.RecentProducts> {
                RecentProductsRoute(
                    uiModel = remember { screenDataProvider.recentProducts() },
                    onBackClick = { navigator.goBack() },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.Coupons> {
                CouponRoute(
                    uiModel = remember { screenDataProvider.coupons() },
                    onBackClick = { navigator.goBack() },
                    onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onPointsClick = { entryContext.open(EumDestination.Points) },
                )
            }
            entry<EumDestination.Points> {
                PointsRoute(
                    uiModel = remember { screenDataProvider.points() },
                    onBackClick = { navigator.goBack() },
                    onShopClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                )
            }
            entry<EumDestination.InquiryHistory> {
                InquiryHistoryRoute(
                    items = remember { screenDataProvider.inquiryHistory() },
                    onBackClick = { navigator.goBack() },
                    onItemClick = { inquiryId -> entryContext.open(EumDestination.InquiryDetail(inquiryId)) },
                    onWriteClick = { entryContext.open(EumDestination.InquiryWrite) },
                )
            }
            entry<EumDestination.CustomerSupport> {
                CustomerSupportRoute(
                    uiModel = remember { screenDataProvider.customerSupport() },
                    onBackClick = { navigator.goBack() },
                    onInquiryClick = { entryContext.open(EumDestination.InquiryWrite) },
                )
            }
            entry<EumDestination.NoticeList> {
                NoticeListRoute(
                    notices = remember { screenDataProvider.notices() },
                    onBackClick = { navigator.goBack() },
                    onNoticeClick = { noticeId -> navigator.push(EumDestination.NoticeDetail(noticeId)) },
                )
            }
            entry<EumDestination.EventList> {
                EventListRoute(
                    events = remember { screenDataProvider.events() },
                    onBackClick = { navigator.goBack() },
                    onEventClick = { eventId -> navigator.push(EumDestination.EventDetail(eventId)) },
                )
            }
            entry<EumDestination.CompanyIntro> {
                CompanyIntroRoute(
                    uiModel = remember { screenDataProvider.companyIntro() },
                    onBackClick = { navigator.goBack() },
                )
            }
            entry<EumDestination.Login> {
                AuthLoginRoute(
                    authenticator = loginAuthenticator,
                    // 세션을 먼저 갱신한 뒤 이동해, 도착한 화면이 로그인 상태로 그려지게 한다.
                    onLoginSuccess = { completeAuthentication() },
                    onCloseClick = { navigator.goBack() },
                    onFindEmailClick = { navigator.push(EumDestination.AccountRecovery()) },
                    onFindPasswordClick = { navigator.push(EumDestination.AccountRecovery(findsPassword = true)) },
                    onSignupClick = { navigator.push(EumDestination.Signup) },
                )
            }
            addAuthEntries(entryContext, accountService) {
                // 가입 완료는 곧 로그인이다. 인증 화면이 맨 위일 때만 진행하고 환영 안내를 띄운다.
                if (completeAuthentication()) {
                    orderScope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(message = signupWelcomeMessage, duration = SnackbarDuration.Short)
                    }
                }
            }
            addOrderEntries(entryContext, addressSelection)
            addSupportEntries(entryContext)
            addCatalogDetailEntries(entryContext)
        }
    }
    Box(modifier = modifier) {
        NavDisplay(
            entries = navigationState.rememberNavEntries(entryProvider),
            sceneStrategies = listOf(
                DialogSceneStrategy(),
                SinglePaneSceneStrategy(),
            ),
            transitionSpec = EumNavTransitions.transitionSpec,
            popTransitionSpec = EumNavTransitions.popTransitionSpec,
            predictivePopTransitionSpec = EumNavTransitions.predictivePopTransitionSpec,
            onBack = {
                // 시스템 뒤로가기도 A-10에서는 홈으로 보낸다. BackHandler로 가로채지 않고 이 경로로 통일한다.
                if (!navigator.closeTopAndOpenTabRootIfTop(EumDestination.OrderComplete, MainNavigationTab.Home)) {
                    navigator.goBack()
                }
            },
        )
        EumSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding(),
        )
        SplashOverlay()
    }
}

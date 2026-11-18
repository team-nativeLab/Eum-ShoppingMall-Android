package com.nativelap.eumshoppingmall.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.nativelap.eumshoppingmall.data.local.CartLineReplaceResult
import com.nativelap.eumshoppingmall.ui.account.view.AccountDrawerRoute
import com.nativelap.eumshoppingmall.ui.account.view.MyPageRoute
import com.nativelap.eumshoppingmall.ui.auth.createAccountService
import com.nativelap.eumshoppingmall.ui.auth.createLoginAuthenticator
import com.nativelap.eumshoppingmall.ui.auth.view.AuthLoginRoute
import com.nativelap.eumshoppingmall.ui.benefits.CouponRoute
import com.nativelap.eumshoppingmall.ui.benefits.PointsRoute
import com.nativelap.eumshoppingmall.ui.cart.view.CartRoute
import com.nativelap.eumshoppingmall.ui.catalog.CartAddOutcome
import com.nativelap.eumshoppingmall.ui.catalog.CartEditOutcome
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryDefinitions
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchResultsRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSearchRoute
import com.nativelap.eumshoppingmall.ui.catalog.QuickCartGate
import com.nativelap.eumshoppingmall.ui.catalog.clearedIfShown
import com.nativelap.eumshoppingmall.ui.catalog.formatWonLabel
import com.nativelap.eumshoppingmall.ui.catalog.resolveHomeQuickCartLine
import com.nativelap.eumshoppingmall.ui.catalog.resolveQuickCartLine
import com.nativelap.eumshoppingmall.ui.catalog.saveAddToCartLines
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutAddressRoute
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentRoute
import com.nativelap.eumshoppingmall.ui.checkout.view.checkoutPaymentCanComplete
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutOrderSnapshot
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutOrderSource
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.checkoutOrderSnapshot
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.checkoutOrderSnapshotFromCartItems
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.resolveCheckoutOrderForSelection
import com.nativelap.eumshoppingmall.ui.common.DismissSnackbarOnNavigationChange
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroRoute
import com.nativelap.eumshoppingmall.ui.events.EventListRoute
import com.nativelap.eumshoppingmall.ui.favorites.FavoritesRoute
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeRoute
import com.nativelap.eumshoppingmall.ui.home.homeQueryForSearchQuery
import com.nativelap.eumshoppingmall.ui.ordercomplete.view.OrderCompleteRoute
import com.nativelap.eumshoppingmall.ui.orders.view.OrderHistoryRoute
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteCompleteRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteHistoryRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteInquiryDetailRoute
import com.nativelap.eumshoppingmall.ui.quote.view.QuoteRequestRoute
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteDetailItemUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.toQuoteCompletePreview
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.toQuoteHistoryPreview
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.unavailableQuoteCompletePreview
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.unavailableQuoteInquiryDetailPreview
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
    /** 저장된 장바구니 줄. null이면 아직 읽지 못한 상태라 편집 원본을 판정하지 않는다. */
    savedCartLines: List<CartLine>?,
    screenDataProvider: ShoppingScreenDataProvider,
    onAddToCart: suspend (addedLines: List<CartLine>) -> Unit,
    onCartQuantityChanged: suspend (lineId: String, quantity: Int) -> Unit,
    onCartItemsRemoved: suspend (lineIds: Set<String>) -> Unit,
    onCartLinesRestored: suspend (lines: List<CartLine>) -> Unit,
    onReplaceCartLine: suspend (sourceLineId: String, replacement: CartLine) -> CartLineReplaceResult,
    modifier: Modifier = Modifier,
) {
    val navigationState = rememberEumMainNavigationState()
    val navigator = remember(navigationState) { EumMainNavigator(navigationState) }
    val quoteProductSelection = rememberEumQuoteProductSelection()
    val loginSession = rememberEumLoginSession()
    val loginAuthenticator = remember { createLoginAuthenticator() }
    val accountService = remember { createAccountService() }
    val addressSelection = rememberEumAddressSelection()
    val snackbarHostState = remember { SnackbarHostState() }
    val signupWelcomeMessage = stringResource(R.string.flow_signup_welcome)
    val unavailableActionMessage = stringResource(R.string.flow_feature_unavailable)
    val signedInFallbackName = stringResource(R.string.account_member_fallback)
    val checkoutUnavailableLabel = stringResource(R.string.checkout_order_information_unavailable)
    // 결제 직후 장바구니 제거는 결제 화면이 사라져도 끝나야 하므로 화면 밖 scope에서 실행한다.
    val orderScope = rememberCoroutineScope()
    val showUnavailableAction: () -> Unit = remember(snackbarHostState, orderScope, unavailableActionMessage) {
        {
            orderScope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = unavailableActionMessage,
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }
    val entryContext = remember(navigator, loginSession, screenDataProvider, showUnavailableAction) {
        EumEntryContext(navigator, loginSession, screenDataProvider, showUnavailableAction)
    }
    DismissSnackbarOnNavigationChange(snackbarHostState, navigationState.currentBackStack.lastOrNull())
    val currentOnCartItemsRemoved by rememberUpdatedState(onCartItemsRemoved)
    val currentCartItemCount by rememberUpdatedState(cartItemCount)
    val currentSavedCartLines by rememberUpdatedState(savedCartLines)
    // 담기 결과는 Route가 아니라 여기서 보관한다. Route가 사라져도 결과가 남아 다음 화면 구성에서 안내된다.
    var cartAddOutcome by rememberSaveable(stateSaver = CartAddOutcome.Saver) { mutableStateOf<CartAddOutcome?>(null) }
    // 편집 화면이 장바구니로 넘기는 안내 결과. 장바구니 화면이 한 번 보여 주면 표시를 남기고 비운다(C).
    var cartEditOutcome by rememberSaveable(stateSaver = CartEditOutcome.Saver) { mutableStateOf<CartEditOutcome?>(null) }
    // 홈 검색창과 검색 결과가 같은 검색어를 쓴다. 결과에서 지우면 여기서도 비운다.
    var homeSearchQuery by rememberSaveable { mutableStateOf("") }
    var activeCheckoutOrder by rememberSaveable(stateSaver = CheckoutOrderSnapshot.Saver) { mutableStateOf<CheckoutOrderSnapshot?>(null) }
    val quotePreviews = remember { mutableStateListOf<Pair<String, QuoteRequestUiModel>>() }
    var latestQuotePreviewId by remember { mutableStateOf<String?>(null) }
    var quotePreviewSerial by remember { mutableStateOf(0) }
    LaunchedEffect(navigationState.currentBackStack.lastOrNull(), activeCheckoutOrder) {
        val destination = navigationState.currentBackStack.lastOrNull()
        if ((destination is EumDestination.CheckoutAddress || destination is EumDestination.CheckoutPayment) && activeCheckoutOrder == null) {
            showUnavailableAction()
        }
    }
    val currentCartAddOutcome by rememberUpdatedState(cartAddOutcome)
    // 카드 담기는 상세의 옵션 없는 담기와 같은 줄 형식을 쓴다. 결과는 상세와 같은 cartAddOutcome에 남겨 Route가 안내한다.
    val defaultOptionLabel = stringResource(R.string.catalog_option_default_label)
    // 담기가 끝나기 전의 연타는 한 번만 처리한다(상세의 닫기 게이트와 같은 방식).
    val quickCartGate = remember { QuickCartGate() }
    // 담기 한 줄을 저장하고 결과를 cartAddOutcome에 남긴다. 연타는 게이트에서 한 번만 받는다.
    val addQuickCartLine: (CartLine) -> Unit = { line ->
        if (quickCartGate.tryEnter()) {
            orderScope.launch {
                try {
                    val saved = saveAddToCartLines(1) { onAddToCart(listOf(line)) }
                    cartAddOutcome = CartAddOutcome(serial = System.nanoTime(), saved = saved)
                } finally {
                    quickCartGate.exit()
                }
            }
        }
    }
    val quickAddToCart: (CatalogProductUiModel) -> Unit = { product ->
        resolveQuickCartLine(product, defaultOptionLabel)?.let(addQuickCartLine)
    }
    val quickAddHomeProduct: (HomeProductUiModel) -> Unit = { product ->
        resolveHomeQuickCartLine(product, defaultOptionLabel)?.let(addQuickCartLine)
    }

    // 맨 위가 인증 화면일 때만 세션을 갱신하고 이동한다. 늦게 도착한 성공 콜백이 세션만 바꾸는 불일치를 막는다.
    fun completeAuthentication(): Boolean {
        val topKey = navigationState.currentBackStack.lastOrNull() ?: return false
        if (!isAuthDestination(topKey)) return false
        // 세션을 먼저 갱신한 뒤 이동해, 도착한 화면이 로그인 상태로 그려지게 한다.
        loginSession.markLoggedIn()
        navigator.completeLogin()
        return true
    }

    // 카테고리 탭과 홈 카테고리 행의 단일 이동 경로. 상품이 없는 카테고리는 목록 목적지를 만들지 않아 이동하지 않는다.
    fun openCategory(categoryId: String?) {
        destinationForCategoryIfAvailable(categoryId, screenDataProvider.productList(categoryId))
            ?.let { destination -> navigator.push(destination) }
    }
    val entryProvider = remember(navigationState, navigator, screenDataProvider, quoteProductSelection, addressSelection, entryContext, loginSession, loginAuthenticator, accountService, onAddToCart, showUnavailableAction, signedInFallbackName) {
        entryProvider<NavKey> {
            entry<EumDestination.Home> {
                HomeRoute(
                    // 홈 카테고리는 카테고리 탭과 같은 공통 정의를 쓰고, 고르면 그 카테고리 상품 목록으로 간다.
                    categories = remember { CatalogCategoryDefinitions.homeItems() },
                    recommendedProducts = remember { screenDataProvider.homeRecommendedProducts() },
                    todayProducts = remember { screenDataProvider.homeTodayProducts() },
                    bestSellerProducts = remember { screenDataProvider.homeBestSellerProducts() },
                    companyIntro = remember { screenDataProvider.homeCompanyIntro() },
                    cartItemCount = currentCartItemCount,
                    selectedTab = MainNavigationTab.Home,
                    onNotificationClick = showUnavailableAction,
                    onSearch = { query ->
                        if (query.isBlank()) {
                            navigator.navigateToTab(MainNavigationTab.Search)
                        } else {
                            navigator.push(EumDestination.SearchResults(query.trim()))
                        }
                    },
                    onProductClick = { product -> navigator.push(EumDestination.ProductDetail(product.id)) },
                    onCategoryClick = { category -> openCategory(category.id) },
                    onHeroAction = { action -> navigator.push(destinationForHomeHeroAction(action)) },
                    onQuoteClick = { navigator.push(EumDestination.QuoteRequest()) },
                    onMenuClick = { navigator.push(EumDestination.Drawer) },
                    onSeeAllProductsClick = { navigator.push(EumDestination.ProductList()) },
                    onSeeRecommendedProductsClick = { navigator.push(EumDestination.ProductList()) },
                    isFavoriteAllowed = loginSession.isLoggedIn,
                    onFavoriteBlocked = entryContext::openLogin,
                    onNavigationSelected = navigator::navigateToTab,
                    isLoggedIn = loginSession.isLoggedIn,
                    onLoginRequired = entryContext::openLogin,
                    onQuickAddToCart = quickAddHomeProduct,
                    cartAddOutcome = currentCartAddOutcome,
                    onCartAddOutcomeChange = { outcome -> cartAddOutcome = outcome },
                    onCartClick = { navigator.navigateToTab(MainNavigationTab.Cart) },
                    query = homeSearchQuery,
                    onQueryChange = { homeSearchQuery = it },
                    onCartLoginRequired = entryContext::openLoginForCart,
                )
            }
            entry<EumDestination.Categories> {
                CatalogCategoryRoute(
                    uiModel = remember { screenDataProvider.categoryScreen() },
                    selectedTab = MainNavigationTab.Categories,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onCategoryClick = { categoryId -> openCategory(categoryId) },
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
                    savedLines = currentSavedCartLines.orEmpty(),
                    editOutcome = cartEditOutcome,
                    onEditOutcomeChange = { outcome -> cartEditOutcome = outcome },
                    onEditOutcomeCleared = { serial -> cartEditOutcome = cartEditOutcome.clearedIfShown(serial) },
                    fallback = remember { screenDataProvider.cart() },
                    onQuantityChanged = onCartQuantityChanged,
                    onItemsRemoved = onCartItemsRemoved,
                    onCartLinesRestored = onCartLinesRestored,
                    isLoggedIn = loginSession.isLoggedIn,
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
                    onCheckoutClick = { selectedItems ->
                        val selectedIds = selectedItems.map { item -> item.id }
                        val savedLines = currentSavedCartLines
                        val snapshot = when {
                            savedLines == null -> null

                            savedLines.isEmpty() && screenDataProvider.enablesDebugCartFallback ->
                                checkoutOrderSnapshotFromCartItems(selectedItems)

                            savedLines.isNotEmpty() -> resolveCheckoutOrderForSelection(savedLines, selectedIds)

                            else -> null
                        }
                        if (snapshot == null) {
                            showUnavailableAction()
                        } else {
                            activeCheckoutOrder = snapshot
                            entryContext.open(EumDestination.CheckoutAddress(snapshot.cartLineIdsToRemove.toList()))
                        }
                    },
                    // 배너에서는 돌아갈 곳이 지금 장바구니이므로 보류 목적지 없이 로그인만 연다.
                    onLoginClick = { navigator.push(EumDestination.Login()) },
                    onQuoteRequestClick = { navigator.push(EumDestination.QuoteRequest()) },
                    onOptionChangeClick = { line -> navigator.push(EumDestination.ProductDetail(line.productId, line.lineId())) },
                )
            }
            entry<EumDestination.MyPage> {
                MyPageRoute(
                    uiModel = remember(screenDataProvider, loginSession.isLoggedIn, signedInFallbackName) {
                        val myPageUiModel = screenDataProvider.myPage()
                        val profile = if (loginSession.isLoggedIn && myPageUiModel.profile.name.isBlank()) {
                            myPageUiModel.profile.copy(name = signedInFallbackName)
                        } else {
                            myPageUiModel.profile
                        }
                        myPageUiModel.copy(
                            isLoggedIn = loginSession.isLoggedIn,
                            profile = profile,
                        )
                    },
                    cartItemCount = currentCartItemCount,
                    onTabSelected = navigator::navigateToTab,
                    onLoginClick = { navigator.push(EumDestination.Login()) },
                    onDrawerClick = { navigator.push(EumDestination.Drawer) },
                    onShortcutClick = { shortcutId ->
                        EumMenuDestinations.myPageShortcut(shortcutId)?.let(entryContext::open)
                    },
                    onMenuRowClick = { rowId ->
                        val destination = EumMenuDestinations.myPageMenuRow(rowId)
                        if (destination == null) {
                            showUnavailableAction()
                        } else {
                            entryContext.open(destination)
                        }
                    },
                    onUnavailableAction = showUnavailableAction,
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
                    profile = remember(screenDataProvider, loginSession.isLoggedIn, signedInFallbackName) {
                        val accountProfile = screenDataProvider.drawerProfile()
                        if (loginSession.isLoggedIn && accountProfile.name.isBlank()) {
                            accountProfile.copy(name = signedInFallbackName)
                        } else {
                            accountProfile
                        }
                    },
                    isLoggedIn = loginSession.isLoggedIn,
                    onCloseClick = { navigator.goBack() },
                    onMenuClick = { menuId ->
                        val destination = EumMenuDestinations.drawerMenu(menuId)
                        if (destination == null) {
                            navigator.goBack()
                            showUnavailableAction()
                        } else {
                            navigator.replaceTop(destination)
                        }
                    },
                    onUnavailableAction = {
                        navigator.goBack()
                        showUnavailableAction()
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
                    onQuickAddToCart = quickAddToCart,
                    cartAddOutcome = currentCartAddOutcome,
                    onCartAddOutcomeChange = { outcome -> cartAddOutcome = outcome },
                    onCartClick = { navigator.navigateToTab(MainNavigationTab.Cart) },
                    onCartLoginRequired = entryContext::openLoginForCart,
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
                    onSearch = { query ->
                        // 결과 화면에서 다시 검색하면 홈 검색창 공유 상태도 같은 검색어로 맞춘다.
                        homeSearchQuery = homeQueryForSearchQuery(query)
                        navigator.replaceTop(EumDestination.SearchResults(query))
                    },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onQuoteRequestClick = { navigator.push(EumDestination.QuoteRequest()) },
                    isLoggedIn = loginSession.isLoggedIn,
                    onLoginRequired = entryContext::openLogin,
                    // 검색을 비우면 옛 결과를 홈 백스택에 남기지 않고 검색 탭 루트로 돌아간다.
                    onSearchCleared = {
                        homeSearchQuery = ""
                        navigator.closeTopAndOpenTabRoot(MainNavigationTab.Search)
                    },
                    onQuickAddToCart = quickAddToCart,
                    cartAddOutcome = currentCartAddOutcome,
                    onCartAddOutcomeChange = { outcome -> cartAddOutcome = outcome },
                    onCartClick = { navigator.navigateToTab(MainNavigationTab.Cart) },
                    onCartLoginRequired = entryContext::openLoginForCart,
                )
            }
            entry<EumDestination.ProductDetail> { destination ->
                CatalogProductDetailRoute(
                    uiModel = remember(destination) { screenDataProvider.productDetail(destination.productId) },
                    onBackClick = { navigator.goBack() },
                    onCartClick = { navigator.navigateToTab(MainNavigationTab.Cart) },
                    onBuyNowClick = { lines ->
                        val snapshot = checkoutOrderSnapshot(lines, CheckoutOrderSource.BuyNow)
                        if (snapshot == null) {
                            showUnavailableAction()
                        } else {
                            activeCheckoutOrder = snapshot
                            entryContext.open(EumDestination.CheckoutAddress())
                        }
                    },
                    onQuoteRequestClick = { productId -> navigator.push(EumDestination.QuoteRequest(productId)) },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    isLoggedIn = loginSession.isLoggedIn,
                    onLoginRequired = entryContext::openLogin,
                    onAllReviewsClick = { productId -> navigator.push(EumDestination.ProductReviews(productId)) },
                    onReviewWriteClick = { productId -> entryContext.open(EumDestination.ReviewWrite(productId)) },
                    onQuestionWriteClick = { productId -> entryContext.open(EumDestination.ProductInquiryWrite(productId)) },
                    onAddToCart = onAddToCart,
                    editSourceLineId = destination.editSourceLineId,
                    editSourceLine = destination.editSourceLineId?.let { lineId -> currentSavedCartLines?.firstOrNull { it.lineId() == lineId } },
                    isCartLoaded = currentSavedCartLines != null,
                    onReplaceCartLine = onReplaceCartLine,
                    onCartEditNotice = { notice -> cartEditOutcome = CartEditOutcome(serial = System.nanoTime(), notice = notice) },
                    cartAddOutcome = currentCartAddOutcome,
                    onCartAddOutcomeChange = { outcome -> cartAddOutcome = outcome },
                    onUnavailableAction = showUnavailableAction,
                )
            }
            entry<EumDestination.CheckoutAddress> { destination ->
                CheckoutAddressRoute(
                    uiModel = remember(destination, activeCheckoutOrder) {
                        checkoutUiModelForOrder(activeCheckoutOrder, screenDataProvider.checkout(), checkoutUnavailableLabel)
                    },
                    onBackClick = { navigator.goBack() },
                    onNextClick = {
                        if (activeCheckoutOrder == null) {
                            showUnavailableAction()
                        } else {
                            navigator.push(EumDestination.CheckoutPayment(destination.orderedCartLineIds))
                        }
                    },
                    onAddressChangeClick = { navigator.push(EumDestination.AddressSearch) },
                    pickedAddressId = addressSelection.pendingAddressId,
                    addressLabelById = screenDataProvider::addressLabel,
                    onPickedAddressConsumed = addressSelection::consume,
                )
            }
            entry<EumDestination.CheckoutPayment> { destination ->
                CheckoutPaymentRoute(
                    uiModel = remember(destination, activeCheckoutOrder) {
                        checkoutUiModelForOrder(activeCheckoutOrder, screenDataProvider.checkout(), checkoutUnavailableLabel)
                    },
                    onBackClick = { navigator.goBack() },
                    onPaymentClick = { paymentUiModel ->
                        val checkoutOrder = activeCheckoutOrder
                        if (checkoutOrder == null || !checkoutPaymentCanComplete(checkoutOrder, paymentUiModel)) {
                            showUnavailableAction()
                        } else {
                            // 주문 완료 뒤 뒤로가기로 결제 단계에 다시 들어가지 않도록 A-08·A-09를 걷어 낸다.
                            val isPaymentCompleted = navigator.popWhileAndPushIfTop(
                                topDestination = destination,
                                destination = EumDestination.OrderComplete,
                            ) { navKey ->
                                navKey is EumDestination.CheckoutAddress || navKey is EumDestination.CheckoutPayment
                            }
                            // 이동은 제거 완료를 기다리지 않는다. 연타된 두 번째 결제는 맨 위가 달라 여기에 오지 않는다.
                            val orderToRemove = checkoutOrder.cartLineIdsToRemove
                            if (isPaymentCompleted && orderToRemove.isNotEmpty()) {
                                orderScope.launch {
                                    try {
                                        currentOnCartItemsRemoved(orderToRemove)
                                    } catch (exception: CancellationException) {
                                        throw exception
                                    } catch (exception: Exception) {
                                        // 저장 실패는 주문 완료를 되돌리지 않는다. 장바구니에 남은 줄은 사용자가 직접 지울 수 있다.
                                    }
                                }
                            }
                        }
                    },
                    onUnavailableAction = showUnavailableAction,
                )
            }
            entry<EumDestination.OrderComplete> {
                // A-10의 뒤로가기·X·쇼핑 계속은 결제 직전 화면이 아니라 홈으로 간다(F1). 연타는 맨 위가 OrderComplete일 때만 처리한다.
                fun closeOrderComplete() {
                    navigator.closeTopAndOpenTabRootIfTop(EumDestination.OrderComplete, MainNavigationTab.Home)
                }

                val orderCompleteUiModel = remember(activeCheckoutOrder) {
                    val base = screenDataProvider.orderComplete()
                    base.copy(paymentAmountLabel = activeCheckoutOrder?.finalTotalWon?.let(::formatWonLabel) ?: "주문 시 확인")
                }
                OrderCompleteRoute(
                    uiModel = orderCompleteUiModel,
                    onContinueShoppingClick = ::closeOrderComplete,
                    onCloseClick = ::closeOrderComplete,
                    // 서버 연동 전이라 주문 ID가 없어 주문 번호를 주문 ID로 쓴다.
                    onViewOrderDetailsClick = {
                        entryContext.open(EumDestination.OrderDetail(orderCompleteUiModel.orderNumber))
                    },
                    onUnavailableAction = showUnavailableAction,
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
                    onSubmit = { submitted ->
                        quotePreviewSerial += 1
                        val previewId = "preview-$quotePreviewSerial"
                        quotePreviews.add(previewId to submitted)
                        latestQuotePreviewId = previewId
                        // 완료 뒤 뒤로가기로 작성 폼에 다시 들어가지 않도록 A-15를 걷어 낸다.
                        navigator.popWhileAndPush(EumDestination.QuoteComplete) { navKey ->
                            navKey is EumDestination.QuoteRequest
                        }
                    },
                    onUnavailableAction = showUnavailableAction,
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
                    isQuotePicker = true,
                )
            }
            entry<EumDestination.QuoteComplete> {
                val unavailablePreviewLabel = stringResource(R.string.quote_preview_unavailable)
                val preview = quotePreviews.lastOrNull { it.first == latestQuotePreviewId }?.second
                QuoteCompleteRoute(
                    uiModel = preview?.toQuoteCompletePreview(
                        when (preview.requestType) {
                            com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestType.CustomManufacturing -> stringResource(R.string.quote_custom_manufacturing)
                            com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestType.BulkPurchase -> stringResource(R.string.quote_bulk_purchase)
                            com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestType.RegularDelivery -> stringResource(R.string.quote_regular_delivery)
                            com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestType.Other -> stringResource(R.string.quote_other)
                        },
                        noProductsLabel = stringResource(R.string.quote_product_empty),
                    )
                        ?: unavailableQuoteCompletePreview(unavailablePreviewLabel),
                    onCloseClick = { navigator.goBack() },
                    onContinueShoppingClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onViewInquiriesClick = { navigator.replaceTop(gateIfProtected(EumDestination.QuoteHistory, loginSession.isLoggedIn)) },
                )
            }
            entry<EumDestination.QuoteHistory> {
                val previewSessionLabel = stringResource(R.string.quote_preview_session)
                val previewBadgeLabel = stringResource(R.string.quote_preview_badge)
                val noProductsLabel = stringResource(R.string.quote_product_empty)
                val dueDateLabel = stringResource(R.string.quote_preferred_due_date)
                val previewItems = quotePreviews.map { (id, preview) -> preview.toQuoteHistoryPreview(id, previewSessionLabel, previewBadgeLabel, noProductsLabel, dueDateLabel) }
                QuoteHistoryRoute(
                    uiModel = QuoteHistoryUiModel(inquiries = previewItems),
                    onBackClick = { navigator.goBack() },
                    onInquiryClick = { inquiryId -> entryContext.open(EumDestination.QuoteInquiryDetail(inquiryId)) },
                    onNewInquiryClick = { navigator.push(EumDestination.QuoteRequest()) },
                    onUnavailableAction = showUnavailableAction,
                )
            }
            entry<EumDestination.QuoteInquiryDetail> { destination ->
                val unavailablePreviewLabel = stringResource(R.string.quote_preview_unavailable)
                val preview = quotePreviews.firstOrNull { entry -> entry.first == destination.inquiryId }?.second
                QuoteInquiryDetailRoute(
                    uiModel = preview?.let { request ->
                        QuoteInquiryDetailUiModel(
                            title = request.companyName,
                            dateLabel = stringResource(R.string.quote_preview_session),
                            inquiryNumber = stringResource(R.string.quote_preview_badge),
                            status = QuoteInquiryStatus.Preview,
                            companyName = request.companyName,
                            contactName = request.contactName,
                            contactPhone = request.phoneNumber,
                            contactEmail = request.email,
                            items = request.products.map { product -> QuoteDetailItemUiModel(product.id, product.name, "") },
                            preferredDeliveryDate = request.preferredDeliveryDate,
                            deliveryAddress = request.deliveryAddress,
                            requestDetails = request.requestDetails,
                        )
                    } ?: remember(destination) {
                        unavailableQuoteInquiryDetailPreview(unavailablePreviewLabel)
                    },
                    onBackClick = { navigator.goBack() },
                    onRequestAgainClick = { navigator.push(EumDestination.QuoteRequest()) },
                    onUnavailableAction = showUnavailableAction,
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
                    onUnavailableAction = showUnavailableAction,
                )
            }
            entry<EumDestination.Favorites> {
                FavoritesRoute(
                    uiModel = remember { screenDataProvider.favorites() },
                    onBackClick = { navigator.goBack() },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onUnavailableAction = showUnavailableAction,
                )
            }
            entry<EumDestination.RecentProducts> {
                RecentProductsRoute(
                    uiModel = remember { screenDataProvider.recentProducts() },
                    onBackClick = { navigator.goBack() },
                    onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
                    onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onUnavailableAction = showUnavailableAction,
                )
            }
            entry<EumDestination.Coupons> {
                CouponRoute(
                    uiModel = remember { screenDataProvider.coupons() },
                    onBackClick = { navigator.goBack() },
                    onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
                    onPointsClick = { entryContext.open(EumDestination.Points) },
                    onUnavailableAction = showUnavailableAction,
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
                    onUnavailableAction = showUnavailableAction,
                )
            }
            entry<EumDestination.Login> { destination ->
                AuthLoginRoute(
                    authenticator = loginAuthenticator,
                    // 세션을 먼저 갱신한 뒤 이동해, 도착한 화면이 로그인 상태로 그려지게 한다.
                    onLoginSuccess = { completeAuthentication() },
                    onCloseClick = { navigator.goBack() },
                    onFindEmailClick = { navigator.push(EumDestination.AccountRecovery()) },
                    onFindPasswordClick = { navigator.push(EumDestination.AccountRecovery(findsPassword = true)) },
                    onSignupClick = { navigator.push(EumDestination.Signup) },
                    loginNotice = if (destination.cartNotice) stringResource(R.string.login_required_for_cart) else null,
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

private fun checkoutUiModelForOrder(
    snapshot: CheckoutOrderSnapshot?,
    base: CheckoutUiModel,
    unavailableLabel: String,
): CheckoutUiModel = snapshot?.toCheckoutUiModel(base) ?: base.copy(
    address = base.address.copy(recipientName = "", contactLabel = "", addressLabel = "", isDefaultAddress = false),
    deliveryMethodLabel = "",
    products = emptyList(),
    couponCount = 0,
    pointsAvailableLabel = "",
    paymentSummary = base.paymentSummary.copy(
        productAmountLabel = unavailableLabel,
        shippingFeeLabel = unavailableLabel,
        discountLabel = unavailableLabel,
        finalAmountLabel = unavailableLabel,
        shippingFeeWon = null,
        finalAmountWon = null,
    ),
)

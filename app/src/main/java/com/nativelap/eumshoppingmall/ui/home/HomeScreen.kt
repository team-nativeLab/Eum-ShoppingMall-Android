package com.nativelap.eumshoppingmall.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CartAddOutcome
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSelectionChip
import com.nativelap.eumshoppingmall.ui.catalog.QuickCartDecision
import com.nativelap.eumshoppingmall.ui.catalog.decideQuickCart
import com.nativelap.eumshoppingmall.ui.catalog.rememberCartAddSnackbarHostState
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.home.components.HomeBottomNavigation
import com.nativelap.eumshoppingmall.ui.home.components.HomeCategoryRow
import com.nativelap.eumshoppingmall.ui.home.components.HomeCompanyInfoFooter
import com.nativelap.eumshoppingmall.ui.home.components.HomeHeroCarousel
import com.nativelap.eumshoppingmall.ui.home.components.HomeNavigationRail
import com.nativelap.eumshoppingmall.ui.home.components.HomeProductCard
import com.nativelap.eumshoppingmall.ui.home.components.HomeQuoteBanner
import com.nativelap.eumshoppingmall.ui.home.components.HomeSearchBar
import com.nativelap.eumshoppingmall.ui.home.components.HomeSectionHeader
import com.nativelap.eumshoppingmall.ui.home.components.HomeSeeAllProductsButton
import com.nativelap.eumshoppingmall.ui.home.components.HomeServiceBand
import com.nativelap.eumshoppingmall.ui.home.components.HomeTopBar
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeRoute(
    categories: List<HomeCategoryUiModel> = emptyList(),
    recommendedProducts: List<HomeProductUiModel> = emptyList(),
    todayProducts: List<HomeProductUiModel> = emptyList(),
    bestSellerProducts: List<HomeProductUiModel> = emptyList(),
    companyIntro: HomeCompanyInfoUiModel = HomeCompanyInfoUiModel(),
    cartItemCount: Int = 0,
    selectedTab: MainNavigationTab = MainNavigationTab.Home,
    onSearch: (String) -> Unit = {},
    onCategoryClick: (HomeCategoryUiModel) -> Unit = {},
    onProductClick: (HomeProductUiModel) -> Unit = {},
    onHeroAction: (HomeHeroActionId) -> Unit = {},
    onQuoteClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSeeAllProductsClick: () -> Unit = {},
    onSeeRecommendedProductsClick: () -> Unit = {},
    onFavoriteClick: (HomeProductUiModel) -> Unit = {},
    isFavoriteAllowed: Boolean = true,
    onFavoriteBlocked: () -> Unit = {},
    onNavigationSelected: (MainNavigationTab) -> Unit = {},
    isLoggedIn: Boolean = false,
    onLoginRequired: () -> Unit = {},
    onQuickAddToCart: ((HomeProductUiModel) -> Unit)? = null,
    cartAddOutcome: CartAddOutcome? = null,
    onCartAddOutcomeChange: (CartAddOutcome?) -> Unit = {},
    onCartClick: () -> Unit = {},
    // 검색 화면과 공유하는 검색어. null이면 홈 자체 입력을 쓴다.
    query: String? = null,
    onQueryChange: (String) -> Unit = {},
    // 담기 버튼을 비로그인으로 눌렀을 때. 찜과 달리 장바구니 안내가 붙은 로그인으로 간다.
    onCartLoginRequired: () -> Unit = onLoginRequired,
) {
    var internalQuery by rememberSaveable { mutableStateOf("") }
    val currentQuery = effectiveHomeQuery(hoisted = query, internal = internalQuery)
    val snackbarHostState = rememberCartAddSnackbarHostState(cartAddOutcome, onCartAddOutcomeChange, onCartClick)
    var favoriteProductIds by rememberSaveable { mutableStateOf(emptySet<String>()) }
    // 베스트 탭 선택은 Route가 보관하고, null이면 전체 상품을 보여 준다.
    var selectedBestCategoryId by rememberSaveable { mutableStateOf<String?>(null) }
    HomeScreen(
        query = currentQuery,
        recommendedProducts = recommendedProducts,
        todayProducts = todayProducts,
        bestSellerProducts = bestSellerProducts.filterByBestCategory(selectedBestCategoryId),
        categories = categories,
        bestCategories = categories.withBestProducts(bestSellerProducts),
        showQuickCart = onQuickAddToCart != null,
        snackbarHostState = snackbarHostState,
        selectedBestCategoryId = selectedBestCategoryId,
        companyIntro = companyIntro,
        cartItemCount = cartItemCount,
        selectedNavigation = selectedTab,
        favoriteProductIds = favoriteProductIds,
        onEvent = { event ->
            when (event) {
                is HomeScreenEvent.QueryChanged -> {
                    if (query == null) internalQuery = event.query
                    onQueryChange(event.query)
                }

                HomeScreenEvent.SearchSubmitted -> onSearch(currentQuery)

                is HomeScreenEvent.CategoryClicked -> onCategoryClick(event.category)

                is HomeScreenEvent.BestCategorySelected -> selectedBestCategoryId = event.categoryId

                is HomeScreenEvent.QuickCartClicked -> when (decideQuickCart(isLoggedIn)) {
                    QuickCartDecision.Add -> onQuickAddToCart?.invoke(event.product)
                    QuickCartDecision.RequireLogin -> onCartLoginRequired()
                }

                is HomeScreenEvent.ProductClicked -> onProductClick(event.product)

                is HomeScreenEvent.HeroActionClicked -> onHeroAction(event.action)

                HomeScreenEvent.QuoteClicked -> onQuoteClick()

                HomeScreenEvent.MenuClicked -> onMenuClick()

                HomeScreenEvent.NotificationsClicked -> onNotificationClick()

                HomeScreenEvent.SeeAllProductsClicked -> onSeeAllProductsClick()

                HomeScreenEvent.SeeRecommendedProductsClicked -> onSeeRecommendedProductsClick()

                is HomeScreenEvent.FavoriteClicked -> if (!isFavoriteAllowed) {
                    onFavoriteBlocked()
                } else {
                    val product = event.product
                    favoriteProductIds = if (product.id in favoriteProductIds) {
                        favoriteProductIds - product.id
                    } else {
                        favoriteProductIds + product.id
                    }
                    onFavoriteClick(product)
                }

                is HomeScreenEvent.NavigationSelected -> onNavigationSelected(event.item)
            }
        },
    )
}

@Composable
private fun HomeScreen(
    query: String,
    recommendedProducts: List<HomeProductUiModel>,
    todayProducts: List<HomeProductUiModel>,
    bestSellerProducts: List<HomeProductUiModel>,
    categories: List<HomeCategoryUiModel>,
    bestCategories: List<HomeCategoryUiModel>,
    showQuickCart: Boolean,
    snackbarHostState: SnackbarHostState,
    selectedBestCategoryId: String?,
    companyIntro: HomeCompanyInfoUiModel,
    cartItemCount: Int,
    selectedNavigation: MainNavigationTab,
    favoriteProductIds: Set<String>,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    // 담기 버튼은 담기 동작을 넘긴 경우에만 카드에 나온다.
    val quickCartAction: (HomeProductUiModel) -> (() -> Unit)? = { product ->
        if (showQuickCart) {
            { onEvent(HomeScreenEvent.QuickCartClicked(product)) }
        } else {
            null
        }
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= 600.dp
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            snackbarHost = { EumSnackbarHost(hostState = snackbarHostState) },
            topBar = {
                if (!isExpanded) {
                    HomeTopBar(
                        onMenuClick = { onEvent(HomeScreenEvent.MenuClicked) },
                        onNotificationClick = { onEvent(HomeScreenEvent.NotificationsClicked) },
                        modifier = Modifier.statusBarsPadding(),
                    )
                }
            },
            bottomBar = {
                if (!isExpanded) {
                    HomeBottomNavigation(
                        selectedItem = selectedNavigation,
                        onItemSelected = { onEvent(HomeScreenEvent.NavigationSelected(it)) },
                        cartItemCount = cartItemCount,
                    )
                }
            },
            containerColor = MaterialTheme.eumColors.background,
        ) { contentPadding ->
            Row(Modifier.fillMaxSize().padding(contentPadding)) {
                if (isExpanded) {
                    HomeNavigationRail(
                        selectedItem = selectedNavigation,
                        onItemSelected = { onEvent(HomeScreenEvent.NavigationSelected(it)) },
                        cartItemCount = cartItemCount,
                    )
                }
                Column(Modifier.weight(1f).fillMaxSize()) {
                    if (isExpanded) {
                        HomeTopBar(
                            onMenuClick = {},
                            onNotificationClick = { onEvent(HomeScreenEvent.NotificationsClicked) },
                            modifier = Modifier.statusBarsPadding(),
                            showMenu = false,
                            horizontalPadding = EumSpacing.x20,
                        )
                    }
                    BoxWithConstraints(
                        Modifier.weight(1f).fillMaxWidth().then(
                            if (isExpanded) Modifier.navigationBarsPadding() else Modifier,
                        ),
                    ) {
                        val maxContentWidth = if (maxWidth >= 1080.dp) 1080.dp else 680.dp
                        val contentWidth = maxWidth.coerceAtMost(maxContentWidth)
                        val columnCount = ((contentWidth - EumSpacing.x40 + EumSpacing.x12) / 181.dp)
                            .toInt().coerceIn(2, 4)
                        // 베스트 순위는 탭으로 거른 뒤 보이는 순서대로 매긴다.
                        val productRows = bestSellerProducts.withSectionRanks().chunked(columnCount)
                        val bottomListPadding = if (LocalDensity.current.fontScale >= 1.5f) 96.dp else EumSpacing.x24
                        LazyColumn(
                            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()
                                .widthIn(max = maxContentWidth).fillMaxHeight(),
                            contentPadding = PaddingValues(top = EumSpacing.x8, bottom = bottomListPadding),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            item {
                                HomeSearchBar(
                                    query = query,
                                    onQueryChange = { onEvent(HomeScreenEvent.QueryChanged(it)) },
                                    onSearch = { onEvent(HomeScreenEvent.SearchSubmitted) },
                                    modifier = Modifier.padding(horizontal = EumSpacing.x20),
                                )
                            }
                            item { Spacer(Modifier.height(EumSpacing.x16)) }
                            item {
                                HomeHeroCarousel(
                                    onActionClick = {
                                        onEvent(HomeScreenEvent.HeroActionClicked(it))
                                    },
                                    onQuoteClick = { onEvent(heroQuoteCtaEvent()) },
                                )
                            }
                            item { Spacer(Modifier.height(EumSpacing.x24)) }
                            if (categories.isNotEmpty()) {
                                item(key = "home-categories") {
                                    HomeCategoryRow(
                                        categories = categories,
                                        onCategoryClick = { onEvent(HomeScreenEvent.CategoryClicked(it)) },
                                    )
                                }
                                item { Spacer(Modifier.height(EumSpacing.x24)) }
                            }
                            item(key = "home-service-band") {
                                HomeServiceBand(
                                    freeShippingThresholdLabel = stringResource(R.string.home_free_shipping_threshold),
                                    onQuoteClick = { onEvent(HomeScreenEvent.QuoteClicked) },
                                    modifier = Modifier.padding(horizontal = EumSpacing.x20),
                                )
                            }
                            item { Spacer(Modifier.height(EumSpacing.x40)) }
                            item(key = "home-recommended-products") {
                                HomeProductSection(
                                    title = stringResource(R.string.home_recommended_products),
                                    products = recommendedProducts,
                                    quickCartAction = quickCartAction,
                                    favoriteProductIds = favoriteProductIds,
                                    onProductClick = { onEvent(HomeScreenEvent.ProductClicked(it)) },
                                    onFavoriteClick = { onEvent(HomeScreenEvent.FavoriteClicked(it)) },
                                    onSeeAllClick = { onEvent(HomeScreenEvent.SeeRecommendedProductsClicked) },
                                )
                            }
                            item { Spacer(Modifier.height(EumSpacing.x40)) }
                            if (todayProducts.isNotEmpty()) {
                                item(key = "home-today-products") {
                                    HomeProductSection(
                                        title = stringResource(R.string.home_today_products),
                                        products = todayProducts,
                                        quickCartAction = quickCartAction,
                                        favoriteProductIds = favoriteProductIds,
                                        onProductClick = { onEvent(HomeScreenEvent.ProductClicked(it)) },
                                        onFavoriteClick = { onEvent(HomeScreenEvent.FavoriteClicked(it)) },
                                        onSeeAllClick = { onEvent(HomeScreenEvent.SeeAllProductsClicked) },
                                    )
                                }
                                item { Spacer(Modifier.height(EumSpacing.x40)) }
                            }
                            item {
                                HomeSectionHeader(
                                    title = stringResource(R.string.home_best_sellers),
                                    modifier = Modifier.padding(horizontal = EumSpacing.x20),
                                    onSeeAllClick = { onEvent(HomeScreenEvent.SeeAllProductsClicked) },
                                )
                            }
                            item(key = "home-best-category-tabs") {
                                HomeBestCategoryTabs(
                                    categories = bestCategories,
                                    selectedCategoryId = selectedBestCategoryId,
                                    onCategorySelected = { onEvent(HomeScreenEvent.BestCategorySelected(it)) },
                                )
                            }
                            item { Spacer(Modifier.height(EumSpacing.x16)) }
                            if (bestSellerProducts.isEmpty()) {
                                item { HomeEmptyProductMessage() }
                            } else {
                                items(productRows.size, key = { index -> productRows[index].joinToString { it.id } }) { rowIndex ->
                                    val rowProducts = productRows[rowIndex]
                                    val gap = EumSpacing.x12
                                    val itemWidth = (contentWidth - EumSpacing.x40 - gap * (columnCount - 1)) / columnCount
                                    // 같은 행의 카드는 가장 높은 카드에 맞춘다.
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20).height(IntrinsicSize.Max),
                                        horizontalArrangement = Arrangement.spacedBy(gap),
                                    ) {
                                        rowProducts.forEach { product ->
                                            HomeProductCard(
                                                product = product,
                                                isFavorite = product.id in favoriteProductIds,
                                                onClick = { onEvent(HomeScreenEvent.ProductClicked(it)) },
                                                onFavoriteClick = { onEvent(HomeScreenEvent.FavoriteClicked(it)) },
                                                modifier = Modifier.width(itemWidth).fillMaxHeight(),
                                                onQuickCartClick = quickCartAction(product),
                                            )
                                        }
                                        repeat(columnCount - rowProducts.size) { Spacer(Modifier.width(itemWidth)) }
                                    }
                                    if (rowIndex < productRows.lastIndex) {
                                        Spacer(Modifier.height(EumSpacing.x24))
                                    }
                                }
                            }
                            item { Spacer(Modifier.height(EumSpacing.x16)) }
                            item {
                                HomeSeeAllProductsButton(
                                    { onEvent(HomeScreenEvent.SeeAllProductsClicked) },
                                    Modifier.padding(horizontal = EumSpacing.x20),
                                )
                            }
                            item { Spacer(Modifier.height(EumSpacing.x40)) }
                            item {
                                HomeQuoteBanner(
                                    onClick = { onEvent(HomeScreenEvent.QuoteClicked) },
                                    modifier = Modifier.padding(horizontal = EumSpacing.x20),
                                )
                            }
                            // 사업자 정보 값이 하나도 없으면 푸터 섹션 전체를 그리지 않는다.
                            val companyInfoRows = companyIntro.visibleInfoRows()
                            if (companyInfoRows.isNotEmpty()) {
                                item { Spacer(Modifier.height(EumSpacing.x40)) }
                                item(key = "home-company-info") {
                                    HomeCompanyInfoFooter(rows = companyInfoRows)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeProductSection(
    title: String,
    products: List<HomeProductUiModel>,
    favoriteProductIds: Set<String>,
    onProductClick: (HomeProductUiModel) -> Unit,
    onFavoriteClick: (HomeProductUiModel) -> Unit,
    onSeeAllClick: () -> Unit,
    quickCartAction: (HomeProductUiModel) -> (() -> Unit)?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
        HomeSectionHeader(
            title = title,
            modifier = Modifier.padding(horizontal = EumSpacing.x20),
            onSeeAllClick = onSeeAllClick,
        )
        if (products.isEmpty()) {
            HomeEmptyProductMessage()
        } else {
            androidx.compose.foundation.lazy.LazyRow(
                contentPadding = PaddingValues(horizontal = EumSpacing.x20),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
            ) {
                items(products.size, key = { products[it].id }) { index ->
                    HomeProductCard(
                        product = products[index],
                        isFavorite = products[index].id in favoriteProductIds,
                        onClick = onProductClick,
                        onFavoriteClick = onFavoriteClick,
                        modifier = Modifier.width(148.dp).heightIn(min = 248.dp),
                        onQuickCartClick = quickCartAction(products[index]),
                    )
                }
            }
        }
    }
}

// 베스트 셀러 카테고리 탭. 선택 상태는 Route가 가지며, 여기서는 이벤트만 올린다.
@Composable
private fun HomeBestCategoryTabs(
    categories: List<HomeCategoryUiModel>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = EumSpacing.x20),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        item(key = "best-tab-all") {
            CatalogSelectionChip(
                label = stringResource(R.string.home_best_category_all),
                selected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) },
            )
        }
        items(categories, key = { category -> category.id }) { category ->
            CatalogSelectionChip(
                label = stringResource(category.labelResId),
                selected = category.id == selectedCategoryId,
                onClick = { onCategorySelected(category.id) },
            )
        }
    }
}

@Composable
private fun HomeEmptyProductMessage() {
    Text(
        text = stringResource(R.string.home_products_empty),
        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
        color = MaterialTheme.eumColors.textTertiary,
    )
}

package com.nativelap.eumshoppingmall.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.nativelap.eumshoppingmall.ui.home.components.HomeBottomNavigation
import com.nativelap.eumshoppingmall.ui.home.components.HomeHeroCarousel
import com.nativelap.eumshoppingmall.ui.home.components.HomeNavigationRail
import com.nativelap.eumshoppingmall.ui.home.components.HomeProductCard
import com.nativelap.eumshoppingmall.ui.home.components.HomeQuoteBanner
import com.nativelap.eumshoppingmall.ui.home.components.HomeSearchBar
import com.nativelap.eumshoppingmall.ui.home.components.HomeSectionHeader
import com.nativelap.eumshoppingmall.ui.home.components.HomeSeeAllProductsButton
import com.nativelap.eumshoppingmall.ui.home.components.HomeTopBar
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeRoute(
    recommendedProducts: List<HomeProductUiModel> = emptyList(),
    bestSellerProducts: List<HomeProductUiModel> = emptyList(),
    cartItemCount: Int = 0,
    onSearch: (String) -> Unit = {},
    onCategoryClick: (HomeCategoryUiModel) -> Unit = {},
    onProductClick: (HomeProductUiModel) -> Unit = {},
    onHeroAction: (Int) -> Unit = {},
    onQuoteClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSeeAllProductsClick: () -> Unit = {},
    onSeeRecommendedProductsClick: () -> Unit = {},
    onFavoriteClick: (HomeProductUiModel) -> Unit = {},
    onNavigationSelected: (HomeNavigationItem) -> Unit = {},
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedNavigation by rememberSaveable { mutableStateOf(HomeNavigationItem.Home) }
    var favoriteProductIds by rememberSaveable { mutableStateOf(emptySet<String>()) }
    HomeScreen(
        query = query,
        recommendedProducts = recommendedProducts,
        bestSellerProducts = bestSellerProducts,
        cartItemCount = cartItemCount,
        selectedNavigation = selectedNavigation,
        favoriteProductIds = favoriteProductIds,
        onEvent = { event ->
            when (event) {
                is HomeScreenEvent.QueryChanged -> query = event.query

                HomeScreenEvent.SearchSubmitted -> onSearch(query)

                is HomeScreenEvent.CategoryClicked -> onCategoryClick(event.category)

                is HomeScreenEvent.ProductClicked -> onProductClick(event.product)

                is HomeScreenEvent.HeroActionClicked -> onHeroAction(event.slideIndex)

                HomeScreenEvent.QuoteClicked -> onQuoteClick()

                HomeScreenEvent.MenuClicked -> onMenuClick()

                HomeScreenEvent.NotificationsClicked -> onNotificationClick()

                HomeScreenEvent.SeeAllProductsClicked -> onSeeAllProductsClick()

                HomeScreenEvent.SeeRecommendedProductsClicked -> onSeeRecommendedProductsClick()

                is HomeScreenEvent.FavoriteClicked -> {
                    val product = event.product
                    favoriteProductIds = if (product.id in favoriteProductIds) {
                        favoriteProductIds - product.id
                    } else {
                        favoriteProductIds + product.id
                    }
                    onFavoriteClick(product)
                }

                is HomeScreenEvent.NavigationSelected -> {
                    selectedNavigation = event.item
                    onNavigationSelected(event.item)
                }
            }
        },
    )
}

@Composable
private fun HomeScreen(
    query: String,
    recommendedProducts: List<HomeProductUiModel>,
    bestSellerProducts: List<HomeProductUiModel>,
    cartItemCount: Int,
    selectedNavigation: HomeNavigationItem,
    favoriteProductIds: Set<String>,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= 600.dp
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
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
                        val productRows = bestSellerProducts.chunked(columnCount)
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
                                HomeHeroCarousel(onActionClick = {
                                    onEvent(HomeScreenEvent.HeroActionClicked(it))
                                })
                            }
                            item { Spacer(Modifier.height(EumSpacing.x40)) }
                            item {
                                HomeProductSection(
                                    title = stringResource(R.string.home_recommended_products),
                                    products = recommendedProducts,
                                    favoriteProductIds = favoriteProductIds,
                                    onProductClick = { onEvent(HomeScreenEvent.ProductClicked(it)) },
                                    onFavoriteClick = { onEvent(HomeScreenEvent.FavoriteClicked(it)) },
                                    onSeeAllClick = { onEvent(HomeScreenEvent.SeeRecommendedProductsClicked) },
                                )
                            }
                            item { Spacer(Modifier.height(EumSpacing.x40)) }
                            item {
                                HomeSectionHeader(
                                    title = stringResource(R.string.home_best_sellers),
                                    modifier = Modifier.padding(horizontal = EumSpacing.x20),
                                    onSeeAllClick = { onEvent(HomeScreenEvent.SeeAllProductsClicked) },
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20),
                                        horizontalArrangement = Arrangement.spacedBy(gap),
                                    ) {
                                        rowProducts.forEach { product ->
                                            HomeProductCard(
                                                product = product,
                                                isFavorite = product.id in favoriteProductIds,
                                                onClick = { onEvent(HomeScreenEvent.ProductClicked(it)) },
                                                onFavoriteClick = { onEvent(HomeScreenEvent.FavoriteClicked(it)) },
                                                modifier = Modifier.width(itemWidth),
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
                    )
                }
            }
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

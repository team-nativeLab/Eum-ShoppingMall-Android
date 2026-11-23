package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.common.rememberFavoriteToggles

@Composable
fun CatalogProductListRoute(
    uiModel: CatalogProductListUiModel,
    selectedTab: MainNavigationTab,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
    // null이면 카드에 담기 버튼을 그리지 않는다(쿼트 선택 화면). 담기 동작을 넘긴 목록에서만 버튼이 보인다.
    onQuickAddToCart: ((CatalogProductUiModel) -> Unit)? = null,
    cartAddOutcome: CartAddOutcome? = null,
    onCartAddOutcomeChange: (CartAddOutcome?) -> Unit = {},
    onCartClick: () -> Unit = {},
    // 견적 상품 선택 화면이면 제목·안내를 바꾸고 찜 하트를 숨긴다.
    isQuotePicker: Boolean = false,
    onCartLoginRequired: () -> Unit = onLoginRequired,
) {
    var selectedCategoryId by rememberSaveable { mutableStateOf(uiModel.selectedCategoryId) }
    var isListLayout by rememberSaveable { mutableStateOf(uiModel.isListLayout) }
    val snackbarHostState = rememberCartAddSnackbarHostState(cartAddOutcome, onCartAddOutcomeChange, onCartClick)
    val listTitle = uiModel.titleResId?.let { titleResId -> stringResource(titleResId) } ?: uiModel.title
    val screenTitle = if (isQuotePicker) stringResource(R.string.quote_product_picker_title) else listTitle
    val filterState = rememberProductFilterState()
    val favoriteToggles = rememberFavoriteToggles(
        initialFavoriteIds = uiModel.products.filter { product -> product.isFavorite }.map { product -> product.id }.toSet(),
        isLoggedIn = isLoggedIn,
        onLoginRequired = onLoginRequired,
    )
    // 칩 라벨은 표시용이고, 선택은 id로 비교한다. 필터 시트의 라벨 목록은 여기서 id와 맞춘다.
    val categoryLabels = uiModel.categoryChips.map { chip -> chip.label }
    val selectedCategoryLabel = uiModel.categoryChips.firstOrNull { chip -> chip.id == selectedCategoryId }?.label
    val filteredProducts = filterState.apply(
        filterCatalogProductsBySubcategory(uiModel.products, selectedCategoryId),
    )
    // 결과 수는 필터 후 전체 개수이고, 목록에는 현재 페이지 분량만 보여 준다.
    val shownProducts = filterState.visibleProducts(filteredProducts).map { product ->
        product.copy(isFavorite = favoriteToggles.isFavorite(product.id))
    }
    ProductFilterHost(
        state = filterState,
        categories = categoryLabels,
        selectedCategory = selectedCategoryLabel,
        onCategorySelected = { label ->
            uiModel.categoryChips.firstOrNull { chip -> chip.label == label }?.let { chip ->
                selectedCategoryId = chip.id
            }
        },
        sourceProducts = uiModel.products,
    ) {
        CatalogProductListScreen(
            uiModel = uiModel.copy(
                title = screenTitle,
                selectedCategoryId = selectedCategoryId,
                resultCountLabel = stringResource(R.string.catalog_product_result_count, filteredProducts.size),
                isListLayout = isListLayout,
                sortLabel = productSortLabel(filterState.criteria.sort, uiModel.sortLabel),
                products = shownProducts,
            ),
            hasMoreProducts = filteredProducts.size > shownProducts.size,
            selectedBottomItem = selectedTab,
            onBottomItemSelected = onTabSelected,
            cartItemCount = cartItemCount,
            snackbarHostState = snackbarHostState,
            showQuickCart = onQuickAddToCart != null,
            guidanceText = if (isQuotePicker) stringResource(R.string.quote_product_picker_guidance) else null,
            showFavorite = showsFavoriteToggle(isQuotePicker),
            showBlockReason = showsBlockReasonChip(isQuotePicker),
            onEvent = { event ->
                when (event) {
                    CatalogProductListEvent.BackClicked -> onBackClick()

                    CatalogProductListEvent.SearchClicked -> onSearchClick()

                    is CatalogProductListEvent.CategorySelected -> {
                        selectedCategoryId = event.categoryId
                        filterState.resetVisibleCount()
                    }

                    CatalogProductListEvent.LoadMoreClicked -> filterState.loadMore()

                    CatalogProductListEvent.LayoutToggled -> isListLayout = !isListLayout

                    is CatalogProductListEvent.ProductSelected -> onProductClick(event.productId)

                    // 정렬과 필터는 같은 A-48 화면에서 고른다.
                    CatalogProductListEvent.SortClicked,
                    CatalogProductListEvent.FilterClicked,
                    -> filterState.open()

                    is CatalogProductListEvent.FavoriteToggled -> favoriteToggles.toggle(event.productId)

                    is CatalogProductListEvent.QuickCartClicked -> {
                        if (isLoggedIn) {
                            uiModel.products.firstOrNull { product -> product.id == event.productId }?.let { product ->
                                onQuickAddToCart?.invoke(product)
                            }
                        } else {
                            onCartLoginRequired()
                        }
                    }
                }
            },
        )
    }
}

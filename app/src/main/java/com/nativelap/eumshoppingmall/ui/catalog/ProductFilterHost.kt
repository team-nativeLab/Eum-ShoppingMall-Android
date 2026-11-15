package com.nativelap.eumshoppingmall.ui.catalog

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceRange
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterScreen
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterScreenEvent
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterSort
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterUiModel

private const val PRICE_LOWER_BOUND_WON = 10_000L
private const val PRICE_UPPER_BOUND_WON = 30_000L

/**
 * 목록 Route가 소유하는 필터·정렬 상태(A-48). 별도 NavKey 없이 목록 화면 안에서 연다.
 * 값은 enum과 Boolean뿐이라 rememberSaveable로 회전·복원을 견딘다.
 */
@Stable
class ProductFilterState(
    private val isVisibleState: MutableState<Boolean>,
    private val priceRangeState: MutableState<ProductFilterPriceRange?>,
    private val sortState: MutableState<ProductFilterSort>,
) {
    val isVisible: Boolean
        get() = isVisibleState.value
    val priceRange: ProductFilterPriceRange?
        get() = priceRangeState.value
    val sort: ProductFilterSort
        get() = sortState.value

    fun open() {
        isVisibleState.value = true
    }

    fun close() {
        isVisibleState.value = false
    }

    fun selectPriceRange(range: ProductFilterPriceRange) {
        // 같은 범위를 다시 누르면 필터를 해제한다.
        priceRangeState.value = if (priceRangeState.value == range) null else range
    }

    fun selectSort(sort: ProductFilterSort) {
        sortState.value = sort
    }

    fun apply(products: List<CatalogProductUiModel>): List<CatalogProductUiModel> = applyProductFilter(products, priceRange, sort)
}

@Composable
fun rememberProductFilterState(): ProductFilterState {
    val isVisibleState = rememberSaveable { mutableStateOf(false) }
    val priceRangeState = rememberSaveable { mutableStateOf<ProductFilterPriceRange?>(null) }
    val sortState = rememberSaveable { mutableStateOf(ProductFilterSort.Recommended) }
    return remember(isVisibleState, priceRangeState, sortState) {
        ProductFilterState(isVisibleState, priceRangeState, sortState)
    }
}

/**
 * 필터가 열려 있으면 필터 화면을, 아니면 [content]를 그린다. 선택은 바로 반영되고 뒤로가기·적용은 화면만 닫는다.
 */
@Composable
fun ProductFilterHost(
    state: ProductFilterState,
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    if (!state.isVisible) {
        content()
        return
    }
    BackHandler(onBack = state::close)
    ProductFilterScreen(
        uiModel = ProductFilterUiModel(
            categories = categories,
            selectedCategory = selectedCategory,
            selectedPriceRange = state.priceRange,
            selectedSort = state.sort,
        ),
        onEvent = { event ->
            when (event) {
                ProductFilterScreenEvent.BackClicked,
                ProductFilterScreenEvent.ApplyClicked,
                -> state.close()

                is ProductFilterScreenEvent.CategorySelected -> onCategorySelected(event.category)

                is ProductFilterScreenEvent.PriceRangeSelected -> state.selectPriceRange(event.range)

                is ProductFilterScreenEvent.SortSelected -> state.selectSort(event.sort)
            }
        },
    )
}

/** 사용자가 정렬을 바꿨을 때 목록 헤더에 보여 줄 정렬 이름. 기본 정렬이면 원래 라벨을 쓴다. */
@Composable
fun productSortLabel(
    sort: ProductFilterSort,
    defaultLabel: String,
): String = when (sort) {
    ProductFilterSort.Recommended -> defaultLabel
    ProductFilterSort.PriceAscending -> stringResource(R.string.ui_audit_sort_price_ascending)
    ProductFilterSort.PriceDescending -> stringResource(R.string.ui_audit_sort_price_descending)
    ProductFilterSort.Latest -> stringResource(R.string.ui_audit_sort_latest)
}

/**
 * 가격 범위와 정렬을 목록에 적용한다. 가격 문의 상품(금액 없음)은 가격 범위를 고르면 제외한다.
 * 추천·최신순은 데이터에 순서 근거가 없어 원래 순서를 유지한다.
 */
internal fun applyProductFilter(
    products: List<CatalogProductUiModel>,
    priceRange: ProductFilterPriceRange?,
    sort: ProductFilterSort,
): List<CatalogProductUiModel> {
    val filteredProducts = if (priceRange == null) {
        products
    } else {
        products.filter { product ->
            val priceWon = product.unitPriceWon() ?: return@filter false
            when (priceRange) {
                ProductFilterPriceRange.UnderTenThousand -> priceWon < PRICE_LOWER_BOUND_WON
                ProductFilterPriceRange.TenToThirtyThousand -> priceWon in PRICE_LOWER_BOUND_WON until PRICE_UPPER_BOUND_WON
                ProductFilterPriceRange.OverThirtyThousand -> priceWon >= PRICE_UPPER_BOUND_WON
            }
        }
    }
    // 가격 문의 상품은 가격순 정렬에서 방향과 상관없이 맨 뒤에 둔다.
    val (pricedProducts, quoteRequiredProducts) = filteredProducts.partition { product -> product.unitPriceWon() != null }
    return when (sort) {
        ProductFilterSort.PriceAscending -> pricedProducts.sortedBy { product -> product.unitPriceWon() } + quoteRequiredProducts

        ProductFilterSort.PriceDescending -> pricedProducts.sortedByDescending { product -> product.unitPriceWon() } + quoteRequiredProducts

        ProductFilterSort.Recommended,
        ProductFilterSort.Latest,
        -> filteredProducts
    }
}

private fun CatalogProductUiModel.unitPriceWon(): Long? = (price as? CatalogPriceUiModel.Available)?.unitPriceWon

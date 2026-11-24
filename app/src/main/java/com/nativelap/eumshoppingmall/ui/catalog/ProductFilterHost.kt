package com.nativelap.eumshoppingmall.ui.catalog

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPageSize
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceBound
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceRange
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterScreen
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterScreenEvent
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterSort
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterUiModel

// 직접 입력 가격은 이 자릿수까지만 받는다(999,999만원).
private const val MAX_PRICE_DIGITS = 6

/**
 * 목록 Route가 소유하는 필터·정렬·페이지 상태(A-48). 별도 NavKey 없이 목록 화면 안에서 연다.
 * 적용된 값은 [ProductFilterCriteria]로, 입력 중인 가격은 draft 문자열로 둔다. 모두 rememberSaveable로 회전·복원을 견딘다.
 */
@Stable
internal class ProductFilterState(
    private val isVisibleState: MutableState<Boolean>,
    private val criteriaState: MutableState<ProductFilterCriteria>,
    private val minPriceDraftState: MutableState<String>,
    private val maxPriceDraftState: MutableState<String>,
    private val pageSizeState: MutableState<ProductFilterPageSize>,
    private val visibleCountState: MutableState<Int>,
) {
    val isVisible: Boolean
        get() = isVisibleState.value
    val criteria: ProductFilterCriteria
        get() = criteriaState.value
    val minPriceDraft: String
        get() = minPriceDraftState.value
    val maxPriceDraft: String
        get() = maxPriceDraftState.value
    val pageSize: ProductFilterPageSize
        get() = pageSizeState.value
    val visibleCount: Int
        get() = visibleCountState.value

    fun open() {
        isVisibleState.value = true
    }

    fun close() {
        isVisibleState.value = false
    }

    fun selectPriceRange(range: ProductFilterPriceRange) {
        // 같은 범위를 다시 누르면 필터를 해제한다. 칩을 고르면 직접 입력 가격은 지운다(마지막 선택 우선).
        val nextRange = if (criteria.priceRange == range) null else range
        updateCriteria(criteria.copy(priceRange = nextRange, minPriceManwon = null, maxPriceManwon = null))
        minPriceDraftState.value = ""
        maxPriceDraftState.value = ""
    }

    fun changePriceDraft(
        bound: ProductFilterPriceBound,
        text: String,
    ) {
        val digitsOnly = text.filter { char -> char.isDigit() }.take(MAX_PRICE_DIGITS)
        when (bound) {
            ProductFilterPriceBound.Min -> minPriceDraftState.value = digitsOnly
            ProductFilterPriceBound.Max -> maxPriceDraftState.value = digitsOnly
        }
    }

    fun applyPriceDraft() {
        val minManwon = minPriceDraft.toIntOrNull()
        val maxManwon = maxPriceDraft.toIntOrNull()
        // 최소가 최대보다 크면 두 값을 바꿔 적용한다.
        val (appliedMin, appliedMax) = if (minManwon != null && maxManwon != null && minManwon > maxManwon) {
            maxManwon to minManwon
        } else {
            minManwon to maxManwon
        }
        // 직접 입력을 적용하면 가격 칩 선택은 해제한다(마지막 선택 우선).
        updateCriteria(criteria.copy(priceRange = null, minPriceManwon = appliedMin, maxPriceManwon = appliedMax))
        minPriceDraftState.value = appliedMin?.toString().orEmpty()
        maxPriceDraftState.value = appliedMax?.toString().orEmpty()
    }

    /** 직접 입력으로 적용된 가격 조건을 해제한다. 적용 조건과 입력 칸(draft)을 함께 비운다. */
    fun clearDirectPrice() {
        updateCriteria(criteria.copy(minPriceManwon = null, maxPriceManwon = null))
        minPriceDraftState.value = ""
        maxPriceDraftState.value = ""
    }

    /** 필터 적용 버튼: 직접 입력 칸이 하나라도 채워져 있을 때만 가격 draft를 반영한다. 두 칸이 모두 비어 있으면 아무 것도 바꾸지 않는다. */
    fun applyPriceDraftIfEntered() {
        if (minPriceDraft.isEmpty() && maxPriceDraft.isEmpty()) return
        applyPriceDraft()
    }

    fun toggleBrand(brandId: String) {
        updateCriteria(criteria.copy(brandIds = criteria.brandIds.toggled(brandId)))
    }

    fun toggleSafetyCertification(certification: SafetyCertificationType) {
        updateCriteria(criteria.copy(safetyCertifications = criteria.safetyCertifications.toggled(certification)))
    }

    fun changeSoldOutHidden(hidden: Boolean) {
        updateCriteria(criteria.copy(hideSoldOut = hidden))
    }

    fun selectSort(sort: ProductFilterSort) {
        updateCriteria(criteria.copy(sort = sort))
    }

    fun selectPageSize(pageSize: ProductFilterPageSize) {
        pageSizeState.value = pageSize
        visibleCountState.value = pageSize.count
    }

    /** "더 보기"를 누르면 페이지 크기만큼 더 보여 준다. */
    fun loadMore() {
        visibleCountState.value = visibleCount + pageSize.count
    }

    /** 카테고리·필터가 바뀌면 첫 페이지로 되돌린다. */
    fun resetVisibleCount() {
        visibleCountState.value = pageSize.count
    }

    /** 현재 상품 목록에 없는 브랜드는 선택에서 뺀다(검색어가 바뀐 경우 등). 바뀔 때만 목록을 첫 페이지로 되돌린다. */
    fun keepBrandsIfAvailable(availableBrandIds: List<String>) {
        val keptBrandIds = criteria.brandIds.intersect(availableBrandIds.toSet())
        if (keptBrandIds != criteria.brandIds) updateCriteria(criteria.copy(brandIds = keptBrandIds))
    }

    fun apply(products: List<CatalogProductUiModel>): List<CatalogProductUiModel> = applyProductFilter(products, criteria)

    /** 필터 후 목록에서 현재 페이지 분량만 돌려준다. 결과 수는 이 함수 전의 개수를 쓴다. */
    fun visibleProducts(filteredProducts: List<CatalogProductUiModel>): List<CatalogProductUiModel> = filteredProducts.take(visibleCount)

    /** 적용된 필터와 입력 중인 가격을 모두 기본값으로 되돌리고 첫 페이지로 돌아간다. 정렬도 기본(추천순)으로 돌아간다. */
    fun reset() {
        minPriceDraftState.value = ""
        maxPriceDraftState.value = ""
        updateCriteria(ProductFilterCriteria())
    }

    private fun updateCriteria(nextCriteria: ProductFilterCriteria) {
        criteriaState.value = nextCriteria
        resetVisibleCount()
    }
}

// 집합에서 항목이 있으면 빼고 없으면 넣는다. 여러 개를 독립적으로 고르는 체크박스 조건에 쓴다.
private fun <T> Set<T>.toggled(item: T): Set<T> = if (item in this) this - item else this + item

@Composable
internal fun rememberProductFilterState(): ProductFilterState {
    val isVisibleState = rememberSaveable { mutableStateOf(false) }
    val criteriaState = rememberSaveable(stateSaver = ProductFilterCriteriaSaver) { mutableStateOf(ProductFilterCriteria()) }
    val minPriceDraftState = rememberSaveable { mutableStateOf("") }
    val maxPriceDraftState = rememberSaveable { mutableStateOf("") }
    val pageSizeState = rememberSaveable { mutableStateOf(ProductFilterPageSize.TwentyFour) }
    val visibleCountState = rememberSaveable { mutableStateOf(ProductFilterPageSize.TwentyFour.count) }
    return remember(
        isVisibleState,
        criteriaState,
        minPriceDraftState,
        maxPriceDraftState,
        pageSizeState,
        visibleCountState,
    ) {
        ProductFilterState(
            isVisibleState = isVisibleState,
            criteriaState = criteriaState,
            minPriceDraftState = minPriceDraftState,
            maxPriceDraftState = maxPriceDraftState,
            pageSizeState = pageSizeState,
            visibleCountState = visibleCountState,
        )
    }
}

/**
 * 필터가 열려 있으면 필터 화면을, 아니면 [content]를 그린다. 브랜드·인증·품절·칩은 바로 반영되고,
 * 직접 입력 가격은 적용 버튼 또는 하단 필터 적용 버튼으로 반영한다. 뒤로가기는 화면만 닫고 입력 중인 가격은 반영하지 않는다.
 * 필터 옵션은 [sourceProducts]에서 만든다.
 */
@Composable
internal fun ProductFilterHost(
    state: ProductFilterState,
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    sourceProducts: List<CatalogProductUiModel>,
    content: @Composable () -> Unit,
) {
    val options = productFilterOptions(sourceProducts)
    val brandIds = options.brands.map { brand -> brand.id }
    LaunchedEffect(brandIds) { state.keepBrandsIfAvailable(brandIds) }
    if (!state.isVisible) {
        content()
        return
    }
    BackHandler(onBack = state::close)
    ProductFilterScreen(
        uiModel = ProductFilterUiModel(
            categories = categories,
            selectedCategory = selectedCategory,
            selectedPriceRange = state.criteria.priceRange,
            selectedSort = state.criteria.sort,
            hasPricedProducts = options.hasPricedProducts,
            minPriceDraft = state.minPriceDraft,
            maxPriceDraft = state.maxPriceDraft,
            brands = options.brands,
            selectedBrandIds = state.criteria.brandIds,
            safetyCertificationOptions = options.safetyCertificationOptions,
            selectedSafetyCertifications = state.criteria.safetyCertifications,
            showSoldOutToggle = options.hasSoldOutProducts,
            isSoldOutHidden = state.criteria.hideSoldOut,
            selectedPageSize = state.pageSize,
            appliedMinPriceManwon = state.criteria.minPriceManwon,
            appliedMaxPriceManwon = state.criteria.maxPriceManwon,
        ),
        onEvent = { event ->
            when (event) {
                ProductFilterScreenEvent.BackClicked -> state.close()

                ProductFilterScreenEvent.ApplyClicked -> {
                    // 필터 적용은 입력 중인 가격 draft도 함께 반영한다. 가격 섹션이 없으면 draft를 적용하지 않는다.
                    if (options.hasPricedProducts) state.applyPriceDraftIfEntered()
                    state.close()
                }

                is ProductFilterScreenEvent.CategorySelected -> {
                    onCategorySelected(event.category)
                    state.resetVisibleCount()
                }

                is ProductFilterScreenEvent.PriceRangeSelected -> state.selectPriceRange(event.range)

                is ProductFilterScreenEvent.PriceDraftChanged -> state.changePriceDraft(event.bound, event.text)

                ProductFilterScreenEvent.PriceApplyClicked -> state.applyPriceDraft()

                ProductFilterScreenEvent.PriceDirectInputClearClicked -> state.clearDirectPrice()

                is ProductFilterScreenEvent.BrandToggled -> state.toggleBrand(event.brandId)

                is ProductFilterScreenEvent.SafetyCertificationToggled -> state.toggleSafetyCertification(event.certification)

                is ProductFilterScreenEvent.SoldOutHiddenChanged -> state.changeSoldOutHidden(event.checked)

                is ProductFilterScreenEvent.SortSelected -> state.selectSort(event.sort)

                is ProductFilterScreenEvent.PageSizeSelected -> state.selectPageSize(event.pageSize)
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
    ProductFilterSort.Latest -> stringResource(R.string.ui_audit_sort_latest)
    ProductFilterSort.Rating -> stringResource(R.string.ui_audit_sort_rating)
    ProductFilterSort.PriceAscending -> stringResource(R.string.ui_audit_sort_price_ascending)
    ProductFilterSort.PriceDescending -> stringResource(R.string.ui_audit_sort_price_descending)
}

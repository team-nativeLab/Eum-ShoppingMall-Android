package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPageSize
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceBound
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceRange
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterSort
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductFilterStateTest {
    @Test
    fun pageSizeLimitsVisibleProductsAndLoadMoreAddsOnePage() {
        val state = newState()
        val products = (1..30).map { index -> product("p$index", 1_000L) }

        state.selectPageSize(ProductFilterPageSize.Twelve)
        assertEquals(12, state.visibleProducts(state.apply(products)).size)

        state.loadMore()
        assertEquals(24, state.visibleProducts(state.apply(products)).size)

        state.selectPageSize(ProductFilterPageSize.FortyEight)
        assertEquals(30, state.visibleProducts(state.apply(products)).size)
    }

    @Test
    fun changingSortResetsVisibleCountToOnePage() {
        val state = newState()
        state.selectPageSize(ProductFilterPageSize.Twelve)
        state.loadMore()
        assertEquals(24, state.visibleCount)

        state.selectSort(ProductFilterSort.PriceAscending)

        assertEquals(12, state.visibleCount)
    }

    @Test
    fun directPriceDraftIsAppliedOnlyWhenApplyIsCalled() {
        val state = newState()

        state.changePriceDraft(ProductFilterPriceBound.Min, "1")
        assertNull(state.criteria.minPriceManwon)

        state.applyPriceDraft()
        assertEquals(1, state.criteria.minPriceManwon)
    }

    @Test
    fun nonDigitPriceInputIsDroppedFromDraft() {
        val state = newState()

        state.changePriceDraft(ProductFilterPriceBound.Max, "1a2,000")

        assertEquals("12000", state.maxPriceDraft)
    }

    @Test
    fun reversedDirectPriceBoundsAreSwappedOnApply() {
        val state = newState()
        state.changePriceDraft(ProductFilterPriceBound.Min, "5")
        state.changePriceDraft(ProductFilterPriceBound.Max, "2")

        state.applyPriceDraft()

        assertEquals(2, state.criteria.minPriceManwon)
        assertEquals(5, state.criteria.maxPriceManwon)
    }

    @Test
    fun lastChosenPriceInputWinsBetweenChipAndDirectInput() {
        val state = newState()
        state.changePriceDraft(ProductFilterPriceBound.Min, "1")
        state.applyPriceDraft()

        state.selectPriceRange(ProductFilterPriceRange.UnderTenThousand)
        assertNull(state.criteria.minPriceManwon)
        assertEquals(ProductFilterPriceRange.UnderTenThousand, state.criteria.priceRange)

        state.changePriceDraft(ProductFilterPriceBound.Max, "3")
        state.applyPriceDraft()
        assertNull(state.criteria.priceRange)
        assertEquals(3, state.criteria.maxPriceManwon)
    }

    // 필터 적용 버튼은 입력 중인 가격 draft를 반영한다.
    @Test
    fun filterApplyButtonAppliesEnteredPriceDraft() {
        val state = newState()
        state.changePriceDraft(ProductFilterPriceBound.Min, "2")

        state.applyPriceDraftIfEntered()

        assertEquals(2, state.criteria.minPriceManwon)
    }

    // 직접 입력 칸이 비어 있으면 필터 적용 버튼은 기존 조건을 바꾸지 않는다.
    @Test
    fun filterApplyButtonKeepsChipSelectionWhenPriceDraftIsEmpty() {
        val state = newState()
        state.selectPriceRange(ProductFilterPriceRange.TenToThirtyThousand)

        state.applyPriceDraftIfEntered()

        assertEquals(ProductFilterPriceRange.TenToThirtyThousand, state.criteria.priceRange)
        assertNull(state.criteria.minPriceManwon)
        assertNull(state.criteria.maxPriceManwon)
    }

    @Test
    fun brandSelectionTogglesEachBrandIndependently() {
        val state = newState()

        state.toggleBrand("brand-a")
        state.toggleBrand("brand-b")
        assertEquals(setOf("brand-a", "brand-b"), state.criteria.brandIds)

        state.toggleBrand("brand-a")
        assertEquals(setOf("brand-b"), state.criteria.brandIds)
    }

    @Test
    fun safetyCertificationTogglesEachTypeIndependently() {
        val state = newState()

        state.toggleSafetyCertification(SafetyCertificationType.KcSafety)
        state.toggleSafetyCertification(SafetyCertificationType.TypeApproval)
        assertEquals(setOf(SafetyCertificationType.KcSafety, SafetyCertificationType.TypeApproval), state.criteria.safetyCertifications)

        state.toggleSafetyCertification(SafetyCertificationType.KcSafety)
        assertEquals(setOf(SafetyCertificationType.TypeApproval), state.criteria.safetyCertifications)
    }

    @Test
    fun brandSelectionIsDroppedWhenBrandIsNoLongerInSourceProducts() {
        val state = newState()
        state.toggleBrand("brand-a")
        state.toggleBrand("brand-b")

        state.keepBrandsIfAvailable(listOf("brand-b"))

        assertEquals(setOf("brand-b"), state.criteria.brandIds)
    }

    @Test
    fun criteriaWithSetsSurvivesSaverRoundTrip() {
        val criteria = ProductFilterCriteria(
            brandIds = setOf("brand-a", "brand-b"),
            safetyCertifications = setOf(SafetyCertificationType.SelfSafetyCheck),
            hideSoldOut = true,
        )

        val saved = ProductFilterCriteriaSaver.run { SaverScope { true }.save(criteria) }

        assertEquals(criteria, ProductFilterCriteriaSaver.restore(saved!!))
    }

    // 직접 입력을 적용한 뒤 칸을 모두 지우고 하단 필터 적용을 누르면 빈 draft는 무시되어 조건이 남아야 한다.
    @Test
    fun filterApplyWithClearedDraftKeepsAppliedDirectPrice() {
        val state = newState()
        state.changePriceDraft(ProductFilterPriceBound.Min, "5")
        state.changePriceDraft(ProductFilterPriceBound.Max, "9")
        state.applyPriceDraft()
        state.changePriceDraft(ProductFilterPriceBound.Min, "")
        state.changePriceDraft(ProductFilterPriceBound.Max, "")

        state.applyPriceDraftIfEntered()

        assertEquals(5, state.criteria.minPriceManwon)
        assertEquals(9, state.criteria.maxPriceManwon)
    }

    // 해제하면 적용된 직접 입력 조건과 입력 칸이 함께 사라진다.
    @Test
    fun clearDirectPriceRemovesAppliedConditionAndDrafts() {
        val state = newState()
        state.changePriceDraft(ProductFilterPriceBound.Min, "5")
        state.applyPriceDraft()

        state.clearDirectPrice()

        assertNull(state.criteria.minPriceManwon)
        assertNull(state.criteria.maxPriceManwon)
        assertEquals("", state.minPriceDraft)
        assertEquals("", state.maxPriceDraft)
    }

    private fun newState(): ProductFilterState = ProductFilterState(
        isVisibleState = mutableStateOf(false),
        criteriaState = mutableStateOf(ProductFilterCriteria()),
        minPriceDraftState = mutableStateOf(""),
        maxPriceDraftState = mutableStateOf(""),
        pageSizeState = mutableStateOf(ProductFilterPageSize.TwentyFour),
        visibleCountState = mutableStateOf(ProductFilterPageSize.TwentyFour.count),
    )

    private fun product(
        productId: String,
        priceWon: Long,
    ) = CatalogProductUiModel(
        id = productId,
        name = productId,
        price = CatalogPriceUiModel.Available(currentLabel = "$priceWon", unitPriceWon = priceWon),
    )
}

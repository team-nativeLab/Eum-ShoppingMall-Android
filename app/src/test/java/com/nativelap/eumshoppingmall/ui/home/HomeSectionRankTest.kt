package com.nativelap.eumshoppingmall.ui.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeSectionRankTest {
    private val products = (1..7).map { index ->
        HomeProductUiModel(id = "p$index", name = "상품 $index", categoryId = if (index % 2 == 0) "fire" else "protective")
    }

    @Test
    fun sectionRanksFollowDisplayOrderOnlyWithinTopFive() {
        val ranks = products.withSectionRanks().map { product -> product.bestRank }

        assertEquals(listOf(1, 2, 3, 4, 5), ranks.take(5))
        assertNull(ranks[5])
        assertNull(ranks[6])
    }

    @Test
    fun sectionRanksAreUniqueWithinSection() {
        val ranks = products.withSectionRanks().mapNotNull { product -> product.bestRank }

        assertEquals(ranks.size, ranks.toSet().size)
    }

    @Test
    fun filteredTabRanksRestartFromFirstVisibleProduct() {
        val fireOnly = products.filterByBestCategory("fire").withSectionRanks()

        assertEquals(listOf("p2", "p4", "p6"), fireOnly.map { product -> product.id })
        assertEquals(listOf(1, 2, 3), fireOnly.map { product -> product.bestRank })
    }

    @Test
    fun nullTabKeepsEveryProduct() {
        assertEquals(products, products.filterByBestCategory(null))
    }
}

class HomeBestTabVisibilityTest {
    private val categories = listOf(
        HomeCategoryUiModel(label = "개인보호장비", iconResId = 0, id = "protective"),
        HomeCategoryUiModel(label = "작업복", iconResId = 0, id = "uniform"),
        HomeCategoryUiModel(label = "소방", iconResId = 0, id = "fire"),
    )

    @Test
    fun categoryWithoutAnyBestProductIsHidden() {
        val products = listOf(
            HomeProductUiModel(id = "p1", name = "a", categoryId = "protective"),
            HomeProductUiModel(id = "p2", name = "b", categoryId = "fire"),
        )

        assertEquals(listOf("protective", "fire"), categories.withBestProducts(products).map { category -> category.id })
    }

    @Test
    fun noBestProductsHidesEveryCategoryTab() {
        assertTrue(categories.withBestProducts(emptyList()).isEmpty())
    }
}

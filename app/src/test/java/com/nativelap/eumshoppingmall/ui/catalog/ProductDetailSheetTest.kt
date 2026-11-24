package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.saveable.SaverScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductDetailSheetTest {
    @Test
    fun closeGateAllowsOnlyOneCloseUntilExited() {
        val gate = OptionsSheetCloseGate()

        assertTrue(gate.tryEnter())
        assertFalse(gate.tryEnter())
        gate.exit()
        assertTrue(gate.tryEnter())
    }

    @Test
    fun closeRunsHideThenMarksClosedThenFollowUp() = runBlocking {
        val events = mutableListOf<String>()

        closeSheetThen(
            hide = { events.add("hide") },
            markClosed = { events.add("closed") },
            afterClosed = { events.add("after") },
        )

        assertEquals(listOf("hide", "closed", "after"), events)
    }

    @Test
    fun followUpStillRunsWhenHideIsCancelled() = runBlocking {
        val events = mutableListOf<String>()

        closeSheetThen(
            hide = {
                events.add("hide")
                throw CancellationException("rotation")
            },
            markClosed = { events.add("closed") },
            afterClosed = { events.add("after") },
        )

        assertEquals(listOf("hide", "closed", "after"), events)
    }

    @Test
    fun hideFailureKeepsSheetStateConsistentButSkipsFollowUp() {
        val events = mutableListOf<String>()
        var thrown: Throwable? = null
        runBlocking {
            try {
                closeSheetThen(
                    hide = {
                        events.add("hide")
                        throw IllegalStateException("broken")
                    },
                    markClosed = { events.add("closed") },
                    afterClosed = { events.add("after") },
                )
            } catch (exception: IllegalStateException) {
                thrown = exception
            }
        }

        assertEquals(listOf("hide", "closed"), events)
        assertTrue(thrown is IllegalStateException)
    }

    @Test
    fun restoresSavedSelectionsAndDropsBrokenEntries() {
        val saver = OptionSelectionsSaver
        val restored = saver.restore(
            arrayListOf("size", "size-l", "2", "size", "size-xl", "abc", "color", "", "1", "size", "size-m", "0", "trailing"),
        )

        assertEquals(
            listOf(
                OptionSelection("size", "size-l", 2),
                OptionSelection("size", "size-m", 1),
            ),
            restored,
        )
    }

    @Test
    fun restoreOfUnexpectedShapeYieldsNoSelections() {
        assertEquals(emptyList<OptionSelection>(), OptionSelectionsSaver.restore(arrayListOf()))
    }

    @Test
    fun saveThenRestoreRoundTrips() {
        val original = listOf(OptionSelection("size", "size-l", 3), OptionSelection("color", "red", 1))
        val saved = with(SaverScope { true }) { with(OptionSelectionsSaver) { save(original) } }!!

        assertEquals(original, OptionSelectionsSaver.restore(saved))
    }

    @Test
    fun cartEditGroupRequiresOneCurrentGroupAndMatchingProduct() {
        val source = com.nativelap.eumshoppingmall.data.local.CartLine(
            productId = "product-1",
            productName = "상품",
            optionId = "option-1",
            optionLabel = "옵션",
            quantity = 2,
            optionExtraWon = 0,
            productPriceLabel = "₩1,000",
            isPriceUndisclosed = false,
        )
        val group = CatalogOptionGroupUiModel("group-1", "그룹", listOf(CatalogOptionUiModel("option-1", "옵션", 1, null)))

        assertEquals(group, resolveCartOptionEditGroup(source, "product-1", listOf(group)))
        assertEquals(null, resolveCartOptionEditGroup(source, "product-2", listOf(group)))
        assertEquals(null, resolveCartOptionEditGroup(source, "product-1", emptyList()))
        assertEquals(null, resolveCartOptionEditGroup(source, "product-1", listOf(group, group.copy(id = "group-2"))))
        assertTrue(isCartOptionEditAllowed(source, "product-1", group, isSoldOut = false, isPriceUndisclosed = false))
        assertFalse(isCartOptionEditAllowed(source, "product-1", group, isSoldOut = true, isPriceUndisclosed = false))
        assertFalse(isCartOptionEditAllowed(source, "product-1", group, isSoldOut = false, isPriceUndisclosed = true))
        assertTrue(isSingleValidCartOptionSelection(listOf(CatalogSelectedOptionUiModel("group-1", group.options.single())), group))
        assertFalse(isSingleValidCartOptionSelection(emptyList(), group))
        assertFalse(isSingleValidCartOptionSelection(listOf(CatalogSelectedOptionUiModel("other", group.options.single())), group))
        assertFalse(
            isSingleValidCartOptionSelection(
                listOf(CatalogSelectedOptionUiModel("group-1", group.options.single()), CatalogSelectedOptionUiModel("other", group.options.single())),
                group,
            ),
        )
    }
}

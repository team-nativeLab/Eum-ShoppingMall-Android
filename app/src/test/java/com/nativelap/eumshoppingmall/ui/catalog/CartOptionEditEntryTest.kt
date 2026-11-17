package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.data.local.CartLineReplaceResult
import org.junit.Assert.assertEquals
import org.junit.Test

class CartOptionEditEntryTest {
    private val productId = "product-1"
    private val sourceId = "$productId/old-option"
    private val groups = listOf(
        CatalogOptionGroupUiModel(
            "group-1",
            "유형",
            listOf(
                CatalogOptionUiModel("old-option", "기존 옵션", 1, null, 0),
                CatalogOptionUiModel("new-option", "대체 옵션", 1, "₩500", 500),
            ),
        ),
    )

    @Test
    fun beforeCartIsLoadedTheEntryIsPendingNotInvalid() {
        val state = entry(isCartLoaded = false, sourceLine = null)

        assertEquals(CartOptionEditEntry.Pending, state)
    }

    @Test
    fun afterLoadingAnExistingSourceLineIsEditable() {
        val state = entry(isCartLoaded = true, sourceLine = sourceLine())

        assertEquals(CartOptionEditEntry.Editable, state)
    }

    @Test
    fun afterLoadingAMissingSourceLineIsInvalid() {
        val state = entry(isCartLoaded = true, sourceLine = null)

        assertEquals(CartOptionEditEntry.Invalid, state)
    }

    @Test
    fun sourceLineWhoseOptionIsNotInTheCatalogIsInvalid() {
        val state = entry(isCartLoaded = true, sourceLine = sourceLine(optionId = "stale-option"))

        assertEquals(CartOptionEditEntry.Invalid, state)
    }

    @Test
    fun soldOutProductIsInvalidEvenWhenTheLineExists() {
        val state = entry(isCartLoaded = true, sourceLine = sourceLine(), isSoldOut = true)

        assertEquals(CartOptionEditEntry.Invalid, state)
    }

    @Test
    fun sourceLineRemovedByASuccessfulSaveDoesNotShowInvalidMessage() {
        val state = entry(isCartLoaded = true, sourceLine = null, isSaving = true)

        assertEquals(CartOptionEditEntry.Saving, state)
    }

    @Test
    fun withoutEditSourceIdThereIsNoEditEntry() {
        val state = entry(editSourceLineId = null, isCartLoaded = true, sourceLine = null)

        assertEquals(CartOptionEditEntry.NotEditing, state)
    }

    @Test
    fun openSheetIsClosedWhenTheEntryBecomesInvalid() {
        assertEquals(false, optionSheetVisibleFor(CartOptionEditEntry.Invalid, isVisible = true))
        assertEquals(true, optionSheetVisibleFor(CartOptionEditEntry.Editable, isVisible = true))
        assertEquals(false, optionSheetVisibleFor(CartOptionEditEntry.Editable, isVisible = false))
    }

    @Test
    fun invalidEntryCannotOpenTheOptionSheetButOrdinaryFlowsCan() {
        assertEquals(false, canOpenOptionSheet(CartOptionEditEntry.Invalid))
        assertEquals(true, canOpenOptionSheet(CartOptionEditEntry.Editable))
        assertEquals(true, canOpenOptionSheet(CartOptionEditEntry.NotEditing))
    }

    @Test
    fun replacedResultHasNoSaveFailure() {
        assertEquals(null, cartOptionEditSaveFailure(CartLineReplaceResult.Replaced))
    }

    @Test
    fun missingOrDuplicateSourceIsReportedAsSourceMissingWithoutRetryWording() {
        assertEquals(CartOptionEditSaveFailure.SourceMissing, cartOptionEditSaveFailure(CartLineReplaceResult.SourceMissing))
    }

    @Test
    fun writeFailureOrMissingResultIsReportedAsSaveFailed() {
        assertEquals(CartOptionEditSaveFailure.SaveFailed, cartOptionEditSaveFailure(CartLineReplaceResult.Failed))
        assertEquals(CartOptionEditSaveFailure.SaveFailed, cartOptionEditSaveFailure(null))
    }

    private fun entry(
        editSourceLineId: String? = sourceId,
        isCartLoaded: Boolean,
        sourceLine: CartLine?,
        isSoldOut: Boolean = false,
        isSaving: Boolean = false,
    ): CartOptionEditEntry = cartOptionEditEntry(
        editSourceLineId = editSourceLineId,
        isCartLoaded = isCartLoaded,
        sourceLine = sourceLine,
        currentProductId = productId,
        optionGroups = groups,
        isSoldOut = isSoldOut,
        isQuoteRequired = false,
        isSaving = isSaving,
    )

    private fun sourceLine(optionId: String = "old-option") = CartLine(
        productId = productId,
        productName = "상품",
        optionId = optionId,
        optionLabel = "기존 옵션",
        quantity = 3,
        optionExtraWon = 0,
        productPriceLabel = "₩1,000",
        isQuoteRequired = false,
        productUnitPriceWon = 1_000,
    )
}

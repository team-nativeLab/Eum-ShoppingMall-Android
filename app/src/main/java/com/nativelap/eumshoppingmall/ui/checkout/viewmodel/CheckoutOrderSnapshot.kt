package com.nativelap.eumshoppingmall.ui.checkout.viewmodel

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.catalog.cartShippingFeeWon
import com.nativelap.eumshoppingmall.ui.catalog.formatWonLabel

/** One in-memory order source shared by address and payment. It contains no address/payment data. */
data class CheckoutOrderSnapshot(
    val lines: List<CheckoutOrderLine>,
    val source: CheckoutOrderSource,
    val cartLineIdsToRemove: Set<String>,
) {
    val productTotalWon: Long? = calculateProductTotal(lines)

    val shippingFeeWon: Long? = productTotalWon?.let(::cartShippingFeeWon)
    val finalTotalWon: Long? = productTotalWon?.let { total -> shippingFeeWon?.let { fee -> runCatching { Math.addExact(total, fee) }.getOrNull() } }

    companion object {
        val Saver: Saver<CheckoutOrderSnapshot?, Any> = listSaver(
            save = { snapshot -> snapshot?.let(::encodeCheckoutOrderSnapshot).orEmpty() },
            restore = ::decodeCheckoutOrderSnapshot,
        )
    }

    fun toCheckoutUiModel(base: CheckoutUiModel): CheckoutUiModel = base.copy(
        products = lines.map { line ->
            CheckoutProductUiModel(
                id = line.id,
                name = line.name,
                optionAndQuantityLabel = "${line.optionLabel} · ${line.quantity}개",
                priceLabel = line.lineTotalWon?.let(::formatWonLabel) ?: "가격 확인 필요",
            )
        },
        paymentSummary = base.paymentSummary.copy(
            productAmountLabel = productTotalWon?.let(::formatWonLabel) ?: "가격 확인 필요",
            shippingFeeLabel = shippingFeeWon?.let(::formatWonLabel) ?: "주문 시 확인",
            shippingFeeWon = shippingFeeWon,
            finalAmountLabel = finalTotalWon?.let(::formatWonLabel) ?: "주문 시 확인",
            finalAmountWon = finalTotalWon,
        ),
    )
}

data class CheckoutOrderLine(
    val id: String,
    val name: String,
    val optionLabel: String,
    val quantity: Int,
    val lineTotalWon: Long?,
)

enum class CheckoutOrderSource { SavedCart, DebugFallback, BuyNow }

internal fun encodeCheckoutOrderSnapshot(snapshot: CheckoutOrderSnapshot): List<Any?> = buildList {
    val source = snapshot.source
    val cartLineIdsToRemove = snapshot.cartLineIdsToRemove
    val lines = snapshot.lines
    add(source.name)
    add(cartLineIdsToRemove.toList())
    add(lines.size)
    lines.forEach { line ->
        add(line.id)
        add(line.name)
        add(line.optionLabel)
        add(line.quantity)
        add(line.lineTotalWon != null)
        add(line.lineTotalWon ?: 0L)
    }
}

internal fun decodeCheckoutOrderSnapshot(values: List<Any?>): CheckoutOrderSnapshot? = runCatching {
    if (values.isEmpty()) return null
    val source = CheckoutOrderSource.valueOf(values[0] as String)
    val lineIdsToRemove = (values[1] as List<*>).map { it as String }.toSet()
    val lineCount = values[2] as Int
    if (lineCount <= 0 || values.size != 3 + lineCount * 6) return null
    var valueIndex = 3
    val lines = List(lineCount) {
        val id = values[valueIndex++] as String
        val name = values[valueIndex++] as String
        val optionLabel = values[valueIndex++] as String
        val quantity = values[valueIndex++] as Int
        val hasLineTotal = values[valueIndex++] as Boolean
        val amount = values[valueIndex++] as Long
        CheckoutOrderLine(id, name, optionLabel, quantity, amount.takeIf { hasLineTotal })
    }
    CheckoutOrderSnapshot(lines, source, lineIdsToRemove)
}.getOrNull()

fun canCompleteCheckoutPayment(snapshot: CheckoutOrderSnapshot?): Boolean = snapshot?.finalTotalWon != null

private fun calculateProductTotal(lines: List<CheckoutOrderLine>): Long? {
    var total: Long? = 0L
    for (line in lines) {
        val amount = line.lineTotalWon ?: return null
        if (amount < 0L) return null
        total = total?.let { current -> runCatching { Math.addExact(current, amount) }.getOrNull() } ?: return null
    }
    return total
}

/** Builds the debug cart order from the rows currently rendered by CartRoute. */
fun checkoutOrderSnapshotFromCartItems(items: List<CartItemUiModel>): CheckoutOrderSnapshot? {
    val selectedRows = items.filter { item -> item.isSelected && item.type == CartItemType.Orderable && !item.isSoldOut }
    if (selectedRows.isEmpty() || selectedRows.any { it.quantity <= 0 } || selectedRows.map { it.id }.toSet().size != selectedRows.size) return null
    return CheckoutOrderSnapshot(
        lines = selectedRows.map { item -> CheckoutOrderLine(item.id, item.name, item.optionLabel, item.quantity, item.lineTotalWon) },
        source = CheckoutOrderSource.DebugFallback,
        cartLineIdsToRemove = emptySet(),
    )
}

/** Null price or arithmetic overflow stays visibly unpriced; invalid/quote-only lines are rejected. */
fun checkoutOrderSnapshot(
    lines: List<CartLine>,
    source: CheckoutOrderSource,
): CheckoutOrderSnapshot? {
    if (lines.isEmpty() || lines.any { it.isQuoteRequired || it.quantity <= 0 }) return null
    val mapped = lines.map { line ->
        val total = if (line.productUnitPriceWon == null || line.optionExtraWon == null || line.productUnitPriceWon < 0L || line.optionExtraWon < 0L) {
            null
        } else {
            runCatching {
                Math.multiplyExact(Math.addExact(line.productUnitPriceWon, line.optionExtraWon), line.quantity.toLong())
            }.getOrNull()
        }
        CheckoutOrderLine(line.lineId(), line.productName, line.optionLabel, line.quantity, total)
    }
    val idsToRemove = if (source == CheckoutOrderSource.SavedCart) lines.map(CartLine::lineId).toSet() else emptySet()
    return CheckoutOrderSnapshot(mapped, source, idsToRemove)
}

/** Resolves exactly the selected IDs. Null storage means still loading and can never use debug rows. */
fun resolveCheckoutOrderForSelection(
    savedLines: List<CartLine>?,
    orderedLineIds: List<String>,
    debugFallbackLines: List<CheckoutOrderLine>? = null,
): CheckoutOrderSnapshot? {
    if (savedLines == null || orderedLineIds.isEmpty() || orderedLineIds.toSet().size != orderedLineIds.size) return null
    if (savedLines.isEmpty()) {
        val fallback = debugFallbackLines ?: return null
        val rowsById = fallback.groupBy(CheckoutOrderLine::id)
        if (orderedLineIds.any { id -> rowsById[id]?.size != 1 }) return null
        return CheckoutOrderSnapshot(
            lines = orderedLineIds.map { id -> rowsById.getValue(id).single() },
            source = CheckoutOrderSource.DebugFallback,
            cartLineIdsToRemove = emptySet(),
        )
    }
    val savedById = savedLines.groupBy(CartLine::lineId)
    if (orderedLineIds.any { id -> savedById[id]?.size != 1 }) return null
    return checkoutOrderSnapshot(orderedLineIds.map { id -> savedById.getValue(id).single() }, CheckoutOrderSource.SavedCart)
}

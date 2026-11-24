package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.common.EUM_MAX_QUANTITY
import com.nativelap.eumshoppingmall.ui.common.EumQuantityStepper

@Composable
fun CartQuantityStepper(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val safeQuantity = quantity.coerceAtLeast(1)
    EumQuantityStepper(
        quantity = safeQuantity,
        onDecrease = { onQuantityChanged((safeQuantity - 1).coerceAtLeast(1)) },
        onIncrease = { onQuantityChanged((safeQuantity + 1).coerceAtMost(EUM_MAX_QUANTITY)) },
        modifier = modifier,
        canDecrease = safeQuantity > 1,
        canIncrease = safeQuantity < EUM_MAX_QUANTITY,
        enabled = enabled,
    )
}

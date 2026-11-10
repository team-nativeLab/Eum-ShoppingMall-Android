package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartPriceLabels
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartScreenEvent
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartAmountLabel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartAmountSummary
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartExpectedPaymentText
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartItemId
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartProductAmountWon
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartUiModelOf
import com.nativelap.eumshoppingmall.ui.catalog.formatWonLabel
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * 장바구니 화면. 저장된 줄([savedLines])이 있으면 그것을 보여 준다.
 * 저장 기록이 한 번이라도 있었으면(마지막 줄을 지운 경우 포함) 기본 목록([fallback])은 다시 쓰지 않는다(L3).
 * 수량 변경과 삭제는 저장된 줄에서 [onQuantityChanged]·[onItemsRemoved]로 저장소에 반영한다.
 * 저장 기록이 없을 때만 기본 목록(샘플)을 쓰며, 그 편집은 이 화면 안에서만 유지된다.
 * 선택(체크) 상태는 저장하지 않는다. 재진입하면 주문 가능한 줄은 다시 기본 선택이 된다.
 */
@Composable
fun CartRoute(
    savedLines: List<CartLine>,
    fallback: CartUiModel,
    isLoggedIn: Boolean,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onSearchClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onQuoteRequestClick: () -> Unit,
    onQuantityChanged: suspend (itemId: String, quantity: Int) -> Unit,
    onItemsRemoved: suspend (itemIds: Set<String>) -> Unit,
    onCartLinesRestored: suspend (lines: List<CartLine>) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val priceLabels = CartPriceLabels(
        priceCheck = stringResource(R.string.cart_price_check_needed),
        quotePrice = stringResource(R.string.cart_price_inquiry),
    )
    val freeShippingLabel = stringResource(R.string.checkout_delivery_free)
    val shippingAtOrderLabel = stringResource(R.string.cart_shipping_at_order)
    val deletedMessage = stringResource(R.string.cart_deleted_message)
    val undoLabel = stringResource(R.string.cart_undo)
    val partialAmountTemplate = stringResource(R.string.cart_product_amount_partial)
    val saveFailedMessage = stringResource(R.string.cart_save_failed)

    val hasSavedLines = savedLines.isNotEmpty()
    // 저장 기록이 한 번이라도 있었는지 기억한다. 이후 빈 목록이어도 샘플이 다시 나오지 않는다.
    var hadSavedLines by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(hasSavedLines) {
        if (hasSavedLines) hadSavedLines = true
    }
    val isSavedMode = hasSavedLines || hadSavedLines

    // 기본 목록의 로컬 편집. 저장 기록이 있으면 쓰이지 않는다.
    var fallbackItems by remember(fallback) { mutableStateOf(fallback.items) }
    // 선택 상태는 항목 id별 덮어쓰기로만 둔다. 재진입하면 비어 있으므로 기본 선택으로 돌아간다.
    var selectionOverrides by remember { mutableStateOf(emptyMap<String, Boolean>()) }
    // 수량 저장 중인 줄 id. 쓰기가 끝날 때까지 그 줄의 스테퍼를 잠근다(L1).
    var quantityLockIds by remember { mutableStateOf(emptySet<String>()) }

    val baseItems = if (isSavedMode) {
        cartUiModelOf(savedLines, priceLabels) { CartUiModel() }.items
    } else {
        fallbackItems
    }
    // 견적 상품은 주문 선택에서 항상 제외한다. 선택 덮어쓰기는 주문 가능한 줄에만 적용된다.
    val items = baseItems.map { item ->
        val isSelected = item.type == CartItemType.Orderable && (selectionOverrides[item.id] ?: item.isSelected)
        item.copy(isSelected = isSelected)
    }

    val productWon = cartProductAmountWon(items)
    val shippingWon = if (isSavedMode) null else fallback.shippingFeeWon
    val expectedWon = if (productWon != null && shippingWon != null) {
        try {
            Math.addExact(productWon, shippingWon)
        } catch (exception: ArithmeticException) {
            null
        }
    } else {
        null
    }
    val summaryUiModel = CartUiModel(
        items = items,
        productAmountLabel = cartAmountLabel(
            summary = cartAmountSummary(items),
            priceCheck = priceLabels.priceCheck,
            partialTemplate = partialAmountTemplate,
            formatWon = ::formatWonLabel,
        ),
        shippingFeeLabel = when (shippingWon) {
            null -> shippingAtOrderLabel
            0L -> freeShippingLabel
            else -> formatWonLabel(shippingWon)
        },
        expectedPaymentLabel = cartExpectedPaymentText(
            productWon = productWon,
            shippingWon = shippingWon,
            priceCheck = priceLabels.priceCheck,
            shippingAtOrder = shippingAtOrderLabel,
            formatWon = ::formatWonLabel,
        ),
        expectedPaymentWon = expectedWon,
    )

    // 저장소 쓰기는 화면 밖에서도 끝나야 하므로 scope로 실행한다. 잠금은 쓰기가 끝나는 즉시 풀고, 실패는 안내로만 알린다.
    fun saveChange(lockedItemId: String?, block: suspend () -> Unit) {
        if (lockedItemId != null) quantityLockIds = quantityLockIds + lockedItemId
        scope.launch {
            var failed = false
            try {
                block()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                failed = true
            } finally {
                if (lockedItemId != null) quantityLockIds = quantityLockIds - lockedItemId
            }
            if (failed) snackbarHostState.showSnackbar(message = saveFailedMessage)
        }
    }

    // 삭제하면 "삭제했어요 / 실행 취소" 안내를 띄운다. 실행 취소는 지운 줄을 그대로 다시 넣는다(화면을 떠나면 취소할 수 없다).
    fun removeItems(itemIds: Set<String>) {
        if (itemIds.isEmpty()) return
        if (isSavedMode) {
            val removedLines = savedLines.filter { line -> line.cartItemId() in itemIds }
            saveChange(lockedItemId = null) { onItemsRemoved(itemIds) }
            scope.launch {
                val result = snackbarHostState.showSnackbar(message = deletedMessage, actionLabel = undoLabel, duration = SnackbarDuration.Short)
                if (result == SnackbarResult.ActionPerformed) {
                    saveChange(lockedItemId = null) { onCartLinesRestored(removedLines) }
                }
            }
        } else {
            val removedItems = fallbackItems.filter { item -> item.id in itemIds }
            fallbackItems = fallbackItems.filterNot { item -> item.id in itemIds }
            scope.launch {
                val result = snackbarHostState.showSnackbar(message = deletedMessage, actionLabel = undoLabel, duration = SnackbarDuration.Short)
                if (result == SnackbarResult.ActionPerformed) {
                    fallbackItems = fallbackItems + removedItems.filterNot { item -> fallbackItems.any { it.id == item.id } }
                }
            }
        }
    }

    fun changeQuantity(itemId: String, quantity: Int) {
        val safeQuantity = quantity.coerceAtLeast(1)
        if (isSavedMode) {
            if (itemId in quantityLockIds) return
            saveChange(lockedItemId = itemId) { onQuantityChanged(itemId, safeQuantity) }
        } else {
            fallbackItems = fallbackItems.map { item ->
                if (item.id == itemId) item.copy(quantity = safeQuantity) else item
            }
        }
    }

    CartScreen(
        uiModel = summaryUiModel,
        isLoggedIn = isLoggedIn,
        cartItemCount = cartItemCount,
        snackbarHostState = snackbarHostState,
        quantityLockedItemIds = quantityLockIds,
        onEvent = { event ->
            when (event) {
                CartScreenEvent.SearchClicked -> onSearchClick()

                is CartScreenEvent.BottomTabSelected -> onTabSelected(event.tab)

                is CartScreenEvent.SelectAllChanged -> {
                    selectionOverrides = selectionOverrides + items
                        .filter(::isSelectable)
                        .associate { item -> item.id to event.isSelected }
                }

                is CartScreenEvent.ItemSelectionChanged -> {
                    selectionOverrides = selectionOverrides + (event.itemId to event.isSelected)
                }

                CartScreenEvent.DeleteSelectedClicked -> {
                    removeItems(items.filter { item -> item.type == CartItemType.Orderable && item.isSelected }.map { it.id }.toSet())
                }

                is CartScreenEvent.ItemRemoved -> removeItems(setOf(event.itemId))

                is CartScreenEvent.QuantityChanged -> changeQuantity(event.itemId, event.quantity)

                CartScreenEvent.QuoteRequestClicked -> onQuoteRequestClick()

                CartScreenEvent.CheckoutClicked -> onCheckoutClick()

                // 옵션 변경 시트는 이번 범위가 아니다.
                is CartScreenEvent.OptionChangeClicked -> Unit
            }
        },
    )
}

private fun isSelectable(item: CartItemUiModel): Boolean = item.type == CartItemType.Orderable && !item.isSoldOut

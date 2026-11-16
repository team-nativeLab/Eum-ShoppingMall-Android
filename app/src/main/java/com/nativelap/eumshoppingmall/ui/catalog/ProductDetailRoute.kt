package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.data.local.totalQuantity
import com.nativelap.eumshoppingmall.ui.common.rememberFavoriteToggles
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 상품 상세(A-05)와 옵션 시트(A-05s).
 *
 * 옵션 시트는 아직 NavKey overlay가 아니라 이 Route의 상태로 연다(후속 작업에서 overlay로 옮길 수 있다).
 * 시트 닫힘은 Route가 맡는다. 시트를 hide()로 닫은 뒤에 상태를 false로 바꾸고, 담기·구매·견적 같은 후속 동작을 실행한다.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CatalogProductDetailRoute(
    uiModel: CatalogProductDetailUiModel,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onBuyNowClick: () -> Unit,
    onQuoteRequestClick: (productId: String) -> Unit,
    onProductClick: (productId: String) -> Unit,
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
    onAllReviewsClick: (productId: String) -> Unit,
    onReviewWriteClick: (productId: String) -> Unit,
    onQuestionWriteClick: (productId: String) -> Unit,
    onAddToCart: suspend (addedLines: List<CartLine>) -> Unit,
    editSourceLineId: String? = null,
    editSourceLine: CartLine? = null,
    onReplaceCartLine: suspend (sourceLineId: String, replacement: CartLine) -> Boolean = { _, _ -> false },
    cartAddOutcome: CartAddOutcome?,
    onCartAddOutcomeChange: (CartAddOutcome?) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val optionsSheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetCloseGate = remember { OptionsSheetCloseGate() }
    var selectedImageIndex by rememberSaveable { mutableStateOf(uiModel.selectedImageIndex) }
    var selectedTabIndex by rememberSaveable { mutableStateOf(uiModel.selectedTabIndex) }
    var isOptionsSheetVisible by rememberSaveable(editSourceLineId) { mutableStateOf(uiModel.optionsSheetVisible || editSourceLineId != null) }
    var isProductFavorite by rememberSaveable { mutableStateOf(uiModel.product.isFavorite) }
    val relatedFavoriteToggles = rememberFavoriteToggles(
        initialFavoriteIds = uiModel.relatedProducts.filter { product -> product.isFavorite }.map { product -> product.id }.toSet(),
        isLoggedIn = isLoggedIn,
        onLoginRequired = onLoginRequired,
    )
    // 선택은 id와 수량만 저장하고(회전 복원), 표시에 쓸 옵션 정보는 현재 UiModel에서 다시 찾는다. 상품이 바뀌면 선택도 새로 시작한다.
    val editGroup = remember(editSourceLine, uiModel.product.id, uiModel.optionGroups) {
        resolveCartOptionEditGroup(editSourceLine, uiModel.product.id, uiModel.optionGroups)
    }
    val isEditMode = isCartOptionEditAllowed(
        sourceLine = editSourceLine,
        currentProductId = uiModel.product.id,
        editGroup = editGroup,
        isSoldOut = uiModel.product.isSoldOut,
        isQuoteRequired = uiModel.product.price is CatalogPriceUiModel.QuoteRequired,
    )
    var optionSelections by rememberSaveable(uiModel.product.id, editSourceLineId, stateSaver = OptionSelectionsSaver) {
        mutableStateOf(emptyList<OptionSelection>())
    }
    LaunchedEffect(editSourceLine, editGroup) {
        if (isEditMode && optionSelections.isEmpty()) {
            optionSelections = listOf(OptionSelection(editGroup!!.id, editSourceLine!!.optionId, editSourceLine.quantity))
        }
    }
    val selectedOptions = remember(uiModel.optionGroups, optionSelections) {
        resolveSelectedOptions(uiModel.optionGroups, optionSelections)
    }
    val totalPriceLabel = remember(selectedOptions) {
        calculateSelectedOptionTotalWon(selectedOptions)?.let(::formatWonLabel)
    }
    val cartSavedMessage = stringResource(R.string.catalog_added_to_cart)
    val cartFailedMessage = stringResource(R.string.catalog_add_to_cart_failed)
    val invalidEditMessage = stringResource(R.string.cart_option_edit_failed)
    val viewCartLabel = stringResource(R.string.catalog_view_cart)
    val currentOnCartClick by rememberUpdatedState(onCartClick)
    // 담기 결과 안내. Route 밖에 보관된 결과를 다음 composition에서 한 번 띄우고 비운다(N5).
    LaunchedEffect(cartAddOutcome) {
        val outcome = cartAddOutcome ?: return@LaunchedEffect
        if (outcome.saved) {
            val result = snackbarHostState.showSnackbar(
                message = cartSavedMessage,
                actionLabel = viewCartLabel,
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) currentOnCartClick()
        } else {
            snackbarHostState.showSnackbar(message = cartFailedMessage, duration = SnackbarDuration.Short)
        }
        onCartAddOutcomeChange(null)
    }
    val productPriceLabel = when (val price = uiModel.product.price) {
        is CatalogPriceUiModel.Available -> price.currentLabel
        CatalogPriceUiModel.QuoteRequired -> stringResource(R.string.product_price_inquiry)
    }
    val defaultOptionLabel = stringResource(R.string.catalog_option_default_label)
    val productUnitPriceWon = (uiModel.product.price as? CatalogPriceUiModel.Available)?.unitPriceWon

    LaunchedEffect(editSourceLineId, isEditMode) {
        if (editSourceLineId != null && !isEditMode) {
            snackbarHostState.showSnackbar(invalidEditMessage, duration = SnackbarDuration.Short)
            onBackClick()
        }
    }

    // 시트를 닫고(hide 완료 뒤 상태 false) 후속 동작을 실행한다. 닫는 중 다시 누른 요청은 무시한다.
    fun closeOptionsSheet(afterClosed: suspend () -> Unit) {
        if (!sheetCloseGate.tryEnter()) return
        scope.launch {
            try {
                closeSheetThen(
                    hide = { if (optionsSheetState.isVisible) optionsSheetState.hide() },
                    markClosed = { isOptionsSheetVisible = false },
                    afterClosed = afterClosed,
                )
            } finally {
                sheetCloseGate.exit()
            }
        }
    }

    // 옵션 그룹이 없는 상품은 기본 한 줄을 담는다. 그룹이 있으면 선택된 옵션만 담는다.
    fun cartLinesForCurrentSelection(): List<CartLine> = if (uiModel.optionGroups.isEmpty()) {
        listOf(
            CartLine(
                productId = uiModel.product.id,
                productName = uiModel.product.name,
                optionId = DEFAULT_OPTION_ID,
                optionLabel = defaultOptionLabel,
                quantity = 1,
                // 옵션 그룹이 없으면 추가금도 없다.
                optionExtraWon = 0L,
                productPriceLabel = productPriceLabel,
                isQuoteRequired = uiModel.product.price is CatalogPriceUiModel.QuoteRequired,
                productUnitPriceWon = productUnitPriceWon,
            ),
        )
    } else {
        selectedOptions
            .filter { selected -> !isEditMode || selected.groupId == editGroup?.id }
            .map { selected ->
                CartLine(
                    productId = uiModel.product.id,
                    productName = uiModel.product.name,
                    optionId = selected.option.id,
                    optionLabel = selected.option.label,
                    quantity = if (isEditMode) editSourceLine!!.quantity else selected.option.quantity.coerceAtLeast(1),
                    optionExtraWon = selected.option.unitPriceWon,
                    productPriceLabel = productPriceLabel,
                    isQuoteRequired = uiModel.product.price is CatalogPriceUiModel.QuoteRequired,
                    productUnitPriceWon = productUnitPriceWon,
                )
            }
    }

    CatalogProductDetailContent(
        uiModel = uiModel.copy(
            optionGroups = if (isEditMode) listOf(editGroup!!) else uiModel.optionGroups,
            product = uiModel.product.copy(isFavorite = isProductFavorite),
            relatedProducts = uiModel.relatedProducts.map { product ->
                product.copy(isFavorite = relatedFavoriteToggles.isFavorite(product.id))
            },
            selectedImageIndex = selectedImageIndex,
            selectedTabIndex = selectedTabIndex,
            optionsSheetVisible = isOptionsSheetVisible,
            isCartOptionEdit = editSourceLineId != null,
            selectedOptions = selectedOptions.filter { selected -> !isEditMode || selected.groupId == editGroup?.id },
            totalPriceLabel = totalPriceLabel,
        ),
        onEvent = { event ->
            when (event) {
                CatalogProductDetailEvent.BackClicked -> onBackClick()

                CatalogProductDetailEvent.CartClicked -> onCartClick()

                is CatalogProductDetailEvent.ImageSelected -> selectedImageIndex = event.index

                is CatalogProductDetailEvent.TabSelected -> selectedTabIndex = event.tabIndex

                is CatalogProductDetailEvent.SelectOptionClicked -> isOptionsSheetVisible = true

                CatalogProductDetailEvent.CloseOptionsSheet -> closeOptionsSheet(afterClosed = {})

                is CatalogProductDetailEvent.OptionSelected -> {
                    if (isEditMode && event.groupId != editGroup?.id) return@CatalogProductDetailContent
                    val selectedOption = uiModel.optionGroups
                        .firstOrNull { group -> group.id == event.groupId }
                        ?.options
                        ?.firstOrNull { option -> option.id == event.optionId }
                    val isAlreadySelected = optionSelections.any { selection ->
                        selection.groupId == event.groupId && selection.optionId == event.optionId
                    }
                    if (selectedOption != null && !isAlreadySelected) {
                        optionSelections = if (isEditMode) {
                            listOf(OptionSelection(event.groupId, event.optionId, editSourceLine!!.quantity))
                        } else {
                            optionSelections + OptionSelection(event.groupId, event.optionId, quantity = 1)
                        }
                    }
                }

                is CatalogProductDetailEvent.QuantityChanged -> {
                    if (isEditMode) return@CatalogProductDetailContent
                    optionSelections = optionSelections.map { selection ->
                        if (selection.groupId == event.groupId && selection.optionId == event.optionId) {
                            selection.copy(quantity = event.quantity.coerceAtLeast(1))
                        } else {
                            selection
                        }
                    }
                }

                is CatalogProductDetailEvent.OptionRemoved -> {
                    if (isEditMode) return@CatalogProductDetailContent
                    optionSelections = optionSelections.filterNot { selection ->
                        selection.groupId == event.groupId && selection.optionId == event.optionId
                    }
                }

                CatalogProductDetailEvent.BuyNowClicked -> closeOptionsSheet(afterClosed = { onBuyNowClick() })

                CatalogProductDetailEvent.QuoteRequestClicked -> {
                    closeOptionsSheet(afterClosed = { onQuoteRequestClick(uiModel.product.id) })
                }

                CatalogProductDetailEvent.AddToCartClicked -> {
                    if (editSourceLineId != null && !isEditMode) return@CatalogProductDetailContent
                    val lines = cartLinesForCurrentSelection()
                    if (isEditMode && (!isSingleValidCartOptionSelection(selectedOptions, editGroup) || lines.size != 1)) {
                        closeOptionsSheet(afterClosed = {
                            snackbarHostState.showSnackbar(invalidEditMessage, duration = SnackbarDuration.Short)
                            onBackClick()
                        })
                        return@CatalogProductDetailContent
                    }
                    if (lines.isNotEmpty()) {
                        closeOptionsSheet(afterClosed = {
                            // NonCancellable 흐름 안에서 결과를 기록만 한다. 안내는 다음 composition에서 띄운다.
                            val saved = if (isEditMode && lines.size == 1) {
                                saveAddToCartLines(lines.totalQuantity()) {
                                    checkNotNull(editSourceLine)
                                    check(onReplaceCartLine(editSourceLine.lineId(), lines.single()))
                                }
                            } else {
                                saveAddToCartLines(lines.totalQuantity()) { onAddToCart(lines) }
                            }
                            if (isEditMode && saved) {
                                onBackClick()
                            } else {
                                onCartAddOutcomeChange(CartAddOutcome(serial = System.nanoTime(), saved = saved))
                            }
                        })
                    }
                }

                is CatalogProductDetailEvent.RelatedProductSelected -> onProductClick(event.productId)

                CatalogProductDetailEvent.ProductFavoriteToggled -> if (isLoggedIn) {
                    isProductFavorite = !isProductFavorite
                } else {
                    onLoginRequired()
                }

                is CatalogProductDetailEvent.FavoriteToggled -> relatedFavoriteToggles.toggle(event.productId)

                CatalogProductDetailEvent.AllReviewsClicked -> onAllReviewsClick(uiModel.product.id)

                CatalogProductDetailEvent.ReviewWriteClicked -> onReviewWriteClick(uiModel.product.id)

                CatalogProductDetailEvent.QuestionWriteClicked -> onQuestionWriteClick(uiModel.product.id)

                // 공유는 이번 범위가 아니다.
                CatalogProductDetailEvent.ShareClicked -> Unit
            }
        },
        optionsSheetState = optionsSheetState,
        snackbarHostState = snackbarHostState,
    )
}

/**
 * 시트 닫힘 한 번을 처리하는 순수 상태. 닫는 중에는 두 번째 요청을 받지 않아 후속 동작이 한 번만 실행된다.
 */
internal class OptionsSheetCloseGate {
    private var isClosing = false

    /** 닫기를 시작할 수 있으면 true. 이미 닫는 중이면 false. */
    fun tryEnter(): Boolean {
        if (isClosing) return false
        isClosing = true
        return true
    }

    fun exit() {
        isClosing = false
    }
}

/** 원본 상품과 옵션이 현재 카탈로그의 정확히 한 옵션 그룹에서 일치할 때만 편집 그룹을 돌려준다. */
internal fun resolveCartOptionEditGroup(
    sourceLine: CartLine?,
    currentProductId: String,
    optionGroups: List<CatalogOptionGroupUiModel>,
): CatalogOptionGroupUiModel? {
    if (sourceLine == null || sourceLine.productId != currentProductId) return null
    return optionGroups.filter { group -> group.options.any { option -> option.id == sourceLine.optionId } }.singleOrNull()
}

internal fun isCartOptionEditAllowed(
    sourceLine: CartLine?,
    currentProductId: String,
    editGroup: CatalogOptionGroupUiModel?,
    isSoldOut: Boolean,
    isQuoteRequired: Boolean,
): Boolean = sourceLine != null && sourceLine.productId == currentProductId && editGroup != null && !isSoldOut && !isQuoteRequired

internal fun isSingleValidCartOptionSelection(
    selectedOptions: List<CatalogSelectedOptionUiModel>,
    editGroup: CatalogOptionGroupUiModel?,
): Boolean {
    if (editGroup == null || selectedOptions.size != 1) return false
    val selected = selectedOptions.single()
    return selected.groupId == editGroup.id && editGroup.options.any { option -> option.id == selected.option.id }
}

/**
 * 시트 닫힘과 후속 동작의 순서를 정한다: hide → 닫힘 상태 false → 후속 동작.
 * hide가 취소돼도(예: 회전으로 Route가 사라지는 중) 닫힘 상태와 후속 동작은 반드시 끝낸다. 담기 저장이 유실되지 않게 하기 위함이다.
 * 취소 예외는 hide 안에서만 삼키고, 후속 동작에서 나는 취소는 그대로 전파된다.
 */
internal suspend fun closeSheetThen(
    hide: suspend () -> Unit,
    markClosed: () -> Unit,
    afterClosed: suspend () -> Unit,
) {
    withContext(NonCancellable) {
        try {
            hide()
        } catch (exception: CancellationException) {
            // hide가 취소되어도 아래 상태 정리와 후속 동작은 실행해야 하므로 여기서만 삼킨다.
        } finally {
            markClosed()
        }
        afterClosed()
    }
}

/** 복원 가능한 옵션 선택 한 줄. 옵션 표시 정보는 담지 않는다. */
internal data class OptionSelection(
    val groupId: String,
    val optionId: String,
    val quantity: Int,
)

// 세 값(그룹 id, 옵션 id, 수량)을 평평한 목록으로 저장한다. 세 개씩 묶어 복원하고, 깨진 항목은 버린다.
internal val OptionSelectionsSaver: Saver<List<OptionSelection>, ArrayList<String>> = Saver(
    save = { selections ->
        ArrayList(selections.flatMap { selection -> listOf(selection.groupId, selection.optionId, selection.quantity.toString()) })
    },
    restore = { saved ->
        saved.chunked(SAVED_FIELDS_PER_SELECTION)
            .filter { fields -> fields.size == SAVED_FIELDS_PER_SELECTION }
            .mapNotNull { fields ->
                val quantity = fields[2].toIntOrNull()
                if (fields[0].isEmpty() || fields[1].isEmpty() || quantity == null) {
                    null
                } else {
                    OptionSelection(groupId = fields[0], optionId = fields[1], quantity = quantity.coerceAtLeast(1))
                }
            }
    },
)

private const val SAVED_FIELDS_PER_SELECTION = 3
private const val DEFAULT_OPTION_ID = "default"

// 현재 옵션 그룹에 없는 선택(상품 정보 변경 등)은 버린다.
internal fun resolveSelectedOptions(
    optionGroups: List<CatalogOptionGroupUiModel>,
    selections: List<OptionSelection>,
): List<CatalogSelectedOptionUiModel> = selections.mapNotNull { selection ->
    optionGroups
        .firstOrNull { group -> group.id == selection.groupId }
        ?.options
        ?.firstOrNull { option -> option.id == selection.optionId }
        ?.let { option ->
            CatalogSelectedOptionUiModel(selection.groupId, option.copy(quantity = selection.quantity))
        }
}

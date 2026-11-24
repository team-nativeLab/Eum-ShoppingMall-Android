package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.data.local.CartLineReplaceResult
import com.nativelap.eumshoppingmall.data.local.totalQuantity
import com.nativelap.eumshoppingmall.ui.common.rememberFavoriteToggles
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 상품 상세(A-05)와 옵션 시트(A-05s).
 *
 * 옵션 시트는 이 Route의 상태로 연다. 시트 닫힘은 Route가 맡아 후속 동작보다 먼저 완료한다.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CatalogProductDetailRoute(
    uiModel: CatalogProductDetailUiModel,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onBuyNowClick: (List<CartLine>) -> Unit,
    // 가격 미표시 상품의 문의 버튼. 상품명을 1:1 문의 제목에 미리 채우도록 넘긴다.
    onPriceInquiryClick: (productName: String) -> Unit,
    onProductClick: (productId: String) -> Unit,
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
    onAllReviewsClick: (productId: String) -> Unit,
    onReviewWriteClick: (productId: String) -> Unit,
    onQuestionWriteClick: (productId: String) -> Unit,
    onAddToCart: suspend (addedLines: List<CartLine>) -> Unit,
    editSourceLineId: String? = null,
    editSourceLine: CartLine? = null,
    /** 장바구니를 아직 읽지 못했으면 false. 이때는 원본 줄 유무를 판정하지 않는다(M-A). */
    isCartLoaded: Boolean = true,
    onReplaceCartLine: suspend (sourceLineId: String, replacement: CartLine) -> CartLineReplaceResult = { _, _ -> CartLineReplaceResult.Failed },
    cartAddOutcome: CartAddOutcome?,
    onCartAddOutcomeChange: (CartAddOutcome?) -> Unit,
    /** 편집 무효·저장 실패 안내는 상세 화면이 아니라 돌아갈 장바구니 화면에서 띄운다(C). */
    onCartEditNotice: (CartEditNotice) -> Unit = {},
    onUnavailableAction: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val optionsSheetState = rememberModalBottomSheetState()
    val sheetCloseGate = remember { OptionsSheetCloseGate() }
    var selectedImageIndex by rememberSaveable { mutableStateOf(uiModel.selectedImageIndex) }
    var selectedTabIndex by rememberSaveable { mutableStateOf(uiModel.selectedTabIndex) }
    var isOptionsSheetVisible by rememberSaveable(editSourceLineId) { mutableStateOf(uiModel.optionsSheetVisible) }
    // 편집 시트는 유효한 편집 진입에서 한 번만 자동으로 연다. 사용자가 닫은 뒤 회전해도 다시 열지 않는다(M-B).
    var hasAutoOpenedEditSheet by rememberSaveable(editSourceLineId) { mutableStateOf(false) }
    // 편집 저장 중(성공 후 원본 소멸 포함)에는 원본 줄 유무로 무효 안내를 띄우지 않는다(M-A).
    var isEditSaving by remember { mutableStateOf(false) }
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
        isPriceUndisclosed = uiModel.product.price is CatalogPriceUiModel.Undisclosed,
    )
    val editEntry = cartOptionEditEntry(
        editSourceLineId = editSourceLineId,
        isCartLoaded = isCartLoaded,
        sourceLine = editSourceLine,
        currentProductId = uiModel.product.id,
        optionGroups = uiModel.optionGroups,
        isSoldOut = uiModel.product.isSoldOut,
        isPriceUndisclosed = uiModel.product.price is CatalogPriceUiModel.Undisclosed,
        isSaving = isEditSaving,
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
    // 담기 결과 안내는 공용 헬퍼가 맡는다(홈 빠른 담기와 같은 규칙).
    val snackbarHostState = rememberCartAddSnackbarHostState(cartAddOutcome, onCartAddOutcomeChange, onCartClick)
    val productPriceLabel = when (val price = uiModel.product.price) {
        is CatalogPriceUiModel.Available -> price.currentLabel
        CatalogPriceUiModel.Undisclosed -> stringResource(R.string.product_price_inquiry)
    }
    val defaultOptionLabel = stringResource(R.string.catalog_option_default_label)
    val productUnitPriceWon = (uiModel.product.price as? CatalogPriceUiModel.Available)?.unitPriceWon

    // 유효한 편집 진입에서만 시트를 연다(M-B). 무효 진입은 시트를 열지 않고 안내 뒤 장바구니로 돌아간다.
    LaunchedEffect(editEntry) {
        if (editEntry == CartOptionEditEntry.Editable && !hasAutoOpenedEditSheet) {
            hasAutoOpenedEditSheet = true
            isOptionsSheetVisible = true
        }
        if (editEntry == CartOptionEditEntry.Invalid) {
            // 안내가 열린 시트에 가려지지 않도록 시트를 먼저 내린다(W3).
            isOptionsSheetVisible = optionSheetVisibleFor(editEntry, isOptionsSheetVisible)
            onCartEditNotice(CartEditNotice.InvalidEdit)
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
                optionId = CATALOG_DEFAULT_OPTION_ID,
                optionLabel = defaultOptionLabel,
                quantity = 1,
                // 옵션 그룹이 없으면 추가금도 없다.
                optionExtraWon = 0L,
                productPriceLabel = productPriceLabel,
                isPriceUndisclosed = uiModel.product.price is CatalogPriceUiModel.Undisclosed,
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
                    isPriceUndisclosed = uiModel.product.price is CatalogPriceUiModel.Undisclosed,
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

                // 무효 편집 진입에서는 시트를 열지 않는다(W3). 일반 흐름은 그대로 연다.
                is CatalogProductDetailEvent.SelectOptionClicked -> if (canOpenOptionSheet(editEntry)) isOptionsSheetVisible = true

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

                CatalogProductDetailEvent.BuyNowClicked -> closeOptionsSheet(afterClosed = { onBuyNowClick(cartLinesForCurrentSelection()) })

                CatalogProductDetailEvent.PriceInquiryClicked -> {
                    closeOptionsSheet(afterClosed = { onPriceInquiryClick(uiModel.product.name) })
                }

                CatalogProductDetailEvent.AddToCartClicked -> {
                    if (editSourceLineId != null && !isEditMode) return@CatalogProductDetailContent
                    val lines = cartLinesForCurrentSelection()
                    if (isEditMode && (!isSingleValidCartOptionSelection(selectedOptions, editGroup) || lines.size != 1)) {
                        closeOptionsSheet(afterClosed = {
                            onCartEditNotice(CartEditNotice.InvalidEdit)
                            onBackClick()
                        })
                        return@CatalogProductDetailContent
                    }
                    if (lines.isNotEmpty()) {
                        closeOptionsSheet(afterClosed = {
                            // NonCancellable 흐름 안에서 결과를 기록만 한다. 안내는 다음 composition에서 띄운다.
                            var replaceResult: CartLineReplaceResult? = null
                            val saved = if (isEditMode && lines.size == 1) {
                                isEditSaving = true
                                saveAddToCartLines(lines.totalQuantity()) {
                                    checkNotNull(editSourceLine)
                                    replaceResult = onReplaceCartLine(editSourceLine.lineId(), lines.single())
                                    check(replaceResult == CartLineReplaceResult.Replaced)
                                }
                            } else {
                                saveAddToCartLines(lines.totalQuantity()) { onAddToCart(lines) }
                            }
                            if (isEditMode) {
                                // 편집 실패도 장바구니로 돌아간다(기존 줄은 유지). 원인에 따라 안내 문구를 고른다(W4).
                                if (!saved) {
                                    // 저장이 실패했으면 원본 줄이 그대로이므로 저장 중 표시를 풀어 판정이 다시 원본 기준으로 돌게 한다.
                                    isEditSaving = false
                                    onCartEditNotice(
                                        when (cartOptionEditSaveFailure(replaceResult) ?: CartOptionEditSaveFailure.SaveFailed) {
                                            CartOptionEditSaveFailure.SourceMissing -> CartEditNotice.SourceMissing
                                            CartOptionEditSaveFailure.SaveFailed -> CartEditNotice.SaveFailed
                                        },
                                    )
                                }
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

                CatalogProductDetailEvent.ShareClicked -> onUnavailableAction()
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
    isPriceUndisclosed: Boolean,
): Boolean = sourceLine != null && sourceLine.productId == currentProductId && editGroup != null && !isSoldOut && !isPriceUndisclosed

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

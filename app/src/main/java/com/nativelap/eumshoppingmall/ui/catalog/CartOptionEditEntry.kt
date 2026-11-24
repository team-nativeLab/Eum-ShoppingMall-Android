package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.data.local.CartLineReplaceResult

/**
 * 장바구니 옵션 편집 진입 상태.
 * [Pending]은 장바구니를 아직 읽지 못한 상태라 원본 줄 유무를 판정하지 않는다(무효 안내를 띄우지 않는다).
 * [Saving]은 편집 저장 중이거나 저장이 성공해 원본 줄이 사라진 상태라 무효 안내를 띄우지 않는다.
 */
internal enum class CartOptionEditEntry { NotEditing, Pending, Editable, Invalid, Saving }

/**
 * 편집 진입을 판정한다. 시트는 [CartOptionEditEntry.Editable]일 때만 열고, [CartOptionEditEntry.Invalid]이면 안내 뒤 장바구니로 돌아간다.
 */
internal fun cartOptionEditEntry(
    editSourceLineId: String?,
    isCartLoaded: Boolean,
    sourceLine: CartLine?,
    currentProductId: String,
    optionGroups: List<CatalogOptionGroupUiModel>,
    isSoldOut: Boolean,
    isPriceUndisclosed: Boolean,
    isSaving: Boolean,
): CartOptionEditEntry = when {
    editSourceLineId == null -> CartOptionEditEntry.NotEditing

    isSaving -> CartOptionEditEntry.Saving

    !isCartLoaded -> CartOptionEditEntry.Pending

    else -> {
        val editGroup = resolveCartOptionEditGroup(sourceLine, currentProductId, optionGroups)
        if (isCartOptionEditAllowed(sourceLine, currentProductId, editGroup, isSoldOut, isPriceUndisclosed)) {
            CartOptionEditEntry.Editable
        } else {
            CartOptionEditEntry.Invalid
        }
    }
}

/** 편집 저장 실패 안내를 고르는 원인. [SourceMissing]은 재시도가 아니라 "찾을 수 없음" 안내를 쓴다(W4). */
internal enum class CartOptionEditSaveFailure { SourceMissing, SaveFailed }

/** 교체 결과의 실패 원인. 교체에 성공했으면 null이다. 결과가 없으면(예외 등) 저장 실패로 본다. */
internal fun cartOptionEditSaveFailure(result: CartLineReplaceResult?): CartOptionEditSaveFailure? = when (result) {
    CartLineReplaceResult.Replaced -> null
    CartLineReplaceResult.SourceMissing -> CartOptionEditSaveFailure.SourceMissing
    CartLineReplaceResult.Failed, null -> CartOptionEditSaveFailure.SaveFailed
}

/** 편집 진입이 무효이면 옵션 시트를 내린다. 무효 안내가 열린 시트에 가려지지 않게 하려는 판정이다(W3). */
internal fun optionSheetVisibleFor(entry: CartOptionEditEntry, isVisible: Boolean): Boolean = if (entry == CartOptionEditEntry.Invalid) false else isVisible

/** 편집 진입이 무효이면 옵션 시트를 열지 않는다. 그 밖의 진입(일반 담기 흐름 포함)은 기존처럼 연다(W3). */
internal fun canOpenOptionSheet(entry: CartOptionEditEntry): Boolean = entry != CartOptionEditEntry.Invalid

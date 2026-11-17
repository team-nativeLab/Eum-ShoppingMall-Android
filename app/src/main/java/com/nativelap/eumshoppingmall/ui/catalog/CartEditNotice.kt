package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

/** 편집 화면을 떠난 뒤 장바구니 화면이 보여 줄 안내 종류. */
enum class CartEditNotice { InvalidEdit, SourceMissing, SaveFailed }

/**
 * 장바구니 화면으로 넘기는 편집 결과. [shown]은 장바구니가 이미 안내를 보여 줬다는 표시다.
 * 회전이나 재진입에서 같은 안내가 두 번 뜨지 않도록 상태에 함께 저장한다(C).
 */
data class CartEditOutcome(
    val serial: Long,
    val notice: CartEditNotice,
    val shown: Boolean = false,
) {
    fun markedShown(): CartEditOutcome = copy(shown = true)

    companion object {
        val Saver: Saver<CartEditOutcome?, Any> = listSaver(
            save = { outcome -> outcome?.let { listOf(it.serial, it.notice.name, it.shown) } ?: emptyList<Any>() },
            restore = { saved ->
                if (saved.size == 3) {
                    CartEditOutcome(saved[0] as Long, CartEditNotice.valueOf(saved[1] as String), saved[2] as Boolean)
                } else {
                    null
                }
            },
        )
    }
}

/**
 * 안내를 비운다. 현재 결과의 serial이 표시한 결과와 같을 때만 비워, 그 사이 도착한 새 결과를 지우지 않는다(S1).
 * 같은 값이면 기존처럼 비운다.
 */
internal fun CartEditOutcome?.clearedIfShown(shownSerial: Long): CartEditOutcome? = if (this?.serial == shownSerial) null else this

/** 장바구니가 지금 보여 줄 안내. 이미 보여 준 결과는 null이라 다시 뜨지 않는다(C). */
internal fun cartEditNoticeToShow(outcome: CartEditOutcome?): CartEditNotice? = outcome?.takeUnless { it.shown }?.notice

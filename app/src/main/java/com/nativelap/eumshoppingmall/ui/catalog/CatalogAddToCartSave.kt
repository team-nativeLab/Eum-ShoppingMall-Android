package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import kotlinx.coroutines.CancellationException

/**
 * 담기 결과. Route 밖(화면 상태)에 보관했다가 다음 composition에서 안내를 띄운다(N5).
 * serial은 같은 결과가 두 번 와도 새 안내로 인식하게 하는 값이다.
 */
data class CartAddOutcome(
    val serial: Long,
    val saved: Boolean,
) {
    companion object {
        val Saver: Saver<CartAddOutcome?, Any> = listSaver(
            save = { outcome -> outcome?.let { listOf(it.serial, it.saved) } ?: emptyList<Any>() },
            restore = { saved ->
                if (saved.size == 2) CartAddOutcome(saved[0] as Long, saved[1] as Boolean) else null
            },
        )
    }
}

/**
 * 담기 저장을 실행하고 성공 여부만 돌려준다. 성공했을 때만 완료 안내를 띄운다.
 * 저장 실패는 false로 바꾸고, 취소 예외는 삼키지 않고 그대로 던진다.
 */
internal suspend fun saveAddToCartLines(
    addedCount: Int,
    save: suspend (Int) -> Unit,
): Boolean = try {
    save(addedCount)
    true
} catch (exception: CancellationException) {
    throw exception
} catch (exception: Exception) {
    false
}

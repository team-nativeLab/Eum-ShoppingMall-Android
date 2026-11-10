package com.nativelap.eumshoppingmall.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * 장바구니에 담은 옵션 한 줄. 상품 목록 원천이 아니라 담을 때의 스냅샷이다.
 * 이미지는 리소스 ID가 빌드마다 바뀔 수 있어 저장하지 않는다.
 */
data class CartLine(
    val productId: String,
    val productName: String,
    val optionId: String,
    val optionLabel: String,
    val quantity: Int,
    /** 옵션 1개당 추가금(원). null이면 추가금을 모른다. */
    val optionExtraWon: Long?,
    /** 담을 때의 상품 기본가 표시 문구. 가격 문의 상품은 문의 문구다. */
    val productPriceLabel: String,
    val isQuoteRequired: Boolean,
    /** 상품 기본가(원). null이면 가격 미상(구 형식 줄 포함)이다. */
    val productUnitPriceWon: Long? = null,
) {
    /** 같은 상품·같은 옵션을 가리키는 식별자. 장바구니 화면의 항목 id와 같은 규칙이다. */
    fun lineId(): String = "$productId/$optionId"
}

/**
 * 장바구니 줄을 DataStore에 저장하고 읽는다(이전 개수 저장을 확장한 최소 저장소).
 * 같은 상품·같은 옵션을 다시 담으면 줄을 새로 만들지 않고 수량을 더한다.
 * 배지 개수는 저장된 줄의 수량 합이다([totalQuantity]).
 */
class CartCountStore(private val dataStore: DataStore<Preferences>) {
    // 파일 읽기가 실패하면 빈 장바구니로 대체해 앱 실행을 막지 않는다.
    val cartLines: Flow<List<CartLine>> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences -> decodeCartLines(preferences[CART_LINES_KEY].orEmpty()) }

    /** 담은 줄을 저장 목록에 합친다. 읽기와 쓰기를 한 번의 edit로 묶어 연속으로 담아도 누락되지 않는다. */
    suspend fun addCartLines(addedLines: List<CartLine>) {
        dataStore.edit { preferences ->
            preferences[CART_LINES_KEY] = mergeCartLinesRaw(preferences[CART_LINES_KEY].orEmpty(), addedLines)
        }
    }

    /** 줄의 수량을 바꾼다. 수량은 1 이상이고, 스테퍼와 같이 Int 최댓값까지 허용한다. 없는 줄은 무시한다. */
    suspend fun updateCartLineQuantity(lineId: String, quantity: Int) {
        dataStore.edit { preferences ->
            preferences[CART_LINES_KEY] = editCartLinesRaw(preferences[CART_LINES_KEY].orEmpty()) { line ->
                if (line.lineId() == lineId) line.copy(quantity = quantity.coerceAtLeast(1)) else line
            }
        }
    }

    /** 선택한 줄들을 저장 목록에서 지운다. 원문을 알 수 없는 깨진 줄은 그대로 남긴다. */
    suspend fun removeCartLines(lineIds: Set<String>) {
        dataStore.edit { preferences ->
            preferences[CART_LINES_KEY] = editCartLinesRaw(preferences[CART_LINES_KEY].orEmpty()) { line ->
                if (line.lineId() in lineIds) null else line
            }
        }
    }

    private companion object {
        // 이전 개수 저장 키(cart_item_count)는 옮기지 않는다. 기존 설치의 배지는 새 줄 저장 기준으로 다시 센다.
        val CART_LINES_KEY = stringPreferencesKey("cart_lines")
    }
}

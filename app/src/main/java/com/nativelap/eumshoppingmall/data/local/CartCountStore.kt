package com.nativelap.eumshoppingmall.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/** 로컬에 저장된 장바구니 상품 개수를 읽고 쓴다. 상품 목록이나 서버 값은 저장하지 않는다. */
class CartCountStore(private val dataStore: DataStore<Preferences>) {
    // 파일 읽기가 실패하면 빈 설정으로 대체해 배지가 0으로 보이게 한다. 앱 실행을 막지 않기 위함이다.
    val cartItemCount: Flow<Int> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences -> preferences[CART_ITEM_COUNT_KEY] ?: 0 }

    /** 장바구니 개수를 저장한다. 음수는 저장하지 않는다. */
    suspend fun saveCartItemCount(count: Int) {
        require(count >= 0) { "장바구니 개수는 0 이상이어야 합니다." }
        dataStore.edit { preferences ->
            preferences[CART_ITEM_COUNT_KEY] = count
        }
    }

    private companion object {
        val CART_ITEM_COUNT_KEY = intPreferencesKey("cart_item_count")
    }
}

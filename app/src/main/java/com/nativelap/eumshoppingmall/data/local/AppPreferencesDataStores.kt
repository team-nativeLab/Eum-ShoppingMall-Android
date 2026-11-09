package com.nativelap.eumshoppingmall.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// 호출자는 Activity가 아닌 applicationContext를 넘겨 Activity 참조가 남지 않게 한다.
internal val Context.cartPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "cart_preferences",
)

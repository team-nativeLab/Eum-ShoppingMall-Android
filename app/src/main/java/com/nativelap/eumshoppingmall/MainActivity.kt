package com.nativelap.eumshoppingmall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.data.local.CartCountStore
import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.data.local.cartPreferencesDataStore
import com.nativelap.eumshoppingmall.data.local.totalQuantity
import com.nativelap.eumshoppingmall.navigation.EumMainNavDisplay
import com.nativelap.eumshoppingmall.ui.home.HomeRoute
import com.nativelap.eumshoppingmall.ui.screendata.createShoppingScreenDataProvider
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

class MainActivity : ComponentActivity() {
    // Activity Context를 저장하지 않도록 applicationContext에서 저장소를 만든다.
    // 초기화 시점에는 applicationContext가 없으므로 lazy로 지연한다.
    private val cartCountStore by lazy { CartCountStore(applicationContext.cartPreferencesDataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EumShoppingMallTheme {
                // 초기값 null은 "아직 읽지 못함"이다. 빈 목록으로 두면 복원·편집 직후 원본이 없는 것으로 오판한다(M-A).
                val cartLines by cartCountStore.cartLines.collectAsState<List<CartLine>, List<CartLine>?>(initial = null)
                val screenDataProvider = remember { createShoppingScreenDataProvider() }
                EumMainNavDisplay(
                    cartItemCount = cartLines.orEmpty().totalQuantity(),
                    savedCartLines = cartLines,
                    screenDataProvider = screenDataProvider,
                    onAddToCart = cartCountStore::addCartLines,
                    onCartQuantityChanged = cartCountStore::updateCartLineQuantity,
                    onCartItemsRemoved = cartCountStore::removeCartLines,
                    onCartLinesRestored = cartCountStore::addCartLines,
                    onReplaceCartLine = cartCountStore::replaceCartLine,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    EumShoppingMallTheme {
        HomeRoute()
    }
}

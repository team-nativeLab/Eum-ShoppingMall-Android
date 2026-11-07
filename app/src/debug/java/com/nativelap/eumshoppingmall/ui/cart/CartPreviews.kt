package com.nativelap.eumshoppingmall.ui.cart

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.cart.view.CartScreen
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewCartItems = listOf(
    CartItemUiModel("helmet", "세이프가드 ABS 안전모", "옵션: 사이즈 L", 1, "₩22,000", "₩18,000", isSelected = true),
    CartItemUiModel("shoes", "경량 안전화 4인치", "옵션: 사이즈 L", 1, "₩54,000", "₩54,000", isSelected = true),
    CartItemUiModel("extinguisher", "분말 소화기 3.3kg", "옵션: 사이즈 L", 1, "₩32,000", "₩32,000", isSoldOut = true),
    CartItemUiModel("quote", "코팅 장갑 10켤레", "옵션: 옵션명", 1, "", "", type = CartItemType.QuoteRequired),
)

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "장바구니 선택·품절·견적 혼합")
@Composable
private fun CartMixedPreview() {
    EumShoppingMallTheme {
        CartScreen(
            uiModel = CartUiModel(previewCartItems, "₩72,000", "₩0", "₩72,000"),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "장바구니 빈 상태")
@Composable
private fun CartEmptyPreview() {
    EumShoppingMallTheme {
        CartScreen(uiModel = CartUiModel(), onEvent = {})
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.2f, name = "좁은 장바구니")
@Composable
private fun CartNarrowPreview() {
    EumShoppingMallTheme {
        CartScreen(
            uiModel = CartUiModel(previewCartItems.take(2), "₩72,000", "₩0", "₩72,000"),
            onEvent = {},
        )
    }
}

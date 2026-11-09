package com.nativelap.eumshoppingmall.ui.cart

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.cart.view.CartScreen
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewCartItems = listOf(
    CartItemUiModel("helmet", "세이프가드 ABS 안전모", "옵션: 사이즈 L", 1, "₩22,000", "₩18,000", isSelected = true),
    CartItemUiModel("shoes", "경량 안전화 4인치", "옵션: 사이즈 L", 1, "₩54,000", "₩54,000", isSelected = true),
    CartItemUiModel("extinguisher", "분말 소화기 3.3kg", "옵션: 사이즈 L", 1, "₩32,000", "₩32,000", isSoldOut = true),
    CartItemUiModel("quote", "코팅 장갑 10켤레", "옵션: 옵션명", 1, "", "", type = CartItemType.QuoteRequired),
)

// A-07 Figma 샘플: 주문 가능 3개(선택 2, 품절 1), 견적 상품 없음.
@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "장바구니 선택·품절")
@Composable
private fun CartMixedPreview() {
    EumShoppingMallTheme {
        CartScreen(
            uiModel = CartUiModel(previewCartItems.take(3), "₩72,000", "₩0", "₩72,000"),
            onEvent = {},
            isLoggedIn = false,
        )
    }
}

// A-07h Figma 샘플: 주문 가능 2개(선택) + 주문할 수 없는 견적 상품 1개, 품절 상품 없음.
@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "장바구니 선택·견적 혼합")
@Composable
private fun CartQuoteMixedPreview() {
    EumShoppingMallTheme {
        CartScreen(
            uiModel = CartUiModel(previewCartItems.filterNot { it.isSoldOut }, "₩72,000", "₩0", "₩72,000"),
            onEvent = {},
            isLoggedIn = false,
        )
    }
}

// 로그인 상태에서는 로그인 안내 배너가 보이지 않는다. 비로그인 배너는 위 샘플들(isLoggedIn = false)에서 확인한다.
@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "장바구니 로그인 상태")
@Composable
private fun CartLoggedInPreview() {
    EumShoppingMallTheme {
        CartScreen(
            uiModel = CartUiModel(previewCartItems.take(3), "₩72,000", "₩0", "₩72,000"),
            onEvent = {},
            isLoggedIn = true,
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "장바구니 빈 상태")
@Composable
private fun CartEmptyPreview() {
    EumShoppingMallTheme {
        CartScreen(uiModel = CartUiModel(), onEvent = {}, isLoggedIn = false)
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.2f, name = "좁은 장바구니")
@Composable
private fun CartNarrowPreview() {
    EumShoppingMallTheme {
        CartScreen(
            uiModel = CartUiModel(previewCartItems.take(2), "₩72,000", "₩0", "₩72,000"),
            onEvent = {},
            isLoggedIn = false,
        )
    }
}

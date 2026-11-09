package com.nativelap.eumshoppingmall.ui.recent

import androidx.compose.runtime.Composable

@Composable
fun RecentProductsRoute(
    uiModel: RecentProductsUiModel,
    onBackClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    onBrowseProductsClick: () -> Unit,
) {
    RecentProductsScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                RecentProductsEvent.BackClicked -> onBackClick()

                RecentProductsEvent.BrowseProductsClicked -> onBrowseProductsClick()

                is RecentProductsEvent.ProductClicked -> onProductClick(event.productId)

                // 최근 본 상품 삭제는 데이터 계층이 생긴 뒤 연결한다.
                RecentProductsEvent.DeleteAllClicked,
                is RecentProductsEvent.ProductRemoved,
                -> Unit
            }
        },
    )
}

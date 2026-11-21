package com.nativelap.eumshoppingmall.ui.recent

import androidx.compose.runtime.Composable

@Composable
fun RecentProductsRoute(
    uiModel: RecentProductsUiModel,
    onBackClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    onBrowseProductsClick: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    RecentProductsScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                RecentProductsEvent.BackClicked -> onBackClick()

                RecentProductsEvent.BrowseProductsClicked -> onBrowseProductsClick()

                is RecentProductsEvent.ProductClicked -> onProductClick(event.productId)

                RecentProductsEvent.DeleteAllClicked,
                is RecentProductsEvent.ProductRemoved,
                -> onUnavailableAction()
            }
        },
    )
}

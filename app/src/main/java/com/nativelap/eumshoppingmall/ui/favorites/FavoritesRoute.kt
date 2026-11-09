package com.nativelap.eumshoppingmall.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun FavoritesRoute(
    uiModel: FavoritesUiModel,
    onBackClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    onBrowseProductsClick: () -> Unit,
) {
    var isEditing by rememberSaveable { mutableStateOf(uiModel.isEditing) }
    FavoritesScreen(
        uiModel = uiModel.copy(isEditing = isEditing),
        onEvent = { event ->
            when (event) {
                FavoritesEvent.BackClicked -> onBackClick()

                FavoritesEvent.EditClicked -> isEditing = !isEditing

                FavoritesEvent.BrowseProductsClicked -> onBrowseProductsClick()

                is FavoritesEvent.ProductClicked -> onProductClick(event.productId)

                // 정렬과 찜 해제 저장은 데이터 계층이 생긴 뒤 연결한다.
                FavoritesEvent.SortClicked,
                is FavoritesEvent.FavoriteToggled,
                -> Unit
            }
        },
    )
}

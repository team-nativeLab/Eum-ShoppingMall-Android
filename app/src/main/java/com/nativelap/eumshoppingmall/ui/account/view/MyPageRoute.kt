package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.common.rememberFavoriteToggles

@Composable
fun MyPageRoute(
    uiModel: MyPageUiModel,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onLoginClick: () -> Unit,
    onDrawerClick: () -> Unit,
    onShortcutClick: (shortcutId: String) -> Unit,
    onMenuRowClick: (rowId: String) -> Unit,
    onProductClick: (productId: String) -> Unit,
    onUnavailableAction: () -> Unit,
) {
    // 최근 본 상품의 찜은 화면 로컬 토글이다(서버 연동 전). 비로그인이면 토글하지 않고 로그인으로 보낸다.
    val favoriteToggles = rememberFavoriteToggles(
        initialFavoriteIds = emptySet(),
        isLoggedIn = uiModel.isLoggedIn,
        onLoginRequired = onLoginClick,
    )
    MyPageScreen(
        uiModel = uiModel.copy(
            recentProducts = uiModel.recentProducts.map { product ->
                product.copy(isFavorite = favoriteToggles.isFavorite(product.id))
            },
        ),
        cartItemCount = cartItemCount,
        onEvent = { event ->
            when (event) {
                MyPageScreenEvent.LoginClicked -> onLoginClick()

                MyPageScreenEvent.DrawerClicked -> onDrawerClick()

                is MyPageScreenEvent.ShortcutClicked -> onShortcutClick(event.shortcutId)

                is MyPageScreenEvent.MenuRowClicked -> onMenuRowClick(event.rowId)

                is MyPageScreenEvent.BottomNavigationClicked -> onTabSelected(event.tab)

                is MyPageScreenEvent.ProductClicked -> onProductClick(event.productId)

                is MyPageScreenEvent.FavoriteClicked -> favoriteToggles.toggle(event.productId)

                // 내 정보 관리(A-23)는 아직 앱 목적지와 연결되지 않았다.
                MyPageScreenEvent.ProfileClicked -> onUnavailableAction()
            }
        },
    )
}

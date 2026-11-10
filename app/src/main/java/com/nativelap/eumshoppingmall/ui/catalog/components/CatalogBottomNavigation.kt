package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.MainBottomNavigationBar
import com.nativelap.eumshoppingmall.ui.common.MainBottomNavigationItem
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.theme.eumColors


@Composable
fun CatalogBottomNavigation(
    selectedItem: MainNavigationTab,
    onItemSelected: (MainNavigationTab) -> Unit,
    modifier: Modifier = Modifier,
    cartItemCount: Int = 0,
) {
    MainBottomNavigationBar(
        items = listOf(
            MainBottomNavigationItem(MainNavigationTab.Home, R.drawable.ic_house, R.string.common_home),
            MainBottomNavigationItem(MainNavigationTab.Categories, R.drawable.ic_layout_grid, R.string.common_categories),
            MainBottomNavigationItem(MainNavigationTab.Search, R.drawable.ic_search, R.string.common_search),
            MainBottomNavigationItem(MainNavigationTab.Cart, R.drawable.ic_shopping_cart, R.string.common_cart),
            // Figma 하단 탭 라벨은 "마이"다(홈 탭과 같은 문자열).
            MainBottomNavigationItem(MainNavigationTab.MyPage, R.drawable.ic_user_round, R.string.home_tab_my_page),
        ),
        selectedItem = selectedItem,
        onItemSelected = onItemSelected,
        cartItemCount = cartItemCount,
        backgroundColor = MaterialTheme.eumColors.background,
        modifier = modifier,
    )
}

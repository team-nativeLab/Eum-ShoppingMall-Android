package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.common.MainBottomNavigationBar
import com.nativelap.eumshoppingmall.ui.common.MainBottomNavigationItem
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeBottomNavigation(
    selectedItem: MainNavigationTab,
    onItemSelected: (MainNavigationTab) -> Unit,
    cartItemCount: Int = 0,
    modifier: Modifier = Modifier,
) {
    MainBottomNavigationBar(
        items = listOf(
            MainBottomNavigationItem(MainNavigationTab.Home, R.drawable.ic_house, R.string.home_tab_home),
            MainBottomNavigationItem(MainNavigationTab.Categories, R.drawable.ic_layout_grid, R.string.home_tab_categories),
            MainBottomNavigationItem(MainNavigationTab.Search, R.drawable.ic_search, R.string.home_tab_search),
            MainBottomNavigationItem(MainNavigationTab.Cart, R.drawable.ic_shopping_cart, R.string.home_tab_cart),
            MainBottomNavigationItem(MainNavigationTab.MyPage, R.drawable.ic_user_round, R.string.home_tab_my_page),
        ),
        selectedItem = selectedItem,
        onItemSelected = onItemSelected,
        cartItemCount = cartItemCount,
        backgroundColor = MaterialTheme.eumColors.surface,
        modifier = modifier,
    )
}

@Composable
fun HomeNavigationRail(
    selectedItem: MainNavigationTab,
    onItemSelected: (MainNavigationTab) -> Unit,
    cartItemCount: Int = 0,
    modifier: Modifier = Modifier,
) {
    val cartCountDescription = stringResource(
        R.string.home_cart_badge_content_description,
        cartItemCount,
    )
    val items = listOf(
        Triple(MainNavigationTab.Home, R.drawable.ic_house, R.string.home_tab_home),
        Triple(MainNavigationTab.Categories, R.drawable.ic_layout_grid, R.string.home_tab_categories),
        Triple(MainNavigationTab.Search, R.drawable.ic_search, R.string.home_tab_search),
        Triple(MainNavigationTab.Cart, R.drawable.ic_shopping_cart, R.string.home_tab_cart),
        Triple(MainNavigationTab.MyPage, R.drawable.ic_user_round, R.string.home_tab_my_page),
    )
    NavigationRail(
        modifier = modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.eumColors.background,
    ) {
        items.forEach { (item, iconRes, labelRes) ->
            NavigationRailItem(
                selected = selectedItem == item,
                onClick = { onItemSelected(item) },
                icon = {
                    if (item == MainNavigationTab.Cart && cartItemCount > 0) {
                        Box(
                            modifier = Modifier.size(width = 48.dp, height = 32.dp).semantics {
                                contentDescription = cartCountDescription
                            },
                        ) {
                            Icon(
                                painterResource(iconRes),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center).size(24.dp),
                            )
                            Box(
                                modifier = Modifier.align(Alignment.TopEnd)
                                    .widthIn(min = 16.dp).heightIn(min = 16.dp)
                                    .clip(CircleShape).background(MaterialTheme.eumColors.textPrimary)
                                    .padding(horizontal = 3.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = if (cartItemCount > 99) "99+" else cartItemCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.eumColors.inverse,
                                    maxLines = 1,
                                )
                            }
                        }
                    } else {
                        Icon(painterResource(iconRes), contentDescription = null)
                    }
                },
                label = { Text(stringResource(labelRes)) },
                colors = androidx.compose.material3.NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.eumColors.textPrimary,
                    selectedTextColor = MaterialTheme.eumColors.textPrimary,
                    unselectedIconColor = MaterialTheme.eumColors.textTertiary,
                    unselectedTextColor = MaterialTheme.eumColors.textTertiary,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}

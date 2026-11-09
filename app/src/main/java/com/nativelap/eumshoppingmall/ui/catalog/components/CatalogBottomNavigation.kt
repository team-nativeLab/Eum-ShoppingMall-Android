package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private data class CatalogNavigationItem(
    val tab: MainNavigationTab,
    val iconResId: Int,
    val labelResId: Int,
)

@Composable
fun CatalogBottomNavigation(
    selectedItem: MainNavigationTab,
    onItemSelected: (MainNavigationTab) -> Unit,
    modifier: Modifier = Modifier,
    cartItemCount: Int = 0,
) {
    val dividerColor = MaterialTheme.eumColors.line
    val items = listOf(
        CatalogNavigationItem(MainNavigationTab.Home, R.drawable.ic_house, R.string.common_home),
        CatalogNavigationItem(MainNavigationTab.Categories, R.drawable.ic_layout_grid, R.string.common_categories),
        CatalogNavigationItem(MainNavigationTab.Search, R.drawable.ic_search, R.string.common_search),
        CatalogNavigationItem(MainNavigationTab.Cart, R.drawable.ic_shopping_cart, R.string.common_cart),
        // Figma 하단 탭 라벨은 "마이"다(홈 탭과 같은 문자열).
        CatalogNavigationItem(MainNavigationTab.MyPage, R.drawable.ic_user_round, R.string.home_tab_my_page),
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .heightIn(min = 64.dp)
            .background(MaterialTheme.eumColors.background)
            .drawBehind {
                drawLine(dividerColor, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            val isSelected = item.tab == selectedItem
            val color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary
            Column(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .selectable(
                        selected = isSelected,
                        role = Role.Tab,
                        onClick = { onItemSelected(item.tab) },
                    )
                    .padding(horizontal = 2.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                if (item.tab == MainNavigationTab.Cart && cartItemCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = MaterialTheme.eumColors.textPrimary, contentColor = MaterialTheme.eumColors.inverse) {
                                Text(if (cartItemCount > 99) "99+" else cartItemCount.toString())
                            }
                        },
                    ) {
                        Icon(painterResource(item.iconResId), contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
                    }
                } else {
                    Icon(
                        painterResource(item.iconResId),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Text(
                    stringResource(item.labelResId),
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    maxLines = 2,
                )
            }
        }
    }
}

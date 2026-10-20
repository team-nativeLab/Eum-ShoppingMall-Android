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
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private data class CatalogNavigationItem(
    val id: String,
    val iconResId: Int,
    val labelResId: Int,
)

@Composable
fun CatalogBottomNavigation(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dividerColor = MaterialTheme.eumColors.line
    val items = listOf(
        CatalogNavigationItem("home", R.drawable.ic_house, R.string.common_home),
        CatalogNavigationItem("categories", R.drawable.ic_layout_grid, R.string.common_categories),
        CatalogNavigationItem("search", R.drawable.ic_search, R.string.common_search),
        CatalogNavigationItem("cart", R.drawable.ic_shopping_cart, R.string.common_cart),
        CatalogNavigationItem("my_page", R.drawable.ic_user_round, R.string.common_my_page),
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
            val isSelected = item.id == selectedItem
            val color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary
            Column(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .selectable(
                        selected = isSelected,
                        role = Role.Tab,
                        onClick = { onItemSelected(item.id) },
                    )
                    .padding(horizontal = 2.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Icon(
                    painterResource(item.iconResId),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp),
                )
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

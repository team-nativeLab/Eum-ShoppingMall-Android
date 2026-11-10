package com.nativelap.eumshoppingmall.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 하단 탭 한 칸. 탭 순서와 아이콘·라벨을 한 곳에서 정한다. */
data class MainBottomNavigationItem(
    val tab: MainNavigationTab,
    @param:DrawableRes val iconResId: Int,
    @param:StringRes val labelResId: Int,
)

/**
 * 앱 하단 탭 바. 홈·카테고리 등 탭 루트 화면이 같은 모양을 쓴다.
 * 장바구니 배지는 이 바에서만 그린다. 선택된 탭은 textPrimary, 나머지는 textTertiary다.
 */
@Composable
fun MainBottomNavigationBar(
    items: List<MainBottomNavigationItem>,
    selectedItem: MainNavigationTab,
    onItemSelected: (MainNavigationTab) -> Unit,
    cartItemCount: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    val dividerColor = MaterialTheme.eumColors.line
    val cartCountDescription = stringResource(R.string.home_cart_badge_content_description, cartItemCount)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .heightIn(min = 64.dp)
            .background(backgroundColor)
            .drawBehind {
                drawLine(dividerColor, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            val isSelected = selectedItem == item.tab
            val itemColor = if (isSelected) {
                MaterialTheme.eumColors.textPrimary
            } else {
                MaterialTheme.eumColors.textTertiary
            }
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
                    Box(
                        modifier = Modifier.size(width = 48.dp, height = 32.dp).semantics {
                            contentDescription = cartCountDescription
                        },
                    ) {
                        Icon(
                            painterResource(item.iconResId),
                            contentDescription = null,
                            tint = itemColor,
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
                    Icon(
                        painterResource(item.iconResId),
                        contentDescription = null,
                        tint = itemColor,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Text(
                    stringResource(item.labelResId),
                    style = MaterialTheme.typography.labelSmall,
                    color = itemColor,
                    maxLines = 2,
                )
            }
        }
    }
}

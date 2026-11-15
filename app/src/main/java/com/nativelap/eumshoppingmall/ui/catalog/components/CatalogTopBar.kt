package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class CatalogTopBarAction(
    val id: String,
    @param:DrawableRes val iconResId: Int,
    val contentDescription: String,
    val label: String? = null,
)

@Composable
fun CatalogTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: List<CatalogTopBarAction> = emptyList(),
    onActionClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    isCloseAction: Boolean = false,
) {
    // 좌우 슬롯을 실제 측정 폭의 최댓값으로 맞춰 제목이 항상 가운데에 오게 한다.
    Layout(
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = EumSpacing.x8),
        content = {
            Box(contentAlignment = Alignment.CenterStart) {
                if (onBackClick != null) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                        Icon(
                            painterResource(
                                if (isCloseAction) R.drawable.ic_close else R.drawable.ic_arrow_left,
                            ),
                            contentDescription = stringResource(
                                if (isCloseAction) R.string.common_close else R.string.common_back,
                            ),
                            tint = MaterialTheme.eumColors.textPrimary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.eumColors.textPrimary,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                actions.forEach { action ->
                    if (action.label != null) {
                        Row(
                            modifier = Modifier.heightIn(min = 48.dp).clickable { onActionClick(action.id) }.padding(horizontal = EumSpacing.x8),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(action.label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary, maxLines = 1)
                        }
                    } else {
                        IconButton(onClick = { onActionClick(action.id) }, modifier = Modifier.size(48.dp)) {
                            Icon(
                                painterResource(action.iconResId),
                                contentDescription = action.contentDescription,
                                tint = MaterialTheme.eumColors.textPrimary,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            }
        },
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val leadingPlaceable = measurables[0].measure(looseConstraints)
        val trailingPlaceable = measurables[2].measure(looseConstraints)
        val sideSlotWidth = maxOf(leadingPlaceable.width, trailingPlaceable.width)
        val titleMaxWidth = (constraints.maxWidth - sideSlotWidth * 2).coerceAtLeast(0)
        val titlePlaceable = measurables[1].measure(looseConstraints.copy(maxWidth = titleMaxWidth))
        val barHeight = maxOf(
            constraints.minHeight,
            leadingPlaceable.height,
            titlePlaceable.height,
            trailingPlaceable.height,
        )
        layout(constraints.maxWidth, barHeight) {
            leadingPlaceable.placeRelative(0, (barHeight - leadingPlaceable.height) / 2)
            titlePlaceable.placeRelative(
                (constraints.maxWidth - titlePlaceable.width) / 2,
                (barHeight - titlePlaceable.height) / 2,
            )
            trailingPlaceable.placeRelative(
                constraints.maxWidth - trailingPlaceable.width,
                (barHeight - trailingPlaceable.height) / 2,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320, fontScale = 1.5f, name = "카탈로그 상단바 · 라벨 액션 · 글꼴 1.5")
@Composable
private fun CatalogTopBarLabelActionPreview() {
    EumShoppingMallTheme {
        CatalogTopBar(
            title = "최근 본 상품",
            onBackClick = {},
            actions = listOf(CatalogTopBarAction("delete-all", R.drawable.ic_trash_2, "전체 삭제", "전체 삭제")),
        )
    }
}

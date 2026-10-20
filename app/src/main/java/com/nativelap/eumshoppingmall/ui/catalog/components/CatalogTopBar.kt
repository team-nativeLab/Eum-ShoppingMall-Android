package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class CatalogTopBarAction(
    val id: String,
    @param:DrawableRes val iconResId: Int,
    val contentDescription: String,
)

@Composable
fun CatalogTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: List<CatalogTopBarAction> = emptyList(),
    onActionClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        if (onBackClick != null) {
            IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                Icon(
                    painterResource(R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textPrimary,
                    modifier = Modifier.size(24.dp),
                )
            }
        } else {
            androidx.compose.foundation.layout.Spacer(Modifier.size(48.dp))
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.eumColors.textPrimary,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
        actions.forEach { action ->
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

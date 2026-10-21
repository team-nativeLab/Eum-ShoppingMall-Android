package com.nativelap.eumshoppingmall.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSelectionChip
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.common.EmptyCollectionState
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun FavoritesScreen(
    uiModel: FavoritesUiModel,
    onEvent: (FavoritesEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        CatalogTopBar(
            title = stringResource(R.string.account_favorites),
            onBackClick = { onEvent(FavoritesEvent.BackClicked) },
            actions = listOf(CatalogTopBarAction("edit", R.drawable.ic_pencil, stringResource(if (uiModel.isEditing) R.string.common_cancel else R.string.common_edit), stringResource(if (uiModel.isEditing) R.string.common_cancel else R.string.common_edit))),
            onActionClick = { onEvent(FavoritesEvent.EditClicked) },
            modifier = Modifier.statusBarsPadding(),
        )
        if (uiModel.products.isEmpty()) {
            EmptyCollectionState(
                title = stringResource(R.string.favorites_empty_title),
                description = stringResource(R.string.favorites_empty_description),
                actionLabel = stringResource(R.string.favorites_empty_action),
                onAction = { onEvent(FavoritesEvent.BrowseProductsClicked) },
            )
        } else {
            Column(Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(stringResource(R.string.favorites_count, uiModel.products.size), style = MaterialTheme.typography.labelMedium)
                    CatalogSelectionChip(uiModel.sortLabel, false) { onEvent(FavoritesEvent.SortClicked) }
                }
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val columns = if (maxWidth >= 600.dp) 3 else 2
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                        contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
                    ) {
                        items(uiModel.products.chunked(columns), key = { row -> row.first().id }) { rowProducts ->
                            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                rowProducts.forEach { product ->
                                    CatalogProductCard(
                                        product = product,
                                        onClick = { onEvent(FavoritesEvent.ProductClicked(product.id)) },
                                        onFavoriteClick = { onEvent(FavoritesEvent.FavoriteToggled(product.id)) },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                                repeat(columns - rowProducts.size) { Spacer(Modifier.weight(1f)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

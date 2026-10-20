package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogSearchScreen(
    uiModel: CatalogSearchUiModel,
    onEvent: (CatalogSearchEvent) -> Unit,
    selectedBottomItem: String = "search",
    onBottomItemSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CatalogScreenScaffold(
        selectedBottomItem = selectedBottomItem,
        onBottomItemSelected = onBottomItemSelected,
        modifier = modifier,
        topBar = {
            CatalogSearchHeader(
                query = uiModel.query,
                onQueryChanged = { onEvent(CatalogSearchEvent.QueryChanged(it)) },
                onSubmit = { onEvent(CatalogSearchEvent.SearchSubmitted) },
                onCancel = { onEvent(CatalogSearchEvent.Cancelled) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        content = { contentModifier ->
            LazyColumn(
                modifier = contentModifier,
                contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x24),
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.search_recent_terms), Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                            Text(
                                stringResource(R.string.search_clear_all),
                                modifier = Modifier.heightIn(min = 44.dp).clickable { onEvent(CatalogSearchEvent.ClearRecentTerms) },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.eumColors.textTertiary,
                            )
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            items(uiModel.recentTerms.size) { index ->
                                val term = uiModel.recentTerms[index]
                                Surface(
                                    onClick = { onEvent(CatalogSearchEvent.RecentTermSelected(term)) },
                                    shape = RoundedCornerShape(EumRadius.capsule),
                                    color = MaterialTheme.eumColors.backgroundSubtle,
                                ) {
                                    Text(term, Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8), style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.search_popular_terms), Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                            uiModel.popularUpdatedAtLabel?.let { updatedAtLabel ->
                                Text(
                                    stringResource(R.string.catalog_popular_search_basis, updatedAtLabel),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.eumColors.textTertiary,
                                )
                            }
                        }
                        uiModel.popularTerms.withIndex().toList().chunked(2).forEach { pair ->
                            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                pair.forEach { (index, term) ->
                                    Row(
                                        modifier = Modifier.weight(1f).heightIn(min = 36.dp)
                                            .clickable { onEvent(CatalogSearchEvent.PopularTermSelected(term)) },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                                    ) {
                                        Text((index + 1).toString(), style = MaterialTheme.typography.labelMedium)
                                        Text(term, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
internal fun CatalogSearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = EumSpacing.x12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        IconButton(onClick = onCancel, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.ic_arrow_left), stringResource(R.string.common_back), tint = MaterialTheme.eumColors.textPrimary)
        }
        Row(
            modifier = Modifier.weight(1f)
                .border(1.dp, MaterialTheme.eumColors.textPrimary, RoundedCornerShape(EumRadius.capsule))
                .background(MaterialTheme.eumColors.backgroundSubtle, RoundedCornerShape(EumRadius.capsule))
                .padding(horizontal = EumSpacing.x12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            Icon(painterResource(R.drawable.ic_search), null, tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                modifier = Modifier.weight(1f).heightIn(min = 44.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.eumColors.textPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(stringResource(R.string.catalog_search_placeholder), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                    }
                    innerTextField()
                },
            )
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }, modifier = Modifier.size(48.dp)) {
                    Icon(painterResource(R.drawable.ic_circle_x), stringResource(R.string.common_delete), tint = MaterialTheme.eumColors.textTertiary)
                }
            }
        }
        Text(
            stringResource(R.string.catalog_cancel),
            modifier = Modifier.heightIn(min = 44.dp).clickable(onClick = onCancel),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

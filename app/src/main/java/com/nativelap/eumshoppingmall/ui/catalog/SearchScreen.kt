package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogScreenScaffold
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogSearchScreen(
    uiModel: CatalogSearchUiModel,
    onEvent: (CatalogSearchEvent) -> Unit,
    selectedBottomItem: MainNavigationTab = MainNavigationTab.Search,
    onBottomItemSelected: (MainNavigationTab) -> Unit = {},
    cartItemCount: Int = 0,
    modifier: Modifier = Modifier,
) {
    CatalogScreenScaffold(
        selectedBottomItem = selectedBottomItem,
        onBottomItemSelected = onBottomItemSelected,
        cartItemCount = cartItemCount,
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
                            Box(
                                modifier = Modifier.heightIn(min = 48.dp).clickable { onEvent(CatalogSearchEvent.ClearRecentTerms) },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    stringResource(R.string.search_clear_all),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.eumColors.textTertiary,
                                )
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            items(uiModel.recentTerms) { term ->
                                Surface(
                                    onClick = { onEvent(CatalogSearchEvent.RecentTermSelected(term)) },
                                    modifier = Modifier.heightIn(min = 48.dp),
                                    shape = RoundedCornerShape(EumRadius.capsule),
                                    color = MaterialTheme.eumColors.background,
                                    border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                                ) {
                                    Text(term, Modifier.padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12), style = MaterialTheme.typography.labelMedium)
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
                        // Figma는 인기 검색어를 왼쪽 열 1~5위, 오른쪽 열 6~10위로 세로 순서대로 배치한다.
                        val indexedPopularTerms = uiModel.popularTerms.withIndex().toList()
                        val leftColumnSize = (indexedPopularTerms.size + 1) / 2
                        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                            listOf(indexedPopularTerms.take(leftColumnSize), indexedPopularTerms.drop(leftColumnSize)).forEach { columnTerms ->
                                Column(modifier = Modifier.weight(1f)) {
                                    columnTerms.forEach { (index, term) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                                .clickable { onEvent(CatalogSearchEvent.PopularTermSelected(term)) },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                                        ) {
                                            Text((index + 1).toString(), style = MaterialTheme.typography.labelMedium)
                                            Text(term, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        }
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
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.eumColors.textPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                decorationBox = { innerTextField ->
                    // 플레이스홀더와 입력 텍스트를 같은 상자에서 세로 가운데로 맞춘다.
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        if (query.isEmpty()) {
                            Text(
                                stringResource(R.string.catalog_search_placeholder),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.eumColors.textTertiary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }, modifier = Modifier.size(48.dp)) {
                    Icon(painterResource(R.drawable.ic_circle_x), stringResource(R.string.common_delete), tint = MaterialTheme.eumColors.textTertiary)
                }
            }
        }
        Box(
            modifier = Modifier.widthIn(min = 48.dp).heightIn(min = 48.dp).clickable(onClick = onCancel),
            contentAlignment = Alignment.Center,
        ) {
            Text(stringResource(R.string.catalog_cancel), style = MaterialTheme.typography.labelMedium)
        }
    }
}

package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogScreenScaffold
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
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
                contentPadding = PaddingValues(top = EumSpacing.x16, bottom = EumSpacing.x16),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x24),
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = EumSpacing.x20),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.search_recent_terms), Modifier.weight(1f), style = EumTypographyTokens.title)
                            // 전체 삭제 터치 영역 48을 위해 행 높이가 24보다 커진다.
                            Box(
                                modifier = Modifier.heightIn(min = 48.dp).clickable { onEvent(CatalogSearchEvent.ClearRecentTerms) },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    stringResource(R.string.search_clear_all),
                                    style = EumTypographyTokens.caption,
                                    color = MaterialTheme.eumColors.textTertiary,
                                )
                            }
                        }
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            uiModel.recentTerms.forEach { term ->
                                // 보이는 칩은 36dp, 터치 영역은 바깥 Box의 투명 여백으로 48dp를 유지한다.
                                Box(
                                    modifier = Modifier.heightIn(min = 48.dp)
                                        .clickable(role = Role.Button) { onEvent(CatalogSearchEvent.RecentTermSelected(term)) },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        term,
                                        modifier = Modifier.heightIn(min = 36.dp)
                                            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.capsule))
                                            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.capsule))
                                            .padding(horizontal = EumSpacing.x14, vertical = EumSpacing.x8),
                                        style = EumTypographyTokens.labelM,
                                        color = MaterialTheme.eumColors.textPrimary,
                                    )
                                }
                            }
                        }
                    }
                }
                item { Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.eumColors.line)) }
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = EumSpacing.x20),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.search_popular_terms), Modifier.weight(1f), style = EumTypographyTokens.title)
                            uiModel.popularUpdatedAtLabel?.let { updatedAtLabel ->
                                Text(
                                    stringResource(R.string.catalog_popular_search_basis, updatedAtLabel),
                                    style = EumTypographyTokens.caption,
                                    color = MaterialTheme.eumColors.textTertiary,
                                )
                            }
                        }
                        // Figma는 인기 검색어를 왼쪽 열 1~5위, 오른쪽 열 6~10위로 세로 순서대로 배치한다.
                        val indexedPopularTerms = uiModel.popularTerms.withIndex().toList()
                        val leftColumnSize = (indexedPopularTerms.size + 1) / 2
                        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                            listOf(indexedPopularTerms.take(leftColumnSize), indexedPopularTerms.drop(leftColumnSize)).forEach { columnTerms ->
                                Column(modifier = Modifier.weight(1f)) {
                                    columnTerms.forEach { (index, term) ->
                                        Row(
                                            // 행 높이는 Figma 40이지만 터치 영역 48을 우선한다.
                                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                                .clickable { onEvent(CatalogSearchEvent.PopularTermSelected(term)) },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                                        ) {
                                            Text(
                                                (index + 1).toString(),
                                                modifier = Modifier.width(20.dp),
                                                style = EumTypographyTokens.labelM,
                                                color = if (index < 3) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary,
                                            )
                                            Text(
                                                term,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.eumColors.textPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
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
        modifier = modifier.fillMaxWidth().background(MaterialTheme.eumColors.background)
            .padding(start = EumSpacing.x8, end = EumSpacing.x12, top = EumSpacing.x4, bottom = EumSpacing.x4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        // 뒤로 버튼은 보이는 아이콘 크기가 Figma 44와 같고 터치 영역은 48을 유지한다.
        IconButton(onClick = onCancel, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.ic_arrow_left), stringResource(R.string.common_back), tint = MaterialTheme.eumColors.textPrimary)
        }
        Row(
            modifier = Modifier.weight(1f).heightIn(min = 44.dp)
                .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.capsule))
                .border(1.5.dp, MaterialTheme.eumColors.textPrimary, RoundedCornerShape(EumRadius.capsule))
                .padding(horizontal = EumSpacing.x16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            Icon(painterResource(R.drawable.ic_search), null, tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                // 44dp 알약 전체가 포커스 영역이 되도록 최소 높이를 둔다.
                modifier = Modifier.weight(1f).heightIn(min = 44.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.eumColors.textPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                decorationBox = { innerTextField ->
                    // 플레이스홀더와 입력 텍스트를 같은 상자에서 세로 가운데로 맞춘다.
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        if (query.isEmpty()) {
                            Text(
                                stringResource(R.string.catalog_search_placeholder),
                                style = MaterialTheme.typography.bodyMedium,
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
                IconButton(onClick = { onQueryChanged("") }, modifier = Modifier.size(44.dp)) {
                    Icon(painterResource(R.drawable.ic_circle_x), stringResource(R.string.common_delete), tint = MaterialTheme.eumColors.textTertiary)
                }
            }
        }
        Box(
            modifier = Modifier.widthIn(min = 48.dp).heightIn(min = 48.dp).clickable(onClick = onCancel),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.catalog_cancel),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.eumColors.textPrimary,
            )
        }
    }
}

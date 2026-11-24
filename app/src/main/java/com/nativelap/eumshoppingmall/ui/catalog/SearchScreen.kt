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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
                onMenuClick = { onEvent(CatalogSearchEvent.MenuClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        content = { contentModifier ->
            LazyColumn(
                modifier = contentModifier,
                contentPadding = PaddingValues(start = EumSpacing.x20, top = EumSpacing.x16, end = EumSpacing.x20, bottom = EumSpacing.x24),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
            ) {
                item {
                    SearchSectionCard(
                        title = stringResource(R.string.search_recent_terms),
                        iconResId = R.drawable.ic_clock,
                        trailing = {
                            if (uiModel.recentTerms.isNotEmpty()) {
                                // 전체 삭제 터치 영역 48을 위해 행 높이가 24보다 커진다.
                                Box(
                                    modifier = Modifier.heightIn(min = 48.dp).clickable(role = Role.Button) { onEvent(CatalogSearchEvent.ClearRecentTerms) },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        stringResource(R.string.search_clear_all),
                                        style = EumTypographyTokens.caption,
                                        color = MaterialTheme.eumColors.textTertiary,
                                    )
                                }
                            }
                        },
                    ) {
                        if (uiModel.recentTerms.isEmpty()) {
                            SearchEmptyHint(stringResource(R.string.search_recent_empty))
                        } else {
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
                                                .background(MaterialTheme.eumColors.surfaceSecondary, RoundedCornerShape(EumRadius.capsule))
                                                .padding(horizontal = EumSpacing.x14, vertical = EumSpacing.x8),
                                            style = EumTypographyTokens.labelM,
                                            color = MaterialTheme.eumColors.textPrimary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    SearchSectionCard(
                        title = stringResource(R.string.search_popular_terms),
                        iconResId = R.drawable.ic_search,
                        trailing = {
                            uiModel.popularUpdatedAtLabel?.let { updatedAtLabel ->
                                Text(
                                    stringResource(R.string.catalog_popular_search_basis, updatedAtLabel),
                                    style = EumTypographyTokens.caption,
                                    color = MaterialTheme.eumColors.textTertiary,
                                )
                            }
                        },
                    ) {
                        if (uiModel.popularTerms.isEmpty()) {
                            SearchEmptyHint(stringResource(R.string.search_popular_empty))
                        } else {
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
                                                    .clickable(role = Role.Button) { onEvent(CatalogSearchEvent.PopularTermSelected(term)) },
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                                            ) {
                                                PopularRankBadge(rank = index + 1)
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
            }
        },
    )
}

// 검색 화면 섹션 카드. 제목 줄(아이콘 + 제목 + 오른쪽 보조 내용) 아래에 내용을 둔다.
@Composable
private fun SearchSectionCard(
    title: String,
    iconResId: Int,
    trailing: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(EumRadius.x12)
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surface, shape)
            .border(1.dp, MaterialTheme.eumColors.line, shape)
            .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x8),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            Icon(painterResource(iconResId), contentDescription = null, tint = MaterialTheme.eumColors.textSecondary, modifier = Modifier.size(18.dp))
            Text(title, Modifier.weight(1f), style = EumTypographyTokens.title)
            trailing()
        }
        content()
    }
}

// 데이터가 없을 때 섹션 안에 보이는 한 줄 안내. 거짓 항목을 만들지 않는다.
@Composable
private fun SearchEmptyHint(text: String) {
    Text(
        text,
        modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x16),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.eumColors.textTertiary,
    )
}

// 순위 배지. 1~3위는 채운 원, 나머지는 숫자만 보인다.
@Composable
private fun PopularRankBadge(rank: Int) {
    val isTopRank = rank <= 3
    Box(
        modifier = Modifier.defaultMinSize(minWidth = 20.dp, minHeight = 20.dp).then(
            if (isTopRank) Modifier.background(MaterialTheme.eumColors.textPrimary, RoundedCornerShape(EumRadius.capsule)) else Modifier,
        ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            rank.toString(),
            softWrap = false,
            style = EumTypographyTokens.labelM,
            color = if (isTopRank) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textTertiary,
        )
    }
}

@Composable
internal fun CatalogSearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    showCancelText: Boolean = true,
    // 탭 루트에서는 뒤로 버튼 자리에 햄버거를 둔다. null이면 뒤로 버튼이다.
    onMenuClick: (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth().background(MaterialTheme.eumColors.background)
            .padding(start = EumSpacing.x8, end = EumSpacing.x12, top = EumSpacing.x4, bottom = EumSpacing.x4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        // 뒤로 버튼은 보이는 아이콘 크기가 Figma 44와 같고 터치 영역은 48을 유지한다.
        if (onMenuClick != null) {
            IconButton(onClick = onMenuClick, modifier = Modifier.size(48.dp)) {
                Icon(painterResource(R.drawable.ic_menu), stringResource(R.string.account_drawer_open), tint = MaterialTheme.eumColors.textPrimary)
            }
        } else {
            IconButton(onClick = onCancel, modifier = Modifier.size(48.dp)) {
                Icon(painterResource(R.drawable.ic_arrow_left), stringResource(R.string.common_back), tint = MaterialTheme.eumColors.textPrimary)
            }
        }
        Row(
            modifier = Modifier.weight(1f).heightIn(min = 44.dp)
                .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.capsule))
                .border(if (isFocused) 1.5.dp else 1.dp, if (isFocused) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineControl, RoundedCornerShape(EumRadius.capsule))
                .padding(horizontal = EumSpacing.x16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            Icon(painterResource(R.drawable.ic_search), null, tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                // 44dp 알약 전체가 포커스 영역이 되도록 최소 높이를 둔다.
                modifier = Modifier.weight(1f).heightIn(min = 44.dp).onFocusChanged { focusState -> isFocused = focusState.isFocused },
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
                IconButton(onClick = { onQueryChanged("") }, modifier = Modifier.size(48.dp)) {
                    Icon(painterResource(R.drawable.ic_circle_x), stringResource(R.string.search_clear_query), tint = MaterialTheme.eumColors.textTertiary)
                }
            }
        }
        if (showCancelText) {
            Box(
                modifier = Modifier.widthIn(min = 48.dp).heightIn(min = 48.dp).clickable(role = Role.Button, onClick = onCancel),
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
}

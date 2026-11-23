package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogLoadMoreButton
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductRow
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogScreenScaffold
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogProductListScreen(
    uiModel: CatalogProductListUiModel,
    onEvent: (CatalogProductListEvent) -> Unit,
    selectedBottomItem: MainNavigationTab = MainNavigationTab.Categories,
    onBottomItemSelected: (MainNavigationTab) -> Unit = {},
    cartItemCount: Int = 0,
    modifier: Modifier = Modifier,
    snackbarHostState: androidx.compose.material3.SnackbarHostState? = null,
    // 담기 동작을 넘긴 화면에서만 카드에 담기 버튼을 그린다(쿼트 선택 화면은 false).
    showQuickCart: Boolean = false,
    // 목록 위 안내 한 줄(견적 상품 선택 등). null이면 그리지 않는다.
    guidanceText: String? = null,
    showFavorite: Boolean = true,
    // 사유 칩(품절·옵션 필요)은 담기 동작이 있는 목록에서만 보인다. 견적 선택 화면은 숨긴다.
    showBlockReason: Boolean = true,
    // 페이지 분량만 보여 줄 때 true면 목록 끝에 "더 보기" 버튼을 둔다.
    hasMoreProducts: Boolean = false,
) {
    CatalogScreenScaffold(
        selectedBottomItem = selectedBottomItem,
        onBottomItemSelected = onBottomItemSelected,
        cartItemCount = cartItemCount,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        topBar = {
            CatalogTopBar(
                title = uiModel.title,
                onBackClick = { onEvent(CatalogProductListEvent.BackClicked) },
                actions = listOf(CatalogTopBarAction("search", R.drawable.ic_search, stringResource(R.string.common_search))),
                onActionClick = { onEvent(CatalogProductListEvent.SearchClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        content = { contentModifier ->
            BoxWithConstraints(contentModifier) {
                val columns = catalogProductColumnCount(maxWidth, LocalDensity.current.fontScale)
                Column(Modifier.fillMaxSize()) {
                    guidanceText?.let { guidance ->
                        Text(
                            guidance,
                            modifier = Modifier.padding(start = EumSpacing.x20, top = EumSpacing.x8, end = EumSpacing.x20),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        items(uiModel.categoryChips, key = { chip -> chip.id }) { chip ->
                            CatalogSelectionChip(
                                label = chip.label,
                                selected = chip.id == uiModel.selectedCategoryId,
                                onClick = { onEvent(CatalogProductListEvent.CategorySelected(chip.id)) },
                                count = chip.count,
                            )
                        }
                    }
                    CatalogProductListControls(
                        countLabel = uiModel.resultCountLabel,
                        sortLabel = stringResource(R.string.common_sort_label, uiModel.sortLabel),
                        isListLayout = uiModel.isListLayout,
                        onSortClick = { onEvent(CatalogProductListEvent.SortClicked) },
                        onFilterClick = { onEvent(CatalogProductListEvent.FilterClicked) },
                        onLayoutClick = { onEvent(CatalogProductListEvent.LayoutToggled) },
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = EumSpacing.x20,
                            top = EumSpacing.x8,
                            end = EumSpacing.x20,
                            bottom = EumSpacing.x20,
                        ),
                        // Figma 격자 행 간격은 24dp다. 리스트 행 간격은 기존 16dp를 유지한다.
                        verticalArrangement = Arrangement.spacedBy(if (uiModel.isListLayout) EumSpacing.x16 else EumSpacing.x24),
                    ) {
                        if (uiModel.products.isEmpty()) {
                            item {
                                Text(
                                    stringResource(R.string.catalog_empty_products),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x32),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.eumColors.textTertiary,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        } else if (uiModel.isListLayout) {
                            items(uiModel.products, key = { it.id }) { product ->
                                CatalogProductRow(
                                    product = product,
                                    showFavorite = showFavorite,
                                    showBlockReason = showBlockReason,
                                    onQuickCartClick = if (showQuickCart) {
                                        { onEvent(CatalogProductListEvent.QuickCartClicked(product.id)) }
                                    } else {
                                        null
                                    },
                                    onClick = { onEvent(CatalogProductListEvent.ProductSelected(product.id)) },
                                    onFavoriteClick = { onEvent(CatalogProductListEvent.FavoriteToggled(product.id)) },
                                )
                            }
                        } else {
                            items(uiModel.products.chunked(columns)) { rowProducts ->
                                // 같은 행의 카드는 가장 높은 카드에 맞춘다(Figma 179:743의 행 카드 높이가 같다).
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                                    modifier = Modifier.height(IntrinsicSize.Max),
                                ) {
                                    rowProducts.forEach { product ->
                                        CatalogProductCard(
                                            product = product,
                                            showFavorite = showFavorite,
                                            showBlockReason = showBlockReason,
                                            onClick = { onEvent(CatalogProductListEvent.ProductSelected(product.id)) },
                                            onFavoriteClick = { onEvent(CatalogProductListEvent.FavoriteToggled(product.id)) },
                                            modifier = Modifier.weight(1f).fillMaxHeight(),
                                            onQuickCartClick = if (showQuickCart) {
                                                { onEvent(CatalogProductListEvent.QuickCartClicked(product.id)) }
                                            } else {
                                                null
                                            },
                                        )
                                    }
                                    repeat(columns - rowProducts.size) { Spacer(Modifier.weight(1f)) }
                                }
                            }
                        }
                        if (hasMoreProducts) {
                            item {
                                CatalogLoadMoreButton(onClick = { onEvent(CatalogProductListEvent.LoadMoreClicked) })
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun CatalogSelectionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    // 개수는 표시용이다. 선택 비교와 이벤트에는 label만 쓰고, 개수가 null이면 라벨만 그린다.
    count: Int? = null,
) {
    val displayLabel = count?.let { stringResource(R.string.catalog_chip_label_with_count, label, it) } ?: label
    Surface(
        onClick = onClick,
        // 레이아웃 크기를 최소 48dp로 예약한다(material3 소스 확인). 칩 행 높이가 48dp까지 커지며, 시각 칩은 가운데에 그려진다.
        modifier = Modifier.minimumInteractiveComponentSize(),
        shape = RoundedCornerShape(EumRadius.capsule),
        color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
    ) {
        Text(
            displayLabel,
            modifier = Modifier.padding(horizontal = EumSpacing.x14, vertical = EumSpacing.x8),
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
        )
    }
}

@Composable
internal fun CatalogProductListControls(
    countLabel: String?,
    sortLabel: String,
    isListLayout: Boolean,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    onLayoutClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
    ) {
        val useStackedControls = shouldStackCatalogControls(maxWidth, LocalDensity.current.fontScale)
        val sortControl: @Composable (Modifier) -> Unit = { controlModifier ->
            Surface(
                onClick = onSortClick,
                modifier = controlModifier,
                shape = RoundedCornerShape(EumRadius.capsule),
                color = MaterialTheme.eumColors.surface,
                border = BorderStroke(1.dp, MaterialTheme.eumColors.textPrimary),
            ) {
                Row(
                    modifier = Modifier.padding(
                        start = EumSpacing.x14,
                        top = EumSpacing.x6,
                        end = EumSpacing.x10,
                        bottom = EumSpacing.x6,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                ) {
                    Text(
                        sortLabel,
                        modifier = Modifier.weight(1f, fill = false),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Icon(painterResource(R.drawable.ic_chevron_down), null, modifier = Modifier.size(14.dp))
                }
            }
        }
        val filterControl: @Composable (Modifier) -> Unit = { controlModifier ->
            Row(
                modifier = controlModifier
                    .heightIn(min = 48.dp)
                    .clickable(onClick = onFilterClick)
                    .padding(horizontal = EumSpacing.x4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            ) {
                Icon(painterResource(R.drawable.ic_sliders_horizontal), null, modifier = Modifier.size(20.dp))
                Text(stringResource(R.string.common_filter), style = MaterialTheme.typography.labelMedium)
            }
        }
        val layoutControl: @Composable () -> Unit = {
            IconButton(onClick = onLayoutClick, modifier = Modifier.size(48.dp)) {
                Icon(
                    painterResource(if (isListLayout) R.drawable.ic_layout_list else R.drawable.ic_layout_grid),
                    contentDescription = stringResource(R.string.catalog_toggle_layout),
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        if (useStackedControls) {
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                ) {
                    countLabel?.let {
                        Text(
                            it,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.eumColors.textTertiary,
                            maxLines = 1,
                        )
                    } ?: Spacer(Modifier.weight(1f))
                    sortControl(Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                ) {
                    filterControl(Modifier.weight(1f))
                    layoutControl()
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
            ) {
                sortControl(Modifier)
                countLabel?.let {
                    Text(
                        it,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                } ?: Spacer(Modifier.weight(1f))
                filterControl(Modifier)
                layoutControl()
            }
        }
    }
}

internal fun catalogProductColumnCount(maxWidth: Dp, fontScale: Float): Int = when {
    maxWidth >= 960.dp -> 4
    maxWidth >= 600.dp -> 3
    maxWidth < 360.dp || fontScale >= 1.35f -> 1
    else -> 2
}

internal fun shouldStackCatalogControls(maxWidth: Dp, fontScale: Float): Boolean = maxWidth < 360.dp || fontScale >= 1.3f

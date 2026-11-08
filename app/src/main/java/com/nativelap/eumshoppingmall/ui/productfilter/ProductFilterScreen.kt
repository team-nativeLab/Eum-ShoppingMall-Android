package com.nativelap.eumshoppingmall.ui.productfilter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

enum class ProductFilterPriceRange {
    UnderTenThousand,
    TenToThirtyThousand,
    OverThirtyThousand,
}

enum class ProductFilterSort {
    Recommended,
    PriceAscending,
    PriceDescending,
    Latest,
}

data class ProductFilterUiModel(
    val categories: List<String>,
    val selectedCategory: String?,
    val selectedPriceRange: ProductFilterPriceRange?,
    val selectedSort: ProductFilterSort,
)

sealed interface ProductFilterScreenEvent {
    data object BackClicked : ProductFilterScreenEvent
    data class CategorySelected(val category: String) : ProductFilterScreenEvent
    data class PriceRangeSelected(val range: ProductFilterPriceRange) : ProductFilterScreenEvent
    data class SortSelected(val sort: ProductFilterSort) : ProductFilterScreenEvent
    data object ApplyClicked : ProductFilterScreenEvent
}

@Composable
fun ProductFilterScreen(
    uiModel: ProductFilterUiModel,
    onEvent: (ProductFilterScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val priceOptions = priceRanges()
    val sortRows = sortOptions()
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_filter_title),
        onBackClick = { onEvent(ProductFilterScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_apply_filter),
        onBottomActionClick = { onEvent(ProductFilterScreenEvent.ApplyClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20, bottom = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            FilterChipSection(
                title = stringResource(R.string.ui_audit_category),
                options = uiModel.categories,
                selected = uiModel.selectedCategory,
                onSelected = { onEvent(ProductFilterScreenEvent.CategorySelected(it)) },
            )
            FilterChipSection(
                title = stringResource(R.string.ui_audit_price_range),
                options = priceOptions.map { it.first },
                selected = priceOptions.firstOrNull { it.second == uiModel.selectedPriceRange }?.first,
                onSelected = { label ->
                    priceOptions.firstOrNull { it.first == label }?.let { onEvent(ProductFilterScreenEvent.PriceRangeSelected(it.second)) }
                },
            )
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(
                    text = stringResource(R.string.ui_audit_filter_sort),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Surface(
                    shape = RoundedCornerShape(EumRadius.x8),
                    color = MaterialTheme.eumColors.surface,
                    border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineNeutral),
                ) {
                    Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x16)) {
                        sortRows.forEachIndexed { index, (label, sort) ->
                            val selected = uiModel.selectedSort == sort
                            Row(
                                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                                    .selectable(
                                        selected = selected,
                                        role = Role.RadioButton,
                                        onClick = { onEvent(ProductFilterScreenEvent.SortSelected(sort)) },
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = label,
                                    modifier = Modifier.weight(1f).padding(vertical = EumSpacing.x12),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.eumColors.textPrimary,
                                )
                                RadioButton(selected = selected, onClick = null)
                            }
                            if (index != sortRows.lastIndex) HorizontalDivider(color = MaterialTheme.eumColors.line)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FilterChipSection(
    title: String,
    options: List<String>,
    selected: String?,
    onSelected: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            options.forEach { label ->
                val isSelected = selected == label
                Box(
                    modifier = Modifier.heightIn(min = 48.dp).selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = { onSelected(label) },
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        modifier = Modifier.heightIn(min = 34.dp),
                        shape = RoundedCornerShape(EumRadius.x12),
                        color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
                        border = BorderStroke(EumSpacing.x1, if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineNeutral),
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x6),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun priceRanges(): List<Pair<String, ProductFilterPriceRange>> = listOf(
    stringResource(R.string.ui_audit_price_under_ten_thousand) to ProductFilterPriceRange.UnderTenThousand,
    stringResource(R.string.ui_audit_price_ten_to_thirty_thousand) to ProductFilterPriceRange.TenToThirtyThousand,
    stringResource(R.string.ui_audit_price_over_thirty_thousand) to ProductFilterPriceRange.OverThirtyThousand,
)

@Composable
private fun sortOptions(): List<Pair<String, ProductFilterSort>> = listOf(
    stringResource(R.string.ui_audit_sort_recommended) to ProductFilterSort.Recommended,
    stringResource(R.string.ui_audit_sort_price_ascending) to ProductFilterSort.PriceAscending,
    stringResource(R.string.ui_audit_sort_price_descending) to ProductFilterSort.PriceDescending,
    stringResource(R.string.ui_audit_sort_latest) to ProductFilterSort.Latest,
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductFilterScreenPreview() {
    EumShoppingMallTheme {
        ProductFilterScreen(ProductFilterUiModel(emptyList(), null, null, ProductFilterSort.Recommended), onEvent = {})
    }
}

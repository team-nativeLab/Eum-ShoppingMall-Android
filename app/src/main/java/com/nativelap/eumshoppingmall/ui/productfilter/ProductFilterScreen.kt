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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditRadioIndicator
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.catalog.SafetyCertificationType
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
    Latest,
    Rating,
    PriceAscending,
    PriceDescending,
}

enum class ProductFilterPageSize(val count: Int) {
    Twelve(12),
    TwentyFour(24),
    FortyEight(48),
}

// 직접 입력 가격의 최소·최대 칸을 구분한다.
enum class ProductFilterPriceBound {
    Min,
    Max,
}

// 브랜드 옵션. productCount는 필터를 적용하기 전 상품 목록 기준 개수다.
data class ProductFilterBrandUiModel(
    val id: String,
    val name: String,
    val productCount: Int,
)

// 안전인증 종류 옵션. productCount는 필터를 적용하기 전 상품 목록 기준 개수다.
data class ProductFilterCertificationUiModel(
    val type: SafetyCertificationType,
    val productCount: Int,
)

data class ProductFilterUiModel(
    val categories: List<String>,
    val selectedCategory: String?,
    val selectedPriceRange: ProductFilterPriceRange?,
    val selectedSort: ProductFilterSort,
    // 가격 정보가 있는 상품이 하나도 없으면 가격 섹션을 숨긴다.
    val hasPricedProducts: Boolean = true,
    // 입력 중인 값(draft)이다. 적용 버튼을 눌러야 목록에 반영된다.
    val minPriceDraft: String = "",
    val maxPriceDraft: String = "",
    // 상품에 실제 있는 브랜드만 담는다. 비어 있으면 브랜드 섹션을 숨긴다.
    val brands: List<ProductFilterBrandUiModel> = emptyList(),
    val selectedBrandIds: Set<String> = emptySet(),
    // 안전인증 종류별 옵션. 안전인증 상품이 없으면 비워 두어 인증 섹션을 숨긴다.
    val safetyCertificationOptions: List<ProductFilterCertificationUiModel> = emptyList(),
    val selectedSafetyCertifications: Set<SafetyCertificationType> = emptySet(),
    val showSoldOutToggle: Boolean = false,
    val isSoldOutHidden: Boolean = false,
    val selectedPageSize: ProductFilterPageSize = ProductFilterPageSize.TwentyFour,
    // 직접 입력으로 적용된 가격 조건. 둘 다 없으면 적용 중 표시와 해제 버튼을 숨긴다.
    val appliedMinPriceManwon: Int? = null,
    val appliedMaxPriceManwon: Int? = null,
)

sealed interface ProductFilterScreenEvent {
    data object BackClicked : ProductFilterScreenEvent
    data class CategorySelected(val category: String) : ProductFilterScreenEvent
    data class PriceRangeSelected(val range: ProductFilterPriceRange) : ProductFilterScreenEvent
    data class PriceDraftChanged(val bound: ProductFilterPriceBound, val text: String) : ProductFilterScreenEvent
    data object PriceApplyClicked : ProductFilterScreenEvent
    data object PriceDirectInputClearClicked : ProductFilterScreenEvent
    data class BrandToggled(val brandId: String) : ProductFilterScreenEvent
    data class SafetyCertificationToggled(val certification: SafetyCertificationType) : ProductFilterScreenEvent
    data class SoldOutHiddenChanged(val checked: Boolean) : ProductFilterScreenEvent
    data class SortSelected(val sort: ProductFilterSort) : ProductFilterScreenEvent
    data class PageSizeSelected(val pageSize: ProductFilterPageSize) : ProductFilterScreenEvent
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
                .padding(contentPadding).padding(top = EumSpacing.x16, bottom = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            FilterChipSection(
                title = stringResource(R.string.ui_audit_category),
                options = uiModel.categories.map { category -> category to category },
                selected = uiModel.selectedCategory,
                onSelected = { category -> onEvent(ProductFilterScreenEvent.CategorySelected(category)) },
            )
            if (uiModel.hasPricedProducts) {
                FilterChipSection(
                    title = stringResource(R.string.ui_audit_price_range),
                    options = priceOptions,
                    selected = uiModel.selectedPriceRange,
                    onSelected = { range -> onEvent(ProductFilterScreenEvent.PriceRangeSelected(range)) },
                )
                PriceDirectInputSection(
                    minDraft = uiModel.minPriceDraft,
                    maxDraft = uiModel.maxPriceDraft,
                    onDraftChanged = { bound, text -> onEvent(ProductFilterScreenEvent.PriceDraftChanged(bound, text)) },
                    onApplyClick = { onEvent(ProductFilterScreenEvent.PriceApplyClicked) },
                    appliedMinManwon = uiModel.appliedMinPriceManwon,
                    appliedMaxManwon = uiModel.appliedMaxPriceManwon,
                    onClearClick = { onEvent(ProductFilterScreenEvent.PriceDirectInputClearClicked) },
                )
            }
            if (uiModel.brands.isNotEmpty()) {
                FilterCheckboxSection(
                    title = stringResource(R.string.ui_audit_brand),
                    items = uiModel.brands.map { brand ->
                        FilterCheckboxItem(
                            value = brand.id,
                            label = brand.name,
                            productCount = brand.productCount,
                            isChecked = brand.id in uiModel.selectedBrandIds,
                        )
                    },
                    onToggle = { brandId -> onEvent(ProductFilterScreenEvent.BrandToggled(brandId)) },
                )
            }
            if (uiModel.safetyCertificationOptions.isNotEmpty()) {
                FilterCheckboxSection(
                    title = stringResource(R.string.ui_audit_filter_safety_certification),
                    items = uiModel.safetyCertificationOptions.map { option ->
                        FilterCheckboxItem(
                            value = option.type,
                            label = safetyCertificationLabel(option.type),
                            productCount = option.productCount,
                            isChecked = option.type in uiModel.selectedSafetyCertifications,
                        )
                    },
                    onToggle = { certification ->
                        onEvent(ProductFilterScreenEvent.SafetyCertificationToggled(certification))
                    },
                )
            }
            if (uiModel.showSoldOutToggle) {
                FilterCheckboxRow(
                    label = stringResource(R.string.ui_audit_filter_hide_sold_out),
                    productCount = null,
                    checked = uiModel.isSoldOutHidden,
                    onCheckedChange = { checked -> onEvent(ProductFilterScreenEvent.SoldOutHiddenChanged(checked)) },
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                Text(
                    text = stringResource(R.string.ui_audit_filter_sort),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Surface(
                    shape = RoundedCornerShape(EumRadius.x8),
                    color = MaterialTheme.eumColors.surface,
                ) {
                    Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x16).selectableGroup()) {
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
                                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                            ) {
                                Text(
                                    text = label,
                                    modifier = Modifier.weight(1f).padding(vertical = EumSpacing.x12),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.eumColors.textPrimary,
                                )
                                UiAuditRadioIndicator(selected = selected)
                            }
                            if (index != sortRows.lastIndex) HorizontalDivider(color = MaterialTheme.eumColors.line)
                        }
                    }
                }
            }
            FilterChipSection(
                title = stringResource(R.string.ui_audit_filter_page_size),
                options = pageSizeOptions(),
                selected = uiModel.selectedPageSize,
                onSelected = { pageSize -> onEvent(ProductFilterScreenEvent.PageSizeSelected(pageSize)) },
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun <T> FilterChipSection(
    title: String,
    options: List<Pair<String, T>>,
    selected: T?,
    onSelected: (T) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
        FlowRow(
            modifier = Modifier.fillMaxWidth().selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            options.forEach { (label, value) ->
                val isSelected = selected == value
                Box(
                    modifier = Modifier.heightIn(min = 48.dp).selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = { onSelected(value) },
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        modifier = Modifier.heightIn(min = 34.dp),
                        shape = RoundedCornerShape(FilterChipRadius),
                        color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
                        border = BorderStroke(EumSpacing.x1, if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineNeutral),
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(horizontal = FilterChipHorizontalPadding, vertical = EumSpacing.x6),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
                        )
                    }
                }
            }
        }
    }
}

// 여러 개를 동시에 고를 수 있는 옵션 목록(브랜드·안전인증 종류)이다. 옵션마다 체크박스와 상품 수를 보여 준다.
@Composable
private fun <T> FilterCheckboxSection(
    title: String,
    items: List<FilterCheckboxItem<T>>,
    onToggle: (T) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
        items.forEach { item ->
            FilterCheckboxRow(
                label = item.label,
                productCount = item.productCount,
                checked = item.isChecked,
                onCheckedChange = { onToggle(item.value) },
            )
        }
    }
}

@Composable
private fun FilterCheckboxRow(
    label: String,
    productCount: Int?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            .toggleable(value = checked, role = Role.Checkbox, onValueChange = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        // 행 전체가 토글을 받으므로 Checkbox 자체의 클릭 처리는 끈다.
        Checkbox(checked = checked, onCheckedChange = null)
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
        productCount?.let { count ->
            Text(
                text = stringResource(R.string.ui_audit_filter_item_count, count),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}

@Composable
private fun PriceDirectInputSection(
    minDraft: String,
    maxDraft: String,
    onDraftChanged: (ProductFilterPriceBound, String) -> Unit,
    onApplyClick: () -> Unit,
    appliedMinManwon: Int?,
    appliedMaxManwon: Int?,
    onClearClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(
            stringResource(R.string.ui_audit_price_direct_input),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.eumColors.textSecondary,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            OutlinedTextField(
                value = minDraft,
                onValueChange = { text -> onDraftChanged(ProductFilterPriceBound.Min, text) },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.ui_audit_price_min)) },
                suffix = { Text(stringResource(R.string.ui_audit_price_unit)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Text("~", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            OutlinedTextField(
                value = maxDraft,
                onValueChange = { text -> onDraftChanged(ProductFilterPriceBound.Max, text) },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.ui_audit_price_max)) },
                suffix = { Text(stringResource(R.string.ui_audit_price_unit)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Surface(
                onClick = onApplyClick,
                modifier = Modifier.heightIn(min = 48.dp).semantics { role = Role.Button },
                shape = RoundedCornerShape(EumRadius.x8),
                color = MaterialTheme.eumColors.textPrimary,
            ) {
                Text(
                    stringResource(R.string.ui_audit_price_apply),
                    modifier = Modifier.padding(horizontal = EumSpacing.x14, vertical = EumSpacing.x12),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.eumColors.inverse,
                )
            }
        }
        // 적용 중인 직접 입력 조건을 화면에 보이고, 해제하면 조건과 입력 칸을 함께 비운다.
        val appliedLabel = directPriceAppliedLabel(appliedMinManwon, appliedMaxManwon)
        if (appliedLabel != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                Text(
                    appliedLabel,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Surface(
                    onClick = onClearClick,
                    modifier = Modifier.heightIn(min = 48.dp).semantics { role = Role.Button },
                    shape = RoundedCornerShape(EumRadius.x8),
                    color = MaterialTheme.eumColors.surface,
                    border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineNeutral),
                ) {
                    Text(
                        stringResource(R.string.ui_audit_price_direct_clear),
                        modifier = Modifier.padding(horizontal = EumSpacing.x14, vertical = EumSpacing.x12),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                }
            }
        }
    }
}

// 적용된 직접 입력 가격의 안내 문구. 두 값이 모두 없으면 null이다.
@Composable
private fun directPriceAppliedLabel(
    minManwon: Int?,
    maxManwon: Int?,
): String? = when {
    minManwon != null && maxManwon != null -> stringResource(R.string.ui_audit_price_direct_applied_range, minManwon, maxManwon)
    minManwon != null -> stringResource(R.string.ui_audit_price_direct_applied_min, minManwon)
    maxManwon != null -> stringResource(R.string.ui_audit_price_direct_applied_max, maxManwon)
    else -> null
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
    stringResource(R.string.ui_audit_sort_latest) to ProductFilterSort.Latest,
    stringResource(R.string.ui_audit_sort_rating) to ProductFilterSort.Rating,
    stringResource(R.string.ui_audit_sort_price_ascending) to ProductFilterSort.PriceAscending,
    stringResource(R.string.ui_audit_sort_price_descending) to ProductFilterSort.PriceDescending,
)

@Composable
private fun safetyCertificationLabel(certification: SafetyCertificationType): String = when (certification) {
    SafetyCertificationType.KcSafety -> stringResource(R.string.ui_audit_safety_cert_kcs)
    SafetyCertificationType.SelfSafetyCheck -> stringResource(R.string.ui_audit_safety_cert_self_check)
    SafetyCertificationType.TypeApproval -> stringResource(R.string.ui_audit_safety_cert_type_approval)
}

@Composable
private fun pageSizeOptions(): List<Pair<String, ProductFilterPageSize>> = ProductFilterPageSize.entries.map { pageSize ->
    stringResource(R.string.ui_audit_page_size_option, pageSize.count) to pageSize
}

private data class FilterCheckboxItem<T>(
    val value: T,
    val label: String,
    val productCount: Int,
    val isChecked: Boolean,
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductFilterScreenPreview() {
    EumShoppingMallTheme {
        ProductFilterScreen(ProductFilterUiModel(emptyList(), null, null, ProductFilterSort.Recommended), onEvent = {})
    }
}

// Figma 칩 시각 높이 34(r17)·좌우 14는 토큰에 없어 이 화면에서만 쓴다. 터치 영역은 바깥 Box 48dp가 유지한다.
private val FilterChipRadius = 17.dp
private val FilterChipHorizontalPadding = 14.dp

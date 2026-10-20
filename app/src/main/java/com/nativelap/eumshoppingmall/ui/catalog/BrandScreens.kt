package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogBrandListScreen(
    uiModel: CatalogBrandListUiModel,
    onEvent: (CatalogBrandListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        topBar = {
            CatalogTopBar(
                title = stringResource(R.string.catalog_brand_tab),
                actions = listOf(CatalogTopBarAction("search", R.drawable.ic_search, stringResource(R.string.common_search))),
                onActionClick = { onEvent(CatalogBrandListEvent.SearchClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    CatalogSelectionChip(stringResource(R.string.catalog_product_tab), !uiModel.brandModeSelected) {
                        onEvent(CatalogBrandListEvent.ProductModeSelected)
                    }
                    CatalogSelectionChip(stringResource(R.string.catalog_brand_tab), uiModel.brandModeSelected) {}
                }
            }
            uiModel.featuredBrand?.let { featured ->
                item {
                    Column {
                        Text(stringResource(R.string.catalog_brand_featured), style = MaterialTheme.typography.titleMedium)
                        Surface(
                            onClick = { onEvent(CatalogBrandListEvent.BrandSelected(featured.id)) },
                            modifier = Modifier.fillMaxWidth().padding(top = EumSpacing.x12),
                            shape = RoundedCornerShape(EumRadius.x8),
                            color = MaterialTheme.eumColors.textPrimary,
                        ) {
                            Row(Modifier.padding(EumSpacing.x16), verticalAlignment = Alignment.CenterVertically) {
                                CatalogBrandImage(featured, Modifier.size(64.dp))
                                Column(Modifier.weight(1f).padding(start = EumSpacing.x12)) {
                                    Text(featured.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.inverse)
                                    Text(featured.productCountLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.inverse)
                                }
                                Icon(painterResource(R.drawable.ic_arrow_right), null, tint = MaterialTheme.eumColors.inverse)
                            }
                        }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth().heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        Modifier.weight(1f).clip(RoundedCornerShape(EumRadius.capsule))
                            .background(MaterialTheme.eumColors.backgroundSubtle).padding(horizontal = EumSpacing.x12),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(painterResource(R.drawable.ic_search), null, modifier = Modifier.size(18.dp), tint = MaterialTheme.eumColors.textTertiary)
                        BasicTextField(
                            uiModel.query,
                            onValueChange = { onEvent(CatalogBrandListEvent.QueryChanged(it)) },
                            modifier = Modifier.weight(1f).heightIn(min = 44.dp).padding(start = EumSpacing.x8),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.eumColors.textPrimary),
                            decorationBox = { inner ->
                                if (uiModel.query.isEmpty()) Text(stringResource(R.string.catalog_brand_search), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                                inner()
                            },
                        )
                    }
                    Text(
                        if (uiModel.sortAscending) "A–Z" else "Z–A",
                        Modifier.padding(start = EumSpacing.x12).heightIn(min = 44.dp).clickable {
                            onEvent(CatalogBrandListEvent.SortToggled)
                        },
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
            uiModel.letters.forEach { letter ->
                val brands = uiModel.brandsByLetter[letter].orEmpty()
                item(key = "letter-$letter") {
                    Text(letter, Modifier.fillMaxWidth().padding(top = EumSpacing.x8), style = MaterialTheme.typography.titleSmall)
                }
                items(brands, key = { it.id }) { brand ->
                    Row(
                        Modifier.fillMaxWidth().heightIn(min = 60.dp).clickable {
                            onEvent(CatalogBrandListEvent.BrandSelected(brand.id))
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CatalogBrandImage(brand, Modifier.size(44.dp))
                        Column(Modifier.weight(1f).padding(start = EumSpacing.x12)) {
                            Text(brand.name, style = MaterialTheme.typography.labelLarge)
                            Text(brand.productCountLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogBrandDetailScreen(
    uiModel: CatalogBrandDetailUiModel,
    onEvent: (CatalogBrandDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        topBar = {
            CatalogTopBar(
                title = uiModel.brand.name,
                onBackClick = { onEvent(CatalogBrandDetailEvent.BackClicked) },
                actions = listOf(CatalogTopBarAction("share", R.drawable.ic_share_2, stringResource(R.string.catalog_share))),
                onActionClick = { onEvent(CatalogBrandDetailEvent.ShareClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
    ) { padding ->
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val columns = when {
                maxWidth >= 960.dp -> 4
                maxWidth >= 600.dp -> 3
                else -> 2
            }
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).navigationBarsPadding(),
                contentPadding = PaddingValues(bottom = EumSpacing.x24),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
            ) {
                item {
                    Box(
                        Modifier.fillMaxWidth().aspectRatio(1.8f).background(MaterialTheme.eumColors.surface),
                        contentAlignment = Alignment.Center,
                    ) {
                        CatalogBrandImage(uiModel.brand, Modifier.fillMaxSize())
                    }
                    Column(Modifier.padding(horizontal = EumSpacing.x20)) {
                        Text(uiModel.brand.name, style = MaterialTheme.typography.headlineSmall)
                        Text(uiModel.description, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        uiModel.informationRows.forEach { row ->
                            Row(Modifier.fillMaxWidth().padding(top = EumSpacing.x12)) {
                                Text(row.label, Modifier.weight(0.35f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textTertiary)
                                Text(row.value, Modifier.weight(0.65f), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Button(onClick = { onEvent(CatalogBrandDetailEvent.WebsiteClicked) }, modifier = Modifier.fillMaxWidth().padding(top = EumSpacing.x16)) {
                            Text(stringResource(R.string.catalog_brand_website))
                        }
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = EumSpacing.x20)) {
                        Text(stringResource(R.string.catalog_brand_products), style = MaterialTheme.typography.titleMedium)
                        LazyRow(
                            modifier = Modifier.padding(top = EumSpacing.x8),
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        ) {
                            items(uiModel.categories) { category ->
                                CatalogSelectionChip(category, category == uiModel.selectedCategory) {
                                    onEvent(CatalogBrandDetailEvent.CategorySelected(category))
                                }
                            }
                        }
                        Text(
                            uiModel.sortLabel,
                            Modifier.padding(top = EumSpacing.x12).heightIn(min = 44.dp).clickable {
                                onEvent(CatalogBrandDetailEvent.SortClicked)
                            },
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
                items(uiModel.products.chunked(columns)) { row ->
                    Row(Modifier.padding(horizontal = EumSpacing.x20), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        row.forEach { product ->
                            CatalogProductCard(
                                product,
                                onClick = { onEvent(CatalogBrandDetailEvent.ProductSelected(product.id)) },
                                onFavoriteClick = { onEvent(CatalogBrandDetailEvent.FavoriteToggled(product.id)) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
                if (uiModel.otherBrands.isNotEmpty()) {
                    item { Text(stringResource(R.string.catalog_other_brands), Modifier.padding(horizontal = EumSpacing.x20), style = MaterialTheme.typography.titleMedium) }
                    items(uiModel.otherBrands) { brand ->
                        Surface(
                            onClick = { onEvent(CatalogBrandDetailEvent.OtherBrandSelected(brand.id)) },
                            modifier = Modifier.padding(horizontal = EumSpacing.x20).fillMaxWidth(),
                            color = MaterialTheme.eumColors.background,
                            border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                            shape = RoundedCornerShape(EumRadius.x8),
                        ) {
                            Row(Modifier.padding(EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
                                CatalogBrandImage(brand, Modifier.size(40.dp))
                                Text(brand.name, Modifier.weight(1f).padding(start = EumSpacing.x12), style = MaterialTheme.typography.labelLarge)
                                Icon(painterResource(R.drawable.ic_arrow_right), null)
                            }
                        }
                    }
                }
            }
        }
    }
}

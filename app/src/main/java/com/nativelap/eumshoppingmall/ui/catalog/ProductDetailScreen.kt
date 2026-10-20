package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogOptionGroupSelector
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogSelectedOptionCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CatalogProductDetailScreen(
    uiModel: CatalogProductDetailUiModel,
    onEvent: (CatalogProductDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        topBar = {
            CatalogTopBar(
                title = "",
                onBackClick = { onEvent(CatalogProductDetailEvent.BackClicked) },
                actions = listOf(
                    CatalogTopBarAction("share", R.drawable.ic_share_2, stringResource(R.string.catalog_share)),
                    CatalogTopBarAction("cart", R.drawable.ic_shopping_cart, stringResource(R.string.common_cart)),
                ),
                onActionClick = { action ->
                    onEvent(if (action == "share") CatalogProductDetailEvent.ShareClicked else CatalogProductDetailEvent.CartClicked)
                },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        bottomBar = {
            ProductDetailBottomActions(uiModel, onEvent, Modifier.navigationBarsPadding())
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
    ) { padding ->
        val listState = rememberLazyListState()
        var hasInitializedTabs by remember { mutableStateOf(false) }
        val selectedTabIndex = uiModel.selectedTabIndex.coerceIn(0, 3)
        LaunchedEffect(selectedTabIndex) {
            if (hasInitializedTabs) {
                val targetItemIndex = when (selectedTabIndex) {
                    0 -> 2
                    1 -> 0
                    2 -> 3
                    else -> 4
                }
                listState.animateScrollToItem(targetItemIndex)
            }
            hasInitializedTabs = true
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            state = listState,
            contentPadding = PaddingValues(bottom = EumSpacing.x24),
        ) {
            item {
                ProductGallery(uiModel, onEvent)
                Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x20)) {
                    Text(uiModel.product.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.eumColors.textPrimary)
                    uiModel.product.subtitle?.let {
                        Text(it, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                    }
                    uiModel.product.ratingLabel?.let {
                        Text(
                            "$it  ${uiModel.product.reviewCountLabel.orEmpty()}",
                            Modifier.padding(top = EumSpacing.x12),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                    Text(
                        when (val price = uiModel.product.price) {
                            is CatalogPriceUiModel.Available -> price.currentLabel
                            CatalogPriceUiModel.QuoteRequired -> stringResource(R.string.product_price_inquiry)
                        },
                        Modifier.padding(top = EumSpacing.x16),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                    uiModel.shippingRows.forEach { row -> ProductKeyValueRow(row) }
                }
            }
            item { ProductDetailTabs(uiModel, onEvent) }
            item {
                Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24)) {
                    Text(stringResource(R.string.catalog_product_details), style = MaterialTheme.typography.titleMedium)
                    uiModel.specifications.forEach { ProductKeyValueRow(it) }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                    ProductSectionTitle(stringResource(R.string.catalog_product_reviews), uiModel.reviewCount)
                    if (uiModel.reviews.isEmpty()) {
                        Text(stringResource(R.string.catalog_no_reviews), Modifier.padding(vertical = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                    } else {
                        uiModel.reviews.forEach { review -> ProductReviewCard(review) }
                    }
                    OutlinedButton(onClick = { onEvent(CatalogProductDetailEvent.TabSelected(2)) }, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.catalog_review_write))
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                    ProductSectionTitle(stringResource(R.string.catalog_product_questions), uiModel.questionCount)
                    if (uiModel.questions.isEmpty()) {
                        Text(stringResource(R.string.catalog_no_questions), Modifier.padding(vertical = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                    } else {
                        uiModel.questions.forEach { question -> ProductQuestionCard(question) }
                    }
                    OutlinedButton(onClick = { onEvent(CatalogProductDetailEvent.TabSelected(3)) }, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.catalog_question_write))
                    }
                }
            }
            item {
                Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                    Text(stringResource(R.string.catalog_product_returns), style = MaterialTheme.typography.titleMedium)
                    uiModel.returnRows.forEach { ProductKeyValueRow(it) }
                }
            }
            if (uiModel.relatedProducts.isNotEmpty()) {
                item { Text(stringResource(R.string.catalog_product_related), Modifier.padding(EumSpacing.x20), style = MaterialTheme.typography.titleMedium) }
                items(uiModel.relatedProducts.chunked(2)) { row ->
                    Row(Modifier.padding(horizontal = EumSpacing.x20), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        row.forEach { product ->
                            CatalogProductCard(
                                product,
                                onClick = { onEvent(CatalogProductDetailEvent.RelatedProductSelected(product.id)) },
                                onFavoriteClick = { onEvent(CatalogProductDetailEvent.FavoriteToggled(product.id)) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
    if (uiModel.optionsSheetVisible) {
        ModalBottomSheet(onDismissRequest = { onEvent(CatalogProductDetailEvent.CloseOptionsSheet) }) {
            ProductOptionsSheet(uiModel, onEvent)
        }
    }
}

@Composable
private fun ProductGallery(uiModel: CatalogProductDetailUiModel, onEvent: (CatalogProductDetailEvent) -> Unit) {
    val product = uiModel.product
    val galleryImages = uiModel.galleryImageResIds.ifEmpty { listOf(product.imageResId) }
    val selectedImageIndex = uiModel.selectedImageIndex.coerceIn(0, galleryImages.lastIndex)
    val selectedImage = galleryImages.getOrNull(selectedImageIndex)
    Box(
        Modifier.fillMaxWidth().aspectRatio(1f).background(MaterialTheme.eumColors.surface),
        contentAlignment = Alignment.Center,
    ) {
        selectedImage?.let { image ->
            Image(painterResource(image), product.imageDescription ?: product.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        } ?: Icon(painterResource(R.drawable.ic_image), stringResource(R.string.catalog_product_image_placeholder), Modifier.size(48.dp), tint = MaterialTheme.eumColors.placeholderGlyph)
        if (galleryImages.size > 1) {
            androidx.compose.material3.IconButton(
                onClick = { onEvent(CatalogProductDetailEvent.ImageSelected((selectedImageIndex - 1).coerceAtLeast(0))) },
                enabled = selectedImageIndex > 0,
                modifier = Modifier.align(Alignment.CenterStart).size(48.dp),
            ) {
                Icon(painterResource(R.drawable.ic_chevron_left), stringResource(R.string.common_previous))
            }
            androidx.compose.material3.IconButton(
                onClick = { onEvent(CatalogProductDetailEvent.ImageSelected((selectedImageIndex + 1).coerceAtMost(galleryImages.lastIndex))) },
                enabled = selectedImageIndex < galleryImages.lastIndex,
                modifier = Modifier.align(Alignment.CenterEnd).size(48.dp),
            ) {
                Icon(painterResource(R.drawable.ic_chevron_right), stringResource(R.string.common_next))
            }
        }
        Text(
            "${selectedImageIndex + 1} / ${galleryImages.size}",
            Modifier.align(Alignment.BottomEnd).padding(EumSpacing.x12)
                .clip(RoundedCornerShape(EumRadius.capsule)).background(MaterialTheme.eumColors.textPrimary.copy(alpha = 0.72f))
                .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
            color = MaterialTheme.eumColors.inverse,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun ProductDetailTabs(uiModel: CatalogProductDetailUiModel, onEvent: (CatalogProductDetailEvent) -> Unit) {
    Row(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.background)) {
        listOf(
            stringResource(R.string.catalog_product_description),
            stringResource(R.string.catalog_product_shipping),
            stringResource(R.string.catalog_product_reviews),
            stringResource(R.string.catalog_product_questions),
        ).forEachIndexed { index, label ->
            Surface(
                onClick = { onEvent(CatalogProductDetailEvent.TabSelected(index)) },
                modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                color = MaterialTheme.eumColors.background,
                border = BorderStroke(1.dp, if (uiModel.selectedTabIndex.coerceIn(0, 3) == index) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.line),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(label, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun ProductKeyValueRow(row: CatalogKeyValueUiModel) {
    Row(
        Modifier.fillMaxWidth().padding(top = EumSpacing.x12),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x16),
    ) {
        Text(row.label, Modifier.weight(0.38f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
        Text(row.value, Modifier.weight(0.62f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

@Composable
private fun ProductSectionTitle(title: String, count: Int) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 64.dp).padding(horizontal = EumSpacing.x20),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text(count.toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
private fun ProductReviewCard(review: CatalogReviewUiModel) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(review.authorLabel, Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
            Icon(
                painterResource(R.drawable.ic_star_catalog_filled),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.eumColors.warning,
            )
            Text(review.ratingLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
        }
        Text(review.content, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(review.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
private fun ProductQuestionCard(question: CatalogQuestionUiModel) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Text(question.question, style = MaterialTheme.typography.bodySmall)
        question.answer?.let {
            Text("${stringResource(R.string.catalog_answer)}  $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
        Text(question.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
private fun ProductDetailBottomActions(
    uiModel: CatalogProductDetailUiModel,
    onEvent: (CatalogProductDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isQuoteRequired = uiModel.product.price is CatalogPriceUiModel.QuoteRequired
    Row(
        modifier.fillMaxWidth().background(MaterialTheme.eumColors.background).padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x8),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        if (isQuoteRequired) {
            Button(
                onClick = {
                    onEvent(
                        if (uiModel.optionGroups.isEmpty()) {
                            CatalogProductDetailEvent.QuoteRequestClicked
                        } else {
                            CatalogProductDetailEvent.SelectOptionClicked(CatalogPurchaseAction.QuoteRequest)
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                Text(stringResource(R.string.catalog_quote_request))
            }
        } else {
            androidx.compose.material3.IconButton(
                onClick = { onEvent(CatalogProductDetailEvent.ProductFavoriteToggled) },
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painterResource(if (uiModel.product.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                    stringResource(
                        if (uiModel.product.isFavorite) R.string.catalog_remove_favorite else R.string.catalog_add_favorite,
                        uiModel.product.name,
                    ),
                    tint = if (uiModel.product.isFavorite) MaterialTheme.eumColors.danger else MaterialTheme.eumColors.textSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
            OutlinedButton(
                onClick = {
                    onEvent(CatalogProductDetailEvent.SelectOptionClicked(CatalogPurchaseAction.AddToCart))
                },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
            ) {
                Text(stringResource(R.string.catalog_add_to_cart))
            }
            Button(
                onClick = {
                    onEvent(CatalogProductDetailEvent.SelectOptionClicked(CatalogPurchaseAction.BuyNow))
                },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
            ) {
                Text(stringResource(R.string.catalog_buy_now))
            }
        }
    }
}

@Composable
private fun ProductOptionsSheet(uiModel: CatalogProductDetailUiModel, onEvent: (CatalogProductDetailEvent) -> Unit) {
    val isQuoteRequired = uiModel.product.price is CatalogPriceUiModel.QuoteRequired
    val hasRequiredSelections = uiModel.optionGroups.all { group ->
        uiModel.selectedOptions.any { selectedOption ->
            selectedOption.groupId == group.id && group.options.any { it.id == selectedOption.option.id }
        }
    }

    Column(
        Modifier.fillMaxWidth().heightIn(max = 760.dp)
            .padding(horizontal = EumSpacing.x20).padding(bottom = EumSpacing.x24),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        Row(
            Modifier.fillMaxWidth().heightIn(min = 56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.catalog_product_options),
                Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            IconButton(
                onClick = { onEvent(CatalogProductDetailEvent.CloseOptionsSheet) },
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painterResource(R.drawable.ic_x),
                    contentDescription = stringResource(R.string.common_close),
                )
            }
        }
        Column(
            Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            if (!isQuoteRequired && uiModel.optionGroups.isEmpty()) {
                Text(stringResource(R.string.catalog_no_options), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
            }
            uiModel.optionGroups.forEach { group ->
                CatalogOptionGroupSelector(
                    group = group,
                    onOptionSelected = { optionId ->
                        onEvent(CatalogProductDetailEvent.OptionSelected(group.id, optionId))
                    },
                )
            }
            if (uiModel.selectedOptions.isEmpty() && uiModel.optionGroups.isNotEmpty()) {
                Text(
                    stringResource(R.string.catalog_option_select_prompt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
            }

            uiModel.selectedOptions.forEach { selectedOption ->
                val option = selectedOption.option
                CatalogSelectedOptionCard(
                    option = option,
                    isQuoteRequired = isQuoteRequired,
                    onQuantityChanged = { quantity ->
                        onEvent(CatalogProductDetailEvent.QuantityChanged(selectedOption.groupId, option.id, quantity))
                    },
                    onRemove = { onEvent(CatalogProductDetailEvent.OptionRemoved(selectedOption.groupId, option.id)) },
                )
            }

            if (uiModel.selectedOptions.isNotEmpty()) {
                if (isQuoteRequired) {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.catalog_options_selected_quantity), style = MaterialTheme.typography.labelMedium)
                        Text(
                            stringResource(
                                R.string.catalog_options_selected_count,
                                uiModel.selectedOptions.sumOf { it.option.quantity.coerceAtLeast(1).toLong() },
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                        Text(
                            stringResource(R.string.catalog_quote_options_notice),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                } else {
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = EumSpacing.x8),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                            Text(stringResource(R.string.catalog_options_total_amount), style = MaterialTheme.typography.bodySmall)
                            Text(
                                stringResource(
                                    R.string.catalog_options_total_count,
                                    uiModel.selectedOptions.sumOf { it.option.quantity.coerceAtLeast(1).toLong() },
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.eumColors.textTertiary,
                            )
                        }
                        uiModel.totalPriceLabel?.takeIf(String::isNotBlank)?.let { totalPriceLabel ->
                            Text(
                                totalPriceLabel,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.eumColors.textPrimary,
                            )
                        }
                    }
                }
            }
        }

        if (isQuoteRequired) {
            Button(
                onClick = { onEvent(CatalogProductDetailEvent.QuoteRequestClicked) },
                enabled = hasRequiredSelections,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
            ) {
                Text(stringResource(R.string.catalog_quote_request))
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                OutlinedButton(
                    onClick = { onEvent(CatalogProductDetailEvent.AddToCartClicked) },
                    enabled = hasRequiredSelections,
                    modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                ) {
                    Text(stringResource(R.string.catalog_add_to_cart_options))
                }
                Button(
                    onClick = { onEvent(CatalogProductDetailEvent.BuyNowClicked) },
                    enabled = hasRequiredSelections,
                    modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                ) {
                    Text(stringResource(R.string.catalog_buy_now))
                }
            }
        }
    }
}

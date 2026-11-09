package com.nativelap.eumshoppingmall.ui.productreview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import kotlin.math.roundToInt

data class ProductReviewItemUiModel(
    val id: String,
    val authorName: String,
    val isPurchaseVerified: Boolean,
    val dateLabel: String,
    val rating: Int,
    val optionLabel: String,
    val body: String,
    val photos: List<ProductReviewPhotoUiModel>,
)

data class ProductReviewPhotoUiModel(
    val id: String,
    val imageResId: Int,
    val isPlaceholder: Boolean,
)

data class ProductReviewUiModel(
    val productId: String,
    val productName: String,
    val brandName: String?,
    val variantLabel: String?,
    val rating: Float,
    val reviewCountLabel: String,
    val reviews: List<ProductReviewItemUiModel>,
)

sealed interface ProductReviewScreenEvent {
    data object BackClicked : ProductReviewScreenEvent
    data object ReviewWriteClicked : ProductReviewScreenEvent
}

@Composable
fun ProductReviewScreen(
    uiModel: ProductReviewUiModel,
    onEvent: (ProductReviewScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rating = uiModel.rating.takeIf { it.isFinite() }?.coerceIn(0f, 5f) ?: 0f
    val ratingLabel = stringResource(R.string.ui_audit_rating_value, rating)
    UiAuditScreenScaffold(
        title = stringResource(R.string.product_reviews_all),
        onBackClick = { onEvent(ProductReviewScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.catalog_review_write),
        onBottomActionClick = { onEvent(ProductReviewScreenEvent.ReviewWriteClicked) },
        showBottomDivider = true,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(top = EumSpacing.x16, bottom = EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            ProductSummaryCard(uiModel)
            RatingSummaryCard(rating, ratingLabel, uiModel.reviewCountLabel)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                ReviewFilterPill("전체 ${uiModel.reviewCountLabel}", selected = true)
                ReviewFilterPill("사진 리뷰", selected = false)
                Text(
                    text = stringResource(R.string.ui_audit_reviews_sort_latest),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.eumColors.textPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                )
            }
            if (uiModel.reviews.isEmpty()) {
                Text(
                    text = stringResource(R.string.product_review_empty),
                    modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x40),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.eumColors.textSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            } else {
                uiModel.reviews.forEach { review ->
                    androidx.compose.runtime.key(review.id) {
                        ReviewCard(review)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductSummaryCard(model: ProductReviewUiModel) {
    Surface(
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(EumSpacing.x12),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(EumSpacing.x64).clip(RoundedCornerShape(EumRadius.x8))
                    .background(MaterialTheme.eumColors.background),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_image),
                    contentDescription = null,
                    modifier = Modifier.size(EumSpacing.x24),
                    tint = MaterialTheme.eumColors.textDisabled,
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
                model.brandName?.let { brandName ->
                    Text(brandName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
                Text(model.productName, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
                model.variantLabel?.let { variantLabel ->
                    Text(variantLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
        }
    }
}

@Composable
private fun RatingSummaryCard(rating: Float, ratingLabel: String, countLabel: String) {
    val ratingAccessibilityDescription = stringResource(R.string.ui_audit_rating_accessibility, ratingLabel)
    Surface(
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(EumSpacing.x12),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(ratingLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
            Row(
                modifier = Modifier.weight(1f).semantics(mergeDescendants = true) {
                    contentDescription = ratingAccessibilityDescription
                },
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            ) {
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(if (index < rating.roundToInt()) R.drawable.ic_star_catalog_filled else R.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier.size(EumSpacing.x24),
                        tint = MaterialTheme.eumColors.ratingStar,
                    )
                }
            }
            Text("리뷰 $countLabel", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
    }
}

@Composable
private fun ReviewFilterPill(label: String, selected: Boolean) {
    Surface(
        shape = CircleShape,
        color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
        border = if (selected) null else BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x8),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
        )
    }
}

@Composable
private fun ReviewCard(review: ProductReviewItemUiModel) {
    Surface(
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(EumSpacing.x12),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            ) {
                Box(
                    Modifier.size(EumSpacing.x32).clip(CircleShape).background(MaterialTheme.eumColors.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(painterResource(R.drawable.ic_user_round), null, Modifier.size(EumSpacing.x20), tint = MaterialTheme.eumColors.textSecondary)
                }
                Text(review.authorName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                if (review.isPurchaseVerified) {
                    Text(stringResource(R.string.review_write_verified), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.success)
                }
                Text(review.dateLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textDisabled)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                repeat(5) { star ->
                    Icon(
                        painterResource(if (star < review.rating.coerceIn(0, 5)) R.drawable.ic_star_catalog_filled else R.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier.size(EumSpacing.x16),
                        tint = MaterialTheme.eumColors.ratingStar,
                    )
                }
                Text(
                    stringResource(R.string.ui_audit_rating_value, review.rating.coerceIn(0, 5).toFloat()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textPrimary,
                )
            }
            Text(review.optionLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            Text(review.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
            if (review.photos.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                ) {
                    items(review.photos, key = ProductReviewPhotoUiModel::id) { photo ->
                        if (photo.isPlaceholder) {
                            Box(
                                Modifier.size(EumSpacing.x56).clip(RoundedCornerShape(EumRadius.x4))
                                    .background(MaterialTheme.eumColors.background),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(painterResource(photo.imageResId), null, Modifier.size(EumSpacing.x24), tint = MaterialTheme.eumColors.textDisabled)
                            }
                        } else {
                            Image(
                                painter = painterResource(photo.imageResId),
                                contentDescription = null,
                                modifier = Modifier.size(EumSpacing.x56).clip(RoundedCornerShape(EumRadius.x4)),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
            }
        }
    }
}

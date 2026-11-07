package com.nativelap.eumshoppingmall.ui.productreview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import kotlin.math.roundToInt

data class ProductReviewUiModel(
    val rating: Float,
    val reviewCountLabel: String,
    val reviews: List<String>,
)

sealed interface ProductReviewScreenEvent {
    data object BackClicked : ProductReviewScreenEvent
}

@Composable
fun ProductReviewScreen(
    uiModel: ProductReviewUiModel,
    onEvent: (ProductReviewScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val normalizedRating = uiModel.rating.takeIf { it.isFinite() }?.coerceIn(0f, 5f) ?: 0f
    val ratingLabel = stringResource(R.string.ui_audit_rating_value, normalizedRating)
    val ratingAccessibilityLabel = stringResource(
        R.string.ui_audit_rating_accessibility,
        ratingLabel,
    )
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_reviews_title),
        onBackClick = { onEvent(ProductReviewScreenEvent.BackClicked) },
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x16, bottom = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Text(
                text = stringResource(
                    R.string.ui_audit_review_summary,
                    ratingLabel,
                    uiModel.reviewCountLabel,
                ),
                modifier = Modifier.padding(bottom = EumSpacing.x24),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.eumColors.textDisabled,
            )
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(
                    text = stringResource(R.string.ui_audit_rating),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().semantics(mergeDescendants = true) {
                        contentDescription = ratingAccessibilityLabel
                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(EumRadius.x8),
                        color = MaterialTheme.eumColors.surface,
                        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineNeutral),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x11),
                        ) {
                            repeat(5) { index ->
                                if (index < normalizedRating.roundToInt()) {
                                    Box(Modifier.size(EumSpacing.x24)) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_star_catalog_filled),
                                            contentDescription = null,
                                            modifier = Modifier.matchParentSize(),
                                            tint = MaterialTheme.eumColors.ratingStar,
                                        )
                                        Icon(
                                            painter = painterResource(R.drawable.ic_star),
                                            contentDescription = null,
                                            modifier = Modifier.matchParentSize(),
                                            tint = MaterialTheme.eumColors.ratingStarOutline,
                                        )
                                    }
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_star),
                                        contentDescription = null,
                                        modifier = Modifier.size(EumSpacing.x24),
                                        tint = MaterialTheme.eumColors.ratingStarOutline,
                                    )
                                }
                            }
                            Text(
                                text = ratingLabel,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.eumColors.textDisabled,
                            )
                        }
                    }
                }
            }
            uiModel.reviews.forEach { review ->
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(
                        text = stringResource(R.string.ui_audit_review_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                    UiAuditTextField(
                        label = stringResource(R.string.ui_audit_review_label),
                        value = review,
                        placeholder = "",
                        onValueChange = {},
                        readOnly = true,
                        minHeight = EumSpacing.x100,
                        maxLines = 5,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductReviewScreenPreview() {
    EumShoppingMallTheme {
        ProductReviewScreen(
            uiModel = ProductReviewUiModel(
                rating = 4.8f,
                reviewCountLabel = "128개",
                reviews = listOf(
                    stringResource(R.string.ui_audit_review_example_one),
                    stringResource(R.string.ui_audit_review_example_two),
                ),
            ),
            onEvent = {},
        )
    }
}

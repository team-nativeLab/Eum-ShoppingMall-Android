package com.nativelap.eumshoppingmall.ui.productreview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class ProductReviewWriteUiModel(
    val brand: String,
    val product: String,
    val option: String,
    val reviewer: String,
    val rating: Int = 0,
    val content: String = "",
    val canSubmit: Boolean = false,
)

sealed interface ProductReviewWriteEvent {
    data object BackClicked : ProductReviewWriteEvent
    data object SearchClicked : ProductReviewWriteEvent
    data class RatingSelected(val value: Int) : ProductReviewWriteEvent
    data class ContentChanged(val value: String) : ProductReviewWriteEvent
    data object PhotoClicked : ProductReviewWriteEvent
    data object SubmitClicked : ProductReviewWriteEvent
}

@Composable
fun ProductReviewWriteScreen(
    uiModel: ProductReviewWriteUiModel,
    onEvent: (ProductReviewWriteEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ResponsiveContentContainer(modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = MaterialTheme.eumColors.background,
            topBar = { ReviewWriteTopBar(onEvent) },
            bottomBar = {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12)) {
                    Button(onClick = { onEvent(ProductReviewWriteEvent.SubmitClicked) }, enabled = uiModel.canSubmit, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                        Text(stringResource(R.string.review_write_submit))
                    }
                }
            },
        ) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                    .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
            ) {
                ReviewWriteProductCard(uiModel)
                ReviewWriteAuthor(uiModel.reviewer)
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                    Text(stringResource(R.string.review_write_rating_title), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.review_write_rating_hint), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    repeat(5) { index ->
                        val star = index + 1
                        val starAccessibilityLabel = stringResource(R.string.review_write_star_accessibility, star)
                        IconButton(
                            onClick = { onEvent(ProductReviewWriteEvent.RatingSelected(star)) },
                            modifier = Modifier.size(48.dp).semantics {
                                contentDescription = starAccessibilityLabel
                            },
                        ) {
                            Icon(painterResource(if (star <= uiModel.rating) R.drawable.ic_star_filled else R.drawable.ic_star), null, Modifier.size(24.dp), tint = if (star <= uiModel.rating) MaterialTheme.eumColors.ratingStar else MaterialTheme.eumColors.textTertiary)
                        }
                    }
                }
                Text(stringResource(R.string.review_write_content_label), style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = uiModel.content,
                    onValueChange = { onEvent(ProductReviewWriteEvent.ContentChanged(it)) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 132.dp),
                    placeholder = { Text(stringResource(R.string.review_write_content_hint)) },
                    minLines = 4,
                )
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                    Text(stringResource(R.string.review_write_photo_title), style = MaterialTheme.typography.labelLarge)
                    Text(stringResource(R.string.review_write_photo_hint), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
                Surface(
                    Modifier.size(82.dp).clickable { onEvent(ProductReviewWriteEvent.PhotoClicked) },
                    shape = RoundedCornerShape(EumRadius.x8),
                    color = MaterialTheme.eumColors.surface,
                    border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                ) {
                    // Figma 사진 추가 박스: 아이콘은 위쪽 여백 16dp, 문구는 아래쪽에 붙는다.
                    Column(
                        Modifier.fillMaxSize().padding(vertical = EumSpacing.x16),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Icon(painterResource(R.drawable.ic_image_plus), null, Modifier.size(24.dp))
                        Text(stringResource(R.string.review_write_add_photo), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewWriteTopBar(onEvent: (ProductReviewWriteEvent) -> Unit) {
    Row(Modifier.fillMaxWidth().statusBarsPadding().heightIn(min = 56.dp).padding(horizontal = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onEvent(ProductReviewWriteEvent.BackClicked) }, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.ic_arrow_left), stringResource(R.string.common_back))
        }
        Text(stringResource(R.string.review_write_title), Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
        IconButton(onClick = { onEvent(ProductReviewWriteEvent.SearchClicked) }, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.ic_search), stringResource(R.string.common_search))
        }
    }
}

@Composable
private fun ReviewWriteProductCard(model: ProductReviewWriteUiModel) {
    Surface(shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = BorderStroke(1.dp, MaterialTheme.eumColors.line)) {
        Row(Modifier.fillMaxWidth().padding(EumSpacing.x12), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(64.dp), shape = RoundedCornerShape(EumRadius.x8), color = MaterialTheme.eumColors.backgroundSubtle) {
                Icon(painterResource(R.drawable.ic_image), null, Modifier.padding(20.dp), tint = MaterialTheme.eumColors.textTertiary)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
                Text(model.brand, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                Text(model.product, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
                Text(model.option, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
        }
    }
}

@Composable
private fun ReviewWriteAuthor(reviewer: String) {
    Surface(shape = RoundedCornerShape(EumRadius.x8), color = MaterialTheme.eumColors.surface, border = BorderStroke(1.dp, MaterialTheme.eumColors.line)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(32.dp), shape = CircleShape, color = MaterialTheme.eumColors.backgroundSubtle) {
                Icon(painterResource(R.drawable.ic_user_round), null, Modifier.padding(6.dp), tint = MaterialTheme.eumColors.textSecondary)
            }
            Text(reviewer, Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
            Surface(shape = RoundedCornerShape(EumRadius.capsule), color = MaterialTheme.eumColors.successTint) {
                Row(Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_circle_check), null, Modifier.size(16.dp), tint = MaterialTheme.eumColors.success)
                    Text(stringResource(R.string.review_write_verified), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.success)
                }
            }
        }
    }
}

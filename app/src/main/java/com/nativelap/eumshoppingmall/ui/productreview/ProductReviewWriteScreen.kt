package com.nativelap.eumshoppingmall.ui.productreview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.EumPrimaryButton
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
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
                Surface(color = MaterialTheme.eumColors.surface) {
                    Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding()) {
                        HorizontalDivider(color = MaterialTheme.eumColors.line)
                        Column(Modifier.fillMaxWidth().padding(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x12, bottom = EumSpacing.x16)) {
                            EumPrimaryButton(onClick = { onEvent(ProductReviewWriteEvent.SubmitClicked) }, enabled = uiModel.canSubmit, modifier = Modifier.fillMaxWidth()) {
                                Text(stringResource(R.string.review_write_submit))
                            }
                        }
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
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(stringResource(R.string.review_write_rating_title), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
                    Text(stringResource(R.string.review_write_rating_hint), style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
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
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(stringResource(R.string.review_write_content_label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                    ReviewContentInput(uiModel.content) { onEvent(ProductReviewWriteEvent.ContentChanged(it)) }
                }
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(stringResource(R.string.review_write_photo_title), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                    Text(stringResource(R.string.review_write_photo_hint), style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
                }
                Surface(
                    Modifier.size(82.dp).clickable { onEvent(ProductReviewWriteEvent.PhotoClicked) },
                    shape = RoundedCornerShape(EumRadius.x10),
                    color = MaterialTheme.eumColors.surface,
                    border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                ) {
                    // Figma 사진 추가 박스: 아이콘은 위쪽 여백 16dp, 문구는 아래쪽에 붙는다.
                    Column(
                        Modifier.fillMaxSize().padding(vertical = EumSpacing.x16),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Icon(painterResource(R.drawable.ic_image_plus), null, Modifier.size(28.dp))
                        Text(stringResource(R.string.review_write_add_photo), style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textSecondary)
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
        Text(stringResource(R.string.review_write_title), Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.eumColors.textPrimary, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
        IconButton(onClick = { onEvent(ProductReviewWriteEvent.SearchClicked) }, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.ic_search), stringResource(R.string.common_search))
        }
    }
}

@Composable
private fun ReviewWriteProductCard(model: ProductReviewWriteUiModel) {
    Surface(shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = BorderStroke(1.dp, MaterialTheme.eumColors.line)) {
        Row(Modifier.fillMaxWidth().padding(EumSpacing.x12), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
            // Figma 135° 그라디언트 #ECECEC→#DCDCDC는 imagePlaceholderStart/End 토큰(#F2F2F2→#E8E8E8)으로 대체한다.
            Box(
                Modifier.size(64.dp).background(
                    Brush.linearGradient(listOf(MaterialTheme.eumColors.imagePlaceholderStart, MaterialTheme.eumColors.imagePlaceholderEnd)),
                ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(painterResource(R.drawable.ic_image), null, Modifier.size(32.dp), tint = MaterialTheme.eumColors.textTertiary)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
                Text(model.brand, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
                Text(model.product, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                Text(model.option, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
            }
        }
    }
}

@Composable
private fun ReviewWriteAuthor(reviewer: String) {
    Column {
        Surface(shape = RoundedCornerShape(EumRadius.x10), color = MaterialTheme.eumColors.surface, border = BorderStroke(1.dp, MaterialTheme.eumColors.line)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(32.dp), shape = CircleShape, color = MaterialTheme.eumColors.surfaceSecondary) {
                    Icon(painterResource(R.drawable.ic_user_round), null, Modifier.padding(4.dp), tint = MaterialTheme.eumColors.textSecondary)
                }
                Text(reviewer, Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                Surface(Modifier.heightIn(min = 22.dp), shape = RoundedCornerShape(EumRadius.capsule), color = MaterialTheme.eumColors.accentTint) {
                    Row(Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x2), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.ic_circle_check), null, Modifier.size(14.dp), tint = ReviewVerifiedAccent)
                        Text(stringResource(R.string.review_write_verified), style = MaterialTheme.typography.labelSmall, color = ReviewVerifiedAccent)
                    }
                }
            }
        }
        HorizontalDivider(Modifier.padding(top = EumSpacing.x12), color = MaterialTheme.eumColors.line)
    }
}

/** 리뷰 입력창. Figma 132h·r10·패딩 14·placeholder 14/20 textTertiary·테두리 line. */
@Composable
private fun ReviewContentInput(content: String, onContentChange: (String) -> Unit) {
    val hint = stringResource(R.string.review_write_content_hint)
    BasicTextField(
        value = content,
        onValueChange = onContentChange,
        modifier = Modifier.fillMaxWidth().heightIn(min = 132.dp)
            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.x10))
            .border(BorderStroke(1.dp, MaterialTheme.eumColors.line), RoundedCornerShape(EumRadius.x10)),
        textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.eumColors.textPrimary),
        cursorBrush = SolidColor(MaterialTheme.eumColors.textPrimary),
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxWidth().padding(ReviewInputPadding)) {
                if (content.isEmpty()) {
                    // placeholder 는 시맨틱을 남겨 입력이 비었을 때 힌트로 읽히고, contentDescription 으로 입력 텍스트를 덮지 않는다.
                    Text(
                        hint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                }
                innerTextField()
            }
        },
    )
}

// TODO: accent 토큰 승격 후속(ui/theme 변경 이슈에서 EumDesignTokenTest 추가). 그 전까지 Figma 인증 뱃지 accent(#0A6E57)는 이 화면 로컬 상수로 쓴다.
// 대비: #0A6E57 on accentTint(#E7F5F1) 약 5.5:1로 AA(4.5:1) 충족.
private val ReviewVerifiedAccent = Color(0xFF0A6E57)

// 입력창 패딩 14dp는 EumSpacing 토큰에 없어 이 화면에서만 쓴다.
private val ReviewInputPadding = 14.dp

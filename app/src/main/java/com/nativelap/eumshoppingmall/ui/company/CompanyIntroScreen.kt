package com.nativelap.eumshoppingmall.ui.company

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumCapsuleShape
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import kotlinx.coroutines.launch

private val ANCHOR_STRIP_HEIGHT = 56.dp
private val ANCHOR_CHIP_VISUAL_HEIGHT = 36.dp
private val ANCHOR_CHIP_HORIZONTAL_PADDING = 14.dp
private const val MAP_PLACEHOLDER_ASPECT_RATIO = 1.776f

data class CompanyBusinessAreaUiModel(val title: String, val description: String, val iconResId: Int)
data class CompanyHistoryItemUiModel(val year: String, val description: String)
data class CompanyCertificationUiModel(val name: String, val description: String)
data class CompanyIntroUiModel(
    val businessAreas: List<CompanyBusinessAreaUiModel>,
    val history: List<CompanyHistoryItemUiModel>,
    val certifications: List<CompanyCertificationUiModel>,
    val partners: List<String>,
    val address: String,
    val phone: String,
    val email: String,
)

sealed interface CompanyIntroEvent {
    data object BackClicked : CompanyIntroEvent
    data object MapClicked : CompanyIntroEvent
    data object ContactClicked : CompanyIntroEvent
}

@Composable
fun CompanyIntroScreen(uiModel: CompanyIntroUiModel, onEvent: (CompanyIntroEvent) -> Unit, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val sectionRequesters = List(6) { remember { BringIntoViewRequester() } }
    val anchors = listOf(
        stringResource(R.string.company_anchor_overview),
        stringResource(R.string.company_anchor_business),
        stringResource(R.string.company_anchor_history),
        stringResource(R.string.company_anchor_certification),
        stringResource(R.string.company_anchor_partners),
        stringResource(R.string.company_anchor_location),
    )
    UiAuditScreenScaffold(stringResource(R.string.company_about_us), { onEvent(CompanyIntroEvent.BackClicked) }, modifier) { _ ->
        // 가로 여백은 각 섹션이 직접 주어, 칩 띠와 구분선이 화면 전체 폭을 쓰게 한다.
        Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
            Column(
                Modifier.fillMaxWidth().padding(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x16, bottom = EumSpacing.x32),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
            ) {
                Image(
                    painter = painterResource(R.drawable.figma_company_about_visual),
                    contentDescription = stringResource(R.string.company_about_us),
                    modifier = Modifier.fillMaxWidth().aspectRatio(1.6f)
                        .clip(RoundedCornerShape(EumRadius.x12)),
                    contentScale = ContentScale.Crop,
                )
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                    Text(stringResource(R.string.company_about_english).uppercase(), style = EumTypographyTokens.overline, color = MaterialTheme.eumColors.textTertiary)
                    Text(stringResource(R.string.company_tagline_home), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.eumColors.textPrimary)
                }
                Text(
                    stringResource(R.string.company_about_description),
                    // 한글 문장이 어절 중간("입니/다.")에서 끊기지 않도록 문단용 줄바꿈 규칙을 쓴다.
                    style = MaterialTheme.typography.bodyMedium.copy(lineBreak = LineBreak.Paragraph),
                    color = MaterialTheme.eumColors.textSecondary,
                )
            }
            CompanyAnchorStrip(
                anchors = anchors,
                onAnchorClick = { index -> scope.launch { sectionRequesters[index].bringIntoView() } },
            )
            CompanyIntroSection(stringResource(R.string.company_overview_title), stringResource(R.string.company_overview_english), modifier = Modifier.bringIntoViewRequester(sectionRequesters[0])) {
                Text(
                    stringResource(R.string.company_about_description),
                    // 한글 문장이 어절 중간("입니/다.")에서 끊기지 않도록 문단용 줄바꿈 규칙을 쓴다.
                    style = MaterialTheme.typography.bodyMedium.copy(lineBreak = LineBreak.Paragraph),
                    color = MaterialTheme.eumColors.textSecondary,
                )
            }
            CompanyIntroSection(stringResource(R.string.company_business), stringResource(R.string.company_business_english), modifier = Modifier.bringIntoViewRequester(sectionRequesters[1])) {
                uiModel.businessAreas.chunked(2).forEach { rowAreas ->
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        rowAreas.forEach { area ->
                            Surface(
                                Modifier.weight(1f),
                                shape = RoundedCornerShape(EumRadius.x12),
                                color = MaterialTheme.eumColors.surface,
                                border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                            ) {
                                Column(Modifier.fillMaxWidth().padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                                    Icon(painterResource(area.iconResId), null, Modifier.size(EumSpacing.x24), tint = MaterialTheme.eumColors.textPrimary)
                                    Text(area.title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                                    Text(area.description, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textSecondary)
                                }
                            }
                        }
                        if (rowAreas.size == 1) Box(Modifier.weight(1f))
                    }
                }
            }
            CompanyIntroSection(stringResource(R.string.company_history), stringResource(R.string.company_history_english), modifier = Modifier.bringIntoViewRequester(sectionRequesters[2])) {
                uiModel.history.forEachIndexed { index, item ->
                    CompanyHistoryTimelineItem(
                        item = item,
                        isLatest = index == 0,
                        isLast = index == uiModel.history.lastIndex,
                    )
                }
            }
            CompanyIntroSection(stringResource(R.string.company_certifications), stringResource(R.string.company_certification_english), modifier = Modifier.bringIntoViewRequester(sectionRequesters[3])) {
                uiModel.certifications.forEach { certification ->
                    CompanyInfoRow(certification.name, certification.description)
                }
            }
            CompanyIntroSection(stringResource(R.string.company_partners), stringResource(R.string.company_partners_english), modifier = Modifier.bringIntoViewRequester(sectionRequesters[4])) {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    uiModel.partners.forEach { partner ->
                        Surface(Modifier.size(width = 108.dp, height = 56.dp), shape = RoundedCornerShape(EumRadius.x8), color = MaterialTheme.eumColors.surface) {
                            Box(contentAlignment = Alignment.Center) { Text(partner, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textSecondary) }
                        }
                    }
                }
            }
            CompanyIntroSection(stringResource(R.string.company_location), stringResource(R.string.company_location_english), isLast = true, modifier = Modifier.bringIntoViewRequester(sectionRequesters[5])) {
                Box(
                    Modifier.fillMaxWidth().aspectRatio(MAP_PLACEHOLDER_ASPECT_RATIO).clip(RoundedCornerShape(EumRadius.x12))
                        .background(
                            Brush.verticalGradient(
                                listOf(MaterialTheme.eumColors.imagePlaceholderStart, MaterialTheme.eumColors.imagePlaceholderEnd),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(painterResource(R.drawable.ic_image), null, Modifier.size(32.dp), tint = MaterialTheme.eumColors.textSecondary)
                }
                CompanyInfoRow(stringResource(R.string.company_address), uiModel.address)
                CompanyInfoRow(stringResource(R.string.customer_support_phone_label), uiModel.phone)
                CompanyInfoRow(stringResource(R.string.company_email), uiModel.email)
                OutlinedButton(
                    onClick = { onEvent(CompanyIntroEvent.MapClicked) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                    shape = RoundedCornerShape(EumRadius.x8),
                    border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x6), verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.company_map_open), style = MaterialTheme.typography.labelMedium)
                        Icon(painterResource(R.drawable.ic_external_link), null, Modifier.size(18.dp))
                    }
                }
                Box(
                    Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        .clickable(role = Role.Button) { onEvent(CompanyIntroEvent.ContactClicked) },
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(stringResource(R.string.customer_support_inquiry), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                }
            }
        }
    }
}

@Composable
private fun CompanyIntroSection(
    title: String,
    englishTitle: String,
    isLast: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier.fillMaxWidth().padding(
            start = EumSpacing.x20,
            end = EumSpacing.x20,
            top = EumSpacing.x32,
            bottom = if (isLast) EumSpacing.x40 else EumSpacing.x32,
        ),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(englishTitle.uppercase(), style = EumTypographyTokens.overline, color = MaterialTheme.eumColors.textTertiary)
            Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
        }
        content()
    }
    if (!isLast) HorizontalDivider(color = MaterialTheme.eumColors.line)
}

@Composable
private fun CompanyAnchorStrip(anchors: List<String>, onAnchorClick: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
        HorizontalDivider(color = MaterialTheme.eumColors.line)
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(min = ANCHOR_STRIP_HEIGHT)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = EumSpacing.x20),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            anchors.forEachIndexed { index, anchor ->
                // 칩 시각 높이는 36dp, 터치 영역은 48dp를 유지한다.
                Box(
                    Modifier.heightIn(min = 48.dp).clickable(role = Role.Button) { onAnchorClick(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        shape = EumCapsuleShape,
                        color = MaterialTheme.eumColors.surface,
                        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong),
                    ) {
                        Box(
                            Modifier.heightIn(min = ANCHOR_CHIP_VISUAL_HEIGHT).padding(horizontal = ANCHOR_CHIP_HORIZONTAL_PADDING),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                anchor,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.eumColors.textSecondary,
                            )
                        }
                    }
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.eumColors.line)
    }
}

@Composable
private fun CompanyHistoryTimelineItem(item: CompanyHistoryItemUiModel, isLatest: Boolean, isLast: Boolean) {
    Row(
        Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.padding(top = EumSpacing.x8).size(8.dp).clip(CircleShape)
                    .background(if (isLatest) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary),
            )
            if (!isLast) Box(Modifier.width(1.dp).weight(1f).background(MaterialTheme.eumColors.line))
        }
        Column(Modifier.padding(bottom = if (isLast) 0.dp else EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
            Text(item.year, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
            Text(item.description, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
        }
    }
}

@Composable
private fun CompanyInfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.Top) {
        Text(label, Modifier.width(80.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
        Text(value, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

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
    data class SectionSelected(val section: String) : CompanyIntroEvent
    data object MapClicked : CompanyIntroEvent
    data object ContactClicked : CompanyIntroEvent
}

@Composable
fun CompanyIntroScreen(uiModel: CompanyIntroUiModel, onEvent: (CompanyIntroEvent) -> Unit, modifier: Modifier = Modifier) {
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
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                Image(
                    painter = painterResource(R.drawable.figma_company_about_visual),
                    contentDescription = stringResource(R.string.company_about_us),
                    modifier = Modifier.fillMaxWidth().aspectRatio(1.6f)
                        .clip(RoundedCornerShape(EumRadius.x12)),
                    contentScale = ContentScale.Crop,
                )
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(stringResource(R.string.company_about_english).uppercase(), style = companyEyebrowStyle(), color = MaterialTheme.eumColors.textTertiary)
                    Text(stringResource(R.string.company_tagline_home), style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.eumColors.textPrimary)
                }
                Text(stringResource(R.string.company_about_description), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surfaceSecondary)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                anchors.forEach { anchor ->
                    Surface(
                        Modifier.heightIn(min = 48.dp).clickable(role = Role.Button) {
                            onEvent(CompanyIntroEvent.SectionSelected(anchor))
                        },
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.eumColors.surface,
                    ) {
                        Box(Modifier.padding(horizontal = EumSpacing.x12), contentAlignment = Alignment.Center) {
                            Text(anchor, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textPrimary)
                        }
                    }
                }
            }
            CompanyIntroSection(stringResource(R.string.company_overview_title), stringResource(R.string.company_overview_english)) {
                Text(stringResource(R.string.company_about_description), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
            CompanyIntroSection(stringResource(R.string.company_business), stringResource(R.string.company_business_english)) {
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
                                    Text(area.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                                }
                            }
                        }
                        if (rowAreas.size == 1) Box(Modifier.weight(1f))
                    }
                }
            }
            CompanyIntroSection(stringResource(R.string.company_history), stringResource(R.string.company_history_english)) {
                uiModel.history.forEachIndexed { index, item ->
                    CompanyHistoryTimelineItem(
                        item = item,
                        isLatest = index == 0,
                        isLast = index == uiModel.history.lastIndex,
                    )
                }
            }
            CompanyIntroSection(stringResource(R.string.company_certifications), stringResource(R.string.company_certification_english)) {
                uiModel.certifications.forEach { certification ->
                    CompanyInfoRow(certification.name, certification.description)
                }
            }
            CompanyIntroSection(stringResource(R.string.company_partners), stringResource(R.string.company_partners_english)) {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    uiModel.partners.forEach { partner ->
                        Surface(Modifier.size(width = 108.dp, height = 56.dp), shape = RoundedCornerShape(EumRadius.x8), color = MaterialTheme.eumColors.surfaceSecondary) {
                            Box(contentAlignment = Alignment.Center) { Text(partner, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary) }
                        }
                    }
                }
            }
            CompanyIntroSection(stringResource(R.string.company_location), stringResource(R.string.company_location_english)) {
                Box(
                    Modifier.fillMaxWidth().aspectRatio(1.8f).clip(RoundedCornerShape(EumRadius.x12))
                        .background(MaterialTheme.eumColors.surfaceSecondary),
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
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.company_map_open), style = MaterialTheme.typography.labelMedium)
                        Icon(painterResource(R.drawable.ic_external_link), null, Modifier.size(16.dp))
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
private fun CompanyIntroSection(title: String, englishTitle: String, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x32), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(englishTitle.uppercase(), style = companyEyebrowStyle(), color = MaterialTheme.eumColors.textTertiary)
            Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
        }
        content()
    }
    HorizontalDivider(color = MaterialTheme.eumColors.line)
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
        Column(Modifier.padding(bottom = if (isLast) 0.dp else EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(item.year, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
            Text(item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
    }
}

@Composable
private fun CompanyInfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.Top) {
        Text(label, Modifier.width(80.dp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
        Text(value, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

@Composable
private fun companyEyebrowStyle(): TextStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.08.em)

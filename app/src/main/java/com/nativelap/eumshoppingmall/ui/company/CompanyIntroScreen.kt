package com.nativelap.eumshoppingmall.ui.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
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
    UiAuditScreenScaffold(stringResource(R.string.company_about_us), { onEvent(CompanyIntroEvent.BackClicked) }, modifier) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) {
            Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                Image(
                    painter = painterResource(R.drawable.figma_company_about_visual),
                    contentDescription = stringResource(R.string.company_about_us),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 219.dp).aspectRatio(1.8f)
                        .clip(RoundedCornerShape(EumRadius.x12)),
                    contentScale = ContentScale.Crop,
                )
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
            CompanyIntroSection(stringResource(R.string.company_overview), stringResource(R.string.company_overview_english)) {
                Text(stringResource(R.string.company_overview_title), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
                Text(stringResource(R.string.company_about_description), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
            CompanyIntroSection(stringResource(R.string.company_business), stringResource(R.string.company_business_english)) {
                uiModel.businessAreas.chunked(2).forEach { rowAreas ->
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        rowAreas.forEach { area ->
                            Surface(Modifier.weight(1f), shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surfaceSecondary) {
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
                uiModel.history.forEach { item ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.Top) {
                        Text(item.year, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                        Text(item.description, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                    }
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
                Button(onClick = { onEvent(CompanyIntroEvent.MapClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text(stringResource(R.string.company_map_open))
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
    Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(englishTitle.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
        }
        content()
    }
    HorizontalDivider(color = MaterialTheme.eumColors.line)
}

@Composable
private fun CompanyInfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.Top) {
        Text(label, Modifier.weight(0.35f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
        Text(value, Modifier.weight(0.65f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

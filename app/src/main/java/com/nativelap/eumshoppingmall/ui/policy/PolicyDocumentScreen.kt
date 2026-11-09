package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class PolicyDocumentStepUiModel(val title: String, val description: String)

data class PolicyDocumentSectionUiModel(
    val title: String,
    val paragraphs: List<String>,
    val chapterTitle: String? = null,
    val keyValueRows: List<Pair<String, String>> = emptyList(),
    val steps: List<PolicyDocumentStepUiModel> = emptyList(),
)

data class PolicyDocumentUiModel(
    val title: String,
    val dateLabel: String,
    val isEffectiveDate: Boolean,
    val sections: List<PolicyDocumentSectionUiModel>,
    val tableOfContents: List<String> = sections.map { it.title },
    val previousVersionLabel: String? = null,
    val noticeLabel: String? = null,
    val versionOptions: List<String> = emptyList(),
    val selectedVersionIndex: Int = 0,
    val isVersionPickerVisible: Boolean = false,
    val isClauseDocument: Boolean = false,
    val upcomingVersionIndex: Int? = null,
)

sealed interface PolicyDocumentEvent {
    data object BackClicked : PolicyDocumentEvent
    data class SectionSelected(val index: Int) : PolicyDocumentEvent
    data object PreviousVersionClicked : PolicyDocumentEvent
    data class VersionSelected(val index: Int) : PolicyDocumentEvent
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PolicyDocumentScreen(uiModel: PolicyDocumentUiModel, onEvent: (PolicyDocumentEvent) -> Unit, modifier: Modifier = Modifier) {
    var isTableOfContentsExpanded by remember { mutableStateOf(false) }
    var isVersionPickerVisible by remember { mutableStateOf(uiModel.isVersionPickerVisible) }
    UiAuditScreenScaffold(uiModel.title, { onEvent(PolicyDocumentEvent.BackClicked) }, modifier) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x24, bottom = EumSpacing.x40), verticalArrangement = Arrangement.spacedBy(EumSpacing.x20)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(
                    stringResource(if (uiModel.isEffectiveDate) R.string.policy_effective_date else R.string.policy_last_updated, uiModel.dateLabel),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
                if (uiModel.previousVersionLabel != null || uiModel.versionOptions.isNotEmpty()) {
                    Surface(
                        Modifier.heightIn(min = 48.dp).clickable {
                            if (uiModel.versionOptions.isNotEmpty()) {
                                isVersionPickerVisible = true
                            } else {
                                onEvent(PolicyDocumentEvent.PreviousVersionClicked)
                            }
                        },
                        shape = RoundedCornerShape(EumRadius.x8),
                        color = MaterialTheme.eumColors.surface,
                        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong),
                    ) {
                        Row(Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                            Text(uiModel.previousVersionLabel ?: uiModel.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textPrimary)
                            Icon(painterResource(R.drawable.ic_chevron_down), null, Modifier.size(EumSpacing.x16))
                        }
                    }
                }
            }
            uiModel.noticeLabel?.let { notice ->
                Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineNeutral)) {
                    Row(Modifier.fillMaxWidth().padding(EumSpacing.x12), verticalAlignment = Alignment.Top) {
                        Icon(painterResource(R.drawable.ic_info), null, Modifier.size(EumSpacing.x16), tint = MaterialTheme.eumColors.textPrimary)
                        Text(notice, Modifier.padding(start = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
                    }
                }
            }
            Surface(shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line)) {
                Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable { isTableOfContentsExpanded = !isTableOfContentsExpanded }, verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.policy_table_of_contents), Modifier.weight(1f), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        val tableOfContentsCount = uiModel.tableOfContents.size
                        Text(
                            if (uiModel.isClauseDocument) {
                                stringResource(R.string.policy_clause_count, tableOfContentsCount)
                            } else {
                                stringResource(R.string.policy_version_count, tableOfContentsCount)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                        Icon(painterResource(R.drawable.ic_chevron_down), null, Modifier.padding(start = EumSpacing.x8).size(EumSpacing.x16))
                    }
                    if (isTableOfContentsExpanded) {
                        uiModel.tableOfContents.forEachIndexed { index, title ->
                            Box(
                                Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                    .clickable { onEvent(PolicyDocumentEvent.SectionSelected(index)) },
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                            }
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x32)) {
                uiModel.sections.forEach { section ->
                    section.chapterTitle?.let { chapter ->
                        Text(chapter, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                        if (section.title.isNotEmpty()) {
                            Text(section.title, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.eumColors.textPrimary)
                        }
                        section.paragraphs.forEach { paragraph ->
                            Text(paragraph, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.eumColors.textSecondary)
                        }
                        if (section.keyValueRows.isNotEmpty()) {
                            PolicyKeyValueCard(section.keyValueRows)
                        }
                        section.steps.forEachIndexed { stepIndex, step ->
                            PolicyStepRow(number = stepIndex + 1, step = step)
                        }
                    }
                }
            }
        }
    }
    if (isVersionPickerVisible) {
        ModalBottomSheet(onDismissRequest = { isVersionPickerVisible = false }, containerColor = MaterialTheme.eumColors.surface) {
            Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20).padding(bottom = EumSpacing.x24)) {
                Text(stringResource(R.string.policy_version_title), Modifier.padding(vertical = EumSpacing.x16), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
                uiModel.versionOptions.forEachIndexed { index, version ->
                    Row(
                        Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable {
                            onEvent(PolicyDocumentEvent.VersionSelected(index))
                            if (index == 0) onEvent(PolicyDocumentEvent.PreviousVersionClicked)
                            isVersionPickerVisible = false
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = index == uiModel.selectedVersionIndex, onClick = null)
                        Text(version, Modifier.weight(1f).padding(start = EumSpacing.x8), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                        if (index == uiModel.upcomingVersionIndex) {
                            Surface(shape = RoundedCornerShape(EumRadius.x4), color = MaterialTheme.eumColors.surface, border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong)) {
                                Text(
                                    stringResource(R.string.policy_version_upcoming_badge),
                                    Modifier.padding(horizontal = EumSpacing.x6, vertical = EumSpacing.x2),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.eumColors.textSecondary,
                                )
                            }
                        }
                    }
                    if (index < uiModel.versionOptions.lastIndex) HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
        }
    }
}

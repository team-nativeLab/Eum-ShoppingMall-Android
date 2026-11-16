package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
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
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x24, bottom = EumSpacing.x40), verticalArrangement = Arrangement.spacedBy(EumSpacing.x24)) {
            Column(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.fillMaxWidth().padding(bottom = EumSpacing.x20),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                ) {
                    Text(
                        stringResource(if (uiModel.isEffectiveDate) R.string.policy_effective_date else R.string.policy_last_updated, uiModel.dateLabel),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                    if (uiModel.previousVersionLabel != null || uiModel.versionOptions.isNotEmpty()) {
                        // 바깥 48dp 박스가 터치를 맡고, 안쪽 36dp Surface가 시각 버튼을 그린다.
                        Box(
                            Modifier.heightIn(min = 48.dp).clickable(role = Role.Button) {
                                if (uiModel.versionOptions.isNotEmpty()) {
                                    isVersionPickerVisible = true
                                } else {
                                    onEvent(PolicyDocumentEvent.PreviousVersionClicked)
                                }
                            },
                            contentAlignment = Alignment.Center,
                        ) {
                            Surface(
                                shape = RoundedCornerShape(EumRadius.x8),
                                color = MaterialTheme.eumColors.surface,
                                border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong),
                            ) {
                                Row(
                                    Modifier.heightIn(min = PolicyVersionButtonHeight).padding(horizontal = EumSpacing.x12),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                                ) {
                                    Text(uiModel.previousVersionLabel ?: uiModel.dateLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                                    Icon(painterResource(R.drawable.ic_chevron_down), null, Modifier.size(EumSpacing.x16))
                                }
                            }
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.eumColors.line)
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
                        Text(stringResource(R.string.policy_table_of_contents), style = MaterialTheme.typography.labelMedium)
                        val tableOfContentsCount = uiModel.tableOfContents.size
                        Text(
                            if (uiModel.isClauseDocument) {
                                stringResource(R.string.policy_clause_count, tableOfContentsCount)
                            } else {
                                stringResource(R.string.policy_version_count, tableOfContentsCount)
                            },
                            modifier = Modifier.weight(1f).padding(start = EumSpacing.x8),
                            style = EumTypographyTokens.caption,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                        Icon(painterResource(R.drawable.ic_chevron_down), null, Modifier.padding(start = EumSpacing.x8).size(PolicyTocIconSize))
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
                uiModel.sections.groupedByChapter().forEach { chapterSections ->
                    PolicyChapterGroup(chapterTitle = chapterSections.first().chapterTitle) {
                        chapterSections.forEach { section ->
                            PolicyClauseArticle(title = section.title, paragraphs = section.paragraphs) {
                                if (section.keyValueRows.isNotEmpty()) {
                                    PolicyKeyValueCard(section.keyValueRows)
                                }
                                if (section.steps.isNotEmpty()) {
                                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                        section.steps.forEachIndexed { stepIndex, step ->
                                            PolicyStepRow(number = stepIndex + 1, step = step)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (isVersionPickerVisible) {
        ModalBottomSheet(
            onDismissRequest = { isVersionPickerVisible = false },
            shape = RoundedCornerShape(topStart = EumRadius.x12, topEnd = EumRadius.x12),
            containerColor = MaterialTheme.eumColors.surface,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    modifier = Modifier.padding(vertical = EumSpacing.x8),
                    width = PolicyVersionHandleWidth,
                    height = EumSpacing.x4,
                    shape = RoundedCornerShape(EumSpacing.x2),
                    color = MaterialTheme.eumColors.lineStrong,
                )
            },
        ) {
            Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = EumSpacing.x24)) {
                Row(
                    Modifier.fillMaxWidth().heightIn(min = PolicyVersionSheetRowHeight).padding(start = EumSpacing.x20, end = EumSpacing.x6),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.policy_version_title), Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
                    // 시각 글리프 기준 44dp 영역은 IconButton 기본 48dp 터치 영역 안에 들어간다.
                    IconButton(onClick = { isVersionPickerVisible = false }) {
                        Icon(painterResource(R.drawable.ic_x), stringResource(R.string.common_close), Modifier.size(EumSpacing.x20), tint = MaterialTheme.eumColors.textPrimary)
                    }
                }
                uiModel.versionOptions.forEachIndexed { index, version ->
                    val isSelected = index == uiModel.selectedVersionIndex
                    Row(
                        Modifier.fillMaxWidth().heightIn(min = PolicyVersionSheetRowHeight).selectable(selected = isSelected, role = Role.RadioButton) {
                            onEvent(PolicyDocumentEvent.VersionSelected(index))
                            if (index == 0) onEvent(PolicyDocumentEvent.PreviousVersionClicked)
                            isVersionPickerVisible = false
                        }.padding(horizontal = EumSpacing.x20),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                    ) {
                        PolicyVersionRadio(isSelected)
                        Text(
                            version,
                            Modifier.weight(1f),
                            style = if (isSelected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                        if (index == uiModel.upcomingVersionIndex) {
                            Surface(shape = RoundedCornerShape(EumRadius.x4), color = MaterialTheme.eumColors.surface, border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong)) {
                                Box(Modifier.heightIn(min = PolicyVersionBadgeHeight).padding(horizontal = EumSpacing.x6), contentAlignment = Alignment.Center) {
                                    Text(
                                        stringResource(R.string.policy_version_upcoming_badge),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.eumColors.textSecondary,
                                    )
                                }
                            }
                        }
                    }
                    if (index < uiModel.versionOptions.lastIndex) HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
        }
    }
}

// 장 제목이 있는 조항에서 새 그룹을 시작해, 장 단위로 조항을 묶는다.
private fun List<PolicyDocumentSectionUiModel>.groupedByChapter(): List<List<PolicyDocumentSectionUiModel>> {
    val groups = mutableListOf<MutableList<PolicyDocumentSectionUiModel>>()
    forEach { section ->
        if (section.chapterTitle != null || groups.isEmpty()) groups.add(mutableListOf())
        groups.last().add(section)
    }
    return groups
}

// 라디오 외곽 14dp 안에 선택 점을 그린다. 점 크기는 Figma 값을 확인하지 못해 외곽의 절반 근처로 둔 추정이다.
@Composable
private fun PolicyVersionRadio(isSelected: Boolean) {
    val ringColor = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineStrong
    Box(Modifier.size(PolicyVersionRadioSize).border(EumSpacing.x1, ringColor, CircleShape), contentAlignment = Alignment.Center) {
        if (isSelected) {
            Box(Modifier.size(PolicyVersionRadioDotSize).background(ringColor, CircleShape))
        }
    }
}

// Figma 값이 EumSpacing 토큰에 없어 이 파일 전용으로 둔다.
private val PolicyVersionButtonHeight = 36.dp
private val PolicyTocIconSize = 20.dp
private val PolicyVersionSheetRowHeight = 56.dp
private val PolicyVersionHandleWidth = 36.dp
private val PolicyVersionBadgeHeight = 20.dp
private val PolicyVersionRadioSize = 14.dp
private val PolicyVersionRadioDotSize = 6.dp

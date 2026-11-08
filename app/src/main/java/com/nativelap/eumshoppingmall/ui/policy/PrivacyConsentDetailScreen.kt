package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditSectionCard
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class PrivacyConsentUiModel(
    val effectiveDate: String,
    val description: String,
    val purpose: String,
    val collectedItems: String,
    val retentionPeriod: String,
)

sealed interface PrivacyConsentEvent {
    data object CloseClicked : PrivacyConsentEvent
    data object AgreeClicked : PrivacyConsentEvent
}

@Composable
fun PrivacyConsentDetailScreen(uiModel: PrivacyConsentUiModel, onEvent: (PrivacyConsentEvent) -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.privacy_consent_title),
        onBackClick = { onEvent(PrivacyConsentEvent.CloseClicked) },
        isCloseAction = true,
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.privacy_consent_agree),
        onBottomActionClick = { onEvent(PrivacyConsentEvent.AgreeClicked) },
        showBottomDivider = true,
    ) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x24, bottom = EumSpacing.x32), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
            Text(stringResource(R.string.policy_effective_date, uiModel.effectiveDate), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
            Text(uiModel.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary)
            UiAuditSectionCard(stringResource(R.string.privacy_consent_purpose), uiModel.purpose, descriptionMaxLines = Int.MAX_VALUE, contentVerticalPadding = EumSpacing.x16)
            UiAuditSectionCard(stringResource(R.string.privacy_consent_items), uiModel.collectedItems, descriptionMaxLines = Int.MAX_VALUE, contentVerticalPadding = EumSpacing.x16)
            UiAuditSectionCard(stringResource(R.string.privacy_consent_retention), uiModel.retentionPeriod, descriptionMaxLines = Int.MAX_VALUE, contentVerticalPadding = EumSpacing.x16)
            Text(stringResource(R.string.privacy_consent_decline), Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
    }
}

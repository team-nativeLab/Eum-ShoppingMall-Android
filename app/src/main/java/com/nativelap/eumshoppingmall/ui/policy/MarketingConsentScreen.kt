package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditSectionCard
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

sealed interface MarketingConsentScreenEvent {
    data object BackClicked : MarketingConsentScreenEvent
}

@Composable
fun MarketingConsentScreen(
    onEvent: (MarketingConsentScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_marketing_title),
        onBackClick = { onEvent(MarketingConsentScreenEvent.BackClicked) },
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20, bottom = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_marketing_helper),
                modifier = Modifier.padding(bottom = EumSpacing.x8),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            UiAuditSectionCard(
                title = stringResource(R.string.ui_audit_collection_purpose),
                description = stringResource(R.string.ui_audit_collection_purpose_value),
                descriptionMaxLines = Int.MAX_VALUE,
                contentVerticalPadding = EumSpacing.x12,
                titleStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            UiAuditSectionCard(
                title = stringResource(R.string.ui_audit_collection_channel),
                description = stringResource(R.string.ui_audit_collection_channel_value),
                descriptionMaxLines = Int.MAX_VALUE,
                contentVerticalPadding = EumSpacing.x12,
                titleStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            UiAuditSectionCard(
                title = stringResource(R.string.ui_audit_retention_period),
                description = stringResource(R.string.ui_audit_retention_value),
                descriptionMaxLines = Int.MAX_VALUE,
                contentVerticalPadding = EumSpacing.x12,
                titleStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            UiAuditSectionCard(
                title = stringResource(R.string.ui_audit_marketing_decline),
                description = stringResource(R.string.ui_audit_decline_description),
                descriptionMaxLines = Int.MAX_VALUE,
                contentVerticalPadding = EumSpacing.x12,
                titleStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MarketingConsentScreenPreview() {
    EumShoppingMallTheme {
        MarketingConsentScreen(onEvent = {})
    }
}

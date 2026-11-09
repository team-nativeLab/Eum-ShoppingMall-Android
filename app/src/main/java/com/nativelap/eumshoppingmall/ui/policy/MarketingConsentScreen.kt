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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditIconInfoCard
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// Figma 동의 항목 카드 높이 80dp(EumSpacing 토큰에 없음). 두 줄 설명은 이 높이를 넘을 수 있다.
private val ConsentCardMinHeight = 80.dp

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
            UiAuditIconInfoCard(
                iconRes = R.drawable.ic_megaphone,
                title = stringResource(R.string.ui_audit_collection_purpose),
                description = stringResource(R.string.ui_audit_collection_purpose_value),
                minHeight = ConsentCardMinHeight,
            )
            UiAuditIconInfoCard(
                iconRes = R.drawable.ic_message_square_text,
                title = stringResource(R.string.ui_audit_collection_channel),
                description = stringResource(R.string.ui_audit_collection_channel_value),
                minHeight = ConsentCardMinHeight,
            )
            UiAuditIconInfoCard(
                iconRes = R.drawable.ic_clock,
                title = stringResource(R.string.ui_audit_retention_period),
                description = stringResource(R.string.ui_audit_retention_value),
                minHeight = ConsentCardMinHeight,
            )
            UiAuditIconInfoCard(
                iconRes = R.drawable.ic_shield_check,
                title = stringResource(R.string.ui_audit_marketing_decline),
                description = stringResource(R.string.ui_audit_decline_description),
                minHeight = ConsentCardMinHeight,
            )
        }
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MarketingConsentScreenPreview() {
    EumShoppingMallTheme {
        MarketingConsentScreen(onEvent = {})
    }
}

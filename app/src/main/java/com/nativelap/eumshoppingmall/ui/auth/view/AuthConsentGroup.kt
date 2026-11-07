package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun AuthConsentGroup(uiModel: AuthSignupUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(EumSpacing.x16),
    ) {
        AuthConsentRow(
            label = stringResource(R.string.consent_all),
            checked = uiModel.isAllConsentChecked,
            onChecked = { onEvent(AuthScreenEvent.SignupConsentChanged("all", it)) },
            onDetails = null,
            emphasized = true,
        )
        HorizontalDivider(Modifier.padding(vertical = EumSpacing.x8), color = MaterialTheme.eumColors.line)
        AuthConsentRow(
            stringResource(R.string.consent_terms_required),
            uiModel.isTermsChecked,
            { onEvent(AuthScreenEvent.SignupConsentChanged("terms", it)) },
            { onEvent(AuthScreenEvent.SignupConsentDetailsClicked) },
        )
        AuthConsentRow(
            stringResource(R.string.consent_privacy_required),
            uiModel.isPrivacyChecked,
            { onEvent(AuthScreenEvent.SignupConsentChanged("privacy", it)) },
            { onEvent(AuthScreenEvent.SignupConsentDetailsClicked) },
        )
        AuthConsentRow(
            stringResource(R.string.consent_marketing_optional),
            uiModel.isMarketingChecked,
            { onEvent(AuthScreenEvent.SignupConsentChanged("marketing", it)) },
            { onEvent(AuthScreenEvent.SignupConsentDetailsClicked) },
        )
    }
}

@Composable
private fun AuthConsentRow(
    label: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    onDetails: (() -> Unit)?,
    emphasized: Boolean = false,
) {
    Row(
        Modifier.fillMaxWidth().clickable { onChecked(!checked) }.heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = onChecked)
        Text(label, Modifier.weight(1f), style = if (emphasized) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodySmall)
        if (onDetails != null) {
            Text(
                stringResource(R.string.auth_consent_details),
                Modifier.clickable(onClick = onDetails).heightIn(min = 48.dp).padding(horizontal = EumSpacing.x8),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}

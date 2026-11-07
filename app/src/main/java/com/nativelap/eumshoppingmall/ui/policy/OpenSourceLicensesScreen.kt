package com.nativelap.eumshoppingmall.ui.policy

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
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditFlatInfoRow
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class OpenSourceLicenseUiModel(
    val libraryName: String,
    val licenseName: String,
)

sealed interface OpenSourceLicensesScreenEvent {
    data object BackClicked : OpenSourceLicensesScreenEvent
}

@Composable
fun OpenSourceLicensesScreen(
    licenses: List<OpenSourceLicenseUiModel>,
    onEvent: (OpenSourceLicensesScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_licenses_title),
        onBackClick = { onEvent(OpenSourceLicensesScreenEvent.BackClicked) },
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20, bottom = EumSpacing.x24),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_licenses_helper),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            licenses.forEach { license ->
                UiAuditFlatInfoRow(
                    title = license.libraryName,
                    description = license.licenseName,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun OpenSourceLicensesScreenPreview() {
    EumShoppingMallTheme {
        OpenSourceLicensesScreen(
            licenses = listOf(
                stringResource(R.string.ui_audit_license_androidx),
                stringResource(R.string.ui_audit_license_kotlin),
                stringResource(R.string.ui_audit_license_coil),
                stringResource(R.string.ui_audit_license_okhttp),
            ).map { OpenSourceLicenseUiModel(it, stringResource(R.string.ui_audit_license_apache)) },
            onEvent = {},
        )
    }
}

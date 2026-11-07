package com.nativelap.eumshoppingmall.ui.support

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
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class OneToOneInquiryUiModel(
    val inquiryType: String,
    val title: String,
    val content: String,
    val attachmentLabel: String,
)

sealed interface OneToOneInquiryFormEvent {
    data object BackClicked : OneToOneInquiryFormEvent
    data object InquiryTypeClicked : OneToOneInquiryFormEvent
    data class TitleChanged(val title: String) : OneToOneInquiryFormEvent
    data class ContentChanged(val content: String) : OneToOneInquiryFormEvent
    data object AttachmentClicked : OneToOneInquiryFormEvent
    data object SubmitClicked : OneToOneInquiryFormEvent
}

@Composable
fun OneToOneInquiryFormScreen(
    uiModel: OneToOneInquiryUiModel,
    onEvent: (OneToOneInquiryFormEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_one_to_one_inquiry_title),
        onBackClick = { onEvent(OneToOneInquiryFormEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_register_inquiry),
        onBottomActionClick = { onEvent(OneToOneInquiryFormEvent.SubmitClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_one_to_one_inquiry_helper),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            InquiryFormField(
                label = stringResource(R.string.ui_audit_inquiry_type),
                value = uiModel.inquiryType,
                placeholder = stringResource(R.string.ui_audit_inquiry_type_placeholder),
                onClick = { onEvent(OneToOneInquiryFormEvent.InquiryTypeClicked) },
            )
            InquiryFormField(
                label = stringResource(R.string.ui_audit_title_label),
                value = uiModel.title,
                placeholder = stringResource(R.string.ui_audit_title_placeholder),
                onValueChange = { onEvent(OneToOneInquiryFormEvent.TitleChanged(it)) },
            )
            InquiryFormField(
                label = stringResource(R.string.ui_audit_content_label),
                value = uiModel.content,
                placeholder = stringResource(R.string.ui_audit_one_to_one_content_placeholder),
                minHeight = EumSpacing.x150,
                onValueChange = { onEvent(OneToOneInquiryFormEvent.ContentChanged(it)) },
            )
            InquiryFormField(
                label = stringResource(R.string.ui_audit_inquiry_photo),
                value = uiModel.attachmentLabel,
                placeholder = stringResource(R.string.ui_audit_inquiry_photo_placeholder),
                onClick = { onEvent(OneToOneInquiryFormEvent.AttachmentClicked) },
            )
        }
    }
}

@Composable
private fun InquiryFormField(
    label: String,
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: androidx.compose.ui.unit.Dp = EumSpacing.x52,
    onValueChange: ((String) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
        UiAuditTextField(
            label = label,
            value = value,
            placeholder = placeholder,
            onValueChange = onValueChange ?: {},
            minHeight = minHeight,
            maxLines = if (minHeight > EumSpacing.x52) 5 else 1,
            readOnly = onValueChange == null,
            onClick = onClick,
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun OneToOneInquiryFormScreenPreview() {
    EumShoppingMallTheme {
        OneToOneInquiryFormScreen(
            uiModel = OneToOneInquiryUiModel("", "", "", ""),
            onEvent = {},
        )
    }
}

package com.nativelap.eumshoppingmall.ui.productinquiry

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

sealed interface ProductInquiryScreenEvent {
    data object BackClicked : ProductInquiryScreenEvent
    data class TitleChanged(val title: String) : ProductInquiryScreenEvent
    data class ContentChanged(val content: String) : ProductInquiryScreenEvent
    data class PrivateModeChanged(val isPrivate: Boolean) : ProductInquiryScreenEvent
    data object SubmitClicked : ProductInquiryScreenEvent
}

@Composable
fun ProductInquiryScreen(
    title: String,
    content: String,
    isPrivate: Boolean,
    onEvent: (ProductInquiryScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_inquiry_title),
        onBackClick = { onEvent(ProductInquiryScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_register_inquiry),
        onBottomActionClick = { onEvent(ProductInquiryScreenEvent.SubmitClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_inquiry_helper),
                modifier = Modifier.padding(bottom = EumSpacing.x24),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            AuditInquiryField(
                label = stringResource(R.string.ui_audit_product_label),
                value = stringResource(R.string.ui_audit_inquiry_product),
            )
            AuditInquiryField(
                label = stringResource(R.string.ui_audit_title_label),
                value = title,
                placeholder = stringResource(R.string.ui_audit_title_placeholder),
                onValueChange = { onEvent(ProductInquiryScreenEvent.TitleChanged(it)) },
            )
            AuditInquiryField(
                label = stringResource(R.string.ui_audit_content_label),
                value = content,
                placeholder = stringResource(R.string.ui_audit_content_placeholder),
                minHeight = EumSpacing.x150,
                onValueChange = { onEvent(ProductInquiryScreenEvent.ContentChanged(it)) },
            )
            AuditInquiryField(
                label = stringResource(R.string.ui_audit_visibility_label),
                value = if (isPrivate) {
                    stringResource(R.string.ui_audit_private_inquiry)
                } else {
                    stringResource(R.string.ui_audit_all)
                },
                onClick = {
                    onEvent(ProductInquiryScreenEvent.PrivateModeChanged(!isPrivate))
                },
            )
        }
    }
}

// Figma 입력칸 높이 50dp. 최소 48dp 기준을 충족하며 EumSpacing 토큰에 맞춘 값이 아니다.
private val InquiryFieldMinHeight = 50.dp

@Composable
private fun AuditInquiryField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    minHeight: Dp = InquiryFieldMinHeight,
    onValueChange: ((String) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.eumColors.textPrimary,
        )
        if (onValueChange != null) {
            UiAuditTextField(
                label = label,
                value = value,
                placeholder = placeholder.orEmpty(),
                onValueChange = onValueChange,
                minHeight = minHeight,
                maxLines = if (minHeight > InquiryFieldMinHeight) 5 else 1,
            )
        } else {
            UiAuditTextField(
                label = label,
                value = value,
                placeholder = "",
                onValueChange = {},
                minHeight = minHeight,
                readOnly = true,
                onClick = onClick,
            )
        }
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductInquiryScreenPreview() {
    EumShoppingMallTheme {
        // Figma 상태: 제목·내용에 입력 값이 보이고 공개 설정은 비밀글.
        ProductInquiryScreen(
            title = stringResource(R.string.ui_audit_title_placeholder),
            content = stringResource(R.string.ui_audit_content_placeholder),
            isPrivate = true,
            onEvent = {},
        )
    }
}

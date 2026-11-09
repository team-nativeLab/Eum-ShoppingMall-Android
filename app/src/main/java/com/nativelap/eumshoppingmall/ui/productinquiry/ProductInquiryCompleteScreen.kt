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

sealed interface ProductInquiryCompleteEvent {
    data object BackClicked : ProductInquiryCompleteEvent
    data object InquiriesClicked : ProductInquiryCompleteEvent
}

@Composable
fun ProductInquiryCompleteScreen(
    onEvent: (ProductInquiryCompleteEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_inquiry_complete),
        onBackClick = { onEvent(ProductInquiryCompleteEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_view_inquiries),
        onBottomActionClick = { onEvent(ProductInquiryCompleteEvent.InquiriesClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_inquiry_complete_helper),
                modifier = Modifier.padding(bottom = EumSpacing.x24),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            CompleteStatusField(
                label = stringResource(R.string.ui_audit_receipt_status),
                value = stringResource(R.string.ui_audit_waiting_answer),
            )
            CompleteStatusField(
                label = stringResource(R.string.ui_audit_guide),
                value = stringResource(R.string.ui_audit_inquiry_guide),
            )
        }
    }
}

/** 접수 결과를 읽기 전용 입력칸 모양으로 보여 준다(Figma 879:8811). */
@Composable
private fun CompleteStatusField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.eumColors.textPrimary,
        )
        UiAuditTextField(
            label = label,
            value = value,
            placeholder = "",
            onValueChange = {},
            minHeight = CompleteFieldMinHeight,
            readOnly = true,
        )
    }
}

// Figma 입력칸 높이 50dp. 최소 48dp 기준을 충족하며 EumSpacing 토큰에 맞춘 값이 아니다.
private val CompleteFieldMinHeight: Dp = 50.dp

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductInquiryCompleteScreenPreview() {
    EumShoppingMallTheme {
        ProductInquiryCompleteScreen(onEvent = {})
    }
}

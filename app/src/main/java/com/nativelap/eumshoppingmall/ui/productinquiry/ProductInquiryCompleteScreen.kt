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
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditSectionCard
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
                .padding(contentPadding).padding(top = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_inquiry_complete_helper),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            UiAuditSectionCard(
                title = stringResource(R.string.ui_audit_receipt_status),
                description = stringResource(R.string.ui_audit_waiting_answer),
            )
            UiAuditSectionCard(
                title = stringResource(R.string.ui_audit_guide),
                description = stringResource(R.string.ui_audit_inquiry_guide),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductInquiryCompleteScreenPreview() {
    EumShoppingMallTheme {
        ProductInquiryCompleteScreen(onEvent = {})
    }
}

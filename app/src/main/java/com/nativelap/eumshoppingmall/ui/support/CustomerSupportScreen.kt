package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class CustomerSupportFaqUiModel(val id: String, val question: String, val answer: String)
data class CustomerSupportUiModel(
    val phoneNumber: String,
    val businessHours: String,
    val query: String,
    val categories: List<String>,
    val selectedCategory: String,
    val faqs: List<CustomerSupportFaqUiModel>,
    val expandedFaqIds: Set<String>,
)

sealed interface CustomerSupportEvent {
    data object BackClicked : CustomerSupportEvent
    data object CallClicked : CustomerSupportEvent
    data object InquiryClicked : CustomerSupportEvent
    data class QueryChanged(val query: String) : CustomerSupportEvent
    data class CategorySelected(val category: String) : CustomerSupportEvent
    data class FaqToggled(val id: String) : CustomerSupportEvent
}

@Composable
fun CustomerSupportScreen(uiModel: CustomerSupportUiModel, onEvent: (CustomerSupportEvent) -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(stringResource(R.string.customer_support_title), { onEvent(CustomerSupportEvent.BackClicked) }, modifier) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x24, bottom = EumSpacing.x40), verticalArrangement = Arrangement.spacedBy(EumSpacing.x24)) {
            Surface(
                shape = RoundedCornerShape(EumRadius.x12),
                color = MaterialTheme.eumColors.surface,
                border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(EumSpacing.x20),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
                ) {
                    Text(stringResource(R.string.customer_support_phone_label), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                    Text(uiModel.phoneNumber, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.eumColors.textPrimary)
                    Text(uiModel.businessHours, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                    Row(Modifier.padding(top = EumSpacing.x8), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        OutlinedButton(
                            onClick = { onEvent(CustomerSupportEvent.CallClicked) },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                            shape = RoundedCornerShape(EumRadius.x8),
                            border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineControl),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                        ) {
                            Text(stringResource(R.string.customer_support_call), style = MaterialTheme.typography.labelMedium)
                        }
                        Button(
                            onClick = { onEvent(CustomerSupportEvent.InquiryClicked) },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                            shape = RoundedCornerShape(EumRadius.x8),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.eumColors.textPrimary,
                                contentColor = MaterialTheme.eumColors.inverse,
                            ),
                        ) {
                            Text(stringResource(R.string.customer_support_inquiry), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
            Column(Modifier.padding(top = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                Text(stringResource(R.string.customer_support_faq_title), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
                OutlinedTextField(
                    value = uiModel.query,
                    onValueChange = { onEvent(CustomerSupportEvent.QueryChanged(it)) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                    placeholder = {
                        Text(stringResource(R.string.customer_support_faq_search_hint), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = MaterialTheme.eumColors.textSecondary,
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.eumColors.surfaceSecondary,
                        unfocusedContainerColor = MaterialTheme.eumColors.surfaceSecondary,
                        focusedBorderColor = MaterialTheme.eumColors.lineStrong,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                )
                if (uiModel.query.isBlank()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        uiModel.categories.forEach { category ->
                            SupportFilterChip(category, category == uiModel.selectedCategory) { onEvent(CustomerSupportEvent.CategorySelected(category)) }
                        }
                    }
                } else {
                    Text(stringResource(R.string.customer_support_result_count, uiModel.faqs.size), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                }
                if (uiModel.query.isNotBlank() && uiModel.faqs.isEmpty()) {
                    SupportEmptyState(
                        iconRes = R.drawable.ic_search_x,
                        title = stringResource(R.string.customer_support_no_result_title, uiModel.query),
                        body = stringResource(R.string.customer_support_no_result_body),
                        topPadding = EumSpacing.x24,
                    )
                } else {
                    Column {
                        uiModel.faqs.forEach { faq ->
                            CustomerSupportFaqItem(faq, expanded = faq.id in uiModel.expandedFaqIds, onClick = { onEvent(CustomerSupportEvent.FaqToggled(faq.id)) })
                        }
                    }
                }
            }
        }
    }
}

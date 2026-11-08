package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
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
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    Text(stringResource(R.string.customer_support_phone_label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
                    Text(uiModel.phoneNumber, style = MaterialTheme.typography.headlineSmall)
                    Text(uiModel.businessHours, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Button(onClick = { onEvent(CustomerSupportEvent.CallClicked) }, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.customer_support_call)) }
                        Button(onClick = { onEvent(CustomerSupportEvent.InquiryClicked) }, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.customer_support_inquiry)) }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                Text(stringResource(R.string.customer_support_faq_title), style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = uiModel.query,
                    onValueChange = { onEvent(CustomerSupportEvent.QueryChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.customer_support_faq_search_hint)) },
                    singleLine = true,
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
                    Text(stringResource(R.string.customer_support_no_search_results), Modifier.fillMaxWidth().padding(vertical = EumSpacing.x24), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary)
                } else {
                    uiModel.faqs.forEach { faq ->
                        Column(Modifier.fillMaxWidth().clickable { onEvent(CustomerSupportEvent.FaqToggled(faq.id)) }.semantics(mergeDescendants = true) { selected = faq.id in uiModel.expandedFaqIds }.padding(vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.Top) {
                                Text(stringResource(R.string.customer_support_question_mark), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                                Text(faq.question, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                            }
                            if (faq.id in uiModel.expandedFaqIds) {
                                Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.Top) {
                                    Text(stringResource(R.string.customer_support_answer_mark), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
                                    Text(faq.answer, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                                }
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.eumColors.line)
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportFilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        Modifier.heightIn(min = 48.dp).clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { selected = isSelected },
        shape = RoundedCornerShape(50),
        color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
        border = if (isSelected) null else BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong),
    ) {
        Box(Modifier.padding(horizontal = EumSpacing.x12), contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary)
        }
    }
}

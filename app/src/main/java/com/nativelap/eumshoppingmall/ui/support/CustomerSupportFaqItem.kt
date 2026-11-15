package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun CustomerSupportFaqItem(
    faq: CustomerSupportFaqUiModel,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .semantics(mergeDescendants = true) { selected = expanded }
                .padding(vertical = EumSpacing.x24),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.customer_support_question_mark),
                    Modifier.width(EumSpacing.x32),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Text(
                    faq.question,
                    Modifier.weight(1f),
                    style = EumTypographyTokens.bodyLMedium,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Icon(
                    painter = painterResource(if (expanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                    contentDescription = null,
                    modifier = Modifier.size(EumSpacing.x20),
                    tint = MaterialTheme.eumColors.textSecondary,
                )
            }
            if (expanded) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(EumRadius.x8),
                    color = MaterialTheme.eumColors.surface,
                    border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(EumSpacing.x24),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            stringResource(R.string.customer_support_answer_mark),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.eumColors.textTertiary,
                        )
                        Text(
                            faq.answer,
                            Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                }
                Spacer(Modifier.height(EumSpacing.x24))
            }
        }
        HorizontalDivider(color = MaterialTheme.eumColors.line)
    }
}

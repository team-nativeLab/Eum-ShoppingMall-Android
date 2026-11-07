package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun UiAuditSectionCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    descriptionMaxLines: Int = 3,
    contentVerticalPadding: Dp = EumSpacing.x8,
    titleStyle: TextStyle = MaterialTheme.typography.labelMedium,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x8),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Column(Modifier.padding(horizontal = EumSpacing.x12, vertical = contentVerticalPadding)) {
            Text(
                text = title,
                style = titleStyle,
                color = MaterialTheme.eumColors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = description,
                modifier = Modifier.padding(top = EumSpacing.x4),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
                maxLines = descriptionMaxLines,
                overflow = if (descriptionMaxLines == Int.MAX_VALUE) TextOverflow.Clip else TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun UiAuditExampleCard(
    titleRes: Int,
    descriptionRes: Int,
    modifier: Modifier = Modifier,
) {
    UiAuditSectionCard(
        title = stringResource(titleRes),
        description = stringResource(descriptionRes),
        modifier = modifier,
    )
}

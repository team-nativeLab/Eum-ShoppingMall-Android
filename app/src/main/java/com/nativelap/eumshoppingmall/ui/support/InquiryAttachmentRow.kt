package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun InquiryAttachmentRow(
    attachment: InquiryAttachmentUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(EumRadius.x8)
    Row(
        modifier
            .fillMaxWidth()
            .border(EumSpacing.x1, MaterialTheme.eumColors.line, shape)
            .clip(shape)
            .heightIn(min = 48.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_paperclip),
            contentDescription = null,
            modifier = Modifier.size(EumSpacing.x20),
            tint = MaterialTheme.eumColors.textSecondary,
        )
        Text(
            text = attachment.fileName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
        Icon(
            painter = painterResource(R.drawable.ic_download),
            contentDescription = stringResource(R.string.inquiry_attachment_download),
            modifier = Modifier.size(EumSpacing.x20),
            tint = MaterialTheme.eumColors.textPrimary,
        )
    }
}

package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun UiAuditTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCloseAction: Boolean = false,
) {
    val backActionDescription = stringResource(
        if (isCloseAction) R.string.ui_audit_close else R.string.ui_audit_back,
    )
    Box(modifier.fillMaxWidth().height(EumSpacing.x56)) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart)
                .padding(start = EumSpacing.x64, end = EumSpacing.x16),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.eumColors.textPrimary,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart).semantics {
                contentDescription = backActionDescription
            },
        ) {
            Icon(
                painter = painterResource(
                    if (isCloseAction) R.drawable.ic_close else R.drawable.ic_arrow_left,
                ),
                contentDescription = null,
                tint = MaterialTheme.eumColors.textPrimary,
            )
        }
    }
}

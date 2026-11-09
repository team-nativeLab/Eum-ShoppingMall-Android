package com.nativelap.eumshoppingmall.ui.quote.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun QuoteFileDownloadRow(fileName: String, downloadDescription: String, onDownload: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth().heightIn(min = 48.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.lineControl),
    ) {
        Row(Modifier.padding(start = EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(R.drawable.ic_paperclip), null, modifier = Modifier.size(16.dp), tint = MaterialTheme.eumColors.textSecondary)
            Text(
                fileName,
                Modifier.weight(1f).padding(start = EumSpacing.x8),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.eumColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(onClick = onDownload, modifier = Modifier.size(48.dp)) {
                Icon(painterResource(R.drawable.ic_download), downloadDescription, modifier = Modifier.size(18.dp), tint = MaterialTheme.eumColors.textPrimary)
            }
        }
    }
}

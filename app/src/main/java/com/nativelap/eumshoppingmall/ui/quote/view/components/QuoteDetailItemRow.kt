package com.nativelap.eumshoppingmall.ui.quote.view.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
fun QuoteDetailItemRow(name: String, quantityLabel: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().padding(vertical = EumSpacing.x6), verticalAlignment = Alignment.CenterVertically) {
        Surface(Modifier.size(44.dp), shape = RoundedCornerShape(4.dp), color = MaterialTheme.eumColors.surfaceSecondary) {
            Icon(
                painterResource(R.drawable.ic_image),
                null,
                modifier = Modifier.padding(EumSpacing.x12),
                tint = MaterialTheme.eumColors.placeholderGlyph,
            )
        }
        Text(
            name,
            Modifier.weight(1f).padding(horizontal = EumSpacing.x12),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(quantityLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
    }
}

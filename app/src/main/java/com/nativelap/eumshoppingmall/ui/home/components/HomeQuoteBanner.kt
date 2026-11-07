package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeQuoteBanner(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 108.dp)
            .clip(RoundedCornerShape(EumRadius.x12)).background(MaterialTheme.eumColors.backgroundSubtle)
            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
            .clickable(onClick = onClick).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x2),
        ) {
            Text(
                stringResource(R.string.quote_custom_order_english),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
            Text(
                stringResource(R.string.home_quote_banner_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.eumColors.textPrimary,
            )
            Text(
                stringResource(R.string.home_quote_banner_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.eumColors.surface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_right),
                null,
                tint = MaterialTheme.eumColors.textPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

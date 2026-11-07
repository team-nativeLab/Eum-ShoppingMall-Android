package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeHeroActionButton(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.inverse),
    ) {
        Row(
            modifier = Modifier.heightIn(min = 36.dp).padding(horizontal = EumSpacing.x16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            Text(
                label,
                style = EumTypographyTokens.bodySMedium,
                color = MaterialTheme.eumColors.inverse,
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = MaterialTheme.eumColors.inverse,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

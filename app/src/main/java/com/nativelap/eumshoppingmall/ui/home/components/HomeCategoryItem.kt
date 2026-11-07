package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.home.HomeCategoryUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeCategoryItem(category: HomeCategoryUiModel, onClick: (HomeCategoryUiModel) -> Unit) {
    val shape = RoundedCornerShape(EumRadius.x12)
    val fontScale = LocalDensity.current.fontScale
    val cardWidth = if (fontScale > 1f) (100.dp * fontScale).coerceAtLeast(144.dp) else 100.dp
    Column(
        modifier = Modifier.width(cardWidth).heightIn(min = 107.dp).shadow(1.dp, shape).clip(shape)
            .background(MaterialTheme.eumColors.background).border(1.dp, MaterialTheme.eumColors.line, shape)
            .clickable { onClick(category) }
            .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painterResource(category.iconResId),
                null,
                Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.eumColors.textPrimary),
            )
        }
        Text(
            category.label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.eumColors.textSecondary,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}

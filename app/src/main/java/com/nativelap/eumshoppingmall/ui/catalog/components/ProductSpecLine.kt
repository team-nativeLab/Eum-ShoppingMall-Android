package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 규격 한 줄. 값이 없어도 minLines로 한 줄을 차지해 카드 높이를 맞춘다.
@Composable
internal fun ProductSpecLine(
    specLabel: String?,
    modifier: Modifier = Modifier,
) {
    Text(
        specLabel.orEmpty(),
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.eumColors.textSecondary,
        minLines = 1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 카드 담기 버튼. IconButton이 48dp 터치 영역을 제공하고, 칩 행 높이가 48dp까지 커질 수 있다.
@Composable
internal fun QuickCartButton(
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            painterResource(R.drawable.ic_shopping_cart),
            contentDescription = contentDescription,
            tint = MaterialTheme.eumColors.textPrimary,
            modifier = Modifier.size(20.dp),
        )
    }
}

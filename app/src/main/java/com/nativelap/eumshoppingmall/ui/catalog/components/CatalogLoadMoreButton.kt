package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 보이는 상품 수만큼만 그린 목록 아래에 두는 "더 보기" 버튼. 다음 페이지를 붙인다. */
@Composable
fun CatalogLoadMoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = 48.dp),
        shape = RoundedCornerShape(EumRadius.x8),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.common_see_more),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.eumColors.textPrimary,
            )
        }
    }
}

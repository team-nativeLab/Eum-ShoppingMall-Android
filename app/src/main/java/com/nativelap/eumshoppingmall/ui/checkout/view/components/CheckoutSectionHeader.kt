package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 섹션 제목은 배경 밴드 없이 페이지 배경 위에 굵은 글자로 둔다(Figma A-08·A-09).
@Composable
fun CheckoutSectionHeader(
    titleResId: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(titleResId),
        modifier = modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.eumColors.textPrimary,
    )
}

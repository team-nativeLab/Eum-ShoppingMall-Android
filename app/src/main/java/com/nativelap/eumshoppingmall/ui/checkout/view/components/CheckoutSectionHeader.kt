package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 섹션 제목은 배경 밴드 없이 페이지 배경 위에 48dp 높이 행으로 둔다(Figma A-09).
@Composable
fun CheckoutSectionHeader(
    titleResId: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth().heightIn(min = 48.dp).padding(horizontal = EumSpacing.x20), contentAlignment = Alignment.CenterStart) {
        Text(
            text = stringResource(titleResId),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}

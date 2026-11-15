package com.nativelap.eumshoppingmall.ui.orders.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderInfoCard(
    modifier: Modifier = Modifier,
    // 기본값은 기존 동작(세로 패딩 16, 내부 간격 0)이다. Figma 신청 상세·철회 화면만 값을 지정한다.
    contentVerticalPadding: Dp = EumSpacing.x16,
    contentSpacing: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = EumSpacing.x16, vertical = contentVerticalPadding),
            verticalArrangement = Arrangement.spacedBy(contentSpacing),
            content = content,
        )
    }
}

// Figma 신청 카드 세로 패딩 14dp·내부 간격 10dp는 EumSpacing 토큰에 없어 옵트인 값으로 공개한다.
val OrderInfoCardFigmaVerticalPadding = 14.dp
val OrderInfoCardFigmaContentGap = 10.dp

package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val CheckoutSectionCardRadius = 16.dp

// Figma A-08 섹션 카드: line 테두리 r16, 헤더 32dp(좌우 16·16/24 Medium textSecondary), 본문 상 16·하 20·좌우 20.
@Composable
fun CheckoutSectionCard(
    titleResId: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardShape = RoundedCornerShape(CheckoutSectionCardRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = EumSpacing.x20)
            .clip(cardShape)
            .background(MaterialTheme.eumColors.surface)
            .border(1.dp, MaterialTheme.eumColors.line, cardShape),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().heightIn(min = 32.dp).padding(horizontal = EumSpacing.x16),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = stringResource(titleResId),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = EumSpacing.x20, top = EumSpacing.x16, end = EumSpacing.x20, bottom = EumSpacing.x20),
            content = content,
        )
    }
}

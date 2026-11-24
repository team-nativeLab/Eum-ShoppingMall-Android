package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 인증 폼 하단 CTA 바. surface 배경, 상단 1dp line, 좌우 20·상하 12 패딩. */
@Composable
internal fun AuthBottomBar(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).navigationBarsPadding().imePadding(),
    ) {
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.eumColors.line)
        Column(
            Modifier.fillMaxWidth().padding(horizontal = EumSpacing.pageGutter, vertical = EumSpacing.x12),
            content = content,
        )
    }
}

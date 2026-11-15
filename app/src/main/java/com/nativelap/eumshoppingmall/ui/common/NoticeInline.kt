package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** Figma NoticeInline: line 테두리 r12 박스 안에 정보 아이콘과 안내 문구를 둔다. */
@Composable
fun NoticeInline(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Row(
            // 가로 14는 Figma 값이며 EumSpacing에 토큰이 없어 화면 안 상수로 둔다.
            modifier = Modifier.padding(horizontal = NOTICE_HORIZONTAL_PADDING, vertical = EumSpacing.x12),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(R.drawable.ic_info),
                contentDescription = null,
                tint = MaterialTheme.eumColors.textSecondary,
                modifier = Modifier.size(16.dp),
            )
            Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
    }
}

private val NOTICE_HORIZONTAL_PADDING = 14.dp

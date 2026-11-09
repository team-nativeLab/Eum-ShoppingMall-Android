package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 입력칸 아래 오류 문구. Figma 오류 상태처럼 경고 아이콘과 danger 색을 함께 쓴다. */
@Composable
internal fun AccountErrorText(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        Icon(
            painterResource(R.drawable.ic_circle_alert),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.eumColors.danger,
        )
        Text(message, color = MaterialTheme.eumColors.danger, style = MaterialTheme.typography.labelSmall)
    }
}

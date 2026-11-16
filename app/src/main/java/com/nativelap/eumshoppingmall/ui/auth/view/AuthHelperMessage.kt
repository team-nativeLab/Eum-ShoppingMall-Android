package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

internal enum class AuthHelperTone { Info, Error, Success, Confirmed }

/**
 * 입력 필드 아래 헬퍼 문구. 오류는 circle-alert, 성공은 circle-check 14dp 아이콘을 앞에 둔다.
 * Success는 success 색, Confirmed는 Figma 비밀번호 일치 문구처럼 textSecondary 색이다.
 */
@Composable
internal fun AuthHelperMessage(text: String, tone: AuthHelperTone, modifier: Modifier = Modifier) {
    val toneColor = when (tone) {
        AuthHelperTone.Info -> MaterialTheme.eumColors.textTertiary
        AuthHelperTone.Error -> MaterialTheme.eumColors.danger
        AuthHelperTone.Success -> MaterialTheme.eumColors.success
        AuthHelperTone.Confirmed -> MaterialTheme.eumColors.textSecondary
    }
    val iconRes = when (tone) {
        AuthHelperTone.Info -> null
        AuthHelperTone.Error -> R.drawable.ic_circle_alert
        AuthHelperTone.Success, AuthHelperTone.Confirmed -> R.drawable.ic_circle_check
    }
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4), verticalAlignment = Alignment.Top) {
        if (iconRes != null) {
            // 12/16 문구의 첫 줄 가운데에 14dp 아이콘을 맞춘다.
            Icon(
                painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.padding(top = EumSpacing.x1).size(14.dp),
                tint = toneColor,
            )
        }
        Text(
            text,
            // 오류 문구는 나타날 때 스크린 리더가 바로 읽도록 한다.
            modifier = Modifier.weight(1f).semantics {
                if (tone == AuthHelperTone.Error) liveRegion = LiveRegionMode.Polite
            },
            style = MaterialTheme.typography.labelSmall,
            color = toneColor,
        )
    }
}

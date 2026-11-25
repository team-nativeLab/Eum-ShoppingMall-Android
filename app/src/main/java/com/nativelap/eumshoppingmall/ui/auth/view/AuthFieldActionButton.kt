package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius

// 입력칸(AuthOutlinedTextField 최소 높이 48dp)과 같은 높이로 맞춰 입력칸 옆에서 짧아 보이지 않게 한다.
private val AuthFieldActionButtonHeight = 48.dp
private val AuthFieldActionButtonMinWidth = 73.dp
private val AuthFieldActionButtonPadding = PaddingValues(horizontal = 16.dp)

/**
 * 전화 인증 버튼(받기·재전송·확인)의 최소 폭. 평소엔 이 폭이고 가장 긴 "인증번호 받기"(14sp, 좌우 여백 16dp)가
 * 글꼴 배율에 따라 커지면 버튼도 함께 늘어난다.
 */
internal val AuthPhoneActionButtonWidth: Dp = 120.dp

/**
 * 입력칸 오른쪽 보조 버튼(중복 확인·재전송·확인)의 공통 모양. 모서리는 입력칸과 같은 EumRadius.x8이고,
 * 테두리 두께는 입력칸 기본 두께([AuthFieldBorderThickness])를 따른다. [minWidth]가 없으면 최소 폭(73dp)을 쓴다.
 */
@Composable
internal fun AuthFieldActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minWidth: Dp? = null,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = AuthFieldActionButtonHeight).defaultMinSize(minWidth = minWidth ?: AuthFieldActionButtonMinWidth),
        shape = RoundedCornerShape(EumRadius.x8),
        // 색은 Material 기본(사용 가능·비활성)을 그대로 쓰고, 두께만 입력칸 기본 두께로 맞춘다.
        border = BorderStroke(AuthFieldBorderThickness, ButtonDefaults.outlinedButtonBorder(enabled = enabled).brush),
        contentPadding = AuthFieldActionButtonPadding,
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 1)
    }
}

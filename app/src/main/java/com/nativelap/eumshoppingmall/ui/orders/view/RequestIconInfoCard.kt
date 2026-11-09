package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditIconInfoCard
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 신청 화면의 아이콘 정보 카드(Figma 879:45279, 927:45901). 구현은 공용 카드를 쓰고 여백·테두리·설명 간격만 맞춘다. */
@Composable
internal fun RequestIconInfoCard(iconResId: Int, title: String, body: String) {
    UiAuditIconInfoCard(
        iconRes = iconResId,
        title = title,
        description = body,
        contentPadding = 16.dp,
        borderColor = MaterialTheme.eumColors.line,
        descriptionTopPadding = EumSpacing.x4,
    )
}

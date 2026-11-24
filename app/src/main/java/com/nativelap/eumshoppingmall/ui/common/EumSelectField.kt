package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * 드롭다운형 선택 칸의 공통 모양. 텍스트가 남는 폭을 쓰고 화살표는 오른쪽 끝에 두며(좌우 16dp 여백),
 * 세로는 56dp 이상이라 터치 영역 48dp를 넘는다. 메뉴(DropdownMenu)는 호출부가 이 칸을 감싸는 Box 안에 둔다.
 * [isPlaceholder]이면 안내 문구 색으로 그린다.
 */
@Composable
fun EumSelectField(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaceholder: Boolean = false,
    enabled: Boolean = true,
    // 접근성 이름. 값이 바뀌어도 칸의 이름이 유지되도록 따로 받는다.
    label: String? = null,
    // 메뉴·목록이 펼쳐져 있으면 true. 접근성 상태 설명과 화살표 방향에 쓴다.
    isExpanded: Boolean = false,
) {
    val shape = RoundedCornerShape(EumRadius.x8)
    val expandedDescription = stringResource(R.string.select_field_expanded)
    val collapsedDescription = stringResource(R.string.select_field_collapsed)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(MaterialTheme.eumColors.surface, shape)
            .border(1.dp, MaterialTheme.eumColors.lineNeutral, shape)
            .clip(shape)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics {
                if (label != null) contentDescription = label
                stateDescription = if (isExpanded) expandedDescription else collapsedDescription
            }
            .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = when {
                !enabled -> MaterialTheme.eumColors.textDisabled
                isPlaceholder -> MaterialTheme.eumColors.textTertiary
                else -> MaterialTheme.eumColors.textPrimary
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.width(EumSpacing.x12))
        Icon(
            painterResource(R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = MaterialTheme.eumColors.textSecondary,
            // 펼쳐져 있으면 화살표를 위로 돌린다.
            modifier = Modifier.size(20.dp).rotate(if (isExpanded) 180f else 0f),
        )
    }
}

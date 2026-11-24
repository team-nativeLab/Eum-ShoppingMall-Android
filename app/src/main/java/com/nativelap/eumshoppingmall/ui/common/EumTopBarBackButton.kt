package com.nativelap.eumshoppingmall.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * 상단 바의 뒤로가기(또는 닫기) 버튼 공용 컴포넌트.
 *
 * 터치 영역은 48dp, 아이콘은 24dp다. 호출하는 상단 바 Row에 시작 인셋
 * `EumSpacing.topBarInset`(4dp)을 주면 아이콘 왼쪽 끝이 페이지 거터(16dp)에 맞는다.
 */
@Composable
fun EumTopBarBackButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int = R.drawable.ic_arrow_left,
) {
    IconButton(onClick = onClick, modifier = modifier.size(48.dp)) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = contentDescription,
            tint = MaterialTheme.eumColors.textPrimary,
            modifier = Modifier.size(24.dp),
        )
    }
}

package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * Figma 동의 체크박스(기본 16dp·모서리 4·테두리 1.5, 로그인 유지는 20dp). 표시 전용이며 토글과 접근성 역할은 감싸는 행의 toggleable이 맡는다.
 */
@Composable
internal fun AuthCheckbox(checked: Boolean, modifier: Modifier = Modifier, size: Dp = 16.dp) {
    val checkedColor = MaterialTheme.eumColors.checkboxChecked
    val boxShape = RoundedCornerShape(EumRadius.x4)
    Box(
        modifier
            .size(size)
            .background(if (checked) checkedColor else Color.Transparent, boxShape)
            .border(1.5.dp, if (checked) checkedColor else MaterialTheme.colorScheme.onSurfaceVariant, boxShape),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                painterResource(R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(size * 0.75f),
                tint = MaterialTheme.eumColors.inverse,
            )
        }
    }
}

package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.data.local.MAX_CART_LINE_QUANTITY
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 수량 상한. 고정 폭 숫자 영역에 세 자리까지 들어가도록 정한 값이다. */
const val EUM_MAX_QUANTITY = MAX_CART_LINE_QUANTITY

// 보이는 스테퍼 높이. 터치 영역은 버튼 상자(48dp 높이)가 맡고 보이는 테두리만 줄여 그린다.
private val StepperVisualHeight = 36.dp
private val StepperTouchHeight = 48.dp
private val StepperButtonWidth = 36.dp

// 숫자 영역 고정 폭. 세 자리까지 한 줄로 들어가며 숫자에 따라 컴포넌트 폭이 변하지 않는다.
private val StepperCountWidth = 36.dp

/**
 * 공통 수량 스테퍼. 숫자 영역은 고정 폭이라 1·9·10·31·45·100처럼 자릿수가 바뀌어도 크기가 달라지지 않는다.
 * 보이는 높이는 36dp, 버튼 터치 영역은 48dp 높이를 유지한다. 큰 글꼴에서는 숫자 폭을 글꼴 배율(최대 1.5배)만큼 넓힌다.
 */
@Composable
fun EumQuantityStepper(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
    canDecrease: Boolean = true,
    canIncrease: Boolean = true,
    enabled: Boolean = true,
) {
    val countWidth = StepperCountWidth * LocalDensity.current.fontScale.coerceIn(1f, 1.5f)
    val shape = RoundedCornerShape(EumRadius.x8)
    val borderColor = if (enabled) MaterialTheme.eumColors.lineControl else MaterialTheme.eumColors.line
    val containerColor = if (enabled) MaterialTheme.eumColors.surface else MaterialTheme.eumColors.surfaceSecondary
    Box(modifier.heightIn(min = StepperTouchHeight), contentAlignment = Alignment.Center) {
        // 보이는 테두리 상자는 터치 높이 안에서 가운데 36dp만 그린다.
        Box(
            Modifier
                .matchParentSize()
                .padding(vertical = (StepperTouchHeight - StepperVisualHeight) / 2)
                .background(containerColor, shape)
                .border(BorderStroke(1.dp, borderColor), shape),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            StepperButton(
                iconResId = R.drawable.ic_minus,
                label = stringResource(R.string.catalog_quantity_decrease),
                isEnabled = enabled && canDecrease,
                onClick = onDecrease,
            )
            AnimatedCountText(
                count = quantity,
                modifier = Modifier.width(countWidth),
                style = MaterialTheme.typography.labelMedium,
                color = if (enabled) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textDisabled,
                textAlign = TextAlign.Center,
            )
            StepperButton(
                iconResId = R.drawable.ic_plus,
                label = stringResource(R.string.catalog_quantity_increase),
                isEnabled = enabled && canIncrease,
                onClick = onIncrease,
            )
        }
    }
}

@Composable
private fun StepperButton(iconResId: Int, label: String, isEnabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(width = StepperButtonWidth, height = StepperTouchHeight)
            .clickable(enabled = isEnabled, role = Role.Button, onClick = onClick)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painterResource(iconResId),
            contentDescription = null,
            tint = if (isEnabled) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textDisabled,
            modifier = Modifier.size(16.dp),
        )
    }
}

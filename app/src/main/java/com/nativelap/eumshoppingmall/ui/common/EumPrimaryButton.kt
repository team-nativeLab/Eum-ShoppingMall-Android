package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 공용 CTA 버튼 최소 높이. Figma CTA 56dp. 글꼴 확대에 잘리지 않도록 최소 높이로 쓴다. */
internal val EumCtaButtonHeight = 56.dp

/** 다이얼로그 버튼 최소 높이. Figma 다이얼로그 값을 확인하지 못해 기존 시각(52dp)을 유지한다. */
internal val EumDialogButtonHeight = 52.dp

/** 주 CTA 버튼. 배경 primary(#111), 글자 onPrimary, r8, 최소 높이 56dp(EumCtaButtonHeight). 누르면 scale 0.98이 더해진다. */
@Composable
fun EumPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    /** 누를 수는 있지만 비활성처럼 보여야 할 때 true. 탭하면 onClick이 호출되어 안내를 띄울 수 있다. 기본값은 enabled의 반대다. */
    inactiveLook: Boolean = !enabled,
    minHeight: Dp = EumCtaButtonHeight,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .pressScale(enabled = enabled && !inactiveLook)
            .heightIn(min = minHeight),
        shape = RoundedCornerShape(EumRadius.x8),
        colors = if (inactiveLook) {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.eumColors.surfaceSecondary,
                contentColor = MaterialTheme.eumColors.textTertiary,
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.eumColors.primary,
                contentColor = MaterialTheme.eumColors.inverse,
                disabledContainerColor = MaterialTheme.eumColors.surfaceSecondary,
                disabledContentColor = MaterialTheme.eumColors.textTertiary,
            )
        },
        content = content,
    )
}

@Preview(showBackground = true, widthDp = 390, name = "공용 주 버튼")
@Composable
private fun EumPrimaryButtonPreview() {
    EumShoppingMallTheme {
        EumPrimaryButton(onClick = {}, modifier = Modifier) { Text("바로 구매") }
    }
}

@Preview(showBackground = true, widthDp = 390, fontScale = 2f, name = "공용 주 버튼 · 글꼴 2배")
@Composable
private fun EumPrimaryButtonLargeFontPreview() {
    EumShoppingMallTheme {
        EumPrimaryButton(onClick = {}, modifier = Modifier) { Text("견적 문의 접수") }
    }
}

@Preview(showBackground = true, widthDp = 390, name = "공용 주 버튼 · 비활성")
@Composable
private fun EumPrimaryButtonDisabledPreview() {
    EumShoppingMallTheme {
        EumPrimaryButton(onClick = {}, modifier = Modifier, enabled = false) { Text("접수하기") }
    }
}

package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 보조 CTA 버튼. 흰 배경에 textPrimary 글자와 테두리, r8, 최소 높이 52dp. 누르면 scale 0.98이 더해진다. */
@Composable
fun EumOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    /** 누를 수는 있지만 비활성처럼 보여야 할 때 true. 기본값은 enabled의 반대다. */
    inactiveLook: Boolean = !enabled,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .pressScale(enabled = enabled && !inactiveLook)
            .heightIn(min = EumCtaButtonHeight),
        shape = RoundedCornerShape(EumRadius.x8),
        border = BorderStroke(
            width = 1.dp,
            color = if (inactiveLook) MaterialTheme.eumColors.line else MaterialTheme.eumColors.lineControl,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (inactiveLook) MaterialTheme.eumColors.textTertiary else MaterialTheme.eumColors.textPrimary,
            disabledContentColor = MaterialTheme.eumColors.textTertiary,
        ),
        content = content,
    )
}

@Preview(showBackground = true, widthDp = 390, name = "공용 보조 버튼")
@Composable
private fun EumOutlinedButtonPreview() {
    EumShoppingMallTheme {
        EumOutlinedButton(onClick = {}, modifier = Modifier) { Text("장바구니 담기") }
    }
}

@Preview(showBackground = true, widthDp = 390, fontScale = 2f, name = "공용 보조 버튼 · 글꼴 2배")
@Composable
private fun EumOutlinedButtonLargeFontPreview() {
    EumShoppingMallTheme {
        EumOutlinedButton(onClick = {}, modifier = Modifier) { Text("취소") }
    }
}

@Preview(showBackground = true, widthDp = 390, name = "공용 보조 버튼 · 비활성")
@Composable
private fun EumOutlinedButtonDisabledPreview() {
    EumShoppingMallTheme {
        EumOutlinedButton(onClick = {}, modifier = Modifier, enabled = false) { Text("담기") }
    }
}

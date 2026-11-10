package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * 앱 공용 확인 다이얼로그. 모서리 r12, 확인은 주 버튼, 취소는 보조 버튼을 쓴다.
 * [isDestructive]이면 확인 버튼을 보조 버튼 형태로 바꾸고 글자를 danger 색으로 쓴다.
 * 주 버튼 배경(검정)에 danger 글자를 올리면 대비가 부족하므로 이 형태를 택했다(가정, 보고서 참고).
 */
@Composable
fun EumAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    dismissLabel: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    text: (@Composable () -> Unit)? = null,
    isDestructive: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        shape = RoundedCornerShape(EumRadius.x12),
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        text = text,
        confirmButton = {
            if (isDestructive) {
                EumOutlinedButton(onClick = onConfirm) {
                    Text(confirmLabel, color = MaterialTheme.eumColors.danger)
                }
            } else {
                EumPrimaryButton(onClick = onConfirm) { Text(confirmLabel) }
            }
        },
        dismissButton = {
            EumOutlinedButton(onClick = onDismiss) { Text(dismissLabel) }
        },
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 640, name = "공용 확인 다이얼로그")
@Composable
private fun EumAlertDialogPreview() {
    EumShoppingMallTheme {
        EumAlertDialog(
            onDismissRequest = {},
            title = "로그아웃할까요?",
            confirmLabel = "로그아웃",
            onConfirm = {},
            dismissLabel = "취소",
            onDismiss = {},
            text = { Text("다시 로그인하면 장바구니 정보가 유지됩니다.") },
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 640, fontScale = 2f, name = "공용 확인 다이얼로그 · 파괴 동작 · 글꼴 2배")
@Composable
private fun EumAlertDialogDestructiveLargeFontPreview() {
    EumShoppingMallTheme {
        EumAlertDialog(
            onDismissRequest = {},
            title = "회원 탈퇴할까요?",
            confirmLabel = "탈퇴하기",
            onConfirm = {},
            dismissLabel = "취소",
            onDismiss = {},
            isDestructive = true,
        )
    }
}

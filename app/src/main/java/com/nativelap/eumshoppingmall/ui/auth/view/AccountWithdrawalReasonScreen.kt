package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalReasonUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountWithdrawalReasonScreen(uiModel: AccountWithdrawalReasonUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    androidx.compose.foundation.layout.Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { AuthTopBar(stringResource(R.string.account_leave_title), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
            bottomBar = {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                    AuthPrimaryAction(stringResource(R.string.auth_withdrawal_submit), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.WithdrawalSubmitted) }
                }
            },
            containerColor = MaterialTheme.eumColors.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
            ) {
                Text(stringResource(R.string.account_leave_reason_prompt), style = MaterialTheme.typography.headlineSmall)
                Text(stringResource(R.string.account_leave_reason_label), style = MaterialTheme.typography.titleMedium)
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    (0..5).chunked(2).forEachIndexed { rowIndex, rowOptions ->
                        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            rowOptions.forEachIndexed { columnIndex, _ ->
                                val index = rowIndex * 2 + columnIndex
                                val selected = uiModel.selectedReasonIndex == index
                                androidx.compose.material3.OutlinedButton(
                                    onClick = { onEvent(AuthScreenEvent.WithdrawalReasonSelected(index)) },
                                    modifier = Modifier.weight(1f).heightIn(min = 56.dp),
                                    border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineStrong),
                                ) {
                                    Text(if (index == 5) stringResource(R.string.auth_other) else stringResource(R.string.account_leave_reason_label))
                                }
                            }
                        }
                    }
                }
                if (uiModel.selectedReasonIndex == 5) {
                    OutlinedTextField(
                        value = uiModel.otherReason,
                        onValueChange = { onEvent(AuthScreenEvent.WithdrawalReasonChanged(it.take(200))) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 96.dp),
                        placeholder = { Text(stringResource(R.string.auth_withdrawal_reason_detail)) },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = MaterialTheme.shapes.medium,
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.auth_withdrawal_reason_detail), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                        Text(stringResource(R.string.auth_withdrawal_reason_count, uiModel.otherReason.length), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                    }
                }
                AuthPasswordField(
                    stringResource(R.string.account_password_confirmation),
                    uiModel.password,
                    uiModel.isPasswordVisible,
                    { onEvent(AuthScreenEvent.WithdrawalPasswordChanged(it)) },
                    { onEvent(AuthScreenEvent.WithdrawalPasswordVisibilityChanged) },
                    helperText = stringResource(R.string.auth_withdrawal_password_help),
                    errorText = if (uiModel.isPasswordInvalid) stringResource(R.string.auth_current_password_invalid) else null,
                )
            }
        }
        if (uiModel.isConfirmationDialogVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(AuthScreenEvent.WithdrawalConfirmDismissed) },
                title = { Text(stringResource(R.string.account_leave_confirmation), color = MaterialTheme.eumColors.danger) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.auth_withdrawal_dialog_description))
                        Text(stringResource(R.string.account_leave_coupon_expire, uiModel.couponCount), style = MaterialTheme.typography.bodySmall)
                        Text(stringResource(R.string.account_leave_points_expire, uiModel.points), style = MaterialTheme.typography.bodySmall)
                    }
                },
                confirmButton = { TextButton(onClick = { onEvent(AuthScreenEvent.WithdrawalConfirmed) }) { Text(stringResource(R.string.auth_withdrawal_submit), color = MaterialTheme.eumColors.danger) } },
                dismissButton = { TextButton(onClick = { onEvent(AuthScreenEvent.WithdrawalConfirmDismissed) }) { Text(stringResource(R.string.common_cancel)) } },
                shape = RoundedCornerShape(12.dp),
            )
        }
    }
}

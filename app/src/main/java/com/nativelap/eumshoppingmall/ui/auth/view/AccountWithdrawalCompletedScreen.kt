package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalCompletedUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountWithdrawalCompletedScreen(uiModel: AccountWithdrawalCompletedUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_leave_title), onBack = { onEvent(AuthScreenEvent.CloseClicked) }, isClose = true) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                AuthPrimaryAction(stringResource(R.string.common_home), true) { onEvent(AuthScreenEvent.WithdrawalCompletedHomeClicked) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.eumColors.textPrimary) {
                Icon(painterResource(R.drawable.ic_check), contentDescription = null, modifier = Modifier.padding(EumSpacing.x20), tint = MaterialTheme.eumColors.background)
            }
            Text(stringResource(R.string.account_leave_complete), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Text(stringResource(R.string.auth_withdrawal_complete_thanks), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary, textAlign = TextAlign.Center)
            Surface(shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surfaceSecondary) {
                Column(Modifier.fillMaxWidth().padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    WithdrawalDateRow(stringResource(R.string.account_leave_at), uiModel.completedAt)
                    WithdrawalDateRow(stringResource(R.string.account_rejoin_available_at), stringResource(R.string.account_rejoin_after, uiModel.rejoinAvailableAt))
                }
            }
            Surface(
                shape = RoundedCornerShape(EumRadius.x12),
                color = MaterialTheme.eumColors.background,
                border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
            ) {
                Row(Modifier.fillMaxWidth().padding(EumSpacing.x16), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_circle_alert), contentDescription = null, tint = MaterialTheme.eumColors.textPrimary)
                    Text(stringResource(R.string.auth_withdrawal_record_retention, uiModel.retentionPeriod), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
        }
    }
}

@Composable
private fun WithdrawalDateRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}

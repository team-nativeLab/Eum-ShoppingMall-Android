package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalGuideUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountWithdrawalGuideScreen(uiModel: AccountWithdrawalGuideUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    val canContinue = uiModel.isAgreementChecked && uiModel.activeOrderCount == 0
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_leave_title), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                AuthPrimaryAction(stringResource(R.string.common_next), canContinue) { onEvent(AuthScreenEvent.WithdrawalNextClicked) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Text(stringResource(R.string.account_leave_intro), style = MaterialTheme.typography.headlineSmall)
            Text(stringResource(R.string.auth_withdrawal_member_summary, uiModel.memberName), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(EumRadius.x12),
                border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                color = MaterialTheme.eumColors.background,
            ) {
                Column(Modifier.padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        WithdrawalBenefit(stringResource(R.string.auth_withdrawal_coupons), stringResource(R.string.account_leave_coupon_expire, uiModel.couponCount))
                        WithdrawalBenefit(stringResource(R.string.auth_withdrawal_points), stringResource(R.string.account_leave_points_expire, uiModel.points))
                    }
                    Text(stringResource(R.string.auth_withdrawal_expiration), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.danger)
                }
            }
            if (uiModel.activeOrderCount > 0) {
                Surface(shape = RoundedCornerShape(EumRadius.x8), color = MaterialTheme.eumColors.dangerTint) {
                    Text(
                        stringResource(R.string.auth_withdrawal_unavailable, uiModel.activeOrderCount),
                        Modifier.fillMaxWidth().padding(EumSpacing.x12),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.danger,
                    )
                }
                Text(stringResource(R.string.account_orders_link), Modifier.clickable { onEvent(AuthScreenEvent.WithdrawalOrderHistoryClicked) }.align(Alignment.End).heightIn(min = 48.dp), style = MaterialTheme.typography.labelMedium)
            }
            Text(stringResource(R.string.account_leave_precautions), style = MaterialTheme.typography.titleMedium)
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                listOf(
                    R.string.auth_withdrawal_data_notice,
                    R.string.auth_withdrawal_benefit_notice,
                    R.string.auth_withdrawal_retention_notice,
                    R.string.auth_withdrawal_rejoin_notice,
                    R.string.auth_withdrawal_inquiries_notice,
                ).forEach { resource ->
                    Text("•  ${stringResource(resource)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
            Row(
                Modifier.fillMaxWidth().clickable { onEvent(AuthScreenEvent.WithdrawalAgreementChanged(!uiModel.isAgreementChecked)) }.heightIn(min = 48.dp).padding(EumSpacing.x8),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(uiModel.isAgreementChecked, onCheckedChange = { onEvent(AuthScreenEvent.WithdrawalAgreementChanged(it)) })
                Text(stringResource(R.string.account_leave_agreement), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun WithdrawalBenefit(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
        Text(value.substringAfter(' '), style = MaterialTheme.typography.headlineSmall)
    }
}

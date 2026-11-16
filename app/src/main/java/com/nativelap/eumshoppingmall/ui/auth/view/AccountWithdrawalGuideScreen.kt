package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalGuideUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountWithdrawalGuideScreen(uiModel: AccountWithdrawalGuideUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    val canContinue = uiModel.isAgreementChecked && uiModel.activeOrderCount == 0
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_leave_title), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            AuthBottomBar {
                AuthPrimaryAction(stringResource(R.string.common_next), canContinue) { onEvent(AuthScreenEvent.WithdrawalNextClicked) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                .padding(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x16, bottom = EumSpacing.x32),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x32),
        ) {
            // intro 제목 스타일은 Figma 노드값을 확인하지 못해 기존 headlineSmall을 유지한다.
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(stringResource(R.string.account_leave_intro), style = MaterialTheme.typography.headlineSmall)
                Text(stringResource(R.string.auth_withdrawal_member_summary, uiModel.memberName), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                AuthCard(contentPadding = EumSpacing.x20, itemSpacing = EumSpacing.x16) {
                    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
                        WithdrawalBenefit(R.drawable.ic_ticket, stringResource(R.string.auth_withdrawal_coupons), stringResource(R.string.auth_withdrawal_coupon_count, uiModel.couponCount), Modifier.weight(1f))
                        Box(Modifier.width(1.dp).fillMaxHeight().background(MaterialTheme.eumColors.line))
                        WithdrawalBenefit(R.drawable.ic_coins, stringResource(R.string.auth_withdrawal_points), uiModel.points, Modifier.weight(1f))
                    }
                    Text(
                        stringResource(R.string.auth_withdrawal_expiration),
                        Modifier.fillMaxWidth(),
                        style = EumTypographyTokens.captionMedium,
                        color = MaterialTheme.eumColors.danger,
                        textAlign = TextAlign.Center,
                    )
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
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                Text(stringResource(R.string.account_leave_precautions), style = MaterialTheme.typography.titleMedium)
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    listOf(
                        R.string.auth_withdrawal_data_notice,
                        R.string.auth_withdrawal_benefit_notice,
                        R.string.auth_withdrawal_retention_notice,
                        R.string.auth_withdrawal_rejoin_notice,
                        R.string.auth_withdrawal_inquiries_notice,
                    ).forEach { noticeResource ->
                        WithdrawalNoticeItem(stringResource(noticeResource))
                    }
                }
            }
            AuthCard {
                Row(
                    Modifier.fillMaxWidth().heightIn(min = 48.dp).toggleable(
                        value = uiModel.isAgreementChecked,
                        role = Role.Checkbox,
                        onValueChange = { onEvent(AuthScreenEvent.WithdrawalAgreementChanged(it)) },
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    AuthCheckbox(uiModel.isAgreementChecked)
                    Text(stringResource(R.string.account_leave_agreement), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun WithdrawalBenefit(iconRes: Int, label: String, benefitValue: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
        Icon(painterResource(iconRes), contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.eumColors.textSecondary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
        Text(benefitValue, style = EumTypographyTokens.priceL)
    }
}

@Composable
private fun WithdrawalNoticeItem(notice: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        // 4×12 슬롯을 14/20 본문 첫 줄 가운데에 맞추고, 점은 슬롯 가운데에 둔다. 점 모양·색은 Figma 값 미확인이라 본문색 원형으로 둔다.
        Box(Modifier.padding(top = EumSpacing.x4).size(width = 4.dp, height = 12.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.size(4.dp).background(MaterialTheme.eumColors.textSecondary, CircleShape))
        }
        Text(notice, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
    }
}

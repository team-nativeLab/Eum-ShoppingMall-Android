package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeCompanyInfoField
import com.nativelap.eumshoppingmall.ui.home.HomeCompanyInfoRow
import com.nativelap.eumshoppingmall.ui.home.HomeFooterLink
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 정책·지원 링크는 항상 그리고, 사업자 정보 행은 값이 있을 때만 그린다.
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeCompanyInfoFooter(
    rows: List<HomeCompanyInfoRow>,
    onLinkClick: (HomeFooterLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.eumColors.surfaceSecondary)
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            HomeFooterLink.entries.forEach { link ->
                // 터치 영역은 48dp 높이를 유지하고, 글자는 세로 가운데에 둔다.
                Box(
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .clickable(role = Role.Button) { onLinkClick(link) }
                        .padding(horizontal = EumSpacing.x4),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        stringResource(link.labelResId()),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                }
            }
        }
        if (rows.isNotEmpty()) {
            Text(
                stringResource(R.string.home_company_info_title),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.eumColors.textPrimary,
            )
            rows.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        stringResource(row.field.labelResId()),
                        modifier = Modifier.weight(0.4f),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                    Text(
                        row.value,
                        modifier = Modifier.weight(0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textSecondary,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

private fun HomeFooterLink.labelResId(): Int = when (this) {
    HomeFooterLink.Terms -> R.string.home_footer_terms
    HomeFooterLink.PrivacyPolicy -> R.string.home_footer_privacy
    HomeFooterLink.UsageGuide -> R.string.home_footer_guide
    HomeFooterLink.Events -> R.string.home_footer_event
    HomeFooterLink.CustomerSupport -> R.string.home_footer_customer
}

private fun HomeCompanyInfoField.labelResId(): Int = when (this) {
    HomeCompanyInfoField.CompanyName -> R.string.business_name
    HomeCompanyInfoField.RepresentativeName -> R.string.business_representative
    HomeCompanyInfoField.BusinessRegistrationNumber -> R.string.business_registration_number
    HomeCompanyInfoField.MailOrderSalesNumber -> R.string.home_company_mail_order_sales
    HomeCompanyInfoField.Address -> R.string.company_address
    HomeCompanyInfoField.Email -> R.string.company_email
    HomeCompanyInfoField.CustomerServiceHours -> R.string.home_company_customer_service
}

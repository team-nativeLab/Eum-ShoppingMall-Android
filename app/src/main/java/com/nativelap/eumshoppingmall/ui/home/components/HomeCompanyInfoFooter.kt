package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeCompanyInfoField
import com.nativelap.eumshoppingmall.ui.home.HomeCompanyInfoRow
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 호출 측은 rows가 비어 있으면 이 푸터를 그리지 않는다.
@Composable
fun HomeCompanyInfoFooter(
    rows: List<HomeCompanyInfoRow>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.eumColors.surfaceSecondary)
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
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

private fun HomeCompanyInfoField.labelResId(): Int = when (this) {
    HomeCompanyInfoField.CompanyName -> R.string.business_name
    HomeCompanyInfoField.RepresentativeName -> R.string.business_representative
    HomeCompanyInfoField.BusinessRegistrationNumber -> R.string.business_registration_number
    HomeCompanyInfoField.MailOrderSalesNumber -> R.string.home_company_mail_order_sales
    HomeCompanyInfoField.Address -> R.string.company_address
    HomeCompanyInfoField.Email -> R.string.company_email
    HomeCompanyInfoField.CustomerServiceHours -> R.string.home_company_customer_service
}

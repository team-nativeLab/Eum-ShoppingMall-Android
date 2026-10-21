package com.nativelap.eumshoppingmall.ui.benefits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.common.EmptyCollectionState
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CouponScreen(
    uiModel: CouponScreenUiModel,
    onEvent: (CouponScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val couponCodeLabel = stringResource(R.string.coupon_code_hint)
    Column(modifier.fillMaxSize()) {
        CatalogTopBar(
            title = stringResource(R.string.coupon_title),
            onBackClick = { onEvent(CouponScreenEvent.BackClicked) },
            modifier = Modifier.statusBarsPadding(),
        )
        if (uiModel.availableCoupons.isEmpty() && uiModel.usedExpiredCoupons.isEmpty()) {
            EmptyCollectionState(
                title = stringResource(R.string.coupon_empty_title),
                description = stringResource(R.string.coupon_empty_description),
                actionLabel = stringResource(R.string.coupon_empty_action),
                onAction = { onEvent(CouponScreenEvent.BrowseProductsClicked) },
            )
        } else {
            Column(Modifier.fillMaxSize().navigationBarsPadding().imePadding()) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        value = uiModel.registeredCode,
                        onValueChange = { onEvent(CouponScreenEvent.CodeChanged(it)) },
                        modifier = Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.eumColors.surface).border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(8.dp))
                            .padding(horizontal = EumSpacing.x12).semantics { contentDescription = couponCodeLabel },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.eumColors.textPrimary),
                        decorationBox = { inner ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (uiModel.registeredCode.isEmpty()) Text(stringResource(R.string.coupon_code_hint), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                                inner()
                            }
                        },
                    )
                    Spacer(Modifier.width(EumSpacing.x8))
                    Button(
                        onClick = { onEvent(CouponScreenEvent.RegisterClicked) },
                        modifier = Modifier.height(48.dp),
                        contentPadding = PaddingValues(horizontal = EumSpacing.x16),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.eumColors.textPrimary),
                    ) { Text(stringResource(R.string.coupon_register), color = MaterialTheme.eumColors.inverse) }
                }
                Surface(
                    onClick = { onEvent(CouponScreenEvent.PointsClicked) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.eumColors.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.eumColors.line),
                ) {
                    Row(Modifier.padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.points_balance), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        Text(uiModel.pointsBalanceLabel, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)
                        .border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(8.dp)).padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    CouponTab.entries.forEach { tab ->
                        val selected = uiModel.selectedTab == tab
                        Surface(
                            onClick = { onEvent(CouponScreenEvent.TabSelected(tab)) },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    stringResource(if (tab == CouponTab.Available) R.string.coupon_available_tab else R.string.coupon_used_expired_tab, uiModel.countFor(tab)),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x4),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    if (uiModel.selectedCoupons.isEmpty()) {
                        item {
                            Column(
                                Modifier.fillMaxWidth().padding(vertical = EumSpacing.x40),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    stringResource(if (uiModel.selectedTab == CouponTab.Available) R.string.coupon_empty_title else R.string.coupon_used_expired_empty_title),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.eumColors.textPrimary,
                                )
                                Text(
                                    stringResource(if (uiModel.selectedTab == CouponTab.Available) R.string.coupon_empty_description else R.string.coupon_used_expired_empty_description),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.eumColors.textSecondary,
                                )
                            }
                        }
                    } else {
                        items(uiModel.selectedCoupons, key = { it.id }) { coupon ->
                            CouponCard(coupon) { onEvent(CouponScreenEvent.CouponClicked(coupon.id)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CouponCard(coupon: CouponUiModel, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.eumColors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Row(Modifier.height(132.dp)) {
            Column(Modifier.weight(1f).padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(coupon.discountLabel, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
                    coupon.badgeLabel?.let { Text(it, Modifier.clip(RoundedCornerShape(4.dp)).background(MaterialTheme.eumColors.textPrimary).padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.inverse) }
                }
                Text(coupon.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(coupon.conditionLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(coupon.expiryLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
            }
            Box(Modifier.width(1.dp).fillMaxSize().background(MaterialTheme.eumColors.lineStrong))
            Column(
                Modifier.width(75.dp).fillMaxSize().padding(horizontal = EumSpacing.x4),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                androidx.compose.material3.Icon(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.ic_ticket),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textPrimary,
                    modifier = Modifier.size(20.dp),
                )
                Text(coupon.applicableLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary, maxLines = 2)
            }
        }
    }
}

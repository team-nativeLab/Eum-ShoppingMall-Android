package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.DeliveryProgressStrip
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderProductThumbnail
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderStatusBadge
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrdersTopBar
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryTrackingUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun DeliveryTrackingScreen(
    uiModel: DeliveryTrackingUiModel,
    onEvent: (DeliveryTrackingScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            OrdersTopBar(
                title = stringResource(R.string.orders_track_delivery),
                onBackClick = { onEvent(DeliveryTrackingScreenEvent.BackClicked) },
                modifier = Modifier.statusBarsPadding().background(MaterialTheme.eumColors.background),
            )
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.eumColors.surfaceSecondary,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.eumColors.line),
                ) {
                    Column(
                        Modifier.padding(EumSpacing.x20),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        OrderStatusBadge(uiModel.status)
                        if (uiModel.expectedArrivalLabel.isNotBlank()) {
                            Text(stringResource(R.string.orders_expected_arrival, uiModel.expectedArrivalLabel), style = MaterialTheme.typography.titleMedium)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val hasCarrier = uiModel.carrierName.isNotBlank()
                            val hasTrackingNumber = uiModel.trackingNumber.isNotBlank()
                            val trackingSummary = when {
                                hasCarrier && hasTrackingNumber -> stringResource(R.string.orders_carrier_tracking, uiModel.carrierName, uiModel.trackingNumber)
                                hasCarrier -> uiModel.carrierName
                                hasTrackingNumber -> stringResource(R.string.orders_tracking_number_only, uiModel.trackingNumber)
                                else -> stringResource(R.string.orders_delivery_info_unavailable)
                            }
                            Text(
                                trackingSummary,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.eumColors.textSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (hasTrackingNumber) {
                                TextButton(
                                    onClick = { onEvent(DeliveryTrackingScreenEvent.TrackingNumberCopyClicked) },
                                    modifier = Modifier.heightIn(min = 48.dp),
                                ) {
                                    Text(stringResource(R.string.common_copy))
                                }
                            }
                        }
                    }
                }
            }
            item { DeliveryProgressStrip(uiModel.progressStage) }
            item {
                Text(stringResource(R.string.orders_shipping_details), style = MaterialTheme.typography.titleSmall)
            }
            if (uiModel.timeline.isEmpty()) {
                item {
                    Text(
                        stringResource(R.string.orders_delivery_history_empty),
                        modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x24),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            } else {
                itemsIndexed(uiModel.timeline) { index, timelineItem ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                modifier = Modifier.padding(top = EumSpacing.x4),
                                shape = androidx.compose.foundation.shape.CircleShape,
                                color = if (timelineItem.isCurrent) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary,
                            ) {
                                androidx.compose.foundation.layout.Box(Modifier.padding(EumSpacing.x4))
                            }
                            if (index < uiModel.timeline.lastIndex) {
                                androidx.compose.foundation.layout.Spacer(
                                    Modifier.padding(top = EumSpacing.x4).width(1.dp).height(28.dp)
                                        .background(MaterialTheme.eumColors.line),
                                )
                            }
                        }
                        Column(Modifier.weight(1f).padding(bottom = EumSpacing.x16)) {
                            Text(
                                timelineItem.title,
                                style = if (timelineItem.isCurrent) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.eumColors.textPrimary,
                            )
                            if (timelineItem.locationAndTimeLabel.isNotBlank()) {
                                Text(timelineItem.locationAndTimeLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                            }
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface, MaterialTheme.shapes.medium).padding(EumSpacing.x16),
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OrderProductThumbnail(null, stringResource(R.string.orders_products_title), size = 52.dp)
                    Text(uiModel.productsSummaryLabel, style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface, MaterialTheme.shapes.medium).padding(EumSpacing.x12),
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    androidx.compose.material3.Icon(
                        painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        tint = MaterialTheme.eumColors.textSecondary,
                    )
                    Text(uiModel.noticeLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
        }
    }
}

package com.nativelap.eumshoppingmall.ui.orders.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.DeliveryProgressStage
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun DeliveryProgressStrip(currentStage: DeliveryProgressStage, modifier: Modifier = Modifier) {
    val stageLabels = listOf(
        R.string.orders_status_paid,
        R.string.orders_status_product_preparing,
        R.string.orders_status_shipping_started,
        R.string.orders_status_in_transit,
        R.string.orders_status_delivered,
    )
    val currentIndex = currentStage.ordinal
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        stageLabels.forEachIndexed { index, labelResId ->
            val stageLabel = stringResource(labelResId)
            val isPast = index < currentIndex
            val isCurrent = index == currentIndex
            Column(
                modifier = Modifier.weight(1f).semantics {
                    contentDescription = stageLabel
                    selected = isCurrent
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (index > 0) {
                        Spacer(
                            Modifier.weight(1f).height(1.dp)
                                .background(if (isPast || isCurrent) MaterialTheme.eumColors.lineStrong else MaterialTheme.eumColors.line),
                        )
                    }
                    Box(
                        Modifier.size(20.dp)
                            .background(
                                color = if (isPast || isCurrent) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
                                shape = CircleShape,
                            )
                            .then(
                                if (isPast || isCurrent) Modifier else Modifier.background(MaterialTheme.eumColors.lineStrong, CircleShape),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isPast) {
                            androidx.compose.material3.Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = MaterialTheme.eumColors.inverse,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                    }
                    if (index < stageLabels.lastIndex) {
                        Spacer(
                            Modifier.weight(1f).height(1.dp)
                                .background(if (isPast) MaterialTheme.eumColors.lineStrong else MaterialTheme.eumColors.line),
                        )
                    }
                }
                Text(
                    stringResource(labelResId),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isCurrent) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textSecondary,
                    maxLines = 2,
                )
            }
        }
    }
}

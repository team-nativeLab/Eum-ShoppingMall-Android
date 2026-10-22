package com.nativelap.eumshoppingmall.ui.benefits

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.common.EmptyCollectionState
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun PointsScreen(
    uiModel: PointsScreenUiModel,
    onEvent: (PointsScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        CatalogTopBar(
            title = stringResource(R.string.points_title),
            onBackClick = { onEvent(PointsScreenEvent.BackClicked) },
            modifier = Modifier.statusBarsPadding(),
        )
        if (uiModel.history.isEmpty()) {
            EmptyCollectionState(
                title = stringResource(R.string.points_empty_title),
                description = stringResource(R.string.points_empty_description),
                actionLabel = stringResource(R.string.points_empty_action),
                onAction = { onEvent(PointsScreenEvent.ShopClicked) },
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
            ) {
                item {
                    Column(
                        Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(12.dp))
                            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(12.dp)).padding(EumSpacing.x20),
                    ) {
                        Text(stringResource(R.string.points_available_amount), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                        Text(uiModel.availableBalanceLabel, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.eumColors.textPrimary)
                        Spacer(Modifier.height(EumSpacing.x8))
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.points_expiring_amount), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                            Text(uiModel.expiringBalanceLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        PointsFilter.entries.forEach { filter ->
                            val selected = filter == uiModel.selectedFilter
                            Surface(
                                onClick = { onEvent(PointsScreenEvent.FilterSelected(filter)) },
                                shape = RoundedCornerShape(50),
                                color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
                                border = if (selected) null else BorderStroke(1.dp, MaterialTheme.eumColors.lineControl),
                            ) {
                                Text(
                                    stringResource(filter.labelRes),
                                    modifier = Modifier.heightIn(min = 48.dp).padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x12),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
                                )
                            }
                        }
                    }
                }
                items(uiModel.history, key = { it.id }) { entry ->
                    val dividerColor = MaterialTheme.eumColors.line
                    Column(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x4)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(entry.title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
                            Text(entry.amountLabel, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
                        }
                        Row(Modifier.fillMaxWidth().padding(top = EumSpacing.x4), verticalAlignment = Alignment.CenterVertically) {
                            Text("${entry.dateLabel} · ${entry.orderNumberLabel}", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                            entry.expiryLabel?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary) }
                        }
                        Spacer(Modifier.height(EumSpacing.x12))
                        androidx.compose.foundation.Canvas(Modifier.fillMaxWidth().height(1.dp)) {
                            drawLine(dividerColor, start = androidx.compose.ui.geometry.Offset.Zero, end = androidx.compose.ui.geometry.Offset(size.width, 0f), strokeWidth = size.height)
                        }
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(12.dp))
                            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(12.dp)).padding(EumSpacing.x12),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("ⓘ", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textSecondary)
                        Text(uiModel.usageNotice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                    }
                }
            }
        }
    }
}

private val PointsFilter.labelRes: Int
    get() = when (this) {
        PointsFilter.All -> R.string.points_filter_all
        PointsFilter.Earned -> R.string.points_filter_earned
        PointsFilter.Used -> R.string.points_filter_used
        PointsFilter.Expired -> R.string.points_filter_expired
    }

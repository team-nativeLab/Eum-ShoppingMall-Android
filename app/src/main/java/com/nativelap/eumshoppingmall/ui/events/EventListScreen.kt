package com.nativelap.eumshoppingmall.ui.events

import androidx.annotation.DrawableRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditIconInfoCard
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.support.SupportFilterChip
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import java.time.LocalDate

/** 이벤트 목록 한 줄. 상태는 기간에서 [eventPeriodStatus]로 판정하므로 저장하지 않는다. */
data class EventUiModel(
    val id: String,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    @param:DrawableRes val iconRes: Int,
)

sealed interface EventListScreenEvent {
    data object BackClicked : EventListScreenEvent
    data class EventClicked(val eventId: String) : EventListScreenEvent
    data class TabSelected(val tab: EventListTab) : EventListScreenEvent
}

@Composable
fun EventListScreen(
    events: List<EventUiModel>,
    today: LocalDate,
    selectedTab: EventListTab,
    onEvent: (EventListScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visibleEvents = events.filterFor(selectedTab, today)
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_events_title),
        onBackClick = { onEvent(EventListScreenEvent.BackClicked) },
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20, bottom = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                EventListTab.entries.forEach { tab ->
                    SupportFilterChip(
                        label = stringResource(tab.labelRes()),
                        isSelected = tab == selectedTab,
                        onClick = { onEvent(EventListScreenEvent.TabSelected(tab)) },
                    )
                }
            }
            if (visibleEvents.isEmpty()) {
                Text(
                    text = stringResource(selectedTab.emptyRes()),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x32),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.eumColors.textSecondary,
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = EumSpacing.x20),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    visibleEvents.forEach { event ->
                        val status = eventPeriodStatus(event.startDate, event.endDate, today)
                        UiAuditIconInfoCard(
                            iconRes = event.iconRes,
                            title = stringResource(status.labelRes()),
                            description = stringResource(
                                R.string.ui_audit_event_summary_format,
                                event.title,
                                stringResource(
                                    R.string.ui_audit_event_period_format,
                                    formatEventDate(event.startDate),
                                    formatEventDate(event.endDate),
                                ),
                            ),
                            minHeight = EventCardMinHeight,
                            onClick = { onEvent(EventListScreenEvent.EventClicked(event.id)) },
                        )
                    }
                }
            }
        }
    }
}

private fun EventListTab.labelRes(): Int = when (this) {
    EventListTab.All -> R.string.ui_audit_event_tab_all
    EventListTab.Ongoing -> R.string.ui_audit_event_ongoing
    EventListTab.Upcoming -> R.string.ui_audit_event_upcoming
    EventListTab.Ended -> R.string.ui_audit_event_ended
}

private fun EventListTab.emptyRes(): Int = when (this) {
    EventListTab.All -> R.string.ui_audit_event_empty_all
    EventListTab.Ongoing -> R.string.ui_audit_event_empty_ongoing
    EventListTab.Upcoming -> R.string.ui_audit_event_empty_upcoming
    EventListTab.Ended -> R.string.ui_audit_event_empty_ended
}

private fun EventPeriodStatus.labelRes(): Int = when (this) {
    EventPeriodStatus.Upcoming -> R.string.ui_audit_event_upcoming
    EventPeriodStatus.Ongoing -> R.string.ui_audit_event_ongoing
    EventPeriodStatus.Ended -> R.string.ui_audit_event_ended
}

// Figma 이벤트 카드 높이 80dp(EumSpacing 토큰에 없음).
private val EventCardMinHeight = 80.dp

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun EventListScreenPreview() {
    EumShoppingMallTheme {
        EventListScreen(
            events = listOf(
                EventUiModel("event-1", "현장 안전용품 특별 기획전", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), R.drawable.ic_calendar),
                EventUiModel("event-2", "겨울맞이 안전장비 할인", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31), R.drawable.ic_calendar),
                EventUiModel("event-3", "신규 회원 웰컴 혜택", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30), R.drawable.ic_clock),
            ),
            today = LocalDate.of(2026, 11, 23),
            selectedTab = EventListTab.All,
            onEvent = {},
        )
    }
}

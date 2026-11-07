package com.nativelap.eumshoppingmall.ui.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditSectionCard
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class EventUiModel(
    val id: String,
    val status: String,
    val summary: String,
)

sealed interface EventListScreenEvent {
    data object BackClicked : EventListScreenEvent
    data class EventClicked(val eventId: String) : EventListScreenEvent
}

@Composable
fun EventListScreen(
    events: List<EventUiModel>,
    onEvent: (EventListScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            Text(
                text = stringResource(R.string.ui_audit_events_helper),
                modifier = Modifier.padding(bottom = EumSpacing.x20),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            events.forEach { event ->
                UiAuditSectionCard(
                    title = event.status,
                    description = event.summary,
                    modifier = Modifier.clickable {
                        onEvent(EventListScreenEvent.EventClicked(event.id))
                    }.semantics { role = Role.Button },
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun EventListScreenPreview() {
    EumShoppingMallTheme {
        EventListScreen(
            events = listOf(
                EventUiModel("event-1", stringResource(R.string.ui_audit_event_ongoing), stringResource(R.string.ui_audit_event_one)),
                EventUiModel("event-2", stringResource(R.string.ui_audit_event_ongoing), stringResource(R.string.ui_audit_event_two)),
                EventUiModel("event-3", stringResource(R.string.ui_audit_event_ended), stringResource(R.string.ui_audit_event_three)),
            ),
            onEvent = {},
        )
    }
}

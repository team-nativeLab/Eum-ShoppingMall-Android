package com.nativelap.eumshoppingmall.ui.events

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditIconInfoCard
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// Figma 이벤트 상세 카드 높이 80dp(EumSpacing 토큰에 없음).
private val EventDetailCardMinHeight = 80.dp

data class EventDetailUiModel(
    val title: String,
    val period: String,
    val benefit: String,
    val participation: String,
    val notice: String,
)

sealed interface EventDetailScreenEvent {
    data object BackClicked : EventDetailScreenEvent
    data object BrowseProductsClicked : EventDetailScreenEvent
}

@Composable
fun EventDetailScreen(
    uiModel: EventDetailUiModel,
    onEvent: (EventDetailScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = uiModel.title,
        onBackClick = { onEvent(EventDetailScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_browse_products),
        onBottomActionClick = { onEvent(EventDetailScreenEvent.BrowseProductsClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Text(
                text = uiModel.period,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            UiAuditIconInfoCard(
                iconRes = R.drawable.ic_gift,
                title = stringResource(R.string.ui_audit_event_benefit_title),
                description = uiModel.benefit,
                modifier = Modifier.padding(top = EumSpacing.x24 + EumSpacing.x4),
                minHeight = EventDetailCardMinHeight,
            )
            UiAuditIconInfoCard(
                iconRes = R.drawable.ic_circle_check,
                title = stringResource(R.string.ui_audit_event_how_title),
                description = uiModel.participation,
                minHeight = EventDetailCardMinHeight,
            )
            UiAuditIconInfoCard(
                iconRes = R.drawable.ic_info,
                title = stringResource(R.string.ui_audit_event_notice_title),
                description = uiModel.notice,
                minHeight = EventDetailCardMinHeight,
            )
        }
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun EventDetailScreenPreview() {
    EumShoppingMallTheme {
        EventDetailScreen(
            uiModel = EventDetailUiModel(
                title = stringResource(R.string.ui_audit_event_detail_title),
                period = stringResource(R.string.ui_audit_event_period),
                benefit = stringResource(R.string.ui_audit_event_benefit),
                participation = stringResource(R.string.ui_audit_event_how),
                notice = stringResource(R.string.ui_audit_event_notice),
            ),
            onEvent = {},
        )
    }
}

package com.nativelap.eumshoppingmall.ui.support

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.support.INQUIRY_FIELD_GAP
import com.nativelap.eumshoppingmall.ui.support.INQUIRY_LABEL_FONT_SIZE
import com.nativelap.eumshoppingmall.ui.support.INQUIRY_LABEL_GAP
import com.nativelap.eumshoppingmall.ui.support.INQUIRY_LABEL_LINE_HEIGHT
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 1:1 문의 유형. 웹 문의 유형 4종과 같은 순서다. */
enum class OneToOneInquiryType(@StringRes val labelRes: Int) {
    OrderPayment(R.string.inquiry_type_order_payment),
    Delivery(R.string.inquiry_type_delivery),
    Product(R.string.inquiry_type_product),
    Other(R.string.inquiry_type_other),
}

/** 주문 선택 목록의 한 줄. 주문 내역 샘플에서 만든다. */
data class OneToOneInquiryOrderOption(val orderId: String, val label: String)

/**
 * 1:1 문의 작성 화면 상태. 입력값은 저장하지 않으며, 제출은 저장 없이 준비 중 안내만 낸다(서버 연동 전).
 * 선택 목록 펼침 상태는 Route가 보관하고, Screen은 이 플래그만 보고 목록을 그린다.
 * 답변 알림 토글은 저장되지 않는 기능이라 비활성으로 두며, [isReplyNotificationOn]은 고정 표시 값이다.
 */
data class OneToOneInquiryUiModel(
    val selectedType: OneToOneInquiryType?,
    val orderOptions: List<OneToOneInquiryOrderOption>,
    val selectedOrderId: String?,
    val title: String,
    val content: String,
    val isReplyNotificationOn: Boolean,
    val isTypeListExpanded: Boolean = false,
    val isOrderListExpanded: Boolean = false,
)

sealed interface OneToOneInquiryFormEvent {
    data object BackClicked : OneToOneInquiryFormEvent
    data object TypeFieldClicked : OneToOneInquiryFormEvent
    data class TypeSelected(val type: OneToOneInquiryType) : OneToOneInquiryFormEvent
    data object OrderFieldClicked : OneToOneInquiryFormEvent
    data class OrderSelected(val orderId: String?) : OneToOneInquiryFormEvent
    data class TitleChanged(val title: String) : OneToOneInquiryFormEvent
    data class ContentChanged(val content: String) : OneToOneInquiryFormEvent
    data object AttachmentClicked : OneToOneInquiryFormEvent
    data object SubmitClicked : OneToOneInquiryFormEvent
}

/** 문의 내용의 최대 글자 수. 입력이 넘으면 이 길이에서 자른다. */
const val ONE_TO_ONE_INQUIRY_CONTENT_MAX_LENGTH = 1_000

/** 문의 내용을 최대 글자 수 안으로 자른다. */
fun limitOneToOneInquiryContent(content: String): String = content.take(ONE_TO_ONE_INQUIRY_CONTENT_MAX_LENGTH)

/** 제출의 결과. 서버 연동 전이라 저장 없이 준비 중 안내만 낸다. 화면은 닫지 않는다. */
enum class OneToOneInquirySubmitOutcome { UnavailableNotice, }

/** 제출 동작을 정한다. 저장하지 않으므로 어떤 입력이든 준비 중 안내로 끝난다. */
fun resolveOneToOneInquirySubmit(): OneToOneInquirySubmitOutcome = OneToOneInquirySubmitOutcome.UnavailableNotice

@Composable
fun OneToOneInquiryFormScreen(
    uiModel: OneToOneInquiryUiModel,
    onEvent: (OneToOneInquiryFormEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_one_to_one_inquiry_title),
        onBackClick = { onEvent(OneToOneInquiryFormEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_register_inquiry),
        onBottomActionClick = { onEvent(OneToOneInquiryFormEvent.SubmitClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(INQUIRY_FIELD_GAP),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_one_to_one_inquiry_helper),
                modifier = Modifier.padding(bottom = EumSpacing.x24),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
            InquiryChoiceField(
                label = stringResource(R.string.ui_audit_inquiry_type),
                placeholder = stringResource(R.string.ui_audit_inquiry_type_placeholder),
                selectedLabel = uiModel.selectedType?.let { stringResource(it.labelRes) },
                choices = OneToOneInquiryType.entries.map { type -> InquiryChoice(type.name, stringResource(type.labelRes)) },
                isExpanded = uiModel.isTypeListExpanded,
                onFieldClick = { onEvent(OneToOneInquiryFormEvent.TypeFieldClicked) },
                onSelect = { id ->
                    OneToOneInquiryType.entries.firstOrNull { it.name == id }?.let { onEvent(OneToOneInquiryFormEvent.TypeSelected(it)) }
                },
            )
            InquiryChoiceField(
                label = stringResource(R.string.inquiry_order_label),
                placeholder = stringResource(R.string.inquiry_order_placeholder),
                selectedLabel = uiModel.orderOptions.firstOrNull { it.orderId == uiModel.selectedOrderId }?.label,
                choices = listOf(InquiryChoice(NO_ORDER_CHOICE_ID, stringResource(R.string.inquiry_order_none))) +
                    uiModel.orderOptions.map { option -> InquiryChoice(option.orderId, option.label) },
                isExpanded = uiModel.isOrderListExpanded,
                onFieldClick = { onEvent(OneToOneInquiryFormEvent.OrderFieldClicked) },
                onSelect = { id -> onEvent(OneToOneInquiryFormEvent.OrderSelected(id.takeUnless { it == NO_ORDER_CHOICE_ID })) },
            )
            InquiryFormField(
                label = stringResource(R.string.ui_audit_title_label),
                value = uiModel.title,
                placeholder = stringResource(R.string.ui_audit_title_placeholder),
                onValueChange = { onEvent(OneToOneInquiryFormEvent.TitleChanged(it)) },
            )
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                InquiryFormField(
                    label = stringResource(R.string.ui_audit_content_label),
                    value = uiModel.content,
                    placeholder = stringResource(R.string.ui_audit_one_to_one_content_placeholder),
                    minHeight = EumSpacing.x150,
                    onValueChange = { onEvent(OneToOneInquiryFormEvent.ContentChanged(it)) },
                )
                Text(
                    text = stringResource(R.string.inquiry_content_count, uiModel.content.length),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                    textAlign = TextAlign.End,
                )
            }
            InquiryFormField(
                label = stringResource(R.string.ui_audit_inquiry_photo),
                value = "",
                placeholder = stringResource(R.string.inquiry_attachment_unavailable),
                onClick = { onEvent(OneToOneInquiryFormEvent.AttachmentClicked) },
            )
            InquiryReplyNotificationRow(isOn = uiModel.isReplyNotificationOn)
        }
    }
}

// 주문 선택의 "선택 안 함" 항목 id. 실제 주문 번호와 겹치지 않는 값이다.
private const val NO_ORDER_CHOICE_ID = "__no_order__"

private data class InquiryChoice(val id: String, val label: String)

// 선택 목록은 필드를 누르면 아래로 펼친다. 펼침 상태는 Route가 보관하며, 항목을 고르면 Route가 닫는다.
@Composable
private fun InquiryChoiceField(
    label: String,
    placeholder: String,
    selectedLabel: String?,
    choices: List<InquiryChoice>,
    isExpanded: Boolean,
    onFieldClick: () -> Unit,
    onSelect: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(INQUIRY_LABEL_GAP)) {
        InquiryFormField(
            label = label,
            value = selectedLabel.orEmpty(),
            placeholder = placeholder,
            onClick = onFieldClick,
        )
        if (isExpanded) {
            Column(Modifier.fillMaxWidth()) {
                choices.forEach { choice ->
                    Row(
                        Modifier.fillMaxWidth().heightIn(min = InquiryChoiceMinHeight)
                            .clickable(role = Role.Button) { onSelect(choice.id) }
                            .padding(horizontal = EumSpacing.x12),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(choice.label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun InquiryReplyNotificationRow(isOn: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
        Row(
            Modifier.fillMaxWidth().heightIn(min = InquiryChoiceMinHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.inquiry_reply_notification),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.eumColors.textPrimary,
            )
            // 저장되지 않는 기능이라 토글은 비활성으로 두고, 상태와 이유를 아래 문구로 알린다.
            Switch(checked = isOn, onCheckedChange = null, enabled = false)
        }
        Text(
            stringResource(R.string.inquiry_reply_notification_unsaved_notice),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textTertiary,
        )
    }
}

@Composable
private fun InquiryFormField(
    label: String,
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: Dp = InquiryFieldMinHeight,
    onValueChange: ((String) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(INQUIRY_LABEL_GAP)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = INQUIRY_LABEL_FONT_SIZE,
                lineHeight = INQUIRY_LABEL_LINE_HEIGHT,
                fontWeight = FontWeight.SemiBold,
            ),
            color = MaterialTheme.eumColors.textPrimary,
        )
        UiAuditTextField(
            label = label,
            value = value,
            placeholder = placeholder,
            onValueChange = onValueChange ?: {},
            minHeight = minHeight,
            maxLines = if (minHeight > InquiryFieldMinHeight) 5 else 1,
            readOnly = onValueChange == null,
            onClick = onClick,
        )
    }
}

// Figma 입력칸 높이 50dp. 최소 48dp 기준을 충족하며 EumSpacing 토큰에 맞춘 값이 아니다.
private val InquiryFieldMinHeight = 50.dp

// 선택 목록 한 줄과 알림 토글의 최소 터치 높이(48dp 기준).
private val InquiryChoiceMinHeight = 48.dp

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun OneToOneInquiryFormScreenPreview() {
    EumShoppingMallTheme {
        OneToOneInquiryFormScreen(
            // Figma 상태: 제목·내용은 입력된 값처럼 진한 글자, 유형·사진은 빈 칸 안내.
            uiModel = OneToOneInquiryUiModel(
                selectedType = null,
                orderOptions = emptyList(),
                selectedOrderId = null,
                title = stringResource(R.string.ui_audit_title_placeholder),
                content = stringResource(R.string.ui_audit_one_to_one_content_placeholder),
                isReplyNotificationOn = false,
            ),
            onEvent = {},
        )
    }
}

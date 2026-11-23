package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R

/** 이용안내(A-38) 화면. 문서 구조는 [rememberUsageGuideDocument]가 정한다. */
@Composable
fun UsageGuideRoute(onBackClick: () -> Unit) {
    PolicyDocumentScreen(
        uiModel = rememberUsageGuideDocument(),
        onEvent = { event ->
            if (event == PolicyDocumentEvent.BackClicked) onBackClick()
        },
    )
}

/**
 * 웹 /guide 구성을 정보 구조로 옮긴다: 상품 선택·주문 및 결제·배송 확인 3단계와 6개 항목 목차.
 * 문구는 웹 텍스트를 그대로 쓰며, 날짜 라벨과 버전 선택은 두지 않는다.
 */
@Composable
fun rememberUsageGuideDocument(): PolicyDocumentUiModel {
    val steps = listOf(
        PolicyDocumentStepUiModel(
            stringResource(R.string.usage_guide_step_select_title),
            stringResource(R.string.usage_guide_step_select_description),
        ),
        PolicyDocumentStepUiModel(
            stringResource(R.string.usage_guide_step_order_title),
            stringResource(R.string.usage_guide_step_order_description),
        ),
        PolicyDocumentStepUiModel(
            stringResource(R.string.usage_guide_step_delivery_title),
            stringResource(R.string.usage_guide_step_delivery_description),
        ),
    )
    val itemSections = listOf(
        PolicyDocumentSectionUiModel(stringResource(R.string.usage_guide_signup_title), listOf(stringResource(R.string.usage_guide_signup_body))),
        PolicyDocumentSectionUiModel(stringResource(R.string.usage_guide_order_title), listOf(stringResource(R.string.usage_guide_order_body))),
        PolicyDocumentSectionUiModel(stringResource(R.string.usage_guide_payment_title), listOf(stringResource(R.string.usage_guide_payment_body))),
        PolicyDocumentSectionUiModel(stringResource(R.string.usage_guide_delivery_title), listOf(stringResource(R.string.usage_guide_delivery_body))),
        PolicyDocumentSectionUiModel(stringResource(R.string.usage_guide_return_title), listOf(stringResource(R.string.usage_guide_return_body))),
        PolicyDocumentSectionUiModel(stringResource(R.string.usage_guide_support_title), listOf(stringResource(R.string.usage_guide_support_body))),
    )
    return PolicyDocumentUiModel(
        title = stringResource(R.string.usage_guide_title),
        dateLabel = null,
        isEffectiveDate = false,
        sections = listOf(
            PolicyDocumentSectionUiModel(
                title = stringResource(R.string.usage_guide_steps_title),
                paragraphs = emptyList(),
                steps = steps,
            ),
        ) + itemSections,
        tableOfContents = itemSections.map { it.title },
        isItemDocument = true,
    )
}

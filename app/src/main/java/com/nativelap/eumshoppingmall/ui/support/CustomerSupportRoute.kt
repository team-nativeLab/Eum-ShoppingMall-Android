package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun CustomerSupportRoute(
    uiModel: CustomerSupportUiModel,
    onBackClick: () -> Unit,
) {
    var query by rememberSaveable { mutableStateOf(uiModel.query) }
    var selectedCategory by rememberSaveable { mutableStateOf(uiModel.selectedCategory) }
    var expandedFaqIds by rememberSaveable { mutableStateOf(uiModel.expandedFaqIds) }
    CustomerSupportScreen(
        uiModel = uiModel.copy(
            query = query,
            selectedCategory = selectedCategory,
            expandedFaqIds = expandedFaqIds,
        ),
        onEvent = { event ->
            when (event) {
                CustomerSupportEvent.BackClicked -> onBackClick()

                is CustomerSupportEvent.QueryChanged -> query = event.query

                is CustomerSupportEvent.CategorySelected -> selectedCategory = event.category

                is CustomerSupportEvent.FaqToggled -> {
                    expandedFaqIds = if (event.id in expandedFaqIds) {
                        expandedFaqIds - event.id
                    } else {
                        expandedFaqIds + event.id
                    }
                }

                // 전화 걸기와 1:1 문의 작성(A-43)은 이번 범위가 아니다.
                CustomerSupportEvent.CallClicked,
                CustomerSupportEvent.InquiryClicked,
                -> Unit
            }
        },
    )
}

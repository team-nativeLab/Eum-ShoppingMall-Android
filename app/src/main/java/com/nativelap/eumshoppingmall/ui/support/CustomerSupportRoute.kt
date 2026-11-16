package com.nativelap.eumshoppingmall.ui.support

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun CustomerSupportRoute(
    uiModel: CustomerSupportUiModel,
    onBackClick: () -> Unit,
    onInquiryClick: () -> Unit = {},
) {
    val context = LocalContext.current
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

                // 전화 앱을 열 뿐 직접 걸지 않는다. 전화 번호가 없거나 전화 앱이 없으면 아무것도 하지 않는다.
                CustomerSupportEvent.CallClicked -> {
                    val phoneNumber = uiModel.phoneNumber.filter { character -> character.isDigit() || character == '+' }
                    if (phoneNumber.isNotEmpty()) {
                        try {
                            context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$phoneNumber".toUri()))
                        } catch (exception: ActivityNotFoundException) {
                            // 전화 앱이 없는 기기에서는 안내할 대안이 없어 무시한다.
                        }
                    }
                }

                CustomerSupportEvent.InquiryClicked -> onInquiryClick()
            }
        },
    )
}

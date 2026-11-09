package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteUiModel

@Composable
fun QuoteCompleteRoute(
    uiModel: QuoteCompleteUiModel,
    onCloseClick: () -> Unit,
    onContinueShoppingClick: () -> Unit,
    onViewInquiriesClick: () -> Unit,
) {
    QuoteCompleteScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                QuoteCompleteEvent.CloseClicked -> onCloseClick()
                QuoteCompleteEvent.ContinueShoppingClicked -> onContinueShoppingClick()
                QuoteCompleteEvent.ViewInquiriesClicked -> onViewInquiriesClick()
            }
        },
    )
}

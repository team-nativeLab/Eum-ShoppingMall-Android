package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

@ResponsiveUiPreview
@Composable
private fun CartSectionsPreview() {
    EumShoppingMallTheme {
        Column(Modifier.padding(vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            CartLoginNoticeBanner(onClick = {})
            CartProductRow(
                item = CartItemUiModel(
                    id = "p1",
                    name = "안전화 방검형",
                    optionLabel = "270 / 1개",
                    quantity = 1,
                    unitPriceLabel = "89,000원",
                    totalPriceLabel = "89,000원",
                    isSelected = true,
                ),
                onSelectionChanged = {},
                onQuantityChanged = {},
                onOptionChangeClicked = {},
                onRemoveClicked = {},
                modifier = Modifier.padding(horizontal = EumSpacing.x20),
            )
            CartOrderSummaryCard(
                uiModel = CartUiModel(expectedPaymentWon = 89_000L, productAmountLabel = "89,000원", shippingFeeLabel = "무료", expectedPaymentLabel = "89,000원"),
                modifier = Modifier.padding(horizontal = EumSpacing.x20),
            )
        }
    }
}

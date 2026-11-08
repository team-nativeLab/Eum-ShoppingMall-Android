package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun DeliveryRequestDoorPreview() {
    EumShoppingMallTheme {
        DeliveryRequestScreen(DeliveryRequestUiModel(DeliveryRequestOption.Door, ""), onEvent = {})
    }
}

@ResponsiveUiPreview
@Composable
private fun DeliveryRequestCustomPreview() {
    EumShoppingMallTheme {
        DeliveryRequestScreen(
            DeliveryRequestUiModel(
                DeliveryRequestOption.Custom,
                "부재 시 문 앞에 놓아 주시고 배송 완료 후 문자 부탁드립니다.",
            ),
            onEvent = {},
        )
    }
}

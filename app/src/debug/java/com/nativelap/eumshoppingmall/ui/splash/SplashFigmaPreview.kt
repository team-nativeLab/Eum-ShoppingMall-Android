package com.nativelap.eumshoppingmall.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

@ResponsiveUiPreview
@Preview(name = "A-01 Splash · Figma", showBackground = true, widthDp = 390, heightDp = 852)
@Composable
private fun SplashFigmaPreview() {
    EumShoppingMallTheme {
        SplashScreen()
    }
}

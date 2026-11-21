package com.nativelap.eumshoppingmall.ui.benefits

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun CouponRoute(
    uiModel: CouponScreenUiModel,
    onBackClick: () -> Unit,
    onBrowseProductsClick: () -> Unit,
    onPointsClick: () -> Unit = {},
    onUnavailableAction: () -> Unit,
) {
    var selectedTab by rememberSaveable { mutableStateOf(uiModel.selectedTab) }
    var registeredCode by rememberSaveable { mutableStateOf(uiModel.registeredCode) }
    CouponScreen(
        uiModel = uiModel.copy(
            selectedTab = selectedTab,
            registeredCode = registeredCode,
        ),
        onEvent = { event ->
            when (event) {
                CouponScreenEvent.BackClicked -> onBackClick()

                is CouponScreenEvent.CodeChanged -> registeredCode = event.code

                is CouponScreenEvent.TabSelected -> selectedTab = event.tab

                CouponScreenEvent.BrowseProductsClicked -> onBrowseProductsClick()

                CouponScreenEvent.PointsClicked -> onPointsClick()

                CouponScreenEvent.RegisterClicked,
                is CouponScreenEvent.CouponClicked,
                -> onUnavailableAction()
            }
        },
    )
}

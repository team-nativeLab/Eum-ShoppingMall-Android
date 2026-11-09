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

                // 쿠폰 등록·상세와 적립금 화면은 이번 범위가 아니다.
                CouponScreenEvent.RegisterClicked,
                is CouponScreenEvent.CouponClicked,
                CouponScreenEvent.PointsClicked,
                -> Unit
            }
        },
    )
}

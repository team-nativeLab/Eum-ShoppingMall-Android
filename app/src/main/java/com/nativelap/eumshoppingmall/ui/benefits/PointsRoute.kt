package com.nativelap.eumshoppingmall.ui.benefits

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun PointsRoute(
    uiModel: PointsScreenUiModel,
    onBackClick: () -> Unit,
    onShopClick: () -> Unit,
) {
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.selectedFilter) }
    PointsScreen(
        // 내역 항목에 적립·사용·소멸 구분 값이 아직 없어 필터는 선택 표시만 바꾼다. 필터별 목록은 데이터 계층이 생긴 뒤 붙인다.
        uiModel = uiModel.copy(selectedFilter = selectedFilter),
        onEvent = { event ->
            when (event) {
                PointsScreenEvent.BackClicked -> onBackClick()
                is PointsScreenEvent.FilterSelected -> selectedFilter = event.filter
                PointsScreenEvent.ShopClicked -> onShopClick()
            }
        },
    )
}

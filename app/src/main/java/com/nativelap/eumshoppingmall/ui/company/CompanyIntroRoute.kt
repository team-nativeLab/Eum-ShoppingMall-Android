package com.nativelap.eumshoppingmall.ui.company

import androidx.compose.runtime.Composable

@Composable
fun CompanyIntroRoute(
    uiModel: CompanyIntroUiModel,
    onBackClick: () -> Unit,
) {
    CompanyIntroScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                CompanyIntroEvent.BackClicked -> onBackClick()

                // 섹션 이동은 화면 안 스크롤이고, 지도·연락은 시스템 앱 연동이 필요해 이번 범위가 아니다.
                is CompanyIntroEvent.SectionSelected,
                CompanyIntroEvent.MapClicked,
                CompanyIntroEvent.ContactClicked,
                -> Unit
            }
        },
    )
}

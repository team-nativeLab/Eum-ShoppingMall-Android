package com.nativelap.eumshoppingmall.ui.company

import androidx.compose.runtime.Composable

@Composable
fun CompanyIntroRoute(
    uiModel: CompanyIntroUiModel,
    onBackClick: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    CompanyIntroScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                CompanyIntroEvent.BackClicked -> onBackClick()

                CompanyIntroEvent.MapClicked,
                CompanyIntroEvent.ContactClicked,
                -> onUnavailableAction()
            }
        },
    )
}

package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
internal fun DismissSnackbarOnNavigationChange(hostState: SnackbarHostState, destinationKey: Any?) {
    LaunchedEffect(destinationKey) {
        hostState.currentSnackbarData?.dismiss()
    }
}

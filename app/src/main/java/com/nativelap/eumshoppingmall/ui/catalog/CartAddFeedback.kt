package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R

// 화면이 이탈할 때 비울지 정한다. 같은 결과(serial)를 이 화면이 띄웠을 때만 비우고, 이탈 뒤 들어온 새 결과는 남긴다.
fun shouldClearCartOutcomeOnLeave(pendingSerial: Long?, shownSerial: Long?): Boolean = pendingSerial != null && pendingSerial == shownSerial

private class ShownCartOutcome {
    var serial: Long? = null
}

// 담기 결과 안내. 상세와 같은 방식이다: 결과를 한 번 스낵바로 띄우고, 장바구니 보기 액션은 onCartClick으로 간다.
@Composable
fun rememberCartAddSnackbarHostState(
    cartAddOutcome: CartAddOutcome?,
    onCartAddOutcomeChange: (CartAddOutcome?) -> Unit,
    onCartClick: () -> Unit,
): SnackbarHostState {
    val snackbarHostState = remember { SnackbarHostState() }
    val cartSavedMessage = stringResource(R.string.catalog_added_to_cart)
    val cartFailedMessage = stringResource(R.string.catalog_add_to_cart_failed)
    val viewCartLabel = stringResource(R.string.catalog_view_cart)
    val currentOnCartClick by rememberUpdatedState(onCartClick)
    val currentOutcome by rememberUpdatedState(cartAddOutcome)
    val currentOnOutcomeChange by rememberUpdatedState(onCartAddOutcomeChange)
    val shown = remember { ShownCartOutcome() }
    LaunchedEffect(cartAddOutcome) {
        val outcome = cartAddOutcome ?: return@LaunchedEffect
        shown.serial = outcome.serial
        if (outcome.saved) {
            val result = snackbarHostState.showSnackbar(
                message = cartSavedMessage,
                actionLabel = viewCartLabel,
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) currentOnCartClick()
        } else {
            snackbarHostState.showSnackbar(message = cartFailedMessage, duration = SnackbarDuration.Short)
        }
        // 정상 완료 경로에서만 비운다. 취소되면 onDispose가 같은 결과를 비운다.
        onCartAddOutcomeChange(null)
    }
    DisposableEffect(Unit) {
        onDispose {
            if (shouldClearCartOutcomeOnLeave(pendingSerial = currentOutcome?.serial, shownSerial = shown.serial)) {
                currentOnOutcomeChange(null)
            }
        }
    }
    return snackbarHostState
}

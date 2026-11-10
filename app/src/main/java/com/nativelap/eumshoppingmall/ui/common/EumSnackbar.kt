package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumDuration
import com.nativelap.eumshoppingmall.ui.theme.EumEasing
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import com.nativelap.eumshoppingmall.ui.theme.rememberReducedMotion

/**
 * 하단 안내 바(Snackbar) 호스트. 등장은 fade + 아래에서 8dp 올라옴(250ms, StandardDecelerate), 퇴장은 fade 150ms.
 * 표시 시간과 액션 처리는 [SnackbarHostState]가 맡는다. 줄이기 설정이면 fade만 쓴다.
 * 퇴장 중에도 내용이 남도록 마지막으로 보낸 데이터를 보관한다.
 */
@Composable
fun EumSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val reducedMotion = rememberReducedMotion()
    val currentData = hostState.currentSnackbarData
    var shownData by remember { mutableStateOf<SnackbarData?>(null) }
    LaunchedEffect(currentData) {
        if (currentData != null) shownData = currentData
    }
    val slideDistancePx = with(LocalDensity.current) { SNACKBAR_SLIDE_DISTANCE.roundToPx() }
    val enterTransition = if (reducedMotion) {
        fadeIn(tween(EumDuration.MEDIUM1, easing = EumEasing.StandardDecelerate))
    } else {
        fadeIn(tween(EumDuration.MEDIUM1, easing = EumEasing.StandardDecelerate)) +
            slideInVertically(tween(EumDuration.MEDIUM1, easing = EumEasing.StandardDecelerate)) { slideDistancePx }
    }
    val exitTransition = fadeOut(tween(EumDuration.SHORT2, easing = EumEasing.StandardAccelerate))
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x8),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedVisibility(
            visible = currentData != null,
            enter = enterTransition,
            exit = exitTransition,
        ) {
            shownData?.let { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.eumColors.toast,
                    contentColor = MaterialTheme.eumColors.inverse,
                    actionColor = MaterialTheme.eumColors.inverse,
                )
            }
        }
    }
}

private val SNACKBAR_SLIDE_DISTANCE = 8.dp

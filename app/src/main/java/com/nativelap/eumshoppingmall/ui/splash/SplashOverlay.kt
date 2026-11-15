package com.nativelap.eumshoppingmall.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.theme.EumDuration
import com.nativelap.eumshoppingmall.ui.theme.EumEasing
import com.nativelap.eumshoppingmall.ui.theme.rememberReducedMotion
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MILLIS = 1_500L

/**
 * 앱 시작 직후 스플래시(A-01)를 A-02 홈 위에 잠시 덮었다가 걷는다. 호출한 Box 안에서 가장 위에 그린다.
 *
 * NavKey가 아니라 화면 위 overlay라 back stack에 남지 않는다. 표시 여부는 rememberSaveable이라 회전·프로세스 복원 뒤에
 * 이미 지나간 스플래시가 다시 나오지 않는다. 덮여 있는 동안 아래 화면이 터치를 받지 않게 입력을 막고, 줄이기 설정이면 페이드 없이 걷는다.
 */
@Composable
fun SplashOverlay() {
    var isSplashVisible by rememberSaveable { mutableStateOf(true) }
    val reducedMotion = rememberReducedMotion()
    LaunchedEffect(Unit) {
        if (isSplashVisible) {
            delay(SPLASH_DURATION_MILLIS)
            isSplashVisible = false
        }
    }
    AnimatedVisibility(
        visible = isSplashVisible,
        exit = if (reducedMotion) {
            fadeOut(tween(durationMillis = 0))
        } else {
            fadeOut(tween(EumDuration.MEDIUM2, easing = EumEasing.StandardAccelerate))
        },
    ) {
        SplashScreen(Modifier.clickable(indication = null, interactionSource = null, onClick = {}))
    }
}

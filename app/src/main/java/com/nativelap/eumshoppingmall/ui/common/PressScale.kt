package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.nativelap.eumshoppingmall.ui.theme.EumDuration
import com.nativelap.eumshoppingmall.ui.theme.EumEasing
import com.nativelap.eumshoppingmall.ui.theme.EumSpring
import com.nativelap.eumshoppingmall.ui.theme.rememberReducedMotion
import kotlinx.coroutines.launch

private const val PRESSED_SCALE = 0.98f

/**
 * 누르는 동안 0.98배로 줄이고 놓으면 원래 크기로 되돌린다. 리플은 그대로 둔다.
 *
 * 스크롤처럼 제스처가 취소되면([waitForUpOrCancellation]이 null) 같은 스프링으로 되돌려 scale이 남지 않게 한다.
 * 줄이기 설정이면 scale을 적용하지 않고 리플만 남긴다.
 */
@Composable
fun Modifier.pressScale(enabled: Boolean = true): Modifier {
    val reducedMotion = rememberReducedMotion()
    // 조건과 무관하게 항상 같은 순서로 remember를 호출해야 한다.
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    if (!enabled || reducedMotion) return this
    return this
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                scope.launch {
                    scale.animateTo(PRESSED_SCALE, tween(EumDuration.SHORT1, easing = EumEasing.Standard))
                }
                waitForUpOrCancellation()
                scope.launch {
                    scale.animateTo(1f, EumSpring.press())
                }
            }
        }
}

package com.nativelap.eumshoppingmall.ui.theme

import android.animation.ValueAnimator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * 시스템 애니메이션 설정이 꺼져 있으면 축소 모션으로 판단한다.
 * 순수 함수로 분리해 판정 분기를 JVM 단위 테스트로 고정한다.
 */
internal fun isReducedMotion(animatorsEnabled: Boolean): Boolean = !animatorsEnabled

/**
 * 줄이기 여부를 화면 수명 동안 한 번 읽는다.
 * 한계: 화면이 떠 있는 동안 설정을 바꿔도 반영되지 않는다. 전환 람다(transitionSpec)는 시작 시점에 값을 읽는다.
 */
@Composable
fun rememberReducedMotion(): Boolean = remember {
    isReducedMotion(ValueAnimator.areAnimatorsEnabled())
}

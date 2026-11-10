package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * 모션 지속 시간(ms). 명세(docs/MOTION_SPEC.md 4.1)의 값과 같다.
 * 퇴장은 진입 시간의 약 0.6배를 기준으로 한다.
 */
object EumDuration {
    const val SHORT1 = 100
    const val SHORT2 = 150
    const val MEDIUM1 = 250
    const val MEDIUM2 = 300
    const val LONG1 = 400
}

/** 모션 가속 곡선. 명세의 cubic-bezier 값을 그대로 쓴다. */
object EumEasing {
    val Standard = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val StandardDecelerate = CubicBezierEasing(0f, 0f, 0f, 1f)
    val StandardAccelerate = CubicBezierEasing(0.3f, 0f, 1f, 1f)
    val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
}

/**
 * 스프링 모션. 타입 매개변수는 대상 값(Float, Dp, Offset 등)에 맞춰 호출부에서 정한다.
 * MaterialTheme의 motionScheme은 material3 1.4.0에서 internal이라 쓰지 않고, 이 토큰을 직접 적용한다.
 */
object EumSpring {
    /** 누름 반응. 튕기지 않는 스프링. */
    fun <T> press(): SpringSpec<T> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium,
    )

    /** 상태 정착. 튕기지 않는 느린 스프링. */
    fun <T> settle(): SpringSpec<T> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )

    /** 주문 완료 등 확정 순간 전용. 약간 튕긴다. */
    fun <T> confirm(): SpringSpec<T> = spring(
        dampingRatio = 0.7f,
        stiffness = Spring.StiffnessMediumLow,
    )
}

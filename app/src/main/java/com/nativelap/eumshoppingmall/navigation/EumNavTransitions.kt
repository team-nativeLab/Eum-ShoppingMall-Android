package com.nativelap.eumshoppingmall.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec

/**
 * NavDisplay 전환 애니메이션의 단일 정의 위치.
 *
 * 화면 전환 모션 설계(#34)가 확정되기 전까지는 Navigation 3 기본값을 그대로 쓴다.
 * 목적지별 metadata 전환은 두지 않는다.
 */
object EumNavTransitions {
    val transitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        defaultTransitionSpec()

    val popTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        defaultPopTransitionSpec()

    val predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.(Int) -> ContentTransform =
        defaultPredictivePopTransitionSpec()
}

package com.nativelap.eumshoppingmall.navigation

import android.animation.ValueAnimator
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.theme.EumDuration
import com.nativelap.eumshoppingmall.ui.theme.EumEasing
import com.nativelap.eumshoppingmall.ui.theme.isReducedMotion
import kotlin.math.roundToInt

/**
 * NavDisplay 전환 애니메이션의 단일 정의 위치.
 *
 * - push: 들어오는 화면이 오른쪽 10%에서 제자리로 오며 fade in(300ms, EmphasizedDecelerate). 나가는 화면은 fade out(150ms, EmphasizedAccelerate).
 * - pop·predictive pop: 아래 화면이 왼쪽 10%에서 제자리로 오며 fade in(300ms). 떠나는 화면은 fade out(150ms).
 * - 탭 전환: fade-through. 나가기 90ms(StandardAccelerate), 들어오기 210ms(90ms 지연, StandardDecelerate). [EumMainNavigationState]의 소속 탭으로 판별한다.
 * - 줄이기: 전환 전부 fade 150ms로 대체하고 탭 전환은 즉시 바꾼다.
 *
 * 줄이기 여부는 전환이 시작될 때 읽는다. 전환 도중 설정이 바뀌어도 그 전환에는 반영되지 않는다.
 * 목적지별 metadata 전환은 두지 않는다.
 */
object EumNavTransitions {
    val transitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
        transformFor(
            kind = navTransitionKind(
                reducedMotion = isReducedMotionNow(),
                pop = false,
                tabSwitch = isTabSwitch(initialState.ownerTab(), targetState.ownerTab()),
            ),
        )
    }

    val popTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
        transformFor(
            kind = navTransitionKind(
                reducedMotion = isReducedMotionNow(),
                pop = true,
                tabSwitch = isTabSwitch(initialState.ownerTab(), targetState.ownerTab()),
            ),
        )
    }

    // predictive back 진행률(Int)은 쓰지 않는다. pop과 같은 전환을 쓴다.
    val predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.(Int) -> ContentTransform = {
        transformFor(
            kind = navTransitionKind(
                reducedMotion = isReducedMotionNow(),
                pop = true,
                tabSwitch = isTabSwitch(initialState.ownerTab(), targetState.ownerTab()),
            ),
        )
    }
}

/** 전환 종류. 실제 ContentTransform 생성은 [transformFor]가 맡고, 어떤 종류인지는 순수 함수가 정한다. */
internal enum class NavTransitionKind {
    Push,
    Pop,
    TabSwitch,
    ReducedFade,
    ReducedInstant,
}

/** 줄이기 설정이면 전환을 fade 또는 즉시로 줄인다. 탭 전환은 줄이기에서도 즉시 바뀌어 상태가 섞이지 않게 한다. */
internal fun navTransitionKind(reducedMotion: Boolean, pop: Boolean, tabSwitch: Boolean): NavTransitionKind = when {
    reducedMotion && tabSwitch -> NavTransitionKind.ReducedInstant
    reducedMotion -> NavTransitionKind.ReducedFade
    tabSwitch -> NavTransitionKind.TabSwitch
    pop -> NavTransitionKind.Pop
    else -> NavTransitionKind.Push
}

/** 두 엔트리의 소속 탭이 모두 확인되고 서로 다를 때만 탭 전환이다. 한쪽이라도 모르면 push·pop으로 본다. */
internal fun isTabSwitch(fromTab: MainNavigationTab?, toTab: MainNavigationTab?): Boolean = fromTab != null && toTab != null && fromTab != toTab

private fun AnimatedContentTransitionScope<Scene<NavKey>>.transformFor(kind: NavTransitionKind): ContentTransform = when (kind) {
    NavTransitionKind.ReducedInstant -> EnterTransition.None togetherWith ExitTransition.None
    NavTransitionKind.ReducedFade -> reducedFade()
    NavTransitionKind.TabSwitch -> tabFadeThrough()
    NavTransitionKind.Push -> pushTransform()
    NavTransitionKind.Pop -> popTransform()
}

private fun isReducedMotionNow(): Boolean = isReducedMotion(ValueAnimator.areAnimatorsEnabled())

private fun Scene<NavKey>.ownerTab(): MainNavigationTab? = entries.lastOrNull()?.metadata?.get(OWNER_TAB_METADATA_KEY) as? MainNavigationTab

private fun reducedFade(): ContentTransform = fadeIn(tween(EumDuration.SHORT2)) togetherWith fadeOut(tween(EumDuration.SHORT2))

private fun tabFadeThrough(): ContentTransform {
    val enter = fadeIn(
        animationSpec = tween(
            durationMillis = TAB_ENTER_MILLIS,
            delayMillis = TAB_EXIT_MILLIS,
            easing = EumEasing.StandardDecelerate,
        ),
    )
    val exit = fadeOut(tween(durationMillis = TAB_EXIT_MILLIS, easing = EumEasing.StandardAccelerate))
    return enter togetherWith exit
}

private fun AnimatedContentTransitionScope<Scene<NavKey>>.pushTransform(): ContentTransform {
    val enter = slideInHorizontally(
        animationSpec = tween(EumDuration.MEDIUM2, easing = EumEasing.EmphasizedDecelerate),
        initialOffsetX = { fullWidth -> (fullWidth * SLIDE_FRACTION).roundToInt() },
    ) + fadeIn(tween(EumDuration.MEDIUM2, easing = EumEasing.EmphasizedDecelerate))
    val exit = fadeOut(tween(EumDuration.SHORT2, easing = EumEasing.EmphasizedAccelerate))
    return enter togetherWith exit
}

private fun AnimatedContentTransitionScope<Scene<NavKey>>.popTransform(): ContentTransform {
    val enter = slideInHorizontally(
        animationSpec = tween(EumDuration.MEDIUM2, easing = EumEasing.EmphasizedDecelerate),
        initialOffsetX = { fullWidth -> -(fullWidth * SLIDE_FRACTION).roundToInt() },
    ) + fadeIn(tween(EumDuration.MEDIUM2, easing = EumEasing.EmphasizedDecelerate))
    val exit = fadeOut(tween(EumDuration.SHORT2, easing = EumEasing.EmphasizedAccelerate))
    return enter togetherWith exit
}

private const val SLIDE_FRACTION = 0.1f
private const val TAB_EXIT_MILLIS = 90
private const val TAB_ENTER_MILLIS = 210

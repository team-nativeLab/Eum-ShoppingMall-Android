package com.nativelap.eumshoppingmall.navigation

import androidx.navigation3.runtime.NavKey

/**
 * 로그인이 필요한 목적지로 가기 전에 거치는 게이트.
 *
 * 로그인 상태면 [target]을 그대로, 아니면 [target]을 보류 목적지로 담은 Login을 돌려준다.
 * Login·Drawer는 다시 감싸지 않아 Login(Login(...))이 쌓이지 않는다.
 */
fun gateDestination(
    target: EumDestination,
    isLoggedIn: Boolean,
): EumDestination = when {
    isLoggedIn -> target

    // Login과 Drawer(overlay)는 보류 목적지로 감싸지 않는다.
    target is EumDestination.Login || target is EumDestination.Drawer -> target

    else -> EumDestination.Login(pendingDestination = target)
}

/** 로그인·가입처럼 인증을 진행하는 화면인지 판별한다. 가입 등 인증 계열 화면이 늘면 여기에 더한다. */
internal fun isAuthDestination(navKey: NavKey): Boolean = navKey is EumDestination.Login

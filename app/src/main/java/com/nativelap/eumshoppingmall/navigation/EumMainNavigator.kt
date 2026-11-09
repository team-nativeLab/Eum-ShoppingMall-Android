package com.nativelap.eumshoppingmall.navigation

import androidx.navigation3.runtime.NavKey
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

/**
 * [EumMainNavigationState]의 back stack만 조작한다.
 *
 * 이동할지 말지, 어떤 데이터로 이동할지는 호출부가 이미 정한 뒤 목적지만 넘긴다.
 */
class EumMainNavigator(
    private val state: EumMainNavigationState,
) {
    // 다른 탭으로 전환하고, 이미 선택된 탭을 다시 누르면 그 탭을 루트까지 되돌린다.
    fun navigateToTab(tab: MainNavigationTab) {
        if (state.currentTab == tab) {
            popToRoot(tab)
        } else {
            state.topLevelIndex = tab.ordinal
        }
    }

    // 현재 탭 back stack 맨 위에 목적지를 쌓는다. 같은 목적지를 연달아 누른 경우는 한 번만 쌓는다.
    fun push(destination: EumDestination) {
        val backStack = state.currentBackStack
        if (backStack.lastOrNull() != destination) {
            backStack.add(destination)
        }
    }

    // 맨 위 항목을 목적지로 바꾼다(예: 드로어 메뉴 선택, 재검색).
    fun replaceTop(destination: EumDestination) {
        val backStack = state.currentBackStack
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
        push(destination)
    }

    /**
     * 맨 위부터 [shouldRemove]에 해당하는 항목을 걷어 낸 뒤 목적지를 쌓는다.
     *
     * 결제·견적 폼처럼 완료 후 뒤로가기로 다시 들어가면 안 되는 항목을 정리할 때 쓴다. 탭 루트는 지우지 않는다.
     */
    fun popWhileAndPush(
        destination: EumDestination,
        shouldRemove: (NavKey) -> Boolean,
    ) {
        val backStack = state.currentBackStack
        while (backStack.size > 1 && shouldRemove(backStack.last())) {
            backStack.removeLastOrNull()
        }
        push(destination)
    }

    /**
     * 현재 탭 맨 위가 [destination]일 때만 pop하고 성공 여부를 돌려준다. 탭 루트는 pop하지 않는다.
     *
     * 퇴장 애니메이션 중 같은 화면에서 두 번째 입력이 들어와도 그 아래 화면까지 닫지 않게 할 때 쓴다.
     */
    fun popIfTop(destination: EumDestination): Boolean {
        val backStack = state.currentBackStack
        if (backStack.size <= 1 || backStack.lastOrNull() != destination) {
            return false
        }
        backStack.removeLastOrNull()
        return true
    }

    /**
     * 맨 위 항목을 닫고, 지정한 탭을 루트만 남긴 뒤 그 탭으로 전환한다(예: 주문·견적 완료의 쇼핑 계속).
     *
     * 출발 탭과 상관없이 항상 대상 탭의 첫 화면이 보여야 하므로, 대상 탭에 남아 있던 기록도 지운다.
     */
    fun closeTopAndOpenTabRoot(tab: MainNavigationTab) {
        val backStack = state.currentBackStack
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
        popToRoot(tab)
        state.topLevelIndex = tab.ordinal
    }

    /**
     * 현재 탭에서 한 단계 뒤로 간다.
     *
     * 비시작 탭의 루트면 시작 탭으로 전환한다. 시작 탭 루트면 처리하지 않고 false를 돌려 시스템 뒤로가기에 맡긴다.
     */
    fun goBack(): Boolean {
        val backStack = state.currentBackStack
        return when {
            backStack.size > 1 -> {
                backStack.removeLastOrNull()
                true
            }

            state.currentTab != EumStartTab -> {
                state.topLevelIndex = EumStartTab.ordinal
                true
            }

            else -> false
        }
    }

    /**
     * 로그인 성공 후 호출한다(F4). 맨 위 [EumDestination.Login]을 pop하고, 보류된 목적지가 있으면 push한다.
     *
     * 로그인 성공 처리는 아직 없으므로 현재는 호출하는 화면이 없다.
     */
    fun completeLogin() {
        val backStack = state.currentBackStack
        val loginDestination = backStack.lastOrNull() as? EumDestination.Login ?: return
        backStack.removeLastOrNull()
        loginDestination.pendingDestination?.let(::push)
    }

    private fun popToRoot(tab: MainNavigationTab) {
        val backStack = state.backStacks.getValue(tab)
        while (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}

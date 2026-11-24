package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

/**
 * 지금 그리는 NavEntry가 속한 탭.
 *
 * 하단 바 선택 표시는 [EumMainNavigationState.currentTab]이 아니라 이 값을 써야 한다. currentTab은 탭 전환·predictive back
 * 애니메이션이 시작되는 순간 바뀌므로, 사라지는 화면의 하단 바까지 미리 새 탭으로 표시되기 때문이다.
 */
internal val LocalEumOwnerTab = staticCompositionLocalOf<MainNavigationTab> {
    error("LocalEumOwnerTab은 rememberNavEntries가 만든 NavEntry 안에서만 읽을 수 있습니다.")
}

/**
 * 하단 탭별 back stack과 현재 탭을 보관만 한다. 변경은 [EumMainNavigator]가 맡는다.
 *
 * 현재 탭은 NavKey가 아니라 [MainNavigationTab]의 ordinal(Int)로 저장한다.
 */
@Stable
class EumMainNavigationState(
    private val topLevelIndexState: MutableIntState,
    val backStacks: Map<MainNavigationTab, NavBackStack<NavKey>>,
) {
    init {
        require(backStacks.keys == MainNavigationTab.entries.toSet()) {
            "모든 하단 탭에 back stack이 있어야 합니다."
        }
    }

    var topLevelIndex: Int
        get() = topLevelIndexState.intValue
        internal set(value) {
            topLevelIndexState.intValue = value
        }

    val currentTab: MainNavigationTab
        get() = MainNavigationTab.entries[topLevelIndex]

    val currentBackStack: NavBackStack<NavKey>
        get() = backStacks.getValue(currentTab)
}

@Composable
fun rememberEumMainNavigationState(): EumMainNavigationState {
    val topLevelIndexState = rememberSaveable { mutableIntStateOf(EumStartTab.ordinal) }
    val backStacks = MainNavigationTab.entries.associateWith { tab ->
        key(tab) {
            rememberNavBackStack(tab.rootDestination())
        }
    }
    return remember(topLevelIndexState, backStacks) {
        EumMainNavigationState(
            topLevelIndexState = topLevelIndexState,
            backStacks = backStacks,
        )
    }
}

/**
 * 모든 탭의 back stack을 NavEntry로 변환하고, NavDisplay에 넘길 목록을 만든다.
 *
 * 시작 탭이 아니면 시작 탭 항목 뒤에 현재 탭 항목을 이어 붙인다. 그래야 비시작 탭 루트에서도 NavDisplay가 뒤로가기를 받아
 * [EumMainNavigator.goBack]으로 시작 탭 전환을 처리하고, 시작 탭 루트에서만 시스템 뒤로가기(앱 종료)로 넘어간다.
 * 비활성 탭도 매번 변환해야 탭을 오가도 화면별 저장 상태가 유지된다.
 */
@Composable
fun EumMainNavigationState.rememberNavEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): List<NavEntry<NavKey>> {
    val saveableStateDecorator = rememberSaveableStateHolderNavEntryDecorator<NavKey>()
    val entryDecorators = remember(saveableStateDecorator) { listOf(saveableStateDecorator) }
    val entriesByTab = backStacks.mapValues { (tab, backStack) ->
        key(tab) {
            // 같은 목적지(예: 같은 상품 상세)가 여러 탭에 있어도 저장 상태가 섞이지 않도록 탭 이름을 contentKey에 붙인다.
            val tabEntryProvider = remember(tab, entryProvider) {
                { navKey: NavKey ->
                    val baseEntry = entryProvider(navKey)
                    NavEntry(
                        key = navKey,
                        contentKey = "${tab.name}/${baseEntry.contentKey}",
                        metadata = baseEntry.metadata,
                    ) {
                        CompositionLocalProvider(LocalEumOwnerTab provides tab) {
                            baseEntry.Content()
                        }
                    }
                }
            }
            rememberDecoratedNavEntries(
                backStack = backStack,
                entryDecorators = entryDecorators,
                entryProvider = tabEntryProvider,
            )
        }
    }
    val startTabEntries = entriesByTab.getValue(EumStartTab)
    return if (currentTab == EumStartTab) {
        startTabEntries
    } else {
        startTabEntries + entriesByTab.getValue(currentTab)
    }
}

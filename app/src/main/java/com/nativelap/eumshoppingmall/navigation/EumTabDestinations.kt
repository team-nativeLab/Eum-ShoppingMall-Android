package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

/** 앱 시작 시 선택되는 탭. 이 탭의 루트에서 뒤로가기는 시스템에 넘긴다. */
val EumStartTab: MainNavigationTab = MainNavigationTab.Home

/** 각 하단 탭 back stack의 첫 목적지. */
fun MainNavigationTab.rootDestination(): EumDestination = when (this) {
    MainNavigationTab.Home -> EumDestination.Home
    MainNavigationTab.Categories -> EumDestination.Categories
    MainNavigationTab.Search -> EumDestination.Search
    MainNavigationTab.Cart -> EumDestination.Cart
    MainNavigationTab.MyPage -> EumDestination.MyPage
}

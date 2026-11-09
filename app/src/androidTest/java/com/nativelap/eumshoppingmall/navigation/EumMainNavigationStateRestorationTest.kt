package com.nativelap.eumshoppingmall.navigation

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.navigation3.runtime.NavKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EumMainNavigationStateRestorationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun tabIndexAndEveryBackStackSurviveSavedStateRestore() {
        val restorationTester = StateRestorationTester(composeRule)
        lateinit var navigationState: EumMainNavigationState
        restorationTester.setContent {
            navigationState = rememberEumMainNavigationState()
        }

        composeRule.runOnIdle {
            val navigator = EumMainNavigator(navigationState)
            navigator.push(EumDestination.ProductList())
            navigator.push(EumDestination.ProductDetail("p1"))
            navigator.navigateToTab(MainNavigationTab.Search)
            navigator.push(EumDestination.SearchResults("안전모"))
            navigator.navigateToTab(MainNavigationTab.Cart)
            navigator.push(EumDestination.QuoteRequest())
            navigator.push(EumDestination.Login(pendingDestination = EumDestination.QuoteHistory))
        }

        restorationTester.emulateSavedInstanceStateRestore()

        composeRule.runOnIdle {
            assertEquals(MainNavigationTab.Cart, navigationState.currentTab)
            assertEquals(
                listOf(EumDestination.Home, EumDestination.ProductList(), EumDestination.ProductDetail("p1")),
                stackOf(navigationState, MainNavigationTab.Home),
            )
            assertEquals(
                listOf(EumDestination.Search, EumDestination.SearchResults("안전모")),
                stackOf(navigationState, MainNavigationTab.Search),
            )
            assertEquals(
                listOf(
                    EumDestination.Cart,
                    EumDestination.QuoteRequest(),
                    EumDestination.Login(pendingDestination = EumDestination.QuoteHistory),
                ),
                stackOf(navigationState, MainNavigationTab.Cart),
            )
            assertEquals(listOf(EumDestination.Categories), stackOf(navigationState, MainNavigationTab.Categories))
            assertEquals(listOf(EumDestination.MyPage), stackOf(navigationState, MainNavigationTab.MyPage))
        }
    }

    private fun stackOf(
        navigationState: EumMainNavigationState,
        tab: MainNavigationTab,
    ): List<NavKey> = navigationState.backStacks.getValue(tab).toList()
}

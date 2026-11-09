package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EumMainNavigatorTest {
    private lateinit var navigationState: EumMainNavigationState
    private lateinit var navigator: EumMainNavigator

    @Before
    fun setUp() {
        navigationState = EumMainNavigationState(
            topLevelIndexState = mutableIntStateOf(EumStartTab.ordinal),
            backStacks = MainNavigationTab.entries.associateWith { tab ->
                NavBackStack<NavKey>(tab.rootDestination())
            },
        )
        navigator = EumMainNavigator(navigationState)
    }

    @Test
    fun startsOnHomeTabWithEachTabRootOnly() {
        assertEquals(MainNavigationTab.Home, navigationState.currentTab)
        MainNavigationTab.entries.forEach { tab ->
            assertEquals(listOf(tab.rootDestination()), stackOf(tab))
        }
    }

    @Test
    fun switchingTabsKeepsEachTabBackStack() {
        navigator.push(EumDestination.ProductDetail("p1"))
        navigator.navigateToTab(MainNavigationTab.Cart)
        navigator.push(EumDestination.CheckoutAddress)

        navigator.navigateToTab(MainNavigationTab.Home)

        assertEquals(MainNavigationTab.Home, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Home, EumDestination.ProductDetail("p1")), stackOf(MainNavigationTab.Home))
        assertEquals(listOf(EumDestination.Cart, EumDestination.CheckoutAddress), stackOf(MainNavigationTab.Cart))
    }

    @Test
    fun reselectingCurrentTabPopsToRoot() {
        navigator.navigateToTab(MainNavigationTab.Categories)
        navigator.push(EumDestination.ProductList("protective"))
        navigator.push(EumDestination.ProductDetail("p1"))

        navigator.navigateToTab(MainNavigationTab.Categories)

        assertEquals(MainNavigationTab.Categories, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Categories), stackOf(MainNavigationTab.Categories))
    }

    @Test
    fun popularSearchFromHomeSearchResultsOpensSearchTab() {
        navigator.push(EumDestination.SearchResults("없는 상품"))

        navigator.navigateToTab(MainNavigationTab.Search)

        assertEquals(MainNavigationTab.Search, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Search), stackOf(MainNavigationTab.Search))
    }

    @Test
    fun popularSearchInsideSearchTabReturnsToSearchRoot() {
        navigator.navigateToTab(MainNavigationTab.Search)
        navigator.push(EumDestination.SearchResults("없는 상품"))

        navigator.navigateToTab(MainNavigationTab.Search)

        assertEquals(MainNavigationTab.Search, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Search), stackOf(MainNavigationTab.Search))
    }

    @Test
    fun goBackPopsWithinCurrentTab() {
        navigator.navigateToTab(MainNavigationTab.Search)
        navigator.push(EumDestination.SearchResults("안전모"))

        val isHandled = navigator.goBack()

        assertTrue(isHandled)
        assertEquals(MainNavigationTab.Search, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Search), stackOf(MainNavigationTab.Search))
    }

    @Test
    fun goBackAtNonStartTabRootSwitchesToStartTabInsteadOfExiting() {
        navigator.navigateToTab(MainNavigationTab.MyPage)

        val isHandled = navigator.goBack()

        assertTrue(isHandled)
        assertEquals(EumStartTab, navigationState.currentTab)
        assertEquals(listOf(EumDestination.MyPage), stackOf(MainNavigationTab.MyPage))
    }

    @Test
    fun goBackAtStartTabRootIsLeftToSystem() {
        val isHandled = navigator.goBack()

        assertFalse(isHandled)
        assertEquals(EumStartTab, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Home), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun pushIgnoresSameDestinationTwiceInARow() {
        navigator.push(EumDestination.ProductDetail("p1"))
        navigator.push(EumDestination.ProductDetail("p1"))

        assertEquals(listOf(EumDestination.Home, EumDestination.ProductDetail("p1")), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun orderCompleteRemovesCheckoutStepsFromBackStack() {
        navigator.navigateToTab(MainNavigationTab.Cart)
        navigator.push(EumDestination.CheckoutAddress)
        navigator.push(EumDestination.CheckoutPayment)

        navigator.popWhileAndPush(EumDestination.OrderComplete) { navKey ->
            navKey is EumDestination.CheckoutAddress || navKey is EumDestination.CheckoutPayment
        }

        assertEquals(listOf(EumDestination.Cart, EumDestination.OrderComplete), stackOf(MainNavigationTab.Cart))
        navigator.goBack()
        assertEquals(listOf(EumDestination.Cart), stackOf(MainNavigationTab.Cart))
    }

    @Test
    fun quoteCompleteRemovesQuoteFormButKeepsPreviousScreen() {
        navigator.push(EumDestination.ProductDetail("p1"))
        navigator.push(EumDestination.QuoteRequest("p1"))

        navigator.popWhileAndPush(EumDestination.QuoteComplete) { navKey -> navKey is EumDestination.QuoteRequest }

        assertEquals(
            listOf(EumDestination.Home, EumDestination.ProductDetail("p1"), EumDestination.QuoteComplete),
            stackOf(MainNavigationTab.Home),
        )
    }

    @Test
    fun popWhileAndPushNeverRemovesTabRoot() {
        navigator.popWhileAndPush(EumDestination.QuoteComplete) { true }

        assertEquals(listOf(EumDestination.Home, EumDestination.QuoteComplete), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun continueShoppingFromCartOpensHomeRootEvenWhenHomeHadProductDetail() {
        navigator.push(EumDestination.ProductDetail("p1"))
        navigator.navigateToTab(MainNavigationTab.Cart)
        navigator.push(EumDestination.CheckoutAddress)
        navigator.push(EumDestination.CheckoutPayment)
        navigator.popWhileAndPush(EumDestination.OrderComplete) { navKey ->
            navKey is EumDestination.CheckoutAddress || navKey is EumDestination.CheckoutPayment
        }

        navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home)

        assertEquals(MainNavigationTab.Home, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Home), stackOf(MainNavigationTab.Home))
        assertEquals(listOf(EumDestination.Cart), stackOf(MainNavigationTab.Cart))
    }

    @Test
    fun continueShoppingFromHomeOrderClearsHomeStackToRoot() {
        navigator.push(EumDestination.ProductList())
        navigator.push(EumDestination.ProductDetail("p1"))
        navigator.push(EumDestination.OrderComplete)

        navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home)

        assertEquals(MainNavigationTab.Home, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Home), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun continueShoppingAfterQuoteCompleteInSearchTabOpensHomeRoot() {
        navigator.push(EumDestination.ProductDetail("p1"))
        navigator.navigateToTab(MainNavigationTab.Search)
        navigator.push(EumDestination.SearchResults("없는 상품"))
        navigator.push(EumDestination.QuoteRequest())
        navigator.popWhileAndPush(EumDestination.QuoteComplete) { navKey -> navKey is EumDestination.QuoteRequest }

        navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home)

        assertEquals(MainNavigationTab.Home, navigationState.currentTab)
        assertEquals(listOf(EumDestination.Home), stackOf(MainNavigationTab.Home))
        assertEquals(
            listOf(EumDestination.Search, EumDestination.SearchResults("없는 상품")),
            stackOf(MainNavigationTab.Search),
        )
    }

    @Test
    fun popIfTopSecondCallDoesNotCloseQuoteForm() {
        navigator.push(EumDestination.QuoteRequest())
        navigator.push(EumDestination.QuoteProductPicker)

        val isFirstPopped = navigator.popIfTop(EumDestination.QuoteProductPicker)
        val isSecondPopped = navigator.popIfTop(EumDestination.QuoteProductPicker)

        assertTrue(isFirstPopped)
        assertFalse(isSecondPopped)
        assertEquals(listOf(EumDestination.Home, EumDestination.QuoteRequest()), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun popIfTopReturnsFalseWhenAnotherKeyIsOnTop() {
        navigator.push(EumDestination.QuoteRequest())

        val isPopped = navigator.popIfTop(EumDestination.QuoteProductPicker)

        assertFalse(isPopped)
        assertEquals(listOf(EumDestination.Home, EumDestination.QuoteRequest()), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun popIfTopNeverRemovesTabRoot() {
        assertFalse(navigator.popIfTop(EumDestination.Home))
        assertEquals(listOf(EumDestination.Home), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun quoteProductPickerReturnsSelectedIdToFormBelow() {
        val quoteProductSelection = EumQuoteProductSelection(mutableStateOf(null))
        navigator.navigateToTab(MainNavigationTab.Cart)
        navigator.push(EumDestination.QuoteRequest())
        navigator.push(EumDestination.QuoteProductPicker)

        if (navigator.popIfTop(EumDestination.QuoteProductPicker)) {
            quoteProductSelection.select("p2")
        }

        assertEquals(listOf(EumDestination.Cart, EumDestination.QuoteRequest()), stackOf(MainNavigationTab.Cart))
        assertEquals("p2", quoteProductSelection.pendingProductId)
        quoteProductSelection.consume()
        assertNull(quoteProductSelection.pendingProductId)
    }

    @Test
    fun replaceTopSwapsOverlayForMenuDestination() {
        navigator.push(EumDestination.Drawer)

        navigator.replaceTop(EumDestination.EventList)

        assertEquals(listOf(EumDestination.Home, EumDestination.EventList), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun replaceTopKeepsTabRoot() {
        navigator.replaceTop(EumDestination.EventList)

        assertEquals(listOf(EumDestination.Home, EumDestination.EventList), stackOf(MainNavigationTab.Home))
    }

    @Test
    fun completeLoginPopsLoginAndOpensPendingDestination() {
        navigator.navigateToTab(MainNavigationTab.MyPage)
        navigator.push(EumDestination.Login(pendingDestination = EumDestination.QuoteHistory))

        navigator.completeLogin()

        assertEquals(listOf(EumDestination.MyPage, EumDestination.QuoteHistory), stackOf(MainNavigationTab.MyPage))
    }

    @Test
    fun completeLoginWithoutPendingDestinationReturnsToPreviousScreen() {
        navigator.navigateToTab(MainNavigationTab.MyPage)
        navigator.push(EumDestination.Login())

        navigator.completeLogin()

        assertEquals(listOf(EumDestination.MyPage), stackOf(MainNavigationTab.MyPage))
    }

    @Test
    fun completeLoginDoesNothingWhenLoginIsNotOnTop() {
        navigator.push(EumDestination.ProductDetail("p1"))

        navigator.completeLogin()

        assertEquals(listOf(EumDestination.Home, EumDestination.ProductDetail("p1")), stackOf(MainNavigationTab.Home))
    }

    private fun stackOf(tab: MainNavigationTab): List<NavKey> = navigationState.backStacks.getValue(tab).toList()
}

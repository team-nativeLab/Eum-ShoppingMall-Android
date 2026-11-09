package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogScreenScaffold(
    selectedBottomItem: MainNavigationTab,
    onBottomItemSelected: (MainNavigationTab) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    cartItemCount: Int = 0,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = {
            CatalogBottomNavigation(selectedBottomItem, onBottomItemSelected, cartItemCount = cartItemCount)
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        ResponsiveContentContainer(Modifier.fillMaxSize().padding(contentPadding)) {
            content(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun CatalogScreenContent(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Column(modifier = modifier, content = content)
}

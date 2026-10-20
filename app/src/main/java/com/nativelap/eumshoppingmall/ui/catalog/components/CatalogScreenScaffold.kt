package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogScreenScaffold(
    selectedBottomItem: String,
    onBottomItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = {
            CatalogBottomNavigation(selectedBottomItem, onBottomItemSelected)
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        content(Modifier.fillMaxSize().padding(contentPadding))
    }
}

@Composable
fun CatalogScreenContent(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Column(modifier = modifier, content = content)
}

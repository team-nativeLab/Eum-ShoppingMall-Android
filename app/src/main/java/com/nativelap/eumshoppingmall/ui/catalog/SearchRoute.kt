package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

@Composable
fun CatalogSearchRoute(
    uiModel: CatalogSearchUiModel,
    selectedTab: MainNavigationTab,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onSearch: (query: String) -> Unit,
    onCancel: () -> Unit,
) {
    var query by rememberSaveable { mutableStateOf(uiModel.query) }
    var isRecentTermsCleared by rememberSaveable { mutableStateOf(false) }
    CatalogSearchScreen(
        uiModel = uiModel.copy(
            query = query,
            recentTerms = if (isRecentTermsCleared) emptyList() else uiModel.recentTerms,
        ),
        selectedBottomItem = selectedTab,
        onBottomItemSelected = onTabSelected,
        cartItemCount = cartItemCount,
        onEvent = { event ->
            when (event) {
                is CatalogSearchEvent.QueryChanged -> query = event.query
                CatalogSearchEvent.SearchSubmitted -> if (query.isNotBlank()) onSearch(query.trim())
                CatalogSearchEvent.Cancelled -> onCancel()
                CatalogSearchEvent.ClearRecentTerms -> isRecentTermsCleared = true
                is CatalogSearchEvent.RecentTermSelected -> onSearch(event.term)
                is CatalogSearchEvent.PopularTermSelected -> onSearch(event.term)
            }
        },
    )
}

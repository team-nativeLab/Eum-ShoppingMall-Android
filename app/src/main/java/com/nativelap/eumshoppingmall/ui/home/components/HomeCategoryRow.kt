package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.home.HomeCategoryUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

@Composable
fun HomeCategoryRow(
    categories: List<HomeCategoryUiModel>,
    onCategoryClick: (HomeCategoryUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        items(categories, key = { it.label }) { category ->
            HomeCategoryItem(category, onCategoryClick)
        }
    }
}

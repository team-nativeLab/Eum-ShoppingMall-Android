package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = EumSpacing.x20),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        // 목 데이터에서 id는 유일하다. 실데이터를 연결하면 id 유일성을 먼저 보장해야 한다(LazyRow 키 충돌 방지).
        items(categories, key = { it.id }) { category ->
            HomeCategoryItem(category, onCategoryClick)
        }
    }
}

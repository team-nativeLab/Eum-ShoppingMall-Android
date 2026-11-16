package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 장 제목과 그 아래 조항들을 묶는다. 장 제목이 없으면 조항만 같은 간격으로 쌓는다. */
@Composable
internal fun PolicyChapterGroup(
    chapterTitle: String?,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x24)) {
        if (chapterTitle != null) {
            Text(
                text = chapterTitle,
                modifier = Modifier.padding(top = EumSpacing.x8),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.eumColors.textPrimary,
            )
        }
        content()
    }
}

/** 조항 제목과 본문 단락을 보여준다. 표·절차처럼 단락 밖 요소는 content 슬롯에 둔다. */
@Composable
internal fun PolicyClauseArticle(
    title: String,
    paragraphs: List<String>,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = EumTypographyTokens.bodyLMedium,
                color = MaterialTheme.eumColors.textPrimary,
            )
        }
        if (paragraphs.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                paragraphs.forEach { paragraph ->
                    Text(
                        text = paragraph,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            }
        }
        content()
    }
}

package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

sealed interface TermsDialogScreenEvent {
    data object CloseClicked : TermsDialogScreenEvent
    data object AgreeClicked : TermsDialogScreenEvent
}

@Composable
fun TermsDialogScreen(
    onEvent: (TermsDialogScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_terms_title),
        onBackClick = { onEvent(TermsDialogScreenEvent.CloseClicked) },
        isCloseAction = true,
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_terms_agree),
        onBottomActionClick = { onEvent(TermsDialogScreenEvent.AgreeClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(vertical = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x24),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_terms_effective_date),
                style = EumTypographyTokens.captionMedium,
                color = MaterialTheme.eumColors.textDisabled,
            )
            PolicyChapterGroup(chapterTitle = stringResource(R.string.ui_audit_terms_chapter_one)) {
                PolicyClauseArticle(
                    title = stringResource(R.string.ui_audit_terms_article_one),
                    paragraphs = listOf(stringResource(R.string.ui_audit_terms_clause)),
                )
                PolicyClauseArticle(
                    title = stringResource(R.string.ui_audit_terms_article_two),
                    paragraphs = listOf(
                        stringResource(R.string.ui_audit_terms_clause),
                        stringResource(R.string.ui_audit_terms_clause_one),
                        stringResource(R.string.ui_audit_terms_clause_two),
                    ),
                )
                PolicyClauseArticle(
                    title = stringResource(R.string.ui_audit_terms_article_three),
                    paragraphs = listOf(stringResource(R.string.ui_audit_terms_clause)),
                )
                PolicyClauseArticle(
                    title = stringResource(R.string.ui_audit_terms_article_four),
                    paragraphs = listOf(stringResource(R.string.ui_audit_terms_clause)),
                )
            }
        }
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun TermsDialogScreenPreview() {
    EumShoppingMallTheme {
        TermsDialogScreen(onEvent = {})
    }
}

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
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
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
        showBottomDivider = true,
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_terms_agree),
        onBottomActionClick = { onEvent(TermsDialogScreenEvent.AgreeClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x40, bottom = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_terms_effective_date),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            Text(
                text = stringResource(R.string.ui_audit_terms_chapter_one),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.eumColors.textPrimary,
            )
            TermsArticle(
                titleRes = R.string.ui_audit_terms_article_one,
                paragraphs = listOf(R.string.ui_audit_terms_clause),
            )
            TermsArticle(
                titleRes = R.string.ui_audit_terms_article_two,
                paragraphs = listOf(
                    R.string.ui_audit_terms_clause,
                    R.string.ui_audit_terms_clause_one,
                    R.string.ui_audit_terms_clause_two,
                ),
            )
            TermsArticle(
                titleRes = R.string.ui_audit_terms_article_three,
                paragraphs = listOf(R.string.ui_audit_terms_clause),
            )
            TermsArticle(
                titleRes = R.string.ui_audit_terms_article_four,
                paragraphs = listOf(R.string.ui_audit_terms_clause),
            )
        }
    }
}

@Composable
private fun TermsArticle(
    titleRes: Int,
    paragraphs: List<Int>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.eumColors.textPrimary,
        )
        paragraphs.forEach { paragraphRes ->
            Text(
                text = stringResource(paragraphRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun TermsDialogScreenPreview() {
    EumShoppingMallTheme {
        TermsDialogScreen(onEvent = {})
    }
}

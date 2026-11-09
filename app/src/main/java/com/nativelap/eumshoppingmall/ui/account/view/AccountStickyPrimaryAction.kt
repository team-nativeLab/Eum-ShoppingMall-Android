package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 계정 화면 하단 고정 CTA. 기본 색은 Figma 검정 CTA(textPrimary)를 따른다. */
@Composable
internal fun AccountStickyPrimaryAction(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
) {
    Column(
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        HorizontalDivider(color = MaterialTheme.eumColors.line)
        Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
            AccountPrimaryButton(label, onClick, enabled, containerColor ?: MaterialTheme.eumColors.textPrimary)
        }
    }
}

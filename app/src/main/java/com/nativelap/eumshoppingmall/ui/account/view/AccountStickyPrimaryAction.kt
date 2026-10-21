package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

@Composable
internal fun AccountStickyPrimaryAction(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
    ) {
        AccountPrimaryButton(label, onClick, enabled)
    }
}

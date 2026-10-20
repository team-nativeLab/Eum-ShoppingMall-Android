package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutSectionHeader(
    titleResId: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(titleResId),
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surfaceSecondary)
            .padding(horizontal = EumSpacing.x20, vertical = 12.dp),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.eumColors.textPrimary,
    )
}

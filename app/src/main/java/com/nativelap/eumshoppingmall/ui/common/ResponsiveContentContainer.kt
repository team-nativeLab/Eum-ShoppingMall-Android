package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Centers mobile-first content on wider screens while preserving its measured phone width. */
@Composable
fun ResponsiveContentContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.widthIn(max = 600.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.TopCenter),
            content = content,
        )
    }
}

@Composable
fun ResponsiveWidthContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.widthIn(max = 600.dp)
                .fillMaxWidth()
                .align(Alignment.Center),
            content = content,
        )
    }
}

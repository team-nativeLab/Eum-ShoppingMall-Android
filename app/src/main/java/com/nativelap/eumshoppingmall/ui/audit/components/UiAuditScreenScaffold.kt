package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun UiAuditScreenScaffold(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCloseAction: Boolean = false,
    bottomActionLabel: String? = null,
    onBottomActionClick: () -> Unit = {},
    isBottomActionEnabled: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.eumColors.background,
        topBar = {
            Box(Modifier.fillMaxWidth().statusBarsPadding()) {
                Box(Modifier.widthIn(max = 600.dp).fillMaxWidth().align(Alignment.Center)) {
                    UiAuditTopBar(
                        title = title,
                        onBackClick = onBackClick,
                        isCloseAction = isCloseAction,
                    )
                }
            }
        },
        bottomBar = {
            if (bottomActionLabel != null) {
                // CTA 바는 Surface 배경과 상단 구분선을 항상 그린다.
                Surface(color = MaterialTheme.eumColors.surface) {
                    Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding()) {
                        HorizontalDivider(color = MaterialTheme.eumColors.line)
                        Box(
                            modifier = Modifier.widthIn(max = 600.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                        ) {
                            UiAuditPrimaryButton(
                                label = bottomActionLabel,
                                onClick = onBottomActionClick,
                                enabled = isBottomActionEnabled,
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
            Box(
                modifier = Modifier.widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .fillMaxSize()
                    .align(Alignment.TopCenter),
            ) {
                content(PaddingValues(horizontal = EumSpacing.pageGutter))
            }
        }
    }
}

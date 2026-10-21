package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthPasswordUpdatedScreen(onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.auth_password_reset_title), onBack = { onEvent(AuthScreenEvent.CloseClicked) }, isClose = true) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                AuthPrimaryAction(stringResource(R.string.account_login), true) { onEvent(AuthScreenEvent.RecoveryLoginClicked) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = EumSpacing.x20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12, Alignment.CenterVertically),
        ) {
            Icon(painterResource(R.drawable.ic_check), contentDescription = null, tint = MaterialTheme.eumColors.background, modifier = Modifier.size(64.dp).background(MaterialTheme.eumColors.success, androidx.compose.foundation.shape.CircleShape).padding(EumSpacing.x20).align(Alignment.CenterHorizontally))
            Text(stringResource(R.string.account_password_changed), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Text(stringResource(R.string.auth_password_updated_instruction), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary, textAlign = TextAlign.Center)
        }
    }
}

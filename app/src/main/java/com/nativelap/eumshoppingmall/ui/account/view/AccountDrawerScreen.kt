package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountDrawerScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountDrawerScreen(profile: AccountProfileUiModel, isLoggedIn: Boolean, onEvent: (AccountDrawerScreenEvent) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.eumColors.dim).clickable { onEvent(AccountDrawerScreenEvent.CloseClicked) })
        Scaffold(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.82f).widthIn(max = 360.dp),
            containerColor = MaterialTheme.eumColors.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).statusBarsPadding()) {
                // Figma 드로어는 닫기(X)를 왼쪽 위에 두고, 회사명은 그 아래에 크게 표시한다.
                Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(horizontal = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onEvent(AccountDrawerScreenEvent.CloseClicked) }) {
                        Icon(painterResource(R.drawable.ic_x), contentDescription = stringResource(R.string.common_close))
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { onEvent(AccountDrawerScreenEvent.NotificationClicked) }) {
                        Icon(painterResource(R.drawable.ic_bell), contentDescription = stringResource(R.string.notifications_title))
                    }
                }
                Text(stringResource(R.string.company_name), Modifier.padding(horizontal = EumSpacing.x20), style = MaterialTheme.typography.headlineSmall)
                Text(stringResource(R.string.company_tagline), Modifier.padding(horizontal = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                Column(
                    Modifier.weight(1f).padding(horizontal = EumSpacing.x20).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                ) {
                    Spacer(Modifier.padding(top = EumSpacing.x8))
                    Row(Modifier.fillMaxWidth().heightIn(min = 60.dp).background(MaterialTheme.eumColors.surface, MaterialTheme.shapes.large).border(1.dp, MaterialTheme.eumColors.line, MaterialTheme.shapes.large).clickable { onEvent(if (isLoggedIn) AccountDrawerScreenEvent.ProfileClicked else AccountDrawerScreenEvent.MenuClicked("login")) }.padding(horizontal = EumSpacing.x16), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (isLoggedIn) stringResource(R.string.account_drawer_member_name, profile.name) else stringResource(R.string.account_login), Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        if (isLoggedIn) {
                            Row(Modifier.clickable { onEvent(AccountDrawerScreenEvent.ProfileClicked) }.padding(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.account_profile), style = MaterialTheme.typography.labelSmall)
                                Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    listOf(
                        "events" to R.string.account_drawer_events,
                        "quotes" to R.string.account_drawer_custom_quote,
                        "support" to R.string.account_drawer_support,
                        "notices" to R.string.account_drawer_notices,
                        "company" to R.string.account_drawer_company,
                    ).forEach { (id, labelResource) ->
                        Text(stringResource(labelResource), Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable(role = Role.Button) { onEvent(AccountDrawerScreenEvent.MenuClicked(id)) }.padding(vertical = EumSpacing.x12), style = MaterialTheme.typography.titleMedium)
                    }
                }
                Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x24), verticalAlignment = Alignment.CenterVertically) {
                    Row(Modifier.heightIn(min = 48.dp).clickable { onEvent(AccountDrawerScreenEvent.SettingsClicked) }.padding(horizontal = EumSpacing.x4), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Icon(painterResource(R.drawable.ic_settings), null)
                        Text(stringResource(R.string.account_settings))
                    }
                    if (isLoggedIn) {
                        Row(Modifier.heightIn(min = 48.dp).clickable { onEvent(AccountDrawerScreenEvent.LogoutClicked) }.padding(horizontal = EumSpacing.x4), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            Icon(painterResource(R.drawable.ic_log_out), null)
                            Text(stringResource(R.string.account_logout))
                        }
                    }
                }
            }
        }
    }
}

package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountMenuRowUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountShortcutUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogBottomNavigation
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun MyPageScreen(uiModel: MyPageUiModel, onEvent: (MyPageScreenEvent) -> Unit) {
    Scaffold(
        topBar = {
            CatalogTopBar(
                title = stringResource(R.string.common_my_page),
                actions = listOf(CatalogTopBarAction("drawer", R.drawable.ic_menu, stringResource(R.string.account_drawer_open))),
                onActionClick = { onEvent(MyPageScreenEvent.DrawerClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        bottomBar = {
            CatalogBottomNavigation(
                selectedItem = "my_page",
                onItemSelected = { onEvent(MyPageScreenEvent.BottomNavigationClicked(it)) },
            )
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = EumSpacing.x20,
                vertical = EumSpacing.x8,
            ),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            if (uiModel.isLoggedIn) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onEvent(MyPageScreenEvent.ProfileClicked) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_user_round),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp).background(MaterialTheme.eumColors.backgroundSubtle, CircleShape).padding(18.dp),
                            tint = MaterialTheme.eumColors.textTertiary,
                        )
                        Column(Modifier.weight(1f).padding(start = EumSpacing.x16)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                                Text(uiModel.profile.name, style = MaterialTheme.typography.titleLarge)
                                if (uiModel.profile.isBusinessMember) {
                                    Text(stringResource(R.string.account_business_badge), modifier = Modifier.border(1.dp, MaterialTheme.eumColors.lineStrong, androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x4)).padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x2), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            Text(stringResource(R.string.account_greeting), color = MaterialTheme.eumColors.textSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                        Icon(painterResource(R.drawable.ic_chevron_right), null, tint = MaterialTheme.eumColors.textTertiary)
                    }
                }
                item { AccountShortcutRow(uiModel.shortcuts, onEvent) }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.account_order_status_section), style = MaterialTheme.typography.titleMedium)
                        Row(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x12)).background(MaterialTheme.eumColors.surfaceSecondary, androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x12)).padding(vertical = EumSpacing.x12), horizontalArrangement = Arrangement.SpaceEvenly) {
                            uiModel.orderStatuses.forEach { status ->
                                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(status.count, style = MaterialTheme.typography.titleMedium)
                                    Text(status.label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
                item {
                    AccountMenuGroup(
                        stringResource(R.string.account_my_info_section),
                        listOf(
                            AccountMenuRowUiModel("profile", stringResource(R.string.account_profile_manage)),
                            AccountMenuRowUiModel("addresses", stringResource(R.string.account_address_manage)),
                        ),
                        onEvent,
                    )
                }
                item { AccountMenuGroup(stringResource(R.string.account_order_quote_section), uiModel.orderRows, onEvent) }
                item { AccountMenuGroup(stringResource(R.string.account_inquiries_support), uiModel.supportRows, onEvent) }
                item {
                    Text(stringResource(R.string.account_settings), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable { onEvent(MyPageScreenEvent.SettingsClicked) }.padding(vertical = EumSpacing.x12), style = MaterialTheme.typography.bodySmall)
                }
            } else {
                item {
                    Column(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x12)).padding(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        Text(stringResource(R.string.account_login_benefits_message), style = MaterialTheme.typography.bodyMedium)
                        AccountPrimaryButton(stringResource(R.string.account_login), onClick = { onEvent(MyPageScreenEvent.LoginClicked) })
                    }
                }
                item { Text(stringResource(R.string.home_recent_products), style = MaterialTheme.typography.titleMedium) }
                items(uiModel.recentProducts) { product -> Text(product, modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp).border(1.dp, MaterialTheme.eumColors.line, androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x8)).padding(EumSpacing.x12), style = MaterialTheme.typography.bodySmall) }
                item { AccountMenuGroup(stringResource(R.string.account_drawer_support), uiModel.supportRows, onEvent) }
                item { Text(stringResource(R.string.account_settings), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable { onEvent(MyPageScreenEvent.SettingsClicked) }.padding(vertical = EumSpacing.x12)) }
            }
        }
    }
}

@Composable
private fun AccountShortcutRow(shortcuts: List<AccountShortcutUiModel>, onEvent: (MyPageScreenEvent) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        shortcuts.forEach { shortcut ->
            Column(
                modifier = Modifier.weight(1f).heightIn(min = 64.dp).clickable(role = Role.Button) { onEvent(MyPageScreenEvent.ShortcutClicked(shortcut.id)) }.padding(vertical = EumSpacing.x8),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            ) {
                val iconResource = when (shortcut.id) {
                    "orders" -> R.drawable.ic_clipboard_list
                    "favorites" -> R.drawable.ic_heart
                    "recent" -> R.drawable.ic_clock
                    "coupons" -> R.drawable.ic_ticket
                    else -> R.drawable.ic_coins
                }
                Icon(painterResource(iconResource), contentDescription = null, modifier = Modifier.size(24.dp), tint = MaterialTheme.eumColors.textPrimary)
                Text(shortcut.label, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (!shortcut.countLabel.isNullOrBlank()) Text(shortcut.countLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
            }
        }
    }
}

@Composable
private fun AccountMenuGroup(title: String, rows: List<AccountMenuRowUiModel>, onEvent: (MyPageScreenEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Column(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x12)).padding(horizontal = EumSpacing.x16)) {
            rows.forEachIndexed { index, row ->
                Row(
                    Modifier.fillMaxWidth().heightIn(min = 52.dp).clickable(role = Role.Button) { onEvent(MyPageScreenEvent.MenuRowClicked(row.id)) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val iconResource = when (row.id) {
                        "profile" -> R.drawable.ic_user_round
                        "addresses" -> R.drawable.ic_map_pin
                        "returns" -> R.drawable.ic_rotate_ccw
                        "quotes" -> R.drawable.ic_message_square_text
                        "inquiries" -> R.drawable.ic_mail
                        "customer" -> R.drawable.ic_headphones
                        else -> R.drawable.ic_megaphone
                    }
                    Icon(painterResource(iconResource), contentDescription = null, tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
                    Text(row.label, Modifier.weight(1f).padding(start = EumSpacing.x12), style = MaterialTheme.typography.bodyMedium)
                    Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = MaterialTheme.eumColors.textTertiary, modifier = Modifier.size(20.dp))
                }
                if (index != rows.lastIndex) androidx.compose.material3.HorizontalDivider(color = MaterialTheme.eumColors.line)
            }
        }
    }
}

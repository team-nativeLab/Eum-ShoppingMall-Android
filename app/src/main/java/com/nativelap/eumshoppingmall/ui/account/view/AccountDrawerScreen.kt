package com.nativelap.eumshoppingmall.ui.account.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountDrawerScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// Figma A-12 패널 그림자(8px 0 32px -4px rgba(17,17,17,.16))를 Modifier.shadow로 근사한다. 방향·퍼짐은 Compose가 정한다.
private val DrawerPanelShadowColor = Color(0x29111111)
private val DrawerPanelMaxWidth = 320.dp
private val DrawerPanelTopPadding = 52.dp
private val DrawerPanelBottomPadding = 34.dp

@Composable
fun AccountDrawerScreen(profile: AccountProfileUiModel, isLoggedIn: Boolean, onEvent: (AccountDrawerScreenEvent) -> Unit) {
    // Figma 상·하 패딩 52·34는 시스템 바를 포함한 값으로 보고, 시스템 바가 더 크면 그 높이를 쓴다.
    val panelTopPadding = maxOf(DrawerPanelTopPadding, WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    val panelBottomPadding = maxOf(DrawerPanelBottomPadding, WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    BoxWithConstraints(Modifier.fillMaxSize()) {
        // fillMaxWidth 뒤의 widthIn(max)은 고정된 제약을 못 줄이므로 패널 폭을 직접 계산한다.
        val panelWidth = minOf(maxWidth * 0.82f, DrawerPanelMaxWidth)
        Box(Modifier.fillMaxSize().background(MaterialTheme.eumColors.dim).clickable { onEvent(AccountDrawerScreenEvent.CloseClicked) })
        Scaffold(
            modifier = Modifier.fillMaxHeight().width(panelWidth)
                .shadow(16.dp, RectangleShape, clip = false, ambientColor = DrawerPanelShadowColor, spotColor = DrawerPanelShadowColor),
            containerColor = MaterialTheme.eumColors.surface,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(top = panelTopPadding, bottom = panelBottomPadding)) {
                // Figma 드로어는 닫기(X)를 왼쪽 위에 두고, 회사명은 그 아래에 크게 표시한다.
                // 닫기 아이콘 24dp는 44dp 영역의 가운데에 놓이도록 48dp 터치 영역 기준 왼쪽 여백을 6dp로 둔다.
                Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(start = 6.dp, end = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onEvent(AccountDrawerScreenEvent.CloseClicked) }) {
                        Icon(painterResource(R.drawable.ic_x), contentDescription = stringResource(R.string.common_close), modifier = Modifier.size(24.dp))
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = { onEvent(AccountDrawerScreenEvent.NotificationClicked) }) {
                        Icon(painterResource(R.drawable.ic_bell), contentDescription = stringResource(R.string.notifications_title))
                    }
                }
                Text(stringResource(R.string.company_name), Modifier.padding(horizontal = EumSpacing.x20), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.eumColors.textPrimary)

                Spacer(Modifier.height(EumSpacing.x4))

                Text(stringResource(R.string.company_tagline), Modifier.padding(horizontal = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)

                Spacer(Modifier.height(EumSpacing.x24))

                Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20)
                            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.x12))
                            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
                            .clip(RoundedCornerShape(EumRadius.x12))
                            .clickable(role = Role.Button) { onEvent(if (isLoggedIn) AccountDrawerScreenEvent.ProfileClicked else AccountDrawerScreenEvent.MenuClicked("login")) }
                            .padding(EumSpacing.x16),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            if (isLoggedIn) stringResource(R.string.account_drawer_member_name, profile.name) else stringResource(R.string.account_login),
                            Modifier.weight(1f).padding(end = EumSpacing.x8),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                        if (isLoggedIn) {
                            Text(stringResource(R.string.account_profile), style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textSecondary)

                            Spacer(Modifier.width(EumSpacing.x2))

                            Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.eumColors.textSecondary)
                        }
                    }

                    Spacer(Modifier.height(EumSpacing.x8))

                    listOf(
                        "events" to R.string.account_drawer_events,
                        "quotes" to R.string.account_drawer_custom_quote,
                        "support" to R.string.account_drawer_support,
                        "notices" to R.string.account_drawer_notices,
                        "company" to R.string.account_drawer_company,
                    ).forEach { (id, labelResource) ->
                        Box(
                            Modifier.fillMaxWidth().heightIn(min = 52.dp).clickable(role = Role.Button) { onEvent(AccountDrawerScreenEvent.MenuClicked(id)) }.padding(horizontal = EumSpacing.x20),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Text(stringResource(labelResource), style = EumTypographyTokens.bodyLMedium, color = MaterialTheme.eumColors.textPrimary)
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.eumColors.line)
                Row(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x8), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                    DrawerFooterButton(R.drawable.ic_settings, stringResource(R.string.account_settings)) { onEvent(AccountDrawerScreenEvent.SettingsClicked) }
                    if (isLoggedIn) {
                        DrawerFooterButton(R.drawable.ic_log_out, stringResource(R.string.account_logout)) { onEvent(AccountDrawerScreenEvent.LogoutClicked) }
                    }
                }
            }
        }
    }
}

// 푸터 버튼: 높이 56, 좌우 12, 아이콘 20 + 간격 6 + 14/20 Medium textSecondary(Figma A-12).
@Composable
private fun DrawerFooterButton(
    @DrawableRes iconResId: Int,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        Modifier.heightIn(min = 56.dp).clickable(role = Role.Button, onClick = onClick).padding(horizontal = EumSpacing.x12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(painterResource(iconResId), contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.eumColors.textSecondary)
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
    }
}

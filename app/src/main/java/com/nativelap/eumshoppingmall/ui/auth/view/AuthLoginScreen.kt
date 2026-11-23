package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthLoginUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumCheckboxColors
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 간편로그인 A/B는 출시 전 임시 항목이라 숨긴다. 이벤트·Route 분기·문자열은 그대로 두어 다시 켤 수 있게 한다.
private const val SHOW_TEMPORARY_SOCIAL_LOGIN = false

@Composable
fun AuthLoginScreen(uiModel: AuthLoginUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_login), onBack = { onEvent(AuthScreenEvent.CloseClicked) }, isClose = true) },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
        ) {
            Spacer(Modifier.height(EumSpacing.x24))

            Text(stringResource(R.string.auth_brand_title), style = MaterialTheme.typography.headlineLarge)

            Spacer(Modifier.height(EumSpacing.x32))

            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                AuthTextField(
                    label = stringResource(R.string.auth_email_label),
                    value = uiModel.email,
                    onValueChange = { onEvent(AuthScreenEvent.LoginEmailChanged(it)) },
                    placeholder = stringResource(R.string.auth_email_placeholder),
                    keyboardType = KeyboardType.Email,
                )
                AuthPasswordField(
                    label = stringResource(R.string.auth_password_label),
                    value = uiModel.password,
                    isVisible = uiModel.isPasswordVisible,
                    onValueChange = { onEvent(AuthScreenEvent.LoginPasswordChanged(it)) },
                    onVisibilityChange = { onEvent(AuthScreenEvent.LoginPasswordVisibilityChanged) },
                    errorText = when {
                        uiModel.isServiceUnavailable -> stringResource(R.string.auth_login_unavailable)
                        uiModel.isCredentialsInvalid -> stringResource(R.string.auth_login_mismatch, uiModel.failedAttemptCount)
                        else -> null
                    },
                )
                Row(
                    Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable { onEvent(AuthScreenEvent.RememberLoginChanged(!uiModel.isRemembered)) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(checked = uiModel.isRemembered, colors = eumCheckboxColors(), onCheckedChange = { onEvent(AuthScreenEvent.RememberLoginChanged(it)) })
                    Text(stringResource(R.string.account_keep_signed_in), style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(EumSpacing.x24))

            AuthPrimaryAction(stringResource(R.string.account_login), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.LoginSubmitted) }
            Column(Modifier.padding(top = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                AuthHelpLinks(onEvent)
                if (SHOW_TEMPORARY_SOCIAL_LOGIN) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.eumColors.line)
                        Text(stringResource(R.string.common_or), style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
                        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.eumColors.line)
                    }
                    AuthSocialButton(stringResource(R.string.auth_social_login_a)) { onEvent(AuthScreenEvent.SocialLoginClicked("provider_a")) }
                    AuthSocialButton(stringResource(R.string.auth_social_login_b)) { onEvent(AuthScreenEvent.SocialLoginClicked("provider_b")) }
                }
            }

            Spacer(Modifier.height(EumSpacing.x24))
        }
    }
}

// 링크와 구분선 사이 간격은 12dp다. 글꼴이 커져 한 줄에 들어가지 않으면 줄바꿈한다.
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AuthHelpLinks(onEvent: (AuthScreenEvent) -> Unit) {
    FlowRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12, Alignment.CenterHorizontally),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        AuthHelpLink(stringResource(R.string.account_find_email), "find_email", onEvent)
        AuthHelpDivider()
        AuthHelpLink(stringResource(R.string.account_find_password), "find_password", onEvent)
        AuthHelpDivider()
        AuthHelpLink(stringResource(R.string.account_sign_up), "signup", onEvent, isEmphasized = true)
    }
}

@Composable
private fun AuthHelpLink(label: String, id: String, onEvent: (AuthScreenEvent) -> Unit, isEmphasized: Boolean = false) {
    Box(
        Modifier.clickable { onEvent(AuthScreenEvent.LoginHelpClicked(id)) }.heightIn(min = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = if (isEmphasized) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodySmall,
            color = if (isEmphasized) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textSecondary,
        )
    }
}

@Composable
private fun AuthHelpDivider() {
    Box(Modifier.width(1.dp).height(12.dp).background(MaterialTheme.eumColors.lineStrong))
}

@Composable
private fun AuthSocialButton(label: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
        shape = RoundedCornerShape(EumRadius.x8),
        border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

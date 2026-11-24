package com.nativelap.eumshoppingmall.navigation

import android.content.pm.PackageManager
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.view.AccountSettingsRoute
import com.nativelap.eumshoppingmall.ui.policy.PolicyUnavailableRoute
import com.nativelap.eumshoppingmall.ui.policy.UsageGuideRoute
import com.nativelap.eumshoppingmall.ui.support.NotificationRoute

/** 이용약관(A-36)·개인정보처리방침(A-37)·이용안내(A-38). 공개 화면이라 로그인 게이트를 거치지 않는다. */
internal fun EntryProviderScope<NavKey>.addPolicyEntries(context: EumEntryContext) {
    val navigator = context.navigator
    entry<EumDestination.Terms> {
        PolicyUnavailableRoute(
            titleRes = R.string.policy_terms_title,
            noticeRes = R.string.policy_terms_unavailable,
            onBackClick = { navigator.goBack() },
        )
    }
    entry<EumDestination.PrivacyPolicy> {
        PolicyUnavailableRoute(
            titleRes = R.string.policy_privacy_title,
            noticeRes = R.string.policy_privacy_unavailable,
            onBackClick = { navigator.goBack() },
        )
    }
    entry<EumDestination.UsageGuide> {
        UsageGuideRoute(onBackClick = { navigator.goBack() })
    }
    // 알림(A-32)과 설정(A-35)은 사이드바에서만 들어온다. 둘 다 서버 데이터·저장 경로가 없어 준비 중 상태로 연다.
    entry<EumDestination.Notifications> {
        NotificationRoute(onBackClick = { navigator.goBack() })
    }
    entry<EumDestination.AccountSettings> {
        val androidContext = LocalContext.current
        val appVersionLabel = remember(androidContext) {
            try {
                androidContext.packageManager.getPackageInfo(androidContext.packageName, 0).versionName.orEmpty()
            } catch (exception: PackageManager.NameNotFoundException) {
                ""
            }
        }
        AccountSettingsRoute(
            isLoggedIn = context.loginSession.isLoggedIn,
            appVersionLabel = appVersionLabel,
            onBackClick = { navigator.goBack() },
            onLoginClick = { navigator.push(EumDestination.Login()) },
            onPolicyClick = { policyId ->
                val destination = when (policyId) {
                    "terms" -> EumDestination.Terms
                    "privacy" -> EumDestination.PrivacyPolicy
                    "guide" -> EumDestination.UsageGuide
                    else -> null
                }
                if (destination != null) navigator.push(destination)
                destination != null
            },
            onUnavailableAction = context.onUnavailableAction,
        )
    }
}

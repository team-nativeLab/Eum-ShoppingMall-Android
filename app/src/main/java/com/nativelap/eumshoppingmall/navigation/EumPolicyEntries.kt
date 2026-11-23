package com.nativelap.eumshoppingmall.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.policy.PolicyUnavailableRoute
import com.nativelap.eumshoppingmall.ui.policy.UsageGuideRoute

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
}

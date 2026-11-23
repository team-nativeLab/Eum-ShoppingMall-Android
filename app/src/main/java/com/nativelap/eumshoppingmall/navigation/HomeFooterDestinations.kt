package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.home.HomeFooterLink

/** 홈 푸터 링크를 목적지로 바꾼다. 웹 푸터의 이용약관·개인정보처리방침·이용안내·이벤트·고객센터 순서와 맞춘다. */
fun destinationForHomeFooterLink(link: HomeFooterLink): EumDestination = when (link) {
    HomeFooterLink.Terms -> EumDestination.Terms
    HomeFooterLink.PrivacyPolicy -> EumDestination.PrivacyPolicy
    HomeFooterLink.UsageGuide -> EumDestination.UsageGuide
    HomeFooterLink.Events -> EumDestination.EventList
    HomeFooterLink.CustomerSupport -> EumDestination.CustomerSupport
}

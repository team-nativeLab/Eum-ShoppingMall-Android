package com.nativelap.eumshoppingmall.navigation

import androidx.navigation3.runtime.NavKey

/** 신청 제출 뒤 뒤로가기로 폼·주문 선택에 다시 들어가지 않도록 걷어 낼 화면인지 판별한다. */
internal fun isClaimSubmitCleanupTarget(navKey: NavKey): Boolean = navKey is EumDestination.ClaimRequest || navKey == EumDestination.ClaimOrderSelection

/** 신청 내역으로 갈 때 내역 사이에 쌓인 신청 화면(상세·철회·반려·완료·이전 내역)을 걷어 낼지 판별한다. */
internal fun isClaimHistoryCleanupTarget(navKey: NavKey): Boolean = navKey is EumDestination.ClaimDetail ||
    navKey is EumDestination.ClaimRejection ||
    navKey is EumDestination.ClaimWithdrawal ||
    navKey is EumDestination.ClaimResult ||
    navKey == EumDestination.ClaimHistory

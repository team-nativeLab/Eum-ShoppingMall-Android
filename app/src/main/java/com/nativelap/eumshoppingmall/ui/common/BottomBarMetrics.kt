package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.ui.unit.dp

/** 하단 바 높이의 단일 정의. 바 구현과 전역 스낵바 위치 계산이 같은 값을 참조한다. */
object EumBottomBarMetrics {
    /** 하단 탭바 최소 높이(내비게이션 바 inset 제외). */
    val TabBarMinHeight = 64.dp

    /** 장바구니 주문 바 최소 높이. CTA 56dp + 세로 패딩 12dp x 2 = 80dp. */
    val CartOrderBarMinHeight = 80.dp

    /** 인증 폼 하단 CTA 바 최소 높이. 구분선 1dp + 세로 패딩 12dp x 2 + CTA 56dp = 81dp. */
    val AuthCtaBarMinHeight = 81.dp
}

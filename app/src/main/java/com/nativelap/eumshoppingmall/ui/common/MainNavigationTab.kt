package com.nativelap.eumshoppingmall.ui.common

/**
 * 하단 탭(Figma IA 01 IA & Flow 기준 홈·카테고리·검색·장바구니·마이) 식별자.
 *
 * 홈 하단 바, 카탈로그 하단 바, 장바구니·마이 화면 이벤트가 같은 값을 쓰도록 문자열 ID 대신 이 enum 하나로 통일한다.
 * 선언 순서는 탭 표시 순서이며, 내비게이션 상태가 ordinal을 탭 index로 저장하므로 순서를 바꾸면 복원 index도 바뀐다.
 */
enum class MainNavigationTab {
    Home,
    Categories,
    Search,
    Cart,
    MyPage,
}

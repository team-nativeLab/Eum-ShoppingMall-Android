package com.nativelap.eumshoppingmall.ui.auth.view

// 장바구니 담기에서 온 로그인 안내를 띄울지 정한다. 안내 문구가 있고 아직 띄우지 않았을 때만 true다.
fun shouldShowLoginNotice(notice: String?, alreadyShown: Boolean): Boolean = notice != null && !alreadyShown

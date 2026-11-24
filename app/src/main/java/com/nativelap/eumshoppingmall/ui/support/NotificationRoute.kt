package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable

/**
 * 알림(A-32). 서버 연동 전이라 알림 데이터가 없다. 거짓 알림을 보여 주지 않고 빈 상태 안내만 보인다.
 * 읽음 처리·항목 열기는 데이터가 생기기 전까지 동작할 대상이 없다.
 */
@Composable
fun NotificationRoute(onBackClick: () -> Unit) {
    NotificationScreen(
        groups = emptyList(),
        unreadCount = 0,
        onEvent = { event ->
            when (event) {
                NotificationScreenEvent.BackClicked -> onBackClick()

                NotificationScreenEvent.MarkAllReadClicked,
                is NotificationScreenEvent.NotificationClicked,
                -> Unit
            }
        },
    )
}

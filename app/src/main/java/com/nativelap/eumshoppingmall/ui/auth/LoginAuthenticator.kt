package com.nativelap.eumshoppingmall.ui.auth

/** 로그인 인증 호출 결과. 실패 사유는 화면 안내 문구를 고르는 데만 쓴다. */
enum class LoginAuthResult {
    Success,
    InvalidCredentials,
    ServiceUnavailable,
}

/**
 * 이메일·비밀번호 인증을 수행한다.
 *
 * 서버 연동 전까지 빌드 타입별 구현을 [createLoginAuthenticator]가 고른다. 비밀번호는 호출한 쪽이 직후 소거하므로
 * 구현은 보관하거나 로그로 남기지 않는다.
 */
interface LoginAuthenticator {
    suspend fun authenticate(email: String, password: CharArray): LoginAuthResult
}

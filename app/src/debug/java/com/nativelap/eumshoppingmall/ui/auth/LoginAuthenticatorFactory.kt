package com.nativelap.eumshoppingmall.ui.auth

/** debug 빌드는 화면 흐름 확인용 mock 로그인을 쓴다. */
fun createLoginAuthenticator(): LoginAuthenticator = MockLoginAuthenticator

/**
 * 실제 인증이 아니다. 이메일 형식이 맞고 비밀번호가 비어 있지 않으면 성공하는 결정적 규칙이며 release 소스셋에는 없다.
 */
internal object MockLoginAuthenticator : LoginAuthenticator {
    private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    override suspend fun authenticate(email: String, password: CharArray): LoginAuthResult = if (emailPattern.matches(email.trim()) && password.isNotEmpty()) {
        LoginAuthResult.Success
    } else {
        LoginAuthResult.InvalidCredentials
    }
}

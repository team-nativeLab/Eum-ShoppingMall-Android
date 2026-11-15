package com.nativelap.eumshoppingmall.ui.auth

/** release 빌드는 인증 서비스가 연결되기 전까지 로그인을 허용하지 않는다. */
fun createLoginAuthenticator(): LoginAuthenticator = UnavailableLoginAuthenticator

internal object UnavailableLoginAuthenticator : LoginAuthenticator {
    override suspend fun authenticate(email: String, password: CharArray): LoginAuthResult = LoginAuthResult.ServiceUnavailable
}

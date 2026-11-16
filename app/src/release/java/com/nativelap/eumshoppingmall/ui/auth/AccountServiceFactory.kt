package com.nativelap.eumshoppingmall.ui.auth

/** release 빌드는 계정 서비스가 연결되기 전까지 가입·찾기를 허용하지 않는다. */
fun createAccountService(): AccountService = UnavailableAccountService

internal object UnavailableAccountService : AccountService {
    override suspend fun checkEmailAvailable(email: String): AccountServiceResult = AccountServiceResult.ServiceUnavailable

    override suspend fun confirmPhoneCode(phoneNumber: String, code: CharArray): AccountServiceResult = AccountServiceResult.ServiceUnavailable

    override suspend fun register(email: String, password: CharArray): AccountServiceResult = AccountServiceResult.ServiceUnavailable

    override suspend fun findMaskedEmail(name: String, phoneNumber: String): AccountLookupResult = AccountLookupResult.ServiceUnavailable
}

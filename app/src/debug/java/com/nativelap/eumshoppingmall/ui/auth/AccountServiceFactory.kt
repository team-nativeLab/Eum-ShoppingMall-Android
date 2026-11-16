package com.nativelap.eumshoppingmall.ui.auth

/** debug 빌드는 화면 흐름 확인용 mock 계정 서비스를 쓴다. */
fun createAccountService(): AccountService = MockAccountService

/**
 * 실제 서버 호출이 아니다. 형식만 맞으면 성공하는 결정적 규칙이며 release 소스셋에는 없다.
 */
internal object MockAccountService : AccountService {
    private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private const val VERIFICATION_CODE_LENGTH = 6

    override suspend fun checkEmailAvailable(email: String): AccountServiceResult = if (emailPattern.matches(email.trim())) {
        AccountServiceResult.Success
    } else {
        AccountServiceResult.Rejected
    }

    override suspend fun confirmPhoneCode(phoneNumber: String, code: CharArray): AccountServiceResult = if (code.size == VERIFICATION_CODE_LENGTH && code.all(Char::isDigit)) {
        AccountServiceResult.Success
    } else {
        AccountServiceResult.Rejected
    }

    override suspend fun register(email: String, password: CharArray): AccountServiceResult = if (emailPattern.matches(email.trim()) && password.isNotEmpty()) {
        AccountServiceResult.Success
    } else {
        AccountServiceResult.Rejected
    }

    // 샘플 조회라 입력과 무관하게 고정 마스킹 값을 돌려준다.
    override suspend fun findMaskedEmail(name: String, phoneNumber: String): AccountLookupResult = if (name.isNotBlank() && phoneNumber.isNotBlank()) {
        AccountLookupResult.Found("sa***@example.com")
    } else {
        AccountLookupResult.NotFound
    }
}

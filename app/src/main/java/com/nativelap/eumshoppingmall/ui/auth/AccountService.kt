package com.nativelap.eumshoppingmall.ui.auth

/** 계정 서비스 호출 결과. 실패 사유는 화면 안내 문구를 고르는 데만 쓴다. */
enum class AccountServiceResult {
    Success,
    Rejected,
    ServiceUnavailable,
}

/** 이메일 찾기 조회 결과. 마스킹된 이메일만 돌려주고 원문 계정 정보는 화면에 넘기지 않는다. */
sealed interface AccountLookupResult {
    data class Found(val maskedEmail: String) : AccountLookupResult

    data object NotFound : AccountLookupResult

    data object ServiceUnavailable : AccountLookupResult
}

/**
 * 회원가입·이메일 찾기처럼 계정 서버가 맡을 호출을 모은 경계.
 *
 * 서버 연동 전까지 빌드 타입별 구현을 [createAccountService]가 고른다. 비밀번호는 호출한 쪽이 직후 소거하므로
 * 구현은 보관하거나 로그로 남기지 않는다.
 */
interface AccountService {
    suspend fun checkEmailAvailable(email: String): AccountServiceResult

    suspend fun confirmPhoneCode(phoneNumber: String, code: CharArray): AccountServiceResult

    suspend fun register(email: String, password: CharArray): AccountServiceResult

    suspend fun findMaskedEmail(name: String, phoneNumber: String): AccountLookupResult
}

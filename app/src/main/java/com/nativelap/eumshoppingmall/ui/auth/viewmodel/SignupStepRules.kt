package com.nativelap.eumshoppingmall.ui.auth.viewmodel

/** 가입 입력 현황. 단계 검증과 최종 제출 검증이 같은 값을 읽는다. */
data class SignupInputs(
    val email: String,
    val isEmailAvailable: Boolean,
    val name: String,
    val password: CharArray,
    val passwordConfirmation: CharArray,
    val isPhoneVerified: Boolean,
    val isTermsChecked: Boolean,
    val isPrivacyChecked: Boolean,
)

/** 회원가입 단계별 검증 규칙. 화면 상태와 무관한 순수 함수라 경계값을 단위 테스트로 고정한다. */
object SignupStepRules {
    const val MIN_PASSWORD_LENGTH = 8

    /** 가입 단계 수. 1 이메일·이름, 2 비밀번호, 3 휴대폰 인증, 4 약관 동의와 가입. */
    const val STEP_COUNT = 4
    const val LAST_STEP = STEP_COUNT - 1

    /** 8자 이상이고 영문자와 숫자를 모두 포함해야 한다. */
    fun isPasswordAcceptable(candidate: CharArray): Boolean = candidate.size >= MIN_PASSWORD_LENGTH &&
        candidate.any(Char::isLetter) &&
        candidate.any(Char::isDigit)

    /** [step](0부터)의 입력이 검증을 통과해 다음으로 갈 수 있는지. 마지막 단계는 필수 약관 동의 여부다. */
    fun isStepValid(step: Int, inputs: SignupInputs): Boolean = when (step) {
        0 -> inputs.isEmailAvailable && inputs.email.isNotBlank() && inputs.name.isNotBlank()
        1 -> isPasswordAcceptable(inputs.password) && inputs.password.contentEquals(inputs.passwordConfirmation)
        2 -> inputs.isPhoneVerified
        else -> inputs.isTermsChecked && inputs.isPrivacyChecked
    }

    /** 모든 단계가 통과해야 가입할 수 있다. */
    fun isReadyToSubmit(inputs: SignupInputs): Boolean = (0..LAST_STEP).all { step -> isStepValid(step, inputs) }
}

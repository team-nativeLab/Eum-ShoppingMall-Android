package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountRecoveryMode
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountRecoveryUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalCompletedUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalGuideUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalReasonUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthLoginUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthMemberType
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPasswordChangeUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPasswordResetUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPhoneVerificationUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewVerifiedPhone = AuthPhoneVerificationUiModel(
    phoneNumber = "010-0000-0000",
    verificationCode = "000000".toCharArray(),
    isCodeRequested = true,
    isVerified = true,
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "로그인 기본")
@Composable
private fun AuthLoginPreview() {
    EumShoppingMallTheme { AuthLoginScreen(AuthLoginUiModel(email = "example@email.com"), {}) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.2f, name = "로그인 오류 · 좁은 화면")
@Composable
private fun AuthLoginErrorPreview() {
    EumShoppingMallTheme {
        AuthLoginScreen(AuthLoginUiModel(email = "example@email.com", password = "invalid".toCharArray(), isCredentialsInvalid = true, failedAttemptCount = 1), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "개인 회원가입")
@Composable
private fun AuthSignupPersonalPreview() {
    EumShoppingMallTheme {
        AuthSignupScreen(
            AuthSignupUiModel(email = "example@email.com", name = "홍길동", phone = previewVerifiedPhone),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.1f, name = "사업자 회원가입 · 좁은 화면")
@Composable
private fun AuthSignupBusinessPreview() {
    EumShoppingMallTheme {
        AuthSignupScreen(
            AuthSignupUiModel(
                memberType = AuthMemberType.BUSINESS,
                email = "example@email.com",
                isEmailAvailable = true,
                name = "홍길동",
                phone = previewVerifiedPhone,
                businessRegistrationNumber = "000-00-00000",
                companyName = "회사명",
                representativeName = "홍길동",
                industry = "건설업 / 안전용품",
                taxEmail = "example@email.com",
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "이메일 찾기")
@Composable
private fun AccountRecoveryEmailPreview() {
    EumShoppingMallTheme {
        AccountRecoveryScreen(AccountRecoveryUiModel(name = "홍길동", phone = previewVerifiedPhone), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "이메일 찾기 결과")
@Composable
private fun AccountRecoveryFoundPreview() {
    EumShoppingMallTheme {
        AccountRecoveryScreen(
            AccountRecoveryUiModel(isResultVisible = true, name = "홍길동", maskedEmail = "ex***@email.com", joinedAt = "0000.00.00"),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "이메일 찾기 결과 없음")
@Composable
private fun AccountRecoveryNotFoundPreview() {
    EumShoppingMallTheme {
        AccountRecoveryScreen(AccountRecoveryUiModel(isResultVisible = true, isAccountFound = false), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "비밀번호 찾기 · 인증 완료")
@Composable
private fun AccountRecoveryPasswordPreview() {
    EumShoppingMallTheme {
        AccountRecoveryScreen(
            AccountRecoveryUiModel(mode = AccountRecoveryMode.FIND_PASSWORD, email = "example@email.com", phone = previewVerifiedPhone),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "비밀번호 재설정 오류")
@Composable
private fun AuthPasswordResetErrorPreview() {
    EumShoppingMallTheme {
        AuthPasswordResetScreen(
            AuthPasswordResetUiModel(password = "safe-pass".toCharArray(), passwordConfirmation = "different".toCharArray(), isPasswordInvalid = true),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "비밀번호 변경 완료")
@Composable
private fun AuthPasswordUpdatedPreview() {
    EumShoppingMallTheme { AuthPasswordUpdatedScreen {} }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "비밀번호 변경 오류")
@Composable
private fun AuthPasswordChangeErrorPreview() {
    EumShoppingMallTheme {
        AuthPasswordChangeScreen(
            AuthPasswordChangeUiModel(
                currentPassword = "current-pass".toCharArray(),
                newPassword = "new-pass-123".toCharArray(),
                confirmation = "new-pass-123".toCharArray(),
                isCurrentPasswordInvalid = true,
                isConfirmationValid = true,
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "휴대폰 번호 변경")
@Composable
private fun AuthPhoneNumberChangePreview() {
    EumShoppingMallTheme {
        AuthPhoneNumberChangeScreen("010-0000-0000", AuthPhoneVerificationUiModel(phoneNumber = "010-0000-0000", isCodeRequested = true), false, {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "회원 탈퇴 안내")
@Composable
private fun AccountWithdrawalGuidePreview() {
    EumShoppingMallTheme {
        AccountWithdrawalGuideScreen(AccountWithdrawalGuideUiModel(memberName = "홍길동", couponCount = 0, points = "0원", isAgreementChecked = true), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "회원 탈퇴 불가")
@Composable
private fun AccountWithdrawalUnavailablePreview() {
    EumShoppingMallTheme {
        AccountWithdrawalGuideScreen(AccountWithdrawalGuideUiModel(memberName = "홍길동", couponCount = 0, points = "0원", activeOrderCount = 1), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "회원 탈퇴 사유 · 확인")
@Composable
private fun AccountWithdrawalConfirmationPreview() {
    EumShoppingMallTheme {
        AccountWithdrawalReasonScreen(AccountWithdrawalReasonUiModel(selectedReasonIndex = 5, otherReason = "서비스 개선을 위해", password = "safe-pass".toCharArray(), isConfirmationDialogVisible = true), {})
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.1f, name = "회원 탈퇴 비밀번호 오류 · 좁은 화면")
@Composable
private fun AccountWithdrawalPasswordErrorPreview() {
    EumShoppingMallTheme {
        AccountWithdrawalReasonScreen(AccountWithdrawalReasonUiModel(selectedReasonIndex = 5, password = "safe-pass".toCharArray(), isPasswordInvalid = true), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "회원 탈퇴 완료")
@Composable
private fun AccountWithdrawalCompletePreview() {
    EumShoppingMallTheme {
        AccountWithdrawalCompletedScreen(AccountWithdrawalCompletedUiModel("0000.00.00 00:00", "0000.00.00", "0년"), {})
    }
}

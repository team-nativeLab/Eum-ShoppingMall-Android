package com.nativelap.eumshoppingmall.ui.auth.viewmodel

data class AuthLoginUiModel(
    val email: String = "",
    val password: CharArray = charArrayOf(),
    val isRemembered: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isCredentialsInvalid: Boolean = false,
    val failedAttemptCount: Int = 0,
    val isSubmitEnabled: Boolean = false,
)

enum class AuthMemberType { PERSONAL, BUSINESS }

data class AuthSignupUiModel(
    val memberType: AuthMemberType = AuthMemberType.PERSONAL,
    val email: String = "",
    val isEmailAvailable: Boolean = false,
    val password: CharArray = charArrayOf(),
    val passwordConfirmation: CharArray = charArrayOf(),
    val isPasswordVisible: Boolean = false,
    val isPasswordConfirmationVisible: Boolean = false,
    val name: String = "",
    val phone: AuthPhoneVerificationUiModel = AuthPhoneVerificationUiModel(),
    val isAllConsentChecked: Boolean = false,
    val isTermsChecked: Boolean = false,
    val isPrivacyChecked: Boolean = false,
    val isMarketingChecked: Boolean = false,
    val businessRegistrationNumber: String = "",
    val companyName: String = "",
    val representativeName: String = "",
    val industry: String = "",
    val taxEmail: String = "",
    val isRegistrationFileSelected: Boolean = false,
    val isSubmitEnabled: Boolean = false,
)

data class AuthPhoneVerificationUiModel(
    val phoneNumber: String = "",
    val verificationCode: CharArray = charArrayOf(),
    val remainingTime: String = "02:59",
    val isCodeRequested: Boolean = false,
    val isVerified: Boolean = false,
    val isPhoneInvalid: Boolean = false,
    val isCodeInvalid: Boolean = false,
)

enum class AccountRecoveryMode { FIND_EMAIL, FIND_PASSWORD }

data class AccountRecoveryUiModel(
    val mode: AccountRecoveryMode = AccountRecoveryMode.FIND_EMAIL,
    val isResultVisible: Boolean = false,
    val isAccountFound: Boolean = true,
    val name: String = "",
    val email: String = "",
    val maskedEmail: String = "",
    val joinedAt: String = "0000.00.00",
    val phone: AuthPhoneVerificationUiModel = AuthPhoneVerificationUiModel(),
    val isSubmitEnabled: Boolean = false,
)

data class AuthPasswordResetUiModel(
    val password: CharArray = charArrayOf(),
    val passwordConfirmation: CharArray = charArrayOf(),
    val isPasswordVisible: Boolean = false,
    val isConfirmationVisible: Boolean = false,
    val isPasswordInvalid: Boolean = false,
    val isSubmitEnabled: Boolean = false,
)

data class AuthPasswordChangeUiModel(
    val currentPassword: CharArray = charArrayOf(),
    val newPassword: CharArray = charArrayOf(),
    val confirmation: CharArray = charArrayOf(),
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmationVisible: Boolean = false,
    val isCurrentPasswordInvalid: Boolean = false,
    val isConfirmationValid: Boolean = false,
    val isSubmitEnabled: Boolean = false,
)

data class AccountWithdrawalGuideUiModel(
    val memberName: String = "",
    val couponCount: Int = 0,
    val points: String = "0원",
    val activeOrderCount: Int = 0,
    val isAgreementChecked: Boolean = false,
)

data class AccountWithdrawalReasonUiModel(
    val selectedReasonIndex: Int? = null,
    val otherReason: String = "",
    val couponCount: Int = 0,
    val points: String = "0원",
    val password: CharArray = charArrayOf(),
    val isPasswordVisible: Boolean = false,
    val isPasswordInvalid: Boolean = false,
    val isConfirmationDialogVisible: Boolean = false,
    val isSubmitEnabled: Boolean = false,
)

data class AccountWithdrawalCompletedUiModel(
    val completedAt: String = "0000.00.00 00:00",
    val rejoinAvailableAt: String = "0000.00.00",
    val retentionPeriod: String = "0년",
)

sealed interface AuthScreenEvent {
    data object BackClicked : AuthScreenEvent
    data object CloseClicked : AuthScreenEvent
    data class LoginEmailChanged(val value: String) : AuthScreenEvent
    data class LoginPasswordChanged(val value: CharArray) : AuthScreenEvent
    data class RememberLoginChanged(val checked: Boolean) : AuthScreenEvent
    data object LoginPasswordVisibilityChanged : AuthScreenEvent
    data object LoginSubmitted : AuthScreenEvent
    data class LoginHelpClicked(val id: String) : AuthScreenEvent
    data class SocialLoginClicked(val providerId: String) : AuthScreenEvent
    data class SignupMemberTypeChanged(val value: AuthMemberType) : AuthScreenEvent
    data class SignupFieldChanged(val fieldId: String, val value: String) : AuthScreenEvent
    data class SignupSecretChanged(val fieldId: String, val value: CharArray) : AuthScreenEvent
    data class SignupPasswordVisibilityChanged(val fieldId: String) : AuthScreenEvent
    data object SignupBusinessVerificationClicked : AuthScreenEvent
    data object EmailDuplicateCheckClicked : AuthScreenEvent
    data object SignupPhoneResendClicked : AuthScreenEvent
    data class SignupVerificationCodeChanged(val value: CharArray) : AuthScreenEvent
    data object SignupVerificationConfirmClicked : AuthScreenEvent
    data class SignupConsentChanged(val consentId: String, val checked: Boolean) : AuthScreenEvent
    data object SignupConsentDetailsClicked : AuthScreenEvent
    data object BusinessRegistrationFileClicked : AuthScreenEvent
    data object SignupSubmitted : AuthScreenEvent
    data class RecoveryModeChanged(val mode: AccountRecoveryMode) : AuthScreenEvent
    data class RecoveryNameChanged(val value: String) : AuthScreenEvent
    data class RecoveryEmailChanged(val value: String) : AuthScreenEvent
    data class RecoveryPhoneChanged(val value: String) : AuthScreenEvent
    data object RecoveryPhoneResendClicked : AuthScreenEvent
    data class RecoveryVerificationCodeChanged(val value: CharArray) : AuthScreenEvent
    data object RecoveryVerificationConfirmClicked : AuthScreenEvent
    data object RecoverySubmitted : AuthScreenEvent
    data object RecoveryRetryClicked : AuthScreenEvent
    data object RecoveryLoginClicked : AuthScreenEvent
    data object RecoveryFindPasswordClicked : AuthScreenEvent
    data object RecoverySignupClicked : AuthScreenEvent
    data class ResetSecretChanged(val fieldId: String, val value: CharArray) : AuthScreenEvent
    data object ResetPasswordVisibilityChanged : AuthScreenEvent
    data object ResetConfirmationVisibilityChanged : AuthScreenEvent
    data object ResetSubmitted : AuthScreenEvent
    data class PasswordChangeSecretChanged(val fieldId: String, val value: CharArray) : AuthScreenEvent
    data class PasswordChangeVisibilityChanged(val fieldId: String) : AuthScreenEvent
    data object PasswordChangeFindPasswordClicked : AuthScreenEvent
    data object PasswordChangeSubmitted : AuthScreenEvent
    data class PhoneChangeFieldChanged(val fieldId: String, val value: String) : AuthScreenEvent
    data object PhoneChangeResendClicked : AuthScreenEvent
    data object PhoneChangeVerificationConfirmClicked : AuthScreenEvent
    data class PhoneChangeVerificationCodeChanged(val value: CharArray) : AuthScreenEvent
    data object PhoneChangeSubmitted : AuthScreenEvent
    data class WithdrawalAgreementChanged(val checked: Boolean) : AuthScreenEvent
    data object WithdrawalNextClicked : AuthScreenEvent
    data class WithdrawalReasonSelected(val index: Int) : AuthScreenEvent
    data class WithdrawalReasonChanged(val value: String) : AuthScreenEvent
    data class WithdrawalPasswordChanged(val value: CharArray) : AuthScreenEvent
    data object WithdrawalPasswordVisibilityChanged : AuthScreenEvent
    data object WithdrawalOrderHistoryClicked : AuthScreenEvent
    data object WithdrawalSubmitted : AuthScreenEvent
    data object WithdrawalConfirmDismissed : AuthScreenEvent
    data object WithdrawalConfirmed : AuthScreenEvent
    data object WithdrawalCompletedHomeClicked : AuthScreenEvent
}

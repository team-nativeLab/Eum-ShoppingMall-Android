package com.nativelap.eumshoppingmall.ui.auth.viewmodel

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignupStepRulesTest {
    private fun inputs(
        email: String = "a@test.com",
        isEmailAvailable: Boolean = true,
        name: String = "홍길동",
        password: String = "abcd1234",
        confirmation: String = "abcd1234",
        isPhoneVerified: Boolean = true,
        isTermsChecked: Boolean = true,
        isPrivacyChecked: Boolean = true,
    ) = SignupInputs(email, isEmailAvailable, name, password.toCharArray(), confirmation.toCharArray(), isPhoneVerified, isTermsChecked, isPrivacyChecked)

    @Test
    fun passwordNeedsEightCharactersWithLetterAndDigit() {
        assertFalse(SignupStepRules.isPasswordAcceptable("abc1234".toCharArray()))
        assertTrue(SignupStepRules.isPasswordAcceptable("abcd1234".toCharArray()))
        assertFalse(SignupStepRules.isPasswordAcceptable("abcdefgh".toCharArray()))
        assertFalse(SignupStepRules.isPasswordAcceptable("12345678".toCharArray()))
    }

    @Test
    fun passwordStepRequiresMatchingConfirmation() {
        assertTrue(SignupStepRules.isStepValid(1, inputs()))
        assertFalse(SignupStepRules.isStepValid(1, inputs(confirmation = "abcd12345")))
        assertFalse(SignupStepRules.isStepValid(1, inputs(password = "abc1234", confirmation = "abc1234")))
    }

    @Test
    fun accountStepNeedsAvailableEmailAndName() {
        assertTrue(SignupStepRules.isStepValid(0, inputs()))
        assertFalse(SignupStepRules.isStepValid(0, inputs(isEmailAvailable = false)))
        assertFalse(SignupStepRules.isStepValid(0, inputs(email = " ")))
        assertFalse(SignupStepRules.isStepValid(0, inputs(name = "")))
    }

    @Test
    fun phoneStepNeedsVerification() {
        assertTrue(SignupStepRules.isStepValid(2, inputs()))
        assertFalse(SignupStepRules.isStepValid(2, inputs(isPhoneVerified = false)))
    }

    @Test
    fun consentStepNeedsBothRequiredConsents() {
        assertTrue(SignupStepRules.isStepValid(3, inputs()))
        assertFalse(SignupStepRules.isStepValid(3, inputs(isTermsChecked = false)))
        assertFalse(SignupStepRules.isStepValid(3, inputs(isPrivacyChecked = false)))
    }

    @Test
    fun submitRequiresEveryStepToPass() {
        assertTrue(SignupStepRules.isReadyToSubmit(inputs()))
        assertFalse(SignupStepRules.isReadyToSubmit(inputs(isPhoneVerified = false)))
        assertFalse(SignupStepRules.isReadyToSubmit(inputs(isTermsChecked = false)))
        assertFalse(SignupStepRules.isReadyToSubmit(inputs(password = "abcdefgh", confirmation = "abcdefgh")))
    }
}

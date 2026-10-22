package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressFormUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressListUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountMenuRowUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountOrderStatusUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileEditUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountShortcutUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewProfile = AccountProfileUiModel(
    name = "홍길동",
    email = "example@email.com",
    phoneNumber = "010-0000-0000",
    companyName = "회사명",
    businessRegistrationNumber = "000-00-00000",
    department = "안전관리팀",
    isBusinessMember = true,
    isSmsConsentEnabled = true,
    isEmailConsentEnabled = false,
)

private val previewAddress = AccountAddressUiModel(
    id = "address-1",
    label = "배송지명",
    recipient = "홍길동",
    phoneNumber = "010-0000-0000",
    postalCode = "00000",
    address = "주소",
    addressDetail = "상세주소",
    deliveryRequest = "",
    isDefault = true,
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "드로어 회원")
@Composable
private fun AccountDrawerMemberPreview() {
    EumShoppingMallTheme {
        AccountDrawerScreen(previewProfile, isLoggedIn = true, onEvent = {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "드로어 게스트")
@Composable
private fun AccountDrawerGuestPreview() {
    EumShoppingMallTheme {
        AccountDrawerScreen(previewProfile, isLoggedIn = false, onEvent = {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "마이페이지 회원")
@Composable
private fun MyPageMemberPreview() {
    EumShoppingMallTheme {
        MyPageScreen(
            MyPageUiModel(
                isLoggedIn = true,
                profile = previewProfile,
                shortcuts = listOf(
                    AccountShortcutUiModel("orders", "주문내역"),
                    AccountShortcutUiModel("favorites", "찜한 상품"),
                    AccountShortcutUiModel("recent", "최근 본 상품"),
                    AccountShortcutUiModel("coupons", "쿠폰함"),
                    AccountShortcutUiModel("points", "적립금"),
                ),
                orderStatuses = listOf(
                    AccountOrderStatusUiModel("입금대기", "0"),
                    AccountOrderStatusUiModel("결제완료", "0"),
                    AccountOrderStatusUiModel("배송준비", "0"),
                    AccountOrderStatusUiModel("배송중", "0"),
                    AccountOrderStatusUiModel("배송완료", "0"),
                ),
                orderRows = listOf(AccountMenuRowUiModel("returns", "취소·반품·교환"), AccountMenuRowUiModel("quotes", "견적 문의 내역")),
                supportRows = listOf(AccountMenuRowUiModel("inquiries", "1:1 문의 내역"), AccountMenuRowUiModel("customer", "고객센터"), AccountMenuRowUiModel("notices", "공지사항")),
                recentProducts = emptyList(),
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.2f, name = "마이페이지 게스트 · 좁은 화면")
@Composable
private fun MyPageGuestPreview() {
    EumShoppingMallTheme {
        MyPageScreen(
            MyPageUiModel(
                isLoggedIn = false,
                profile = previewProfile,
                shortcuts = emptyList(),
                orderStatuses = emptyList(),
                orderRows = emptyList(),
                supportRows = listOf(AccountMenuRowUiModel("customer", "고객센터"), AccountMenuRowUiModel("notices", "공지사항")),
                recentProducts = listOf("최근 본 상품 예시", "긴 상품명은 화면 폭에 맞춰 줄바꿈됩니다"),
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "프로필 비밀번호 오류")
@Composable
private fun AccountPasswordErrorPreview() {
    EumShoppingMallTheme {
        AccountProfileScreen(AccountProfileEditUiModel(previewProfile, isPasswordInvalid = true), true, {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "프로필 수정")
@Composable
private fun AccountProfileEditPreview() {
    EumShoppingMallTheme {
        AccountProfileScreen(AccountProfileEditUiModel(previewProfile), false, {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "배송지 목록")
@Composable
private fun AccountAddressListPreview() {
    EumShoppingMallTheme {
        AccountAddressListScreen(AccountAddressListUiModel(listOf(previewAddress, previewAddress.copy(id = "address-2", isDefault = false)), 10), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "배송지 빈 상태")
@Composable
private fun AccountAddressEmptyPreview() {
    EumShoppingMallTheme {
        AccountAddressListScreen(AccountAddressListUiModel(emptyList(), 10), {})
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.2f, name = "배송지 입력 오류 · 좁은 화면")
@Composable
private fun AccountAddressInvalidPreview() {
    EumShoppingMallTheme {
        AccountAddressFormScreen(AccountAddressFormUiModel(previewAddress.copy(phoneNumber = "010-0000"), false, isPhoneInvalid = true), {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "설정 알림 꺼짐 · 로그아웃 확인")
@Composable
private fun AccountSettingsWarningPreview() {
    EumShoppingMallTheme {
        AccountSettingsScreen(
            AccountSettingUiModel(
                isLoggedIn = true,
                isDeviceNotificationEnabled = false,
                isAppMarketingEnabled = true,
                isSmsMarketingEnabled = false,
                isEmailMarketingEnabled = true,
                isNightMarketingEnabled = false,
                isDeviceNotificationWarningVisible = true,
                isMarketingToastVisible = true,
                isLogoutDialogVisible = true,
                appVersionLabel = "0.0.0 · 최신 버전",
                lastConsentChangedLabel = "0000.00.00",
            ),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "설정 게스트")
@Composable
private fun AccountSettingsGuestPreview() {
    EumShoppingMallTheme {
        AccountSettingsScreen(
            AccountSettingUiModel(false, true, false, false, false, false, false, false, false, "0.0.0", "0000.00.00"),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "설정 수신 변경 토스트")
@Composable
private fun AccountSettingsToastPreview() {
    EumShoppingMallTheme {
        AccountSettingsScreen(
            AccountSettingUiModel(true, true, false, false, false, false, false, true, false, "0.0.0", "0000.00.00"),
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 800, fontScale = 1.2f, name = "설정 좁은 화면 · 큰 글자")
@Composable
private fun AccountSettingsCompactPreview() {
    EumShoppingMallTheme {
        AccountSettingsScreen(
            AccountSettingUiModel(true, false, true, false, true, false, true, false, false, "0.0.0 · 최신 버전", "0000.00.00"),
            {},
        )
    }
}

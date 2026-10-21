package com.nativelap.eumshoppingmall.ui.account.viewmodel

data class AccountOrderStatusUiModel(val label: String, val count: String)
data class AccountShortcutUiModel(val id: String, val label: String, val countLabel: String? = null)
data class AccountMenuRowUiModel(val id: String, val label: String)

data class AccountProfileUiModel(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val companyName: String,
    val businessRegistrationNumber: String,
    val department: String,
    val isBusinessMember: Boolean,
    val isSmsConsentEnabled: Boolean,
    val isEmailConsentEnabled: Boolean,
)

data class AccountAddressUiModel(
    val id: String,
    val label: String,
    val recipient: String,
    val phoneNumber: String,
    val postalCode: String,
    val address: String,
    val addressDetail: String,
    val deliveryRequest: String,
    val isDefault: Boolean,
)

data class MyPageUiModel(
    val isLoggedIn: Boolean,
    val profile: AccountProfileUiModel,
    val shortcuts: List<AccountShortcutUiModel>,
    val orderStatuses: List<AccountOrderStatusUiModel>,
    val orderRows: List<AccountMenuRowUiModel>,
    val supportRows: List<AccountMenuRowUiModel>,
    val recentProducts: List<String>,
)

data class AccountProfileEditUiModel(
    val profile: AccountProfileUiModel,
    val isPasswordInvalid: Boolean = false,
    val passwordValue: CharArray = charArrayOf(),
    val isSaveEnabled: Boolean = true,
)

data class AccountAddressListUiModel(val addresses: List<AccountAddressUiModel>, val maximumAddressCount: Int)

data class AccountAddressFormUiModel(
    val address: AccountAddressUiModel,
    val isEditMode: Boolean,
    val isPhoneInvalid: Boolean = false,
    val isSaveEnabled: Boolean = true,
)

data class AccountSettingUiModel(
    val isLoggedIn: Boolean,
    val isDeviceNotificationEnabled: Boolean,
    val isAppMarketingEnabled: Boolean,
    val isSmsMarketingEnabled: Boolean,
    val isEmailMarketingEnabled: Boolean,
    val isNightMarketingEnabled: Boolean,
    val isDeviceNotificationWarningVisible: Boolean,
    val isMarketingToastVisible: Boolean,
    val isLogoutDialogVisible: Boolean,
    val appVersionLabel: String,
    val lastConsentChangedLabel: String,
)

sealed interface MyPageScreenEvent {
    data object LoginClicked : MyPageScreenEvent
    data object ProfileClicked : MyPageScreenEvent
    data object SettingsClicked : MyPageScreenEvent
    data object DrawerClicked : MyPageScreenEvent
    data class ShortcutClicked(val shortcutId: String) : MyPageScreenEvent
    data class MenuRowClicked(val rowId: String) : MyPageScreenEvent
    data class BottomNavigationClicked(val itemId: String) : MyPageScreenEvent
}

sealed interface AccountDrawerScreenEvent {
    data object CloseClicked : AccountDrawerScreenEvent
    data object NotificationClicked : AccountDrawerScreenEvent
    data object ProfileClicked : AccountDrawerScreenEvent
    data object SettingsClicked : AccountDrawerScreenEvent
    data object LogoutClicked : AccountDrawerScreenEvent
    data class MenuClicked(val menuId: String) : AccountDrawerScreenEvent
}

sealed interface AccountProfileScreenEvent {
    data object BackClicked : AccountProfileScreenEvent
    data object PasswordChanged : AccountProfileScreenEvent
    data object PhoneChangeClicked : AccountProfileScreenEvent
    data object PasswordSubmitted : AccountProfileScreenEvent
    data class PasswordChangedValue(val value: CharArray) : AccountProfileScreenEvent
    data class NameChanged(val value: String) : AccountProfileScreenEvent
    data class PhoneChanged(val value: String) : AccountProfileScreenEvent
    data class CompanyChanged(val value: String) : AccountProfileScreenEvent
    data class DepartmentChanged(val value: String) : AccountProfileScreenEvent
    data class SmsConsentChanged(val enabled: Boolean) : AccountProfileScreenEvent
    data class EmailConsentChanged(val enabled: Boolean) : AccountProfileScreenEvent
    data object WithdrawalClicked : AccountProfileScreenEvent
    data object SaveClicked : AccountProfileScreenEvent
}

sealed interface AccountAddressListScreenEvent {
    data object BackClicked : AccountAddressListScreenEvent
    data object AddClicked : AccountAddressListScreenEvent
    data class EditClicked(val addressId: String) : AccountAddressListScreenEvent
    data class DeleteClicked(val addressId: String) : AccountAddressListScreenEvent
    data class DefaultSelected(val addressId: String) : AccountAddressListScreenEvent
}

sealed interface AccountAddressFormScreenEvent {
    data object BackClicked : AccountAddressFormScreenEvent
    data class LabelChanged(val value: String) : AccountAddressFormScreenEvent
    data class RecipientChanged(val value: String) : AccountAddressFormScreenEvent
    data class PhoneChanged(val value: String) : AccountAddressFormScreenEvent
    data class PostalCodeChanged(val value: String) : AccountAddressFormScreenEvent
    data class AddressChanged(val value: String) : AccountAddressFormScreenEvent
    data class AddressDetailChanged(val value: String) : AccountAddressFormScreenEvent
    data class DeliveryRequestChanged(val value: String) : AccountAddressFormScreenEvent
    data class DefaultChanged(val enabled: Boolean) : AccountAddressFormScreenEvent
    data object AddressSearchClicked : AccountAddressFormScreenEvent
    data object SaveClicked : AccountAddressFormScreenEvent
}

sealed interface AccountSettingsScreenEvent {
    data object BackClicked : AccountSettingsScreenEvent
    data object LoginClicked : AccountSettingsScreenEvent
    data object DeviceNotificationChanged : AccountSettingsScreenEvent
    data class MarketingChanged(val settingId: String, val enabled: Boolean) : AccountSettingsScreenEvent
    data class PolicyClicked(val policyId: String) : AccountSettingsScreenEvent
    data object LogoutClicked : AccountSettingsScreenEvent
    data object LogoutConfirmed : AccountSettingsScreenEvent
    data object LogoutDismissed : AccountSettingsScreenEvent
    data object NotificationSettingsClicked : AccountSettingsScreenEvent
    data object WithdrawalClicked : AccountSettingsScreenEvent
}

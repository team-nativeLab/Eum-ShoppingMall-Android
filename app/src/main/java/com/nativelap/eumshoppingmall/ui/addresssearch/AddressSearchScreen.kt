package com.nativelap.eumshoppingmall.ui.addresssearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 본문과 상단바 폭 상한. 800dp 이상 화면에서 콘텐츠가 가로로 늘어나지 않게 한다.
internal val AddressSearchMaxContentWidth = 600.dp

data class AddressSearchResultUiModel(
    val id: String,
    val roadAddress: String,
    val lotAddress: String,
)

data class AddressSearchUiModel(
    val query: String,
    val results: List<AddressSearchResultUiModel>,
    val selectedAddressId: String?,
    val hasSearched: Boolean,
)

sealed interface AddressSearchScreenEvent {
    data object BackClicked : AddressSearchScreenEvent
    data class QueryChanged(val query: String) : AddressSearchScreenEvent
    data class ResultSelected(val addressId: String) : AddressSearchScreenEvent
    data class AddressSelectionConfirmed(val addressId: String) : AddressSearchScreenEvent
}

@Composable
fun AddressSearchScreen(
    uiModel: AddressSearchUiModel,
    onEvent: (AddressSearchScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedAddressId = uiModel.selectedAddressId
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.eumColors.background,
        topBar = {
            AddressSearchTopBar(
                title = stringResource(R.string.ui_audit_address_title),
                onBackClick = { onEvent(AddressSearchScreenEvent.BackClicked) },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                // 키보드가 올라와도 결과 목록이 키보드 뒤로 가려지지 않게 본문 영역을 줄인다(공용 바닥 바와 같은 순서).
                .navigationBarsPadding()
                .imePadding(),
        ) {
            Column(
                modifier = Modifier.widthIn(max = AddressSearchMaxContentWidth)
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = EumSpacing.x20)
                    .padding(top = EumSpacing.x16, bottom = EumSpacing.x32),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
            ) {
                AddressSearchField(
                    value = uiModel.query,
                    placeholder = stringResource(R.string.ui_audit_address_placeholder),
                    onValueChange = { onEvent(AddressSearchScreenEvent.QueryChanged(it)) },
                )
                Text(
                    text = stringResource(R.string.ui_audit_address_helper),
                    style = addressSearchCaptionStyle(),
                    color = MaterialTheme.eumColors.addressMuted,
                )
                when {
                    uiModel.results.isNotEmpty() -> AddressSearchResults(
                        results = uiModel.results,
                        selectedAddressId = selectedAddressId,
                        onAddressSelected = { addressId ->
                            onEvent(AddressSearchScreenEvent.ResultSelected(addressId))
                        },
                    )

                    uiModel.hasSearched -> Text(
                        text = stringResource(R.string.ui_audit_address_empty),
                        modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x32),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            }
        }
    }
}

// Figma caption(12sp/16) 기준. bodySmall의 자간을 상속하지 않도록 letterSpacing을 0으로 명시한다.
@Composable
internal fun addressSearchCaptionStyle(): TextStyle = MaterialTheme.typography.bodySmall.copy(
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.sp,
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AddressSearchScreenPreview() {
    EumShoppingMallTheme {
        AddressSearchScreen(
            uiModel = AddressSearchUiModel(
                query = "",
                results = emptyList(),
                selectedAddressId = null,
                hasSearched = false,
            ),
            onEvent = {},
        )
    }
}

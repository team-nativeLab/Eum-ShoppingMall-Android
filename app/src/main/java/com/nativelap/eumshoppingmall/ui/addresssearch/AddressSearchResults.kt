package com.nativelap.eumshoppingmall.ui.addresssearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun AddressSearchResults(
    results: List<AddressSearchResultUiModel>,
    selectedAddressId: String?,
    onAddressSelected: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x8),
        color = MaterialTheme.eumColors.surface,
    ) {
        // 구분선이 카드 전체 폭을 쓰도록 좌우 패딩은 각 항목에 둔다.
        Column(Modifier.fillMaxWidth().selectableGroup()) {
            results.forEachIndexed { index, address ->
                val isSelected = address.id == selectedAddressId
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .selectable(
                            selected = isSelected,
                            role = Role.RadioButton,
                            onClick = { onAddressSelected(address.id) },
                        )
                        .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                ) {
                    Text(
                        text = address.roadAddress,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.eumColors.addressRoad,
                    )
                    Text(
                        text = stringResource(R.string.ui_audit_address_lot_format, address.lotAddress),
                        style = addressSearchCaptionStyle(),
                        color = MaterialTheme.eumColors.addressMuted,
                    )
                }
                if (index != results.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.eumColors.addressDivider)
                }
            }
        }
    }
}

package com.nativelap.eumshoppingmall.ui.addresssearch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val AddressSearchBackButtonSize = 44.dp

// 뒤로가기 영역(좌측 패딩 8 + 44dp)과 같은 폭을 오른쪽에 비워 제목을 가운데 맞춘다.
private val AddressSearchTopBarSideSlotWidth = 52.dp

@Composable
internal fun AddressSearchTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    val backActionDescription = stringResource(R.string.ui_audit_back)
    Box(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
        Row(
            modifier = Modifier.align(Alignment.Center)
                .widthIn(max = AddressSearchMaxContentWidth)
                .fillMaxWidth()
                .heightIn(min = EumSpacing.x56),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(start = EumSpacing.x8)
                    .size(AddressSearchBackButtonSize)
                    .semantics { contentDescription = backActionDescription },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textPrimary,
                )
            }
            Text(
                text = title,
                modifier = Modifier.weight(1f).semantics { heading() },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.eumColors.textPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.width(AddressSearchTopBarSideSlotWidth))
        }
    }
}

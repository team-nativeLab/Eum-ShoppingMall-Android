package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CartLoginNoticeBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(horizontal = EumSpacing.x20)
            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.x12))
            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
            .padding(horizontal = 14.dp, vertical = EumSpacing.x12),
        verticalAlignment = Alignment.Top,
    ) {
        // 아이콘은 장식용이다. 안내 문구를 읽어 주므로 contentDescription을 두지 않는다.
        Icon(
            painter = painterResource(R.drawable.ic_info),
            contentDescription = null,
            tint = MaterialTheme.eumColors.textSecondary,
            modifier = Modifier.padding(top = EumSpacing.x2).size(16.dp),
        )

        Spacer(Modifier.width(EumSpacing.x8))

        Text(
            text = stringResource(R.string.cart_login_notice),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textSecondary,
        )
    }
}

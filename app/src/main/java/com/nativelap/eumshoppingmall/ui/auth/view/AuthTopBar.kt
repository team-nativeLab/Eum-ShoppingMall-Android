package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.EumTopBarBackButton
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

@Composable
internal fun AuthTopBar(title: String, onBack: () -> Unit, isClose: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().statusBarsPadding().heightIn(min = 56.dp).padding(horizontal = EumSpacing.topBarInset),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EumTopBarBackButton(
            onClick = onBack,
            contentDescription = stringResource(if (isClose) R.string.common_close else R.string.common_back),
            iconResId = if (isClose) R.drawable.ic_x else R.drawable.ic_arrow_left,
        )
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.size(48.dp))
    }
}

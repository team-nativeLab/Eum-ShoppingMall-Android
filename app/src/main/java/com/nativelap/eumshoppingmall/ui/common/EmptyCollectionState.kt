package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun EmptyCollectionState(
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize().navigationBarsPadding().padding(horizontal = EumSpacing.x20), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(
                Modifier.size(64.dp).background(MaterialTheme.eumColors.surface, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painterResource(R.drawable.ic_message_square_text),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textSecondary,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(Modifier.height(EumSpacing.x20))
            Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
            Spacer(Modifier.height(EumSpacing.x8))
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            Spacer(Modifier.height(64.dp))
            Button(
                onClick = onAction,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.eumColors.textPrimary),
            ) {
                Text(actionLabel, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.inverse)
            }
        }
    }
}

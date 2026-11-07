package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun AuthSegmentedControl(labels: List<String>, selectedIndex: Int, onSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
        color = MaterialTheme.eumColors.background,
    ) {
        Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            labels.forEachIndexed { index, label ->
                Surface(
                    onClick = { onSelected(index) },
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = if (index == selectedIndex) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(
                            label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (index == selectedIndex) MaterialTheme.eumColors.background else MaterialTheme.eumColors.textSecondary,
                        )
                    }
                }
            }
        }
    }
}

package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val SegmentedControlVisualHeight = 36.dp

/**
 * 시각 높이 36dp 세그먼트. 터치 영역은 투명한 48dp 이상 행이 맡고, 라벨은 그 행에 놓아 접근성 이름으로 쓴다.
 */
@Composable
internal fun AuthSegmentedControl(labels: List<String>, selectedIndex: Int, onSelected: (Int) -> Unit) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(SegmentedControlVisualHeight),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
            color = MaterialTheme.eumColors.background,
        ) {
            Row(Modifier.padding(EumSpacing.x4), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                labels.indices.forEach { index ->
                    Surface(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        shape = RoundedCornerShape(6.dp),
                        color = if (index == selectedIndex) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
                    ) {}
                }
            }
        }
        Row(Modifier.fillMaxWidth()) {
            labels.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                Box(
                    Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .selectable(selected = isSelected, onClick = { onSelected(index) }, role = Role.Tab),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) MaterialTheme.eumColors.background else MaterialTheme.eumColors.textSecondary,
                    )
                }
            }
        }
    }
}

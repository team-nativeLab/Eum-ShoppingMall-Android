package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumDuration
import com.nativelap.eumshoppingmall.ui.theme.EumEasing
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import com.nativelap.eumshoppingmall.ui.theme.rememberReducedMotion
import kotlin.math.roundToInt

// 항목 하나의 최소 높이이자 터치 영역. 시각 캡슐도 같은 높이라 터치 영역이 보이는 영역보다 작지 않다.
private val SegmentMinHeight = 48.dp

/**
 * 공통 세그먼트 컨트롤. 선택 캡슐 하나가 항목 사이를 슬라이드로 이동한다(줄이기 설정이면 즉시 이동).
 * 항목은 Role.Tab과 selected 시맨틱을 가지며, 선택 라벨은 반전색, 비선택 라벨은 보조 텍스트색이다.
 */
@Composable
fun EumSegmentedControl(
    labels: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isReducedMotion = rememberReducedMotion()
    val outerShape = RoundedCornerShape(EumRadius.x8)
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = if (isReducedMotion) snap() else tween(EumDuration.MEDIUM1, easing = EumEasing.Standard),
        label = "segmentIndicator",
    )
    var trackWidthPx by remember { mutableIntStateOf(0) }
    val itemCount = labels.size.coerceAtLeast(1)
    Box(
        modifier
            .fillMaxWidth()
            .clip(outerShape)
            .background(MaterialTheme.eumColors.surface)
            .border(1.dp, MaterialTheme.eumColors.textPrimary, outerShape)
            .padding(EumSpacing.x4),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .onSizeChanged { size -> trackWidthPx = size.width },
        ) {
            // 선택 캡슐. 라벨 행 높이에 맞춰 늘어나므로 큰 글꼴에서도 라벨과 어긋나지 않는다.
            Box(
                Modifier
                    .fillMaxWidth(1f / itemCount)
                    .fillMaxHeight()
                    .offset { IntOffset((trackWidthPx.toFloat() / itemCount * animatedIndex).roundToInt(), 0) }
                    .clip(RoundedCornerShape(EumRadius.x6))
                    .background(MaterialTheme.eumColors.textPrimary),
            )
            Row(Modifier.fillMaxWidth()) {
                labels.forEachIndexed { index, label ->
                    val isSelected = index == selectedIndex
                    Box(
                        Modifier
                            .weight(1f)
                            .heightIn(min = SegmentMinHeight)
                            .selectable(selected = isSelected, onClick = { onSelected(index) }, role = Role.Tab)
                            .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x8),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

package com.nativelap.eumshoppingmall.ui.quote.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun QuoteProgressStepper(status: QuoteInquiryStatus, modifier: Modifier = Modifier) {
    val currentStep = when (status) {
        QuoteInquiryStatus.Received -> 0
        QuoteInquiryStatus.Consulting -> 1
        QuoteInquiryStatus.EstimateSent -> 2
        QuoteInquiryStatus.Complete -> 3
        QuoteInquiryStatus.Cancelled -> -1
    }
    // 취소 문의는 진행 단계가 없으므로 아무것도 그리지 않는다.
    if (currentStep < 0) return
    // 완료 상태에서는 마지막 단계까지 모두 끝난 것으로 본다.
    val completedCount = if (status == QuoteInquiryStatus.Complete) 4 else currentStep
    val stepLabels = listOf(
        R.string.quote_progress_inquiry,
        R.string.quote_progress_consultation,
        R.string.quote_progress_estimate,
        R.string.quote_progress_complete,
    )
    val activeColor = MaterialTheme.eumColors.textPrimary
    val inactiveColor = MaterialTheme.eumColors.lineStrong
    Row(modifier.fillMaxWidth()) {
        stepLabels.forEachIndexed { index, labelRes ->
            val isCompleted = index < completedCount
            val isCurrent = index == currentStep && !isCompleted
            val isReached = index <= currentStep
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x6),
            ) {
                Box(
                    Modifier.fillMaxWidth().height(20.dp).drawBehind {
                        val centerY = size.height / 2
                        val lineWidth = 2.dp.toPx()
                        // 왼쪽 선은 이전 단계에서 현재 단계까지 도달했을 때, 오른쪽 선은 현재 단계를 지나 다음 단계로 넘어갔을 때 강조한다.
                        if (index > 0) {
                            drawLine(
                                color = if (index <= currentStep) activeColor else inactiveColor,
                                start = Offset(0f, centerY),
                                end = Offset(size.width / 2, centerY),
                                strokeWidth = lineWidth,
                            )
                        }
                        if (index < stepLabels.lastIndex) {
                            drawLine(
                                color = if (index < currentStep) activeColor else inactiveColor,
                                start = Offset(size.width / 2, centerY),
                                end = Offset(size.width, centerY),
                                strokeWidth = lineWidth,
                            )
                        }
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted || isCurrent) activeColor else MaterialTheme.eumColors.surface)
                            .border(1.dp, if (isReached) activeColor else inactiveColor, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isCompleted) {
                            Icon(
                                painterResource(R.drawable.ic_check),
                                null,
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.eumColors.inverse,
                            )
                        }
                    }
                }
                Text(
                    stringResource(labelRes),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isReached) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

package com.nativelap.eumshoppingmall.ui.common

import android.animation.ValueAnimator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumDuration
import com.nativelap.eumshoppingmall.ui.theme.EumEasing
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.isReducedMotion

private val COUNT_SLIDE_DISTANCE = 6.dp

/**
 * 수량 숫자. 늘면 새 숫자가 아래에서 올라오고, 줄면 위에서 내려온다(6dp + fade 150ms, Standard).
 * 줄이기 설정이면 바로 바꾼다.
 */
@Composable
fun AnimatedCountText(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
) {
    val slidePx = with(LocalDensity.current) { COUNT_SLIDE_DISTANCE.roundToPx() }
    AnimatedContent(
        targetState = count,
        modifier = modifier,
        transitionSpec = {
            // 줄이기 여부는 전환이 시작될 때 읽는다(ReducedMotion.kt 주석과 같은 기준).
            if (isReducedMotion(ValueAnimator.areAnimatorsEnabled())) {
                EnterTransition.None togetherWith ExitTransition.None
            } else if (targetState > initialState) {
                (
                    slideInVertically(tween(EumDuration.SHORT2, easing = EumEasing.Standard)) { slidePx } +
                        fadeIn(tween(EumDuration.SHORT2, easing = EumEasing.Standard))
                    ) togetherWith (
                    slideOutVertically(tween(EumDuration.SHORT2, easing = EumEasing.Standard)) { -slidePx } +
                        fadeOut(tween(EumDuration.SHORT2, easing = EumEasing.Standard))
                    )
            } else {
                (
                    slideInVertically(tween(EumDuration.SHORT2, easing = EumEasing.Standard)) { -slidePx } +
                        fadeIn(tween(EumDuration.SHORT2, easing = EumEasing.Standard))
                    ) togetherWith (
                    slideOutVertically(tween(EumDuration.SHORT2, easing = EumEasing.Standard)) { slidePx } +
                        fadeOut(tween(EumDuration.SHORT2, easing = EumEasing.Standard))
                    )
            }
        },
    ) { value ->
        Box {
            Text(
                text = value.toString(),
                style = style,
                color = color,
                textAlign = textAlign,
            )
        }
    }
}

@Preview(showBackground = true, name = "수량 숫자")
@Composable
private fun AnimatedCountTextPreview() {
    EumShoppingMallTheme {
        AnimatedCountText(count = 2, style = androidx.compose.material3.MaterialTheme.typography.labelMedium)
    }
}

package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumDuration
import com.nativelap.eumshoppingmall.ui.theme.EumEasing
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpring
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import com.nativelap.eumshoppingmall.ui.theme.rememberReducedMotion

/**
 * 찜 토글 버튼. 아이콘은 150ms crossfade로 바뀌고, 찜을 켤 때만 1 → 1.12 → 1 scale 반응을 준다.
 * 줄이기 설정이면 색·아이콘을 즉시 바꾸고 scale은 생략한다.
 * [bordered]가 true이면 상품 상세 CTA 옆의 48dp 테두리 버튼 형태가 된다(테두리 색은 미확인, 임시로 line 토큰).
 */
@Composable
fun FavoriteToggleButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 48.dp,
    iconSize: Dp = 20.dp,
    bordered: Boolean = false,
) {
    val reducedMotion = rememberReducedMotion()
    val scale = remember { Animatable(1f) }
    var hasSeenInitialValue by remember { mutableStateOf(false) }
    // 첫 구성의 값은 애니메이션하지 않는다. 사용자가 바꾼 뒤에만 반응한다.
    LaunchedEffect(isFavorite) {
        if (!hasSeenInitialValue) {
            hasSeenInitialValue = true
            return@LaunchedEffect
        }
        if (!isFavorite) {
            // 찜을 해제하면 진행 중이던 반응도 1배로 되돌린다.
            scale.snapTo(1f)
        } else if (!reducedMotion) {
            scale.animateTo(FAVORITE_POP_SCALE, EumSpring.press())
            scale.animateTo(1f, EumSpring.press())
        }
    }
    val crossfadeSpec: FiniteAnimationSpec<Float> = if (reducedMotion) {
        snap()
    } else {
        tween(durationMillis = EumDuration.SHORT2, easing = EumEasing.Standard)
    }
    val borderModifier = if (bordered) {
        Modifier
            .clip(RoundedCornerShape(EumRadius.x8))
            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x8))
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .size(buttonSize)
            .then(borderModifier)
            .clickable(role = Role.Button, onClick = onToggle)
            .semantics { this.contentDescription = contentDescription }
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(targetState = isFavorite, animationSpec = crossfadeSpec, label = "favoriteIcon") { favorite ->
            Icon(
                painter = painterResource(if (favorite) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                contentDescription = null,
                tint = if (favorite) MaterialTheme.eumColors.heartActive else MaterialTheme.eumColors.heartIdle,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

private const val FAVORITE_POP_SCALE = 1.12f

@Preview(showBackground = true, name = "찜 토글 · 꺼짐")
@Composable
private fun FavoriteToggleButtonIdlePreview() {
    EumShoppingMallTheme {
        FavoriteToggleButton(isFavorite = false, onToggle = {}, contentDescription = "찜하기")
    }
}

@Preview(showBackground = true, name = "찜 토글 · 켜짐 · 테두리")
@Composable
private fun FavoriteToggleButtonActivePreview() {
    EumShoppingMallTheme {
        FavoriteToggleButton(isFavorite = true, onToggle = {}, contentDescription = "찜 해제", bordered = true)
    }
}

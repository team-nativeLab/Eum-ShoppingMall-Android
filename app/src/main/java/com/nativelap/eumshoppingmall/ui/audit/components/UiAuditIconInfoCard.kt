package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * 아이콘 타일과 제목·설명으로 된 정보 카드.
 * onClick이 있으면 카드 전체가 버튼으로 동작하고, 없으면 표시 전용 카드다.
 * 여백·테두리·설명 간격은 화면별 Figma 값에 맞춰 매개변수로 받는다(기본값은 정책·이벤트 화면 값).
 */
@Composable
internal fun UiAuditIconInfoCard(
    @DrawableRes iconRes: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    minHeight: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    contentPadding: Dp = 14.dp,
    borderColor: Color = MaterialTheme.eumColors.lineNeutral,
    descriptionTopPadding: Dp = EumSpacing.x8,
) {
    val cardModifier = modifier.fillMaxWidth().heightIn(min = minHeight)
    val cardShape = RoundedCornerShape(EumRadius.x12)
    val cardColor = MaterialTheme.eumColors.surface
    val cardBorder = BorderStroke(1.dp, borderColor)
    val cardContent: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = contentPadding, vertical = contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(EumRadius.x8),
                color = MaterialTheme.eumColors.successTint,
                modifier = Modifier.size(IconInfoTileSize),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.eumColors.success,
                        modifier = Modifier.size(IconInfoGlyphSize),
                    )
                }
            }
            Column(Modifier.padding(start = EumSpacing.x12)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Text(
                    text = description,
                    modifier = Modifier.padding(top = descriptionTopPadding),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textSecondary,
                )
            }
        }
    }
    if (onClick == null) {
        Surface(
            modifier = cardModifier,
            shape = cardShape,
            color = cardColor,
            border = cardBorder,
        ) {
            cardContent()
        }
    } else {
        Surface(
            onClick = onClick,
            modifier = cardModifier,
            shape = cardShape,
            color = cardColor,
            border = cardBorder,
        ) {
            cardContent()
        }
    }
}

// Figma 아이콘 타일 36dp, 아이콘 18dp는 EumSpacing 토큰에 없어 공용 카드 전용 값으로 둔다.
private val IconInfoTileSize = 36.dp
private val IconInfoGlyphSize = 18.dp

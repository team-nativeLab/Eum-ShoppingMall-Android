package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// ratingValue가 있으면 별을 그리고, 평점이 없으면 "평점 없음"만 표시한다.
// reviewCountText는 리뷰 수를 아는 경우에만 넘긴다. 모르면 "리뷰 0"으로 추측하지 않는다.
@Composable
internal fun ProductRatingLine(
    ratingValue: Float?,
    ratingText: String?,
    reviewCountText: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.heightIn(min = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (ratingText == null && ratingValue == null) {
            Text(
                listOfNotNull(stringResource(R.string.product_rating_none), reviewCountText).joinToString(" · "),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textTertiary,
                minLines = 1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            return@Row
        }
        ratingValue?.takeIf { it in 0f..5f }?.let { value ->
            val filledStarCount = value.toInt()
            repeat(5) { index ->
                Icon(
                    painterResource(
                        if (index < filledStarCount) R.drawable.ic_star_catalog_filled else R.drawable.ic_star,
                    ),
                    contentDescription = null,
                    tint = if (index < filledStarCount) MaterialTheme.eumColors.ratingStar else MaterialTheme.eumColors.lineStrong,
                    modifier = Modifier.size(16.dp),
                )
            }

            Spacer(Modifier.width(EumSpacing.x4))
        }
        Text(
            ratingText.orEmpty(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.eumColors.textTertiary,
            minLines = 1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

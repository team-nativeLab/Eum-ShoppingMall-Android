package com.nativelap.eumshoppingmall.ui.orders.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderProductThumbnail(
    imageResId: Int?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
) {
    Box(
        modifier = modifier.size(size).clip(RoundedCornerShape(EumRadius.x8)).background(MaterialTheme.eumColors.surfaceSecondary),
        contentAlignment = Alignment.Center,
    ) {
        if (imageResId != null) {
            Image(painterResource(imageResId), contentDescription, Modifier.size(size))
        } else {
            Icon(
                painterResource(R.drawable.ic_image),
                contentDescription = stringResource(R.string.catalog_product_image_placeholder),
                tint = MaterialTheme.eumColors.placeholderGlyph,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

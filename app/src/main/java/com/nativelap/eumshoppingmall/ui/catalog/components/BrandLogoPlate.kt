package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogBrandUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 흰 판(r8, 테두리) 안에 브랜드 로고(r4)를 올린다. 배너·목록 행·상세에서 판과 로고 크기만 바꿔 쓴다. */
@Composable
fun BrandLogoPlate(
    brand: CatalogBrandUiModel,
    plateWidth: Dp,
    plateHeight: Dp,
    logoWidth: Dp,
    logoHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val plateShape = RoundedCornerShape(EumRadius.x8)
    val imageResId = brand.imageResId
    Box(
        modifier = modifier.size(width = plateWidth, height = plateHeight)
            .clip(plateShape)
            .background(MaterialTheme.eumColors.surface)
            .border(1.dp, MaterialTheme.eumColors.line, plateShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(width = logoWidth, height = logoHeight).clip(RoundedCornerShape(EumRadius.x4)),
            contentAlignment = Alignment.Center,
        ) {
            if (imageResId == null) {
                Icon(
                    painterResource(R.drawable.ic_image),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.placeholderGlyph,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Image(
                    painterResource(imageResId),
                    // 브랜드명이 옆에 텍스트로 있으므로 별도 이미지 설명이 있을 때만 읽는다.
                    contentDescription = brand.imageDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

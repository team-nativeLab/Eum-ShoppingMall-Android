package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.nativelap.eumshoppingmall.R

/** 앱 안 모든 로고 위치가 쓰는 "안전맛집" 정사각 로고. 크기만 화면이 정하고, 접근성 이름은 브랜드 이름으로 고정한다. */
@Composable
fun EumBrandLogo(size: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_brand_logo),
        contentDescription = stringResource(R.string.brand_logo_description),
        modifier = modifier.size(size),
    )
}

package com.nativelap.eumshoppingmall.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.figma_splash_visual),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to MaterialTheme.eumColors.splashScrimStart,
                        0.45f to MaterialTheme.eumColors.splashScrimMiddle,
                        1f to MaterialTheme.eumColors.splashScrimEnd,
                    ),
                ),
            ),
        )
        Column(
            modifier = Modifier.fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(start = EumSpacing.x32, end = EumSpacing.x32, top = 256.dp, bottom = EumSpacing.x40),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = EumTypographyTokens.webDisplayL,
                    color = MaterialTheme.eumColors.inverse,
                    maxLines = 1,
                )
                Text(
                    text = stringResource(R.string.splash_tagline),
                    style = EumTypographyTokens.webBodyXl,
                    color = MaterialTheme.eumColors.inverse,
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.splash_caption_safety),
                    style = EumTypographyTokens.overline,
                    color = MaterialTheme.eumColors.inverse,
                )
                Text(
                    text = stringResource(R.string.splash_caption_connects),
                    style = EumTypographyTokens.overline,
                    color = MaterialTheme.eumColors.inverse,
                )
                Text(
                    text = stringResource(R.string.splash_caption_tomorrow),
                    style = EumTypographyTokens.overline,
                    color = MaterialTheme.eumColors.inverse,
                )
            }
        }
    }
}

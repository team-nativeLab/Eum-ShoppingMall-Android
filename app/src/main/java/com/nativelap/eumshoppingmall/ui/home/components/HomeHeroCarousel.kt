package com.nativelap.eumshoppingmall.ui.home.components

import android.animation.ValueAnimator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import kotlinx.coroutines.delay

private data class HomeHeroSlide(
    val imageResId: Int,
    val eyebrowResId: Int,
    val titleResId: Int,
    val descriptionResId: Int,
    val actionResId: Int,
    val imageDescriptionResId: Int,
)

private val homeHeroSlides = listOf(
    HomeHeroSlide(
        imageResId = R.drawable.figma_home_hero_safe_tomorrow,
        eyebrowResId = R.string.home_hero_safe_eyebrow,
        titleResId = R.string.home_hero_safe_title,
        descriptionResId = R.string.home_hero_safe_description,
        actionResId = R.string.home_hero_browse_products,
        imageDescriptionResId = R.string.home_hero_safe_image_description,
    ),
    HomeHeroSlide(
        imageResId = R.drawable.figma_home_hero_custom_solution,
        eyebrowResId = R.string.home_hero_custom_eyebrow,
        titleResId = R.string.home_hero_custom_title,
        descriptionResId = R.string.home_hero_custom_description,
        actionResId = R.string.home_hero_quote_action,
        imageDescriptionResId = R.string.home_hero_custom_image_description,
    ),
    HomeHeroSlide(
        imageResId = R.drawable.figma_home_hero_sustainable_value,
        eyebrowResId = R.string.home_hero_sustainable_eyebrow,
        titleResId = R.string.home_hero_sustainable_title,
        descriptionResId = R.string.home_hero_sustainable_description,
        actionResId = R.string.home_hero_company_action,
        imageDescriptionResId = R.string.home_hero_sustainable_image_description,
    ),
)

@Composable
fun HomeHeroCarousel(
    onActionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { homeHeroSlides.size })
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var autoAdvanceResetKey by remember { mutableIntStateOf(0) }
    val lifecycleOwner = LocalView.current.findViewTreeLifecycleOwner()
    var isResumed by remember(lifecycleOwner) {
        mutableStateOf(lifecycleOwner?.lifecycle?.currentState == Lifecycle.State.RESUMED)
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner?.lifecycle
        val observer = LifecycleEventObserver { _, _ ->
            isResumed = lifecycle?.currentState == Lifecycle.State.RESUMED
        }
        lifecycle?.addObserver(observer)
        onDispose { lifecycle?.removeObserver(observer) }
    }

    LaunchedEffect(isResumed, isDragged, autoAdvanceResetKey) {
        if (!isResumed || isDragged || !ValueAnimator.areAnimatorsEnabled()) return@LaunchedEffect
        while (true) {
            delay(HOME_HERO_AUTO_ADVANCE_MILLIS)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % homeHeroSlides.size)
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val fontScale = LocalDensity.current.fontScale
        val horizontalMargin = 20.dp
        val cardWidth = (maxWidth - horizontalMargin * 2).coerceAtMost(HOME_HERO_MAX_CARD_WIDTH)
        val contentSidePadding = ((maxWidth - cardWidth) / 2).coerceAtLeast(horizontalMargin)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(HOME_HERO_HEIGHT * fontScale),
            pageSize = PageSize.Fixed(cardWidth),
            contentPadding = PaddingValues(horizontal = contentSidePadding),
            pageSpacing = EumSpacing.x20,
            beyondViewportPageCount = 1,
        ) { page ->
            val slide = homeHeroSlides[page]
            HomeHeroCard(
                slide = slide,
                pageIndex = page,
                pageCount = homeHeroSlides.size,
                isCurrentPage = pagerState.currentPage == page,
                onActionClick = { onActionClick(page) },
                onIndicatorClick = { targetPage ->
                    autoAdvanceResetKey += 1
                    pagerState.requestScrollToPage(targetPage)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun HomeHeroCard(
    slide: HomeHeroSlide,
    pageIndex: Int,
    pageCount: Int,
    isCurrentPage: Boolean,
    onActionClick: () -> Unit,
    onIndicatorClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageDescription = stringResource(slide.imageDescriptionResId)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(EumRadius.x12))
            .semantics {
                contentDescription = imageDescription
            },
    ) {
        Image(
            painter = painterResource(slide.imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        0f to MaterialTheme.eumColors.heroScrimStart,
                        0.85f to MaterialTheme.eumColors.heroScrimEnd,
                        1f to MaterialTheme.eumColors.heroScrimEnd,
                    ),
                ),
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = EumSpacing.x24, top = EumSpacing.x24, end = EumSpacing.x16),
        ) {
            Text(
                text = stringResource(slide.eyebrowResId),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.76.sp),
                color = MaterialTheme.eumColors.inverseSecondary,
            )
            Spacer(Modifier.height(EumSpacing.x8))
            Text(
                text = stringResource(slide.titleResId),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.eumColors.inverse,
            )
            Spacer(Modifier.height(EumSpacing.x6))
            Text(
                text = stringResource(slide.descriptionResId),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.inverseSecondary,
                maxLines = 2,
            )
            Spacer(Modifier.height(EumSpacing.x12))
            HomeHeroActionButton(stringResource(slide.actionResId), onActionClick)
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = EumSpacing.x8),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount) { index ->
                val selected = index == pageIndex && isCurrentPage
                val pageDescription = stringResource(R.string.common_page_count, index + 1, pageCount)
                Box(
                    modifier = Modifier
                        .size(width = 44.dp, height = 44.dp)
                        .clickable { onIndicatorClick(index) }
                        .semantics {
                            contentDescription = pageDescription
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier.size(width = if (selected) 12.dp else 6.dp, height = 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.inverseSecondary,
                            ),
                    )
                }
            }
        }
    }
}

private val HOME_HERO_HEIGHT: Dp = 224.dp
private val HOME_HERO_MAX_CARD_WIDTH: Dp = 560.dp
private const val HOME_HERO_AUTO_ADVANCE_MILLIS = 5_000L

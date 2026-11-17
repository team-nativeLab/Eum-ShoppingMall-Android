package com.nativelap.eumshoppingmall.ui.home.components

import android.animation.ValueAnimator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
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
import com.nativelap.eumshoppingmall.ui.theme.isReducedMotion
import kotlinx.coroutines.delay
import kotlin.math.abs

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
    onQuoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { homeHeroSlides.size })
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var autoAdvanceResetKey by remember { mutableIntStateOf(0) }
    // 사용자가 누르거나 스와이프·점 탭을 하면 자동 넘김을 멈추고, 마지막 조작 뒤 HERO_INTERACTION_PAUSE_MILLIS 뒤에 재개한다.
    val lastInteraction = remember { HeroInteraction() }
    fun onUserInteraction() {
        lastInteraction.atMillis = System.currentTimeMillis()
        autoAdvanceResetKey += 1
    }
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

    // 치환 전후 차이 없음: 기존처럼 자동 넘김을 재개할 때마다 줄이기 설정을 다시 읽는다.
    LaunchedEffect(isResumed, isDragged, autoAdvanceResetKey) {
        if (!isResumed || isDragged || isReducedMotion(ValueAnimator.areAnimatorsEnabled())) return@LaunchedEffect
        while (true) {
            delay(heroAutoAdvanceDelayMillis(lastInteraction.atMillis, System.currentTimeMillis(), HOME_HERO_AUTO_ADVANCE_MILLIS))
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
                .height(HOME_HERO_HEIGHT * fontScale)
                .pointerInput(Unit) {
                    // 누름만 관찰하고 소비하지 않는다. 버튼 탭과 스와이프는 그대로 전달된다.
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            if (event.changes.any { change -> change.pressed && !change.previousPressed }) onUserInteraction()
                        }
                    }
                },
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
                onQuoteClick = onQuoteClick,
                onIndicatorClick = { targetPage ->
                    onUserInteraction()
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
    onQuoteClick: () -> Unit,
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
                .padding(start = EumSpacing.x24, top = EumSpacing.x16, end = EumSpacing.x16),
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
            Spacer(Modifier.height(EumSpacing.x8))
            // 제품 둘러보기 옆에 보조 CTA를 둔다. 커스텀 견적 슬라이드는 주 버튼이 이미 견적이라 보조 CTA를 숨긴다.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                if (homeHeroTargetForAction(slide.actionResId) != null) {
                    HomeHeroActionButton(stringResource(slide.actionResId), onActionClick)
                }
                if (showsHeroSecondaryQuoteCta(slide.actionResId)) {
                    Box(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .clickable(role = Role.Button, onClick = onQuoteClick)
                            .padding(horizontal = EumSpacing.x4),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            stringResource(R.string.home_quote_action),
                            style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline),
                            color = MaterialTheme.eumColors.inverse,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            // 점은 버튼 바로 아래에 둔다. 버튼 하단과 점 상단 사이는 HERO_INDICATOR_TOP_GAP이다.
            val indicatorLayout = remember(pageCount) { HeroIndicatorLayout(pageCount) }
            Box(modifier = Modifier.padding(top = HERO_INDICATOR_TOP_GAP)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(pageCount) { index ->
                        val selected = index == pageIndex && isCurrentPage
                        val pageDescription = stringResource(R.string.common_page_count, index + 1, pageCount)
                        // 시각 점은 기존 위치와 크기(알약 12dp, 나머지 6dp)를 그대로 둔다. 레이아웃 높이는 44dp 그대로다.
                        Box(
                            modifier = Modifier
                                .size(width = if (selected) 12.dp else 6.dp, height = HERO_INDICATOR_SLOT_HEIGHT)
                                .onGloballyPositioned { coordinates ->
                                    indicatorLayout.dotCentersX[index] =
                                        coordinates.positionInRoot().x + coordinates.size.width / 2f
                                }
                                .semantics {
                                    contentDescription = pageDescription
                                    role = Role.Button
                                    onClick(label = pageDescription) {
                                        onIndicatorClick(index)
                                        true
                                    }
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
                // 점마다 48dp 터치를 겹쳐 두면 이웃 점의 터치와 겹쳐 가운데 점이 눌리지 않는다.
                // 그래서 점 줄 위에 48dp 높이의 띠 하나를 두고, 탭한 x에 가장 가까운 점을 고른다.
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .matchParentSize()
                        .requiredSize(width = HERO_INDICATOR_HIT_WIDTH_PER_DOT * pageCount, height = HERO_INDICATOR_HIT_HEIGHT)
                        .onGloballyPositioned { coordinates -> indicatorLayout.hitAreaLeftX = coordinates.positionInRoot().x }
                        .pointerInput(pageCount) {
                            detectTapGestures { offset ->
                                val index = nearestHeroIndicatorIndex(
                                    tapX = indicatorLayout.hitAreaLeftX + offset.x,
                                    centersX = indicatorLayout.dotCentersX.toList(),
                                )
                                if (index != null) onIndicatorClick(index)
                            }
                        },
                )
            }
        }
    }
}

private val HOME_HERO_HEIGHT: Dp = 224.dp

// 버튼 하단과 점 상단 사이 간격(요청값 3dp). 토큰 목록에 3dp가 없어 이 파일 상수로 둔다.
private val HERO_INDICATOR_TOP_GAP: Dp = 3.dp

// 점 줄의 레이아웃 높이(기존 44dp 유지). 점의 시각 위치가 이 값에 따라 정해진다.
private val HERO_INDICATOR_SLOT_HEIGHT: Dp = 44.dp

// 접근성 최소 터치 크기. 점 줄 위의 터치 띠는 높이 48dp, 점 한 개당 가로 48dp 몫이다.
private val HERO_INDICATOR_HIT_HEIGHT: Dp = 48.dp
private val HERO_INDICATOR_HIT_WIDTH_PER_DOT: Dp = 48.dp

// 사용자 조작 뒤 자동 넘김을 멈추는 시간(요구값 약 15초). 일시정지 버튼은 두지 않는다.
internal const val HERO_INTERACTION_PAUSE_MILLIS = 15_000L

// 자동 넘김까지 기다릴 시간. 조작 뒤 정지 구간 안이면 남은 정지 시간을 먼저 기다리고, 그 뒤 기본 간격을 더한다.
internal fun heroAutoAdvanceDelayMillis(lastInteractionAtMillis: Long?, nowMillis: Long, normalDelayMillis: Long): Long {
    if (lastInteractionAtMillis == null) return normalDelayMillis
    val remaining = lastInteractionAtMillis + HERO_INTERACTION_PAUSE_MILLIS - nowMillis
    return if (remaining > 0) remaining + normalDelayMillis else normalDelayMillis
}

private class HeroInteraction {
    var atMillis: Long? = null
}

// 히어로 버튼이 가는 곳. 주 버튼은 이 대상이 있을 때만 그린다(대상 없는 버튼은 숨긴다).
internal enum class HomeHeroTarget {
    Products,
    Quote,
    CompanyIntro,
}

internal fun homeHeroTargetForAction(actionResId: Int): HomeHeroTarget? = when (actionResId) {
    R.string.home_hero_browse_products -> HomeHeroTarget.Products
    R.string.home_hero_quote_action -> HomeHeroTarget.Quote
    R.string.home_hero_company_action -> HomeHeroTarget.CompanyIntro
    else -> null
}

internal fun homeHeroTargetForSlide(slideIndex: Int): HomeHeroTarget? = homeHeroSlides.getOrNull(slideIndex)?.let { slide -> homeHeroTargetForAction(slide.actionResId) }

// 주 버튼이 이미 견적 문의인 슬라이드(커스텀 견적)에는 보조 견적 CTA를 두지 않는다.
internal fun showsHeroSecondaryQuoteCta(primaryActionResId: Int): Boolean = primaryActionResId != R.string.home_hero_quote_action

// 점 중심 좌표와 탭 위치로 가장 가까운 점을 고른다. 중심 좌표가 없으면 null.
internal fun nearestHeroIndicatorIndex(tapX: Float, centersX: List<Float>): Int? {
    if (centersX.isEmpty()) return null
    return centersX.indices.minByOrNull { index -> abs(centersX[index] - tapX) }
}

// 점 위치를 레이아웃 콜백에서 모은다. 탭 처리는 나중에 이 값을 읽는다.
private class HeroIndicatorLayout(pageCount: Int) {
    val dotCentersX = FloatArray(pageCount)
    var hitAreaLeftX = 0f
}
private val HOME_HERO_MAX_CARD_WIDTH: Dp = 560.dp
private const val HOME_HERO_AUTO_ADVANCE_MILLIS = 5_000L

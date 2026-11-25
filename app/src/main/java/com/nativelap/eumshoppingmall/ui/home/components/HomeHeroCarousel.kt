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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeHeroActionId
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import com.nativelap.eumshoppingmall.ui.theme.isReducedMotion
import kotlinx.coroutines.delay
import kotlin.math.abs

private data class HomeHeroSlide(
    val actionId: HomeHeroActionId,
    val imageResId: Int,
    val eyebrowResId: Int,
    val titleResId: Int,
    val descriptionResId: Int,
    val imageDescriptionResId: Int,
)

private val homeHeroSlides = listOf(
    HomeHeroSlide(
        actionId = HomeHeroActionId.BrowseProducts,
        imageResId = R.drawable.figma_home_hero_safe_tomorrow,
        eyebrowResId = R.string.home_hero_safe_eyebrow,
        titleResId = R.string.home_hero_safe_title,
        descriptionResId = R.string.home_hero_safe_description,
        imageDescriptionResId = R.string.home_hero_safe_image_description,
    ),
    HomeHeroSlide(
        actionId = HomeHeroActionId.BrowseProducts,
        imageResId = R.drawable.figma_home_hero_custom_solution,
        eyebrowResId = R.string.home_hero_custom_eyebrow,
        titleResId = R.string.home_hero_custom_title,
        descriptionResId = R.string.home_hero_custom_description,
        imageDescriptionResId = R.string.home_hero_custom_image_description,
    ),
    HomeHeroSlide(
        actionId = HomeHeroActionId.BrowseProducts,
        imageResId = R.drawable.figma_home_hero_sustainable_value,
        eyebrowResId = R.string.home_hero_sustainable_eyebrow,
        titleResId = R.string.home_hero_sustainable_title,
        descriptionResId = R.string.home_hero_sustainable_description,
        imageDescriptionResId = R.string.home_hero_sustainable_image_description,
    ),
)

/** 슬라이드 순서대로 주 CTA 행동. 테스트가 슬라이드와 행동의 매핑을 확인한다. */
internal fun homeHeroSlideActionIds(): List<HomeHeroActionId> = homeHeroSlides.map { slide -> slide.actionId }

/** 홈 히어로의 우선 행동은 모든 슬라이드에서 상품 탐색이다. */
internal fun homeHeroPrimaryActionId(): HomeHeroActionId = HomeHeroActionId.BrowseProducts

internal fun homeHeroActionLabelResId(actionId: HomeHeroActionId): Int = when (actionId) {
    HomeHeroActionId.BrowseProducts -> R.string.home_hero_browse_products
    HomeHeroActionId.ViewCompany -> R.string.home_hero_company_action
}

@Composable
fun HomeHeroCarousel(
    onActionClick: (HomeHeroActionId) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 가상 페이지로 드래그를 양방향으로 끊김 없이 돌린다. 시작 위치는 가운데 사이클의 첫 슬라이드다.
    val pagerState = rememberPagerState(
        initialPage = heroCycleBasePage(homeHeroSlides.size),
        pageCount = { homeHeroVirtualPageCount },
    )
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var autoAdvanceResetKey by remember { mutableIntStateOf(0) }
    // 접근성 결정 기록: docs/WEB_FORMAT_AUDIT.md '접근성 결정 기록 (히어로 자동 넘김, WCAG 2.2.2)'. 정지 수단을 바꾸기 전에 확인한다.
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
        if (!heroAutoAdvanceEnabled(isResumed, isDragged, isReducedMotion(ValueAnimator.areAnimatorsEnabled()))) return@LaunchedEffect
        while (true) {
            delay(heroAutoAdvanceDelayMillis(lastInteraction.atMillis, System.currentTimeMillis(), HOME_HERO_AUTO_ADVANCE_MILLIS))
            pagerState.animateScrollToPage(heroNextVirtualPage(pagerState.currentPage, homeHeroSlides.size, homeHeroVirtualPageCount))
        }
    }

    // 드래그로 가상 페이지 양 끝 한 바퀴 안에 정착하면 같은 슬라이드의 가운데 사이클 페이지로 애니메이션 없이 옮겨 순환을 이어 간다.
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { settledPage ->
            val rebasedPage = heroEdgeRebasedPage(settledPage, homeHeroSlides.size, homeHeroVirtualPageCount)
            if (rebasedPage != settledPage) pagerState.scrollToPage(rebasedPage)
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val fontScale = LocalDensity.current.fontScale
        val horizontalMargin = HERO_HORIZONTAL_MARGIN
        val cardWidth = (maxWidth - horizontalMargin * 2).coerceAtMost(HOME_HERO_MAX_CARD_WIDTH)
        val contentSidePadding = ((maxWidth - cardWidth) / 2).coerceAtLeast(horizontalMargin)
        val shouldStackActions = shouldStackHeroActions(heroActionContentWidth(maxWidth), fontScale)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(homeHeroHeight(shouldStackActions, fontScale))
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
            val slideIndex = heroSlideIndex(page, homeHeroSlides.size)
            HomeHeroCard(
                slide = homeHeroSlides[slideIndex],
                slideIndex = slideIndex,
                pageCount = homeHeroSlides.size,
                isCurrentPage = pagerState.currentPage == page,
                shouldStackActions = shouldStackActions,
                onActionClick = { onActionClick(homeHeroPrimaryActionId()) },
                onIndicatorClick = { targetSlideIndex ->
                    onUserInteraction()
                    pagerState.requestScrollToPage(
                        heroTargetVirtualPage(pagerState.currentPage, targetSlideIndex, homeHeroSlides.size, homeHeroVirtualPageCount),
                    )
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun HomeHeroCard(
    slide: HomeHeroSlide,
    slideIndex: Int,
    pageCount: Int,
    isCurrentPage: Boolean,
    shouldStackActions: Boolean,
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
                .fillMaxWidth()
                .padding(
                    start = HERO_CONTENT_START_PADDING,
                    top = EumSpacing.x16,
                    end = HERO_CONTENT_END_PADDING,
                    // 텍스트·버튼 열의 아래쪽을 점 슬롯 높이만큼 비워 점 줄과 레이아웃을 분리한다.
                    bottom = HERO_INDICATOR_SLOT_HEIGHT,
                ),
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
                // 설명은 항상 2줄 높이를 차지해, 짧은 슬라이드와 긴 슬라이드 사이에서 아래 버튼 위치가 흔들리지 않는다.
                minLines = 2,
                maxLines = 2,
            )
            Spacer(Modifier.height(EumSpacing.x8))
            val actionResId = homeHeroActionLabelResId(homeHeroPrimaryActionId())
            if (shouldStackActions) {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                    HomeHeroActionButton(stringResource(actionResId), onActionClick, isPrimary = true)
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HomeHeroActionButton(stringResource(actionResId), onActionClick, isPrimary = true)
                }
            }
        }

        // 점 줄은 배너 전체 폭의 가운데, 카드 맨 아래의 점 슬롯(40dp) 안에 둔다. 텍스트 열 아래에 있어 버튼과 겹치지 않는다.
        // 바깥 Box가 배너 폭 전체를 쓰고, 안쪽 Box는 가운데 정렬해서 히트 띠의 폭과 무관하게 점이 가운데 온다.
        val indicatorLayout = remember(pageCount) { HeroIndicatorLayout(pageCount) }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(HERO_INDICATOR_SLOT_HEIGHT),
            contentAlignment = Alignment.Center,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(pageCount) { index ->
                        val selected = index == slideIndex && isCurrentPage
                        val pageDescription = stringResource(R.string.common_page_count, index + 1, pageCount)
                        // 시각 점은 기존 위치와 크기(알약 12dp, 나머지 6dp)를 그대로 둔다. 레이아웃 높이는 점 슬롯 높이(40dp)를 따른다.
                        Box(
                            modifier = Modifier
                                .size(width = if (selected) 12.dp else 6.dp, height = HERO_INDICATOR_SLOT_HEIGHT)
                                .onGloballyPositioned { coordinates ->
                                    indicatorLayout.dotCentersX[index] =
                                        coordinates.positionInRoot().x + coordinates.size.width / 2f
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            // 접근성 클릭 대상은 점마다 최소 24dp로 잡는다. 점의 시각 크기와 간격은 그대로다(N6).
                            Box(
                                Modifier
                                    .requiredSize(width = HERO_INDICATOR_MIN_TOUCH, height = HERO_INDICATOR_SLOT_HEIGHT)
                                    .semantics {
                                        contentDescription = pageDescription
                                        role = Role.Button
                                        onClick(label = pageDescription) {
                                            onIndicatorClick(index)
                                            true
                                        }
                                    },
                            )
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

internal fun shouldStackHeroActions(maxWidth: Dp, fontScale: Float): Boolean = maxWidth < 280.dp || fontScale >= 1.3f

internal fun heroActionContentWidth(maxWidth: Dp): Dp = ((maxWidth - HERO_HORIZONTAL_MARGIN * 2).coerceAtMost(HOME_HERO_MAX_CARD_WIDTH) - HERO_CONTENT_START_PADDING - HERO_CONTENT_END_PADDING)
    .coerceAtLeast(0.dp)

internal fun homeHeroHeight(shouldStackActions: Boolean, fontScale: Float): Dp = (HOME_HERO_HEIGHT + if (shouldStackActions) HOME_HERO_COMPACT_EXTRA_HEIGHT else 0.dp) * fontScale

/** 히어로 자동 넘김을 돌릴지 판정한다. 화면이 보이고, 드래그 중이 아니며, 줄이기 설정이 꺼져 있을 때만 돈다. */
internal fun heroAutoAdvanceEnabled(isResumed: Boolean, isDragged: Boolean, isReducedMotion: Boolean): Boolean = isResumed && !isDragged && !isReducedMotion

private val HOME_HERO_HEIGHT: Dp = 224.dp
private val HOME_HERO_COMPACT_EXTRA_HEIGHT: Dp = 48.dp
private val HERO_HORIZONTAL_MARGIN: Dp = 20.dp
private val HERO_CONTENT_START_PADDING: Dp = 24.dp
private val HERO_CONTENT_END_PADDING: Dp = 16.dp

// 점 줄의 레이아웃 높이. 기기 측정에서 44dp면 점의 터치 노드가 버튼 하단과 1px 겹쳐 40dp로 줄였다(카드 맨 아래에 붙는다).
private val HERO_INDICATOR_SLOT_HEIGHT: Dp = 40.dp

// 점 하나의 접근성 클릭 최소 폭(WCAG 2.5.8). 시각 점은 그대로 두고 클릭 대상만 넓힌다.
private val HERO_INDICATOR_MIN_TOUCH: Dp = 24.dp

// 점 줄 위의 터치 띠 높이는 점 슬롯 높이(40dp)와 같게 둔다. 점 한 개당 가로 48dp 몫이다.
private val HERO_INDICATOR_HIT_HEIGHT: Dp = 40.dp
private val HERO_INDICATOR_HIT_WIDTH_PER_DOT: Dp = 48.dp

// 사용자 조작 뒤 자동 넘김을 멈추는 시간(요구값 약 15초). 일시정지 버튼은 두지 않는다.
internal const val HERO_INTERACTION_PAUSE_MILLIS = 15_000L

// 가상 페이지 사이클 수. 전체 가상 페이지 수는 슬라이드 수 × 사이클 수이고, 시작은 가운데 사이클이다.
internal const val HERO_VIRTUAL_CYCLES = 1000

private val homeHeroVirtualPageCount: Int = homeHeroSlides.size * HERO_VIRTUAL_CYCLES

// 가운데 사이클의 첫 페이지. 이 값은 슬라이드 수의 배수이므로 첫 슬라이드를 가리킨다.
internal fun heroCycleBasePage(slideCount: Int): Int = slideCount * (HERO_VIRTUAL_CYCLES / 2)

// 가상 페이지가 가리키는 슬라이드 번호. 음수 페이지도 같은 순환으로 맞춘다.
internal fun heroSlideIndex(virtualPage: Int, slideCount: Int): Int = Math.floorMod(virtualPage, slideCount)

// 점을 눌렀을 때 갈 가상 페이지. 현재 페이지에서 가장 가까운 해당 슬라이드를 고른다(앞·뒤 중 짧은 쪽).
// 범위 밖이면 가운데 사이클의 같은 슬라이드로 옮긴다.
internal fun heroTargetVirtualPage(currentVirtualPage: Int, slideIndex: Int, slideCount: Int, virtualPageCount: Int): Int {
    val forwardSteps = Math.floorMod(slideIndex - heroSlideIndex(currentVirtualPage, slideCount), slideCount)
    val steps = if (forwardSteps <= slideCount / 2) forwardSteps else forwardSteps - slideCount
    val candidate = currentVirtualPage + steps
    return if (candidate in 0 until virtualPageCount) candidate else heroCycleBasePage(slideCount) + slideIndex
}

// 자동 넘김 목표 페이지. 한 칸씩 넘기며, 끝에 닿으면 같은 슬라이드를 가운데 사이클에서 이어 간다.
internal fun heroNextVirtualPage(currentVirtualPage: Int, slideCount: Int, virtualPageCount: Int): Int {
    val nextPage = currentVirtualPage + 1
    return if (nextPage < virtualPageCount) nextPage else heroCycleBasePage(slideCount) + heroSlideIndex(nextPage, slideCount)
}

// 정착한 가상 페이지가 사이클 양 끝(첫 슬라이드 한 바퀴, 마지막 한 바퀴) 안이면 가운데 사이클의 같은 슬라이드로 옮긴다. 아니면 그대로 둔다.
internal fun heroEdgeRebasedPage(settledPage: Int, slideCount: Int, virtualPageCount: Int): Int {
    val isNearStart = settledPage < slideCount
    val isNearEnd = settledPage >= virtualPageCount - slideCount
    return if (isNearStart || isNearEnd) heroCycleBasePage(slideCount) + heroSlideIndex(settledPage, slideCount) else settledPage
}

// 자동 넘김까지 기다릴 시간. 조작 뒤 정지 구간 안이면 남은 정지 시간을 먼저 기다리고, 그 뒤 기본 간격을 더한다.
internal fun heroAutoAdvanceDelayMillis(lastInteractionAtMillis: Long?, nowMillis: Long, normalDelayMillis: Long): Long {
    if (lastInteractionAtMillis == null) return normalDelayMillis
    val remaining = lastInteractionAtMillis + HERO_INTERACTION_PAUSE_MILLIS - nowMillis
    return if (remaining > 0) remaining + normalDelayMillis else normalDelayMillis
}

private class HeroInteraction {
    var atMillis: Long? = null
}

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

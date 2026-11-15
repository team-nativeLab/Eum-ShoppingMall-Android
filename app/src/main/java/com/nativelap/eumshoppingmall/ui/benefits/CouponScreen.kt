package com.nativelap.eumshoppingmall.ui.benefits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.common.EmptyCollectionState
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CouponScreen(
    uiModel: CouponScreenUiModel,
    onEvent: (CouponScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val couponCodeLabel = stringResource(R.string.coupon_code_hint)
    Column(modifier.fillMaxSize()) {
        CatalogTopBar(
            title = stringResource(R.string.coupon_title),
            onBackClick = { onEvent(CouponScreenEvent.BackClicked) },
            modifier = Modifier.statusBarsPadding(),
        )
        if (uiModel.availableCoupons.isEmpty() && uiModel.usedExpiredCoupons.isEmpty()) {
            EmptyCollectionState(
                title = stringResource(R.string.coupon_empty_title),
                description = stringResource(R.string.coupon_empty_description),
                actionLabel = stringResource(R.string.coupon_empty_action),
                onAction = { onEvent(CouponScreenEvent.BrowseProductsClicked) },
            )
        } else {
            Column(Modifier.fillMaxSize().navigationBarsPadding().imePadding()) {
                // Figma: 등록 행 위 여백 16, 섹션 간 20, 필드 높이는 48dp 유지, 버튼 폭 58.
                Row(
                    Modifier.fillMaxWidth().padding(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x16),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        value = uiModel.registeredCode,
                        onValueChange = { onEvent(CouponScreenEvent.CodeChanged(it)) },
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp).clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.eumColors.surface).border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(8.dp))
                            .padding(horizontal = EumSpacing.x12).semantics { contentDescription = couponCodeLabel },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.eumColors.textPrimary),
                        decorationBox = { inner ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (uiModel.registeredCode.isEmpty()) Text(stringResource(R.string.coupon_code_hint), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                                inner()
                            }
                        },
                    )
                    Spacer(Modifier.width(EumSpacing.x8))
                    Button(
                        onClick = { onEvent(CouponScreenEvent.RegisterClicked) },
                        modifier = Modifier.widthIn(min = COUPON_REGISTER_BUTTON_WIDTH).heightIn(min = 48.dp),
                        contentPadding = PaddingValues(horizontal = EumSpacing.x12),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.eumColors.textPrimary),
                    ) { Text(stringResource(R.string.coupon_register), color = MaterialTheme.eumColors.inverse) }
                }
                Spacer(Modifier.height(EumSpacing.x20))
                // Figma 포인트 링크: 54dp, coins 20 + 라벨 + 잔액 + chevron-right 16.
                Surface(
                    onClick = { onEvent(CouponScreenEvent.PointsClicked) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.eumColors.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.eumColors.line),
                ) {
                    Row(
                        Modifier.heightIn(min = COUPON_POINTS_LINK_HEIGHT).padding(horizontal = EumSpacing.x16),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        Icon(painterResource(R.drawable.ic_coins), contentDescription = null, tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
                        Text(stringResource(R.string.points_balance), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        Text(uiModel.pointsBalanceLabel, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
                        Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = MaterialTheme.eumColors.textSecondary, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.height(EumSpacing.x20))
                // Figma 세그먼트: 높이 46(테두리 1 + 패딩 4 + 탭 36 + 패딩 4 + 테두리 1).
                // 탭 클릭 영역이 컨테이너 높이 46 전체를 쓰고 시각 36 박스는 그 안쪽에 둔다. 터치 높이 46은 48보다 2dp 작다.
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20).heightIn(min = COUPON_SEGMENT_HEIGHT).height(IntrinsicSize.Min)
                        .border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(8.dp))
                        .padding(horizontal = COUPON_SEGMENT_PADDING),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    CouponTab.entries.forEach { tab ->
                        val selected = uiModel.selectedTab == tab
                        Box(
                            Modifier.weight(1f).fillMaxHeight()
                                .clickable(role = Role.Tab, onClick = { onEvent(CouponScreenEvent.TabSelected(tab)) })
                                .semantics { this.selected = selected },
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                Modifier.fillMaxWidth().heightIn(min = COUPON_SEGMENT_TAB_HEIGHT).clip(RoundedCornerShape(6.dp))
                                    .background(if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    stringResource(if (tab == CouponTab.Available) R.string.coupon_available_tab else R.string.coupon_used_expired_tab, uiModel.countFor(tab)),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
                                )
                            }
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x20, bottom = EumSpacing.x20),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    if (uiModel.selectedCoupons.isEmpty()) {
                        item {
                            Column(
                                Modifier.fillMaxWidth().padding(vertical = EumSpacing.x40),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    stringResource(if (uiModel.selectedTab == CouponTab.Available) R.string.coupon_empty_title else R.string.coupon_used_expired_empty_title),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.eumColors.textPrimary,
                                )
                                Text(
                                    stringResource(if (uiModel.selectedTab == CouponTab.Available) R.string.coupon_empty_description else R.string.coupon_used_expired_empty_description),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.eumColors.textSecondary,
                                )
                            }
                        }
                    } else {
                        items(uiModel.selectedCoupons, key = { it.id }) { coupon ->
                            CouponCard(coupon) { onEvent(CouponScreenEvent.CouponClicked(coupon.id)) }
                        }
                    }
                }
            }
        }
    }
}

private val COUPON_REGISTER_BUTTON_WIDTH = 58.dp
private val COUPON_POINTS_LINK_HEIGHT = 54.dp
private val COUPON_SEGMENT_PADDING = 4.dp
private val COUPON_SEGMENT_HEIGHT = 46.dp
private val COUPON_SEGMENT_TAB_HEIGHT = 36.dp
private val COUPON_STUB_WIDTH = 76.dp
private val COUPON_DIVIDER_WIDTH = 1.dp

// Figma 노드 355:3948의 '상하 12px 원'은 지름 12이므로 반경은 6이다.
private val COUPON_NOTCH_RADIUS = 6.dp
private val COUPON_BODY_START_PADDING = 20.dp
private val COUPON_BADGE_VERTICAL_PADDING = 3.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CouponCard(coupon: CouponUiModel, onClick: () -> Unit) {
    val ticketShape = remember { CouponTicketShape(EumRadius.x12, COUPON_NOTCH_RADIUS, COUPON_STUB_WIDTH, COUPON_DIVIDER_WIDTH) }
    val perforationColor = MaterialTheme.eumColors.lineStrong
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = ticketShape,
        color = MaterialTheme.eumColors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Row(Modifier.heightIn(min = 132.dp).height(IntrinsicSize.Min)) {
            Column(
                Modifier.weight(1f).padding(start = COUPON_BODY_START_PADDING, end = EumSpacing.x16, top = EumSpacing.x16, bottom = EumSpacing.x16),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            ) {
                // 좁은 폭·큰 글자에서 할인 수치가 눌리지 않도록 배지를 아래 줄로 넘긴다.
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(coupon.discountLabel, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.eumColors.textPrimary)
                    coupon.badgeLabel?.let {
                        Text(
                            it,
                            Modifier.clip(RoundedCornerShape(EumRadius.x4)).background(MaterialTheme.eumColors.textPrimary)
                                .padding(horizontal = EumSpacing.x6, vertical = COUPON_BADGE_VERTICAL_PADDING),
                            style = EumTypographyTokens.labelXs,
                            color = MaterialTheme.eumColors.inverse,
                        )
                    }
                }
                Text(coupon.name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                Text(coupon.conditionLabel, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
                Text(coupon.expiryLabel, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textPrimary)
            }
            // 절취선: 노치 사이를 잇는 세로 파선.
            Box(
                Modifier.width(COUPON_DIVIDER_WIDTH).fillMaxHeight().padding(vertical = COUPON_NOTCH_RADIUS).drawBehind {
                    drawLine(
                        color = perforationColor,
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = size.width,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx())),
                    )
                },
            )
            Column(
                Modifier.width(COUPON_STUB_WIDTH).fillMaxHeight().padding(horizontal = EumSpacing.x4),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_ticket),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textPrimary,
                    modifier = Modifier.size(20.dp),
                )
                Text(coupon.applicableLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
            }
        }
    }
}

/** 구분선 위치의 위·아래 가장자리에 반원 노치가 파인 쿠폰 티켓 모양. 테두리도 같은 윤곽을 따른다. */
private class CouponTicketShape(
    private val cornerRadius: Dp,
    private val notchRadius: Dp,
    private val stubWidth: Dp,
    private val dividerWidth: Dp,
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cornerPx = with(density) { cornerRadius.toPx() }
        val notchPx = with(density) { notchRadius.toPx() }
        val dividerCenterFromEnd = with(density) { (stubWidth + dividerWidth / 2).toPx() }
        val notchCenterX = if (layoutDirection == LayoutDirection.Ltr) size.width - dividerCenterFromEnd else dividerCenterFromEnd
        val cardPath = Path().apply { addRoundRect(RoundRect(0f, 0f, size.width, size.height, CornerRadius(cornerPx))) }
        val notchPath = Path().apply {
            addOval(Rect(Offset(notchCenterX, 0f), notchPx))
            addOval(Rect(Offset(notchCenterX, size.height), notchPx))
        }
        return Outline.Generic(Path.combine(PathOperation.Difference, cardPath, notchPath))
    }
}

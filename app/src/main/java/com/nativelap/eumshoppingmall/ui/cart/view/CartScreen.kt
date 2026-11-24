package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartScreenEvent
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.orderableItems
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.selectableItems
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogBottomNavigation
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.common.EumBottomBarMetrics
import com.nativelap.eumshoppingmall.ui.common.EumPrimaryButton
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.common.ResponsiveWidthContainer
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumCheckboxColors
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CartScreen(
    uiModel: CartUiModel,
    onEvent: (CartScreenEvent) -> Unit,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier,
    cartItemCount: Int = 0,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    quantityLockedItemIds: Set<String> = emptySet(),
) {
    val orderableItems = uiModel.items.orderableItems()
    val selectableItems = uiModel.items.selectableItems()
    val isAllSelected = selectableItems.isNotEmpty() && selectableItems.all { it.isSelected }
    val selectedCount = selectableItems.count { it.isSelected }
    val undisclosedItems = uiModel.items.filter { it.type == CartItemType.PriceUndisclosed }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { EumSnackbarHost(hostState = snackbarHostState) },
        topBar = {
            // 로그인 안내는 항상 보이도록 상단 바 아래에 둔다. 빈 장바구니에서도 보인다.
            Column {
                CatalogTopBar(
                    title = stringResource(R.string.common_cart),
                    actions = listOf(CatalogTopBarAction("search", R.drawable.ic_search, stringResource(R.string.common_search))),
                    onActionClick = { onEvent(CartScreenEvent.SearchClicked) },
                    leadingAction = CatalogTopBarAction("drawer", R.drawable.ic_menu, stringResource(R.string.account_drawer_open)),
                    onLeadingActionClick = { onEvent(CartScreenEvent.MenuClicked) },
                    modifier = Modifier.statusBarsPadding(),
                )
                if (!isLoggedIn) {
                    CartLoginNoticeBanner(
                        onClick = { onEvent(CartScreenEvent.LoginClicked) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        bottomBar = {
            Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
                ResponsiveWidthContainer {
                    CartBottomActionBar(
                        uiModel = uiModel,
                        selectedCount = selectedCount,
                        onCheckoutClicked = { onEvent(CartScreenEvent.CheckoutClicked) },
                    )
                }
                CatalogBottomNavigation(
                    selectedItem = MainNavigationTab.Cart,
                    onItemSelected = { onEvent(CartScreenEvent.BottomTabSelected(it)) },
                    cartItemCount = cartItemCount,
                )
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        ResponsiveContentContainer(Modifier.fillMaxSize().padding(contentPadding)) {
            if (uiModel.items.isEmpty()) {
                Column(
                    Modifier.fillMaxSize().padding(EumSpacing.x20),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(stringResource(R.string.cart_empty), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary)
                    Text(
                        stringResource(R.string.cart_empty_description),
                        modifier = Modifier.padding(top = EumSpacing.x8),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                    EumPrimaryButton(
                        onClick = { onEvent(CartScreenEvent.BrowseProductsClicked) },
                        modifier = Modifier.padding(top = EumSpacing.x24),
                    ) {
                        Text(stringResource(R.string.cart_browse_products))
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                ) {
                    item {
                        Row(
                            Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = EumSpacing.x12),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                colors = eumCheckboxColors(),
                                checked = isAllSelected,
                                onCheckedChange = { onEvent(CartScreenEvent.SelectAllChanged(it)) },
                                enabled = selectableItems.isNotEmpty(),
                            )
                            Text(
                                stringResource(R.string.common_select_all_progress, selectedCount, selectableItems.size),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.labelMedium,
                            )
                            TextButton(
                                onClick = { onEvent(CartScreenEvent.DeleteSelectedClicked) },
                                enabled = selectedCount > 0,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.eumColors.danger,
                                    disabledContentColor = MaterialTheme.eumColors.textDisabled,
                                ),
                            ) {
                                Text(stringResource(R.string.common_delete_selected))
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.eumColors.line)
                    }
                    if (orderableItems.isNotEmpty()) {
                        // 주문 가능·불가 구역이 함께 있을 때만 구역 제목을 보여 준다(Figma A-07 대비 A-07h).
                        if (undisclosedItems.isNotEmpty()) {
                            item { CartSectionLabel(stringResource(R.string.cart_orderable_products, selectableItems.size)) }
                        }
                        items(orderableItems, key = { it.id }) { item ->
                            Column(Modifier.padding(horizontal = EumSpacing.x20)) {
                                CartProductRow(
                                    item = item,
                                    onSelectionChanged = { onEvent(CartScreenEvent.ItemSelectionChanged(item.id, it)) },
                                    onQuantityChanged = { onEvent(CartScreenEvent.QuantityChanged(item.id, it)) },
                                    onOptionChangeClicked = { onEvent(CartScreenEvent.OptionChangeClicked(item.id)) },
                                    onRemoveClicked = { onEvent(CartScreenEvent.ItemRemoved(item.id)) },
                                    isQuantityLocked = item.id in quantityLockedItemIds,
                                )
                                HorizontalDivider(color = MaterialTheme.eumColors.line)
                            }
                        }
                    }
                    if (undisclosedItems.isNotEmpty()) {
                        item {
                            Text(
                                stringResource(R.string.cart_price_undisclosed_products, undisclosedItems.size),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                        items(undisclosedItems, key = { it.id }) { item ->
                            Column(Modifier.padding(horizontal = EumSpacing.x20)) {
                                CartProductRow(
                                    item = item,
                                    onSelectionChanged = {},
                                    onQuantityChanged = { onEvent(CartScreenEvent.QuantityChanged(item.id, it)) },
                                    onOptionChangeClicked = { onEvent(CartScreenEvent.OptionChangeClicked(item.id)) },
                                    onRemoveClicked = { onEvent(CartScreenEvent.ItemRemoved(item.id)) },
                                    isQuantityLocked = item.id in quantityLockedItemIds,
                                )
                                // 가격이 표시되지 않은 줄은 주문할 수 없으므로 상품명을 채운 1:1 문의로 안내한다.
                                TextButton(
                                    onClick = { onEvent(CartScreenEvent.PriceInquiryClicked(item.id)) },
                                    // 기본 좌우 여백 12dp를 빼 위 줄(상품 행)의 시작선과 맞춘다.
                                    contentPadding = PaddingValues(horizontal = 0.dp),
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                                ) {
                                    Text(stringResource(R.string.cart_price_inquiry_action), textDecoration = TextDecoration.Underline)
                                }
                                HorizontalDivider(color = MaterialTheme.eumColors.line)
                            }
                        }
                    }
                    item {
                        CartOrderSummaryCard(
                            uiModel = uiModel,
                            modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartSectionLabel(label: String) {
    Text(
        text = label,
        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.pageGutter, vertical = EumSpacing.x12),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.eumColors.textPrimary,
    )
}

@Composable
private fun CartBottomActionBar(
    uiModel: CartUiModel,
    selectedCount: Int,
    onCheckoutClicked: () -> Unit,
) {
    Column(
        Modifier.fillMaxWidth()
            .heightIn(min = EumBottomBarMetrics.CartOrderBarMinHeight)
            .background(MaterialTheme.eumColors.surface)
            .padding(horizontal = EumSpacing.pageGutter, vertical = EumSpacing.x12),
    ) {
        // 주문하기는 주문 가능한 줄을 골랐을 때만 활성이다.
        EumPrimaryButton(
            onClick = onCheckoutClicked,
            enabled = selectedCount > 0,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (selectedCount > 0 && uiModel.expectedPaymentWon != null) {
                    stringResource(R.string.cart_order_button, uiModel.expectedPaymentLabel, selectedCount)
                } else if (selectedCount > 0) {
                    // 결제 예정 금액을 모르면 금액 없이 개수만 보인다(M1).
                    stringResource(R.string.cart_order_button_count, selectedCount)
                } else {
                    stringResource(R.string.cart_order_empty)
                },
            )
        }
    }
}

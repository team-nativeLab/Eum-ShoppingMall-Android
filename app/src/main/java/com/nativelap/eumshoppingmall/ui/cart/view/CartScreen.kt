package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.nativelap.eumshoppingmall.ui.common.EumOutlinedButton
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
    val quoteItems = uiModel.items.filter { it.type == CartItemType.QuoteRequired }

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
                    modifier = Modifier.statusBarsPadding(),
                )
                if (!isLoggedIn) {
                    CartLoginNoticeBanner(Modifier.fillMaxWidth())
                }
            }
        },
        bottomBar = {
            Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
                ResponsiveWidthContainer {
                    CartBottomActionBar(
                        uiModel = uiModel,
                        selectedCount = selectedCount,
                        onQuoteClicked = { onEvent(CartScreenEvent.QuoteRequestClicked) },
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
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textSecondary),
                            ) {
                                Text(stringResource(R.string.common_delete_selected))
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.eumColors.line)
                    }
                    if (orderableItems.isNotEmpty()) {
                        // 주문 가능·불가 구역이 함께 있을 때만 구역 제목을 보여 준다(Figma A-07 대비 A-07h).
                        if (quoteItems.isNotEmpty()) {
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
                    if (quoteItems.isNotEmpty()) {
                        item {
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    stringResource(R.string.cart_quote_products, quoteItems.size),
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                TextButton(
                                    onClick = { onEvent(CartScreenEvent.QuoteRequestClicked) },
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                                ) {
                                    Text(stringResource(R.string.cart_request_all_quotes), textDecoration = TextDecoration.Underline)
                                }
                            }
                        }
                        items(quoteItems, key = { it.id }) { item ->
                            Column(Modifier.padding(horizontal = EumSpacing.x20)) {
                                CartProductRow(
                                    item = item,
                                    onSelectionChanged = {},
                                    onQuantityChanged = { onEvent(CartScreenEvent.QuantityChanged(item.id, it)) },
                                    onOptionChangeClicked = { onEvent(CartScreenEvent.OptionChangeClicked(item.id)) },
                                    onRemoveClicked = { onEvent(CartScreenEvent.ItemRemoved(item.id)) },
                                    isQuantityLocked = item.id in quantityLockedItemIds,
                                )
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.eumColors.textPrimary,
    )
}

@Composable
private fun CartBottomActionBar(
    uiModel: CartUiModel,
    selectedCount: Int,
    onQuoteClicked: () -> Unit,
    onCheckoutClicked: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12)) {
        // 주문하기는 주문 가능한 줄을 골랐을 때만 활성이다. 견적 문의는 상품 없이도 폼을 열 수 있어 항상 활성이다.
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
        Spacer(Modifier.height(EumSpacing.x8))
        EumOutlinedButton(onClick = onQuoteClicked, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.cart_quote_request))
        }
    }
}

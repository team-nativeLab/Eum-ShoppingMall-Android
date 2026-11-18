package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHubScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestOrderSelectionScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestOrderSelectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestReason
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestWithdrawalScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus

/** 취소·반품·교환 허브(A-19). */
@Composable
fun ClaimHubRoute(
    onBackClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onRequestHistoryClick: () -> Unit,
    onStartRequestClick: () -> Unit,
) {
    OrderRequestHubScreen(
        onEvent = { event ->
            when (event) {
                OrderRequestHubScreenEvent.BackClicked -> onBackClick()
                OrderRequestHubScreenEvent.OrdersClicked -> onOrdersClick()
                OrderRequestHubScreenEvent.RequestHistoryClicked -> onRequestHistoryClick()
                OrderRequestHubScreenEvent.StartRequestClicked -> onStartRequestClick()
            }
        },
    )
}

/**
 * 신청할 주문 선택. 결제 전·준비 단계는 취소, 배송 이후는 반품으로 시작한다(신청 폼에서 바꿀 수 있다).
 */
@Composable
fun ClaimOrderSelectionRoute(
    uiModel: OrderRequestOrderSelectionUiModel,
    onBackClick: () -> Unit,
    onOrderDetailsClick: (orderId: String) -> Unit,
    onOrderDeliveryClick: (orderId: String) -> Unit,
    onOrderSelected: (orderId: String, requestType: OrderRequestType) -> Unit,
) {
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.filter) }
    var periodLabel by rememberSaveable { mutableStateOf(uiModel.periodLabel) }
    var isPeriodPickerVisible by rememberSaveable { mutableStateOf(uiModel.isPeriodPickerVisible) }
    OrderRequestOrderSelectionScreen(
        uiModel = uiModel.copy(
            filter = selectedFilter,
            periodLabel = periodLabel,
            isPeriodPickerVisible = isPeriodPickerVisible,
        ),
        onEvent = { event ->
            when (event) {
                OrderRequestOrderSelectionScreenEvent.BackClicked -> onBackClick()

                is OrderRequestOrderSelectionScreenEvent.FilterSelected -> selectedFilter = event.filter

                OrderRequestOrderSelectionScreenEvent.FiltersReset -> selectedFilter = OrderHistoryFilter.All

                OrderRequestOrderSelectionScreenEvent.PeriodPickerToggled -> isPeriodPickerVisible = !isPeriodPickerVisible

                is OrderRequestOrderSelectionScreenEvent.PeriodSelected -> {
                    periodLabel = event.period
                    isPeriodPickerVisible = false
                }

                is OrderRequestOrderSelectionScreenEvent.OrderDetailsClicked -> onOrderDetailsClick(event.orderId)

                is OrderRequestOrderSelectionScreenEvent.OrderDeliveryClicked -> onOrderDeliveryClick(event.orderId)

                is OrderRequestOrderSelectionScreenEvent.OrderRequestSelected -> {
                    val selectedStatus = uiModel.orders.firstOrNull { order -> order.id == event.orderId }?.status
                    onOrderSelected(event.orderId, defaultClaimType(selectedStatus))
                }
            }
        },
    )
}

internal fun defaultClaimType(status: OrderStatus?): OrderRequestType = when (status) {
    OrderStatus.Shipping,
    OrderStatus.Delivered,
    -> OrderRequestType.Return

    else -> OrderRequestType.Cancel
}

/**
 * 취소·반품·교환 신청 폼(A-19). 입력은 저장 가능한 값(ID·enum·문자열)으로만 보관한다.
 * 사진 첨부·회수지 변경·교환 옵션 선택은 파일·주소·옵션 연동 전이라 연결하지 않는다.
 */
@Composable
fun ClaimRequestFormRoute(
    uiModel: OrderRequestFormUiModel,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    var requestType by rememberSaveable { mutableStateOf(uiModel.type) }
    var selectedProductIds by rememberSaveable {
        mutableStateOf(uiModel.products.filter { product -> product.isSelected }.map { product -> product.id }.toSet())
    }
    var selectedReason by rememberSaveable { mutableStateOf<OrderRequestReason?>(uiModel.selectedReason) }
    var reasonDetails by rememberSaveable { mutableStateOf(uiModel.reasonDetails) }
    var collectionMethod by rememberSaveable { mutableStateOf(uiModel.collectionMethod) }
    OrderRequestFormScreen(
        uiModel = uiModel.copy(
            type = requestType,
            products = uiModel.products.map { product -> product.copy(isSelected = product.id in selectedProductIds) },
            selectedReason = selectedReason,
            reasonDetails = reasonDetails,
            collectionMethod = collectionMethod,
            canSubmit = selectedProductIds.isNotEmpty() && selectedReason != null,
        ),
        onEvent = { event ->
            when (event) {
                OrderRequestFormScreenEvent.BackClicked -> onBackClick()

                is OrderRequestFormScreenEvent.TypeSelected -> requestType = event.type

                is OrderRequestFormScreenEvent.ProductSelectionChanged -> {
                    selectedProductIds = if (event.isSelected) {
                        selectedProductIds + event.productId
                    } else {
                        selectedProductIds - event.productId
                    }
                }

                is OrderRequestFormScreenEvent.ReasonSelected -> selectedReason = event.reason

                is OrderRequestFormScreenEvent.ReasonDetailsChanged -> reasonDetails = event.value

                is OrderRequestFormScreenEvent.CollectionMethodSelected -> collectionMethod = event.method

                OrderRequestFormScreenEvent.SubmitClicked -> if (selectedProductIds.isNotEmpty() && selectedReason != null) onSubmit()

                is OrderRequestFormScreenEvent.ProductQuantityChanged,
                OrderRequestFormScreenEvent.PhotoAttachmentClicked,
                OrderRequestFormScreenEvent.CollectionAddressChangeClicked,
                OrderRequestFormScreenEvent.ExchangeOptionClicked,
                -> onUnavailableAction()
            }
        },
    )
}

/** 신청 완료(A-50). */
@Composable
fun ClaimResultRoute(
    uiModel: OrderRequestResultUiModel,
    onBackClick: () -> Unit,
    onRequestHistoryClick: () -> Unit,
) {
    OrderRequestResultScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                OrderRequestResultScreenEvent.BackClicked -> onBackClick()
                OrderRequestResultScreenEvent.RequestHistoryClicked -> onRequestHistoryClick()
            }
        },
    )
}

/** 신청 내역(A-51). */
@Composable
fun ClaimHistoryRoute(
    uiModel: OrderRequestHistoryUiModel,
    onBackClick: () -> Unit,
    onRequestDetailsClick: (requestId: String) -> Unit,
    onWithdrawalClick: (requestId: String) -> Unit,
    onRejectionReasonClick: (requestId: String) -> Unit,
    onOrdersClick: () -> Unit,
) {
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.filter) }
    val visibleItems = uiModel.items.filter { item ->
        when (selectedFilter) {
            OrderRequestHistoryFilter.All -> true
            OrderRequestHistoryFilter.Cancel -> item.requestType == OrderRequestType.Cancel
            OrderRequestHistoryFilter.Return -> item.requestType == OrderRequestType.Return
            OrderRequestHistoryFilter.Exchange -> item.requestType == OrderRequestType.Exchange
        }
    }
    OrderRequestHistoryScreen(
        uiModel = uiModel.copy(
            filter = selectedFilter,
            items = visibleItems,
            hasNoRequests = uiModel.items.isEmpty(),
        ),
        onEvent = { event ->
            when (event) {
                OrderRequestHistoryScreenEvent.BackClicked -> onBackClick()
                is OrderRequestHistoryScreenEvent.FilterSelected -> selectedFilter = event.filter
                is OrderRequestHistoryScreenEvent.RequestDetailsClicked -> onRequestDetailsClick(event.requestId)
                is OrderRequestHistoryScreenEvent.WithdrawalClicked -> onWithdrawalClick(event.requestId)
                is OrderRequestHistoryScreenEvent.RejectionReasonClicked -> onRejectionReasonClick(event.requestId)
                OrderRequestHistoryScreenEvent.OrdersClicked -> onOrdersClick()
            }
        },
    )
}

/** 신청 상세(A-51). 철회·반려 사유·내역 보기는 호출부가 목적지를 정한다. */
@Composable
fun ClaimDetailRoute(
    uiModel: OrderRequestDetailUiModel,
    onBackClick: () -> Unit,
    onWithdrawalClick: () -> Unit,
    onRejectionReasonClick: () -> Unit,
    onRequestHistoryClick: () -> Unit,
) {
    OrderRequestDetailScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                OrderRequestDetailScreenEvent.BackClicked -> onBackClick()
                OrderRequestDetailScreenEvent.WithdrawalClicked -> onWithdrawalClick()
                OrderRequestDetailScreenEvent.RejectionReasonClicked -> onRejectionReasonClick()
                OrderRequestDetailScreenEvent.RequestHistoryClicked -> onRequestHistoryClick()
            }
        },
    )
}

/**
 * 신청 철회(A-52). 확인 화면에서 철회하면 같은 목적지 안에서 완료 화면으로 바꾼다.
 * 서버 연동 전이라 철회가 내역 데이터에 반영되지는 않는다.
 */
@Composable
fun ClaimWithdrawalRoute(
    requestNumber: String,
    completedUiModel: OrderRequestResultUiModel,
    onBackClick: () -> Unit,
    onRequestHistoryClick: () -> Unit,
) {
    var isWithdrawn by rememberSaveable { mutableStateOf(false) }
    val onWithdrawalEvent: (OrderRequestWithdrawalScreenEvent) -> Unit = { event ->
        when (event) {
            OrderRequestWithdrawalScreenEvent.BackClicked,
            OrderRequestWithdrawalScreenEvent.WithdrawalCancelled,
            -> onBackClick()

            OrderRequestWithdrawalScreenEvent.WithdrawalConfirmed -> isWithdrawn = true

            OrderRequestWithdrawalScreenEvent.RequestHistoryClicked -> onRequestHistoryClick()
        }
    }
    if (isWithdrawn) {
        OrderRequestWithdrawalCompleteScreen(uiModel = completedUiModel, onEvent = onWithdrawalEvent)
    } else {
        OrderRequestWithdrawalConfirmScreen(requestNumber = requestNumber, onEvent = onWithdrawalEvent)
    }
}

/** 반려 사유(A-53). */
@Composable
fun ClaimRejectionRoute(
    uiModel: OrderRequestRejectionUiModel,
    onBackClick: () -> Unit,
    onRequestHistoryClick: () -> Unit,
) {
    OrderRequestRejectionScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                OrderRequestRejectionScreenEvent.BackClicked -> onBackClick()
                OrderRequestRejectionScreenEvent.RequestHistoryClicked -> onRequestHistoryClick()
            }
        },
    )
}

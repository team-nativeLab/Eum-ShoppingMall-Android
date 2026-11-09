package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * 상품 상세(A-05)와 옵션 시트(A-05s).
 *
 * 옵션 시트는 Navigation 목적지가 아니라 이 Route의 화면 내부 상태로 연다. 시트를 NavKey overlay로 옮기는 일은 후속 작업이다.
 */
@Composable
fun CatalogProductDetailRoute(
    uiModel: CatalogProductDetailUiModel,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onBuyNowClick: () -> Unit,
    onQuoteRequestClick: (productId: String) -> Unit,
    onProductClick: (productId: String) -> Unit,
) {
    var selectedImageIndex by rememberSaveable { mutableStateOf(uiModel.selectedImageIndex) }
    var selectedTabIndex by rememberSaveable { mutableStateOf(uiModel.selectedTabIndex) }
    var isOptionsSheetVisible by rememberSaveable { mutableStateOf(uiModel.optionsSheetVisible) }
    var isProductFavorite by rememberSaveable { mutableStateOf(uiModel.product.isFavorite) }
    // 선택 옵션은 UiModel 목록이라 저장 대상이 아니다. 프로세스 재생성 시 다시 고른다.
    var selectedOptions by remember { mutableStateOf(uiModel.selectedOptions) }
    CatalogProductDetailScreen(
        uiModel = uiModel.copy(
            product = uiModel.product.copy(isFavorite = isProductFavorite),
            selectedImageIndex = selectedImageIndex,
            selectedTabIndex = selectedTabIndex,
            optionsSheetVisible = isOptionsSheetVisible,
            selectedOptions = selectedOptions,
        ),
        onEvent = { event ->
            when (event) {
                CatalogProductDetailEvent.BackClicked -> onBackClick()

                CatalogProductDetailEvent.CartClicked -> onCartClick()

                is CatalogProductDetailEvent.ImageSelected -> selectedImageIndex = event.index

                is CatalogProductDetailEvent.TabSelected -> selectedTabIndex = event.tabIndex

                is CatalogProductDetailEvent.SelectOptionClicked -> isOptionsSheetVisible = true

                CatalogProductDetailEvent.CloseOptionsSheet -> isOptionsSheetVisible = false

                is CatalogProductDetailEvent.OptionSelected -> {
                    val selectedOption = uiModel.optionGroups
                        .firstOrNull { group -> group.id == event.groupId }
                        ?.options
                        ?.firstOrNull { option -> option.id == event.optionId }
                    val isAlreadySelected = selectedOptions.any { selected ->
                        selected.groupId == event.groupId && selected.option.id == event.optionId
                    }
                    if (selectedOption != null && !isAlreadySelected) {
                        selectedOptions = selectedOptions + CatalogSelectedOptionUiModel(event.groupId, selectedOption)
                    }
                }

                is CatalogProductDetailEvent.QuantityChanged -> {
                    selectedOptions = selectedOptions.map { selected ->
                        if (selected.groupId == event.groupId && selected.option.id == event.optionId) {
                            selected.copy(option = selected.option.copy(quantity = event.quantity.coerceAtLeast(1)))
                        } else {
                            selected
                        }
                    }
                }

                is CatalogProductDetailEvent.OptionRemoved -> {
                    selectedOptions = selectedOptions.filterNot { selected ->
                        selected.groupId == event.groupId && selected.option.id == event.optionId
                    }
                }

                CatalogProductDetailEvent.BuyNowClicked -> {
                    isOptionsSheetVisible = false
                    onBuyNowClick()
                }

                CatalogProductDetailEvent.QuoteRequestClicked -> {
                    isOptionsSheetVisible = false
                    onQuoteRequestClick(uiModel.product.id)
                }

                // 장바구니 저장소가 아직 없어 담기는 시트만 닫는다.
                CatalogProductDetailEvent.AddToCartClicked -> isOptionsSheetVisible = false

                is CatalogProductDetailEvent.RelatedProductSelected -> onProductClick(event.productId)

                CatalogProductDetailEvent.ProductFavoriteToggled -> isProductFavorite = !isProductFavorite

                // 공유와 연관 상품 찜 저장은 이번 범위가 아니다.
                CatalogProductDetailEvent.ShareClicked,
                is CatalogProductDetailEvent.FavoriteToggled,
                -> Unit
            }
        },
    )
}
